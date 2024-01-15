package com.alphasystem.docbook.builder.impl.inline;

import com.alphasystem.docbook.builder.Builder;
import org.docbook.model.Link;

public class LinkBuilder extends LinkSupportBuilder<Link> {

    public LinkBuilder(Link source, Builder<?> parent) {
        super("getContent", source, parent);
    }
}
