package com.alphasystem.docbook.builder2.impl.inline;

import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docbook.builder2.impl.InlineBuilder;
import com.alphasystem.docbook.util.Utils;
import com.alphasystem.openxml.builder.wml.WmlAdapter;
import com.alphasystem.openxml.builder.wml.WmlBuilderFactory;
import org.apache.commons.lang3.StringUtils;
import org.docx4j.wml.R;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.alphasystem.docbook.builder.DocumentBuilderHelper.HYPER_LINK;

public abstract class LinkSupportBuilder<S> extends InlineBuilder<S> {

    protected boolean external = false;
    protected String href;

    protected LinkSupportBuilder(String childContentMethodName) {
        super(childContentMethodName, HYPER_LINK);
    }

    protected void initHref() {
        var href = (String) Utils.invokeMethod(source, "getLinkend");
        if (StringUtils.isBlank(href)) {
            href = (String) Utils.invokeMethod(source, "getHref");
            this.external = true;
        }
        this.href = href;
    }

    protected R getLinkLabel() {
        createRunBuilder();
        var text = (String) Utils.invokeMethod(source, "getEndterm");
        if (StringUtils.isBlank(text)) {
            text = ApplicationController.getContext().getLabel(href);
        }

        if (StringUtils.isNotBlank(text)) {
            runBuilder.addContent(WmlAdapter.getText(text));
        }

        return runBuilder.getObject();
    }

    @Override
    protected List<Object> processChildContent(List<Object> childContent) {
        var processedChildContent = super.processChildContent(childContent);
        if (Objects.isNull(processedChildContent) || processedChildContent.isEmpty()) {
            processedChildContent = Collections.singletonList(getLinkLabel());
        }
        return processedChildContent;
    }

    @Override
    protected void doInit(S source) {
        super.doInit(source);
        initHref();
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
