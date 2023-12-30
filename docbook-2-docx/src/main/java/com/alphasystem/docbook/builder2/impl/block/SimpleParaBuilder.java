package com.alphasystem.docbook.builder2.impl.block;

import org.docbook.model.SimplePara;

import java.util.List;

public class SimpleParaBuilder extends AbstractParaBuilder<SimplePara> {

    @Override
    protected List<Object> getChildContent() {
        return source.getContent();
    }
}
