package com.alphasystem.docbook.builder.impl.inline;

import com.alphasystem.commons.util.AppUtil;
import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.InlineBuilder;
import com.alphasystem.docbook.util.Utils;
import com.alphasystem.openxml.builder.wml.WmlAdapter;
import com.alphasystem.openxml.builder.wml.WmlBuilderFactory;
import org.apache.commons.lang3.StringUtils;
import org.docx4j.wml.R;

import java.util.Collections;
import java.util.List;

import static com.alphasystem.docbook.handler.InlineHandlerFactory.HYPERLINK;

public abstract class LinkSupportBuilder<S> extends InlineBuilder<S> {

    protected boolean external = false;
    protected String href;

    protected LinkSupportBuilder(String childContentMethodName, S source, Builder<?> parent) {
        super(childContentMethodName, HYPERLINK, source, parent);
    }

    protected void initHref() {
        href = (String) AppUtil.invokeMethod(source, "getLinkend");
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
            text = (String) AppUtil.invokeMethod(source, "getEndterm");
        }
        if (StringUtils.isBlank(text)) {
            if (external) text = href;
            else text = ApplicationController.getContext().getLabel(href);

        }
        if (StringUtils.isBlank(text)) {
            text = "here";
        }
        runBuilder.addContent(WmlAdapter.getText(text));
        return runBuilder.getObject();
    }

    @Override
    protected void preProcess() {
        super.preProcess();
        initHref();
    }

    @Override
    protected List<Object> processChildContent(List<Object> childContent) {
        return Collections.singletonList(getLinkLabel(childContent));
    }

    @Override
    protected List<Object> doProcess(List<Object> processedChildContent) {
        final var builder = WmlBuilderFactory.getPHyperlinkBuilder().withHistory(true);
        if (!processedChildContent.isEmpty()) {
            builder.addContent(processedChildContent.toArray());
        }
        if (external) {
            builder.withId(WmlAdapter.addExternalHyperlinkRelationship(href, ApplicationController.getContext().getMainDocumentPart()));
        } else {
            builder.withAnchor(href);
        }
        return Collections.singletonList(builder.getObject());
    }
}
