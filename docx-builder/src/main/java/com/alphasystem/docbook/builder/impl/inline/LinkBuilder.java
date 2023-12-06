package com.alphasystem.docbook.builder.impl.inline;

import com.alphasystem.docbook.builder.Builder;
import org.docbook.model.Link;

import java.util.Collections;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author sali
 */
public class LinkBuilder extends LinkSupportBuilder<Link> {

    public LinkBuilder(Builder<?> parent, Link link, int indexInParent) {
        super(parent, link, indexInParent);
    }

    @Override
    protected void initContent() {
        super.initContent();
        linkEnd = source.getLinkend();

        href = source.getHref();
        external = isNotBlank(href);
        if (!external) {
            href = getId(linkEnd);
        }

        // if there is child content within this link then we will use it as link display, otherwise we will try to find
        // in link end
        content = source.getContent();
        if (isNull(content) || content.isEmpty()) {
            Object link = getLink();
            link = (link == null) ? "Error!" : link;
            content = Collections.singletonList(link);
        }
    }

}
