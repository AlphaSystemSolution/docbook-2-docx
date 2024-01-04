package com.alphasystem.docbook.builder2.impl.inline;

import com.alphasystem.docbook.builder2.Builder;
import org.docbook.model.Link;

public class LinkBuilder extends LinkSupportBuilder<Link> {

    public LinkBuilder(Link source, Builder<?> parent) {
        super("getContent", source, parent);
    }
}
