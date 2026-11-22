package com.alphasystem.xml;

import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docbook.DocumentContext;
import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.BuilderFactory;
import com.alphasystem.docbook.util.Utils;
import com.alphasystem.docx4j.builder.wml.TocGenerator;
import com.alphasystem.docx4j.builder.wml.WmlAdapter;
import com.alphasystem.docx4j.builder.wml.WmlBuilderFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import org.docbook.model.Para;
import org.docbook.model.SimplePara;
import org.docbook.model.Title;
import org.docx4j.wml.P;
import org.docx4j.wml.Tbl;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

public final class DocBookRenderer {

  private static final Pattern ATTRIBUTE = Pattern.compile("([\\w-]+)\\s*=\\s*(['\"])(.*?)\\2");
  private final ParsedDocBook parsed;
  private final DocumentContext context;
  private final Map<Node, Map<String, String>> tableOptions = new IdentityHashMap<>();
  private final Set<ProcessingInstruction> consumed =
      Collections.newSetFromMap(new IdentityHashMap<>());
  private int pendingPageBreaks;

  public DocBookRenderer(ParsedDocBook parsed) {
    this.parsed = parsed;
    context = ApplicationController.getContext();
    for (var instruction : parsed.getInstructions()) {
      switch (instruction.getTarget()) {
        case "asciidoc-toc" -> context.getDocumentInfo().setToc(true);
        case "asciidoc-numbered" -> {
          context.getDocumentInfo().setSectionNumbers(true);
          context.getWmlPackageBuilder().multiLevelHeading();
        }
        case "dbhtml", "dbfo" -> indexTableOptions(instruction);
        case "asciidoc-br", "linebreak", "asciidoc-pagebreak", "dblatex" -> {}
        default ->
            LoggerFactory.getLogger(getClass())
                .warn("Unhandled processing instruction: target = {}", instruction.getTarget());
      }
    }
  }

  public void render() {
    renderContainer(parsed.getDocument());
    while (pendingPageBreaks > 0) {
      append(pageBreak());
      pendingPageBreaks--;
    }
    final var info = context.getDocumentInfo();
    if (info.isToc() && info.isSectionNumbers()) {
      new TocGenerator()
          .level(5)
          .tocHeading(info.getTocTitle())
          .mainDocumentPart(context.getMainDocumentPart())
          .generateToc();
    }
  }

  private void renderContainer(Node node) {
    for (var child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
      if (child instanceof ProcessingInstruction instruction) {
        instruction(instruction);
      } else if (child instanceof Element element) {
        switch (element.getLocalName()) {
          case "article", "section" -> renderContainer(element);
          case "info" -> {
            for (var infoChild = element.getFirstChild();
                infoChild != null;
                infoChild = infoChild.getNextSibling()) {
              if (infoChild instanceof Element title && "title".equals(title.getLocalName())) {
                BuilderFactory.getInstance()
                    .process(parsed.getObject(title), null)
                    .forEach(this::append);
              }
            }
          }
          case "date" -> {}
          default -> {
            final var object = parsed.getObject(element);
            if (object == null) {
              throw new IllegalArgumentException(
                  "No bound DocBook object for " + element.getTagName());
            }
            final var content = BuilderFactory.getInstance().process(object, null);
            if (content != null) {
              content.forEach(this::append);
            }
          }
        }
      }
    }
  }

  private void append(Object object) {
    final var table = object instanceof Tbl;
    if (table && context.isPreviousElementTable()) {
      context.getMainDocumentPart().addObject(WmlAdapter.getEmptyParaNoSpacing());
    }
    context.getMainDocumentPart().addObject(object);
    context.setPreviousElementTable(table);
  }

  public List<Object> process(Object object, Builder<?> parent, Supplier<List<Object>> builder) {
    if (object instanceof ProcessingInstruction instruction) {
      instruction(instruction);
      return List.of();
    }
    final List<Object> paragraphContent =
        switch (object) {
          case Para para -> para.getContent();
          case SimplePara para -> para.getContent();
          default -> null;
        };
    if (paragraphContent != null
        && paragraphContent.stream().allMatch(ProcessingInstruction.class::isInstance)) {
      paragraphContent.forEach(value -> instruction((ProcessingInstruction) value));
      if (pendingPageBreaks > 0) {
        return List.of();
      }
    }
    if (object instanceof Title title) {
      final var label = Utils.getLinkText(title.getContent());
      if (!label.isBlank()) {
        context.putLabel(title.getId(), label);
      }
    }
    final var node = parsed.getNode(object);
    if (isTable(node)) {
      final var options = tableOptions.getOrDefault(node, Map.of());
      context.setTableWidth(options.get("table-width"));
      context.setKeepTogether(options.get("keep-together"));
    }
    final var beforeTable = isTable(node) ? pendingPageBreaks : 0;
    if (beforeTable > 0) {
      pendingPageBreaks = 0;
    }
    final var output = builder.get();
    pendingPageBreaks += beforeTable;
    if (output == null || pendingPageBreaks == 0) {
      return output;
    }
    final var result = new ArrayList<Object>();
    for (var item : output) {
      if (pendingPageBreaks > 0 && (item instanceof P || item instanceof Tbl)) {
        while (pendingPageBreaks > 1 || item instanceof Tbl && pendingPageBreaks > 0) {
          result.add(pageBreak());
          pendingPageBreaks--;
        }
        if (pendingPageBreaks > 0 && item instanceof P paragraph) {
          if (paragraph.getPPr() == null) {
            paragraph.setPPr(WmlBuilderFactory.getPPrBuilder().getObject());
          }
          paragraph
              .getPPr()
              .setPageBreakBefore(
                  WmlBuilderFactory.getPPrBuilder()
                      .withPageBreakBefore(true)
                      .getObject()
                      .getPageBreakBefore());
          pendingPageBreaks--;
        }
      }
      result.add(item);
    }
    return result;
  }

  public List<Object> content(Object source, List<Object> fallback) {
    if (fallback == null) {
      return List.of();
    }
    final var node = parsed.getNode(source);
    if (node == null || fallback.stream().anyMatch(ProcessingInstruction.class::isInstance)) {
      return fallback;
    }
    final var result = new ArrayList<>(fallback);
    var insertion = 0;
    for (var child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
      final var object = parsed.getObject(child);
      if (object != null) {
        for (int i = insertion; i < result.size(); i++) {
          if (ParsedDocBook.unwrap(result.get(i)) == object) {
            insertion = i + 1;
            break;
          }
        }
      } else if (child instanceof ProcessingInstruction instruction
          && !DocBookModelNormalizer.isLineBreak(instruction)) {
        result.add(insertion++, instruction);
      }
    }
    return result;
  }

  public void consumeInstructions(Object object) {
    final var node = parsed.getNode(object);
    if (node != null) {
      consumeInstructions(node);
    }
  }

  private void consumeInstructions(Node node) {
    if (node instanceof ProcessingInstruction instruction) {
      instruction(instruction);
    }
    for (var child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
      consumeInstructions(child);
    }
  }

  public String attribute(Object object, String name, Object fallback) {
    final var node = parsed.getNode(object);
    if (node instanceof Element element && element.hasAttribute(name)) {
      return element.getAttribute(name);
    }
    return fallback instanceof String value
        ? value
        : fallback == null ? null : Utils.getId(fallback);
  }

  private void instruction(ProcessingInstruction instruction) {
    if (consumed.add(instruction) && "asciidoc-pagebreak".equals(instruction.getTarget())) {
      pendingPageBreaks++;
    }
  }

  private void indexTableOptions(ProcessingInstruction instruction) {
    Node table = instruction.getParentNode();
    while (table != null && !isTable(table)) {
      table = table.getParentNode();
    }
    if (table == null) {
      for (var next = instruction.getNextSibling(); next != null; next = next.getNextSibling()) {
        if (isTable(next)) {
          table = next;
          break;
        }
      }
    }
    if (table == null) {
      return;
    }
    final var name = "dbhtml".equals(instruction.getTarget()) ? "table-width" : "keep-together";
    final var matcher = ATTRIBUTE.matcher(instruction.getData());
    while (matcher.find()) {
      if (name.equals(matcher.group(1))) {
        tableOptions
            .computeIfAbsent(table, ignored -> new java.util.HashMap<>())
            .put(name, matcher.group(3).replace("%", ""));
      }
    }
  }

  private static boolean isTable(Node node) {
    return node instanceof Element element
        && ("table".equals(element.getLocalName())
            || "informaltable".equals(element.getLocalName()));
  }

  private static P pageBreak() {
    return WmlBuilderFactory.getPBuilder()
        .withPPr(WmlBuilderFactory.getPPrBuilder().withPageBreakBefore(true).getObject())
        .getObject();
  }
}
