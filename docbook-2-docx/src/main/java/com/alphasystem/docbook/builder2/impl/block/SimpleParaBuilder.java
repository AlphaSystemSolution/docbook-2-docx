package com.alphasystem.docbook.builder2.impl.block;

import org.docbook.model.SimplePara;

public class SimpleParaBuilder extends AbstractParaBuilder<SimplePara> {

    public SimpleParaBuilder(SimplePara source) {
        super(source, source.getRole());
    }
}
