package com.alphasystem.docbook.builder.impl.inline;

import static com.alphasystem.docbook.handler.InlineHandlerFactory.HYPERLINK;

import com.alphasystem.commons.util.AppUtil;
import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.InlineBuilder;
import com.alphasystem.docbook.util.Utils;
import com.alphasystem.docx4j.builder.wml.WmlAdapter;
import com.alphasystem.docx4j.builder.wml.WmlBuilderFactory;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.docx4j.wml.R;

public abstract class LinkSupportBuilder<S> extends InlineBuilder<S> {

  protected boolean external = false;
  protected String href;

  protected LinkSupportBuilder(String childContentMethodName, S source, Builder<?> parent) {
    super(childContentMethodName, HYPERLINK, source, parent);
  }

  protected void initHref() {
    final var renderer = ApplicationController.getContext().getDocBookRenderer();
    final var linkend = AppUtil.invokeMethod(source, "getLinkend");
    href = renderer == null ? (String) linkend : renderer.attribute(source, "linkend", linkend);
    if (StringUtils.isBlank(href)) {
      href = (String) AppUtil.invokeMethod(source, "getHref");
      external = StringUtils.isNotBlank(href) && !href.startsWith("#");
      if (!external) {
        href = href.substring(1);
      }
    }
  }

  protected R getLinkLabel(List<Object> childContent) {
    createRunBuilder();

    // Try to populate link display text
    // first get from child content
    // then from getEndterm
    // if link is external then use `href`
    // if link is internal then get text from the linked text
    // if all of the above fails the default to "here".

    var text = Utils.getLinkText(childContent);

    if (StringUtils.isBlank(text)) {
      final var renderer = ApplicationController.getContext().getDocBookRenderer();
      final var endterm = AppUtil.invokeMethod(source, "getEndterm");
      text = renderer == null ? (String) endterm : renderer.attribute(source, "endterm", endterm);
    }
    if (StringUtils.isBlank(text)) {
      if (external) text = href;
      else text = ApplicationController.getContext().getLabel(href);
    }
    if (StringUtils.isBlank(text)) {
      text = "here";
    }
    final var lines = text.split("\\R", -1);
    runBuilder.addContent(WmlAdapter.getText(lines[0]));
    for (int i = 1; i < lines.length; i++) {
      runBuilder.addContent(WmlBuilderFactory.getBrBuilder().getObject());
      runBuilder.addContent(WmlAdapter.getText(lines[i]));
    }
    return runBuilder.getObject();
  }

  @Override
  protected void preProcess() {
    super.preProcess();
    initHref();
  }

  @Override
  protected List<Object> processChildContent(List<Object> childContent) {
    final var renderer = ApplicationController.getContext().getDocBookRenderer();
    if (renderer != null) {
      renderer.consumeInstructions(source);
    }
    return Collections.singletonList(getLinkLabel(childContent));
  }

  @Override
  protected List<Object> doProcess(List<Object> processedChildContent) {
    final var builder = WmlBuilderFactory.getPHyperlinkBuilder().withHistory(true);
    if (!processedChildContent.isEmpty()) {
      builder.addContent(processedChildContent.toArray());
    }
    if (external) {
      builder.withId(
          WmlAdapter.addExternalHyperlinkRelationship(
              href, ApplicationController.getContext().getMainDocumentPart()));
    } else {
      builder.withAnchor(href);
    }
    return Collections.singletonList(builder.getObject());
  }
}
