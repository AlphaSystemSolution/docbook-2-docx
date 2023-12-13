package com.alphasystem.docbook.builder.impl.inline;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.InlineBuilder;
import com.alphasystem.openxml.builder.wml.PHyperlinkBuilder;
import com.alphasystem.openxml.builder.wml.WmlAdapter;

import java.util.List;

import static com.alphasystem.docbook.ApplicationController.getContext;
import static com.alphasystem.docbook.builder.DocumentBuilderHelper.HYPER_LINK;
import static com.alphasystem.openxml.builder.wml.WmlBuilderFactory.getPHyperlinkBuilder;
import static java.lang.String.format;
import static java.util.Collections.singletonList;

/**
 * @author sali
 */
public class LinkSupportBuilder<T> extends InlineBuilder<T> {

    protected String href;
    protected Object linkEnd;
    protected boolean external;

    protected LinkSupportBuilder(Builder<?> parent, T obj, int indexInParent) {
        super(parent, obj, indexInParent);
    }

    @Override
    protected void initContent() {
        styles = new String[]{HYPER_LINK};
    }

    @Override
    public List<Object> buildContent() {
        final PHyperlinkBuilder hyperlinkBuilder = getPHyperlinkBuilder().withHistory(true).addContent(processContent().toArray());

        if (external) {
            hyperlinkBuilder.withId(WmlAdapter.addExternalHyperlinkRelationship(href, getContext().getMainDocumentPart()));
        } else {
            hyperlinkBuilder.withAnchor(href);
        }
        return singletonList(hyperlinkBuilder.getObject());
    }

    protected Object getLink() {
        Object value;
        if (linkEnd == null) {
            return null;
        }
        value = getTitle(linkEnd);
        if (value == null) {
            // there is no title associated with source, let's display the id itself
            value = getId(linkEnd);
            if (value == null) {
                logger.warn("No linkEnd display text found for object \"{}\"", linkEnd.getClass().getName());
            } else {
                value = format("[%s]", value);
            }
        }
        return value;
    }

}
