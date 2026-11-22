package com.alphasystem.xml;

import com.alphasystem.commons.util.IdGenerator;
import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docbook.builder.BuilderFactory;
import com.alphasystem.docbook.util.ConfigurationUtils;
import com.alphasystem.docbook.util.Utils;
import com.alphasystem.docx4j.builder.wml.UnorderedList;
import jakarta.xml.bind.annotation.XmlMixed;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.docbook.model.Entry;
import org.docbook.model.ItemizedList;
import org.docbook.model.Numeration;
import org.docbook.model.OrderedList;
import org.docbook.model.Para;
import org.docbook.model.ProgramListing;
import org.docbook.model.Title;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

public final class DocBookModelNormalizer {

  private final ParsedDocBook parsed;

  public DocBookModelNormalizer(ParsedDocBook parsed) {
    this.parsed = parsed;
  }

  public void normalize() {
    normalize(parsed.getDocument().getDocumentElement(), 0);
  }

  private void normalize(Element element, int level) {
    if ("section".equals(element.getLocalName())) {
      level++;
    }
    final var object = parsed.getObject(element);
    if (object != null) {
      final var id = element.getAttributeNS("http://www.w3.org/XML/1998/namespace", "id");
      setString(object, "setId", id.isBlank() ? IdGenerator.nextId() : id);
      switch (object) {
        case ItemizedList list when (list.getMark() == null || list.getMark().isBlank()) ->
            list.setMark(UnorderedList.values()[0].getStyleName());
        case OrderedList list when list.getNumeration() == null ->
            list.setNumeration(Numeration.ARABIC);
        case ProgramListing listing
            when (listing.getRole() == null || listing.getRole().isBlank()) ->
            listing.setRole("ProgramListingCode");
        default -> {}
      }
      if (object instanceof Title title) {
        final var parentNode = element.getParentNode();
        final var parent =
            parsed.getObject(
                "info".equals(parentNode.getLocalName()) ? parentNode.getParentNode() : parentNode);
        if (parent != null) {
          title.setId(Utils.getId(parent));
          title.setRole(ConfigurationUtils.getInstance().getTitleStyle(level, parent.getClass()));
        }
      }
      final var label = element.getAttribute("xreflabel");
      if (!label.isBlank()) {
        ApplicationController.getContext().putLabel(Utils.getId(object), label);
      }
    }
    for (var child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
      if (child instanceof Element childElement) {
        normalize(childElement, level);
      }
    }
    if (object != null) {
      normalizeMixedContent(element, object);
      if (object instanceof Entry entry) {
        wrapEntryContent(entry);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void normalizeMixedContent(Element element, Object object) {
    for (Field field : object.getClass().getDeclaredFields()) {
      if (!field.isAnnotationPresent(XmlMixed.class)) {
        continue;
      }
      try {
        field.setAccessible(true);
        var content = (List<Object>) field.get(object);
        if (content == null) {
          content = new ArrayList<>();
          field.set(object, content);
        }
        content.clear();
        for (var child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
          if (child instanceof ProcessingInstruction instruction) {
            content.add(isLineBreak(instruction) ? System.lineSeparator() : instruction);
          } else if (child.getNodeType() == Node.TEXT_NODE
              || child.getNodeType() == Node.CDATA_SECTION_NODE) {
            final var text = child.getNodeValue();
            if (object instanceof ProgramListing || !text.isBlank() || " ".equals(text)) {
              content.add(text);
            }
          } else if (child instanceof Element) {
            final var value = parsed.getObject(child);
            if (value != null) {
              content.add(value);
            }
          }
        }
      } catch (IllegalAccessException ex) {
        throw new IllegalStateException(
            "Cannot normalize mixed content of " + object.getClass().getName(), ex);
      }
    }
  }

  private void wrapEntryContent(Entry entry) {
    final var original = new ArrayList<>(entry.getContent());
    entry.getContent().clear();
    Para paragraph = null;
    for (Object child : original) {
      if (child instanceof String
          || child instanceof ProcessingInstruction
          || BuilderFactory.getInstance().isInline(child)) {
        if (paragraph == null) {
          paragraph = new Para().withId(IdGenerator.nextId());
          entry.getContent().add(paragraph);
        }
        paragraph.getContent().add(child);
      } else {
        paragraph = null;
        entry.getContent().add(child);
      }
    }
  }

  static boolean isLineBreak(ProcessingInstruction instruction) {
    return "asciidoc-br".equals(instruction.getTarget())
        || "linebreak".equals(instruction.getTarget());
  }

  private static void setString(Object object, String method, String value) {
    try {
      object.getClass().getMethod(method, String.class).invoke(object, value);
    } catch (NoSuchMethodException ignored) {
    } catch (ReflectiveOperationException ex) {
      throw new IllegalStateException(
          "Cannot set " + method + " on " + object.getClass().getName(), ex);
    }
  }
}
