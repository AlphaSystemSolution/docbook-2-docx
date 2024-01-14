package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import org.docbook.model.SimplePara;

public class SimpleParaBuilder extends AbstractParaBuilder<SimplePara> {

    public SimpleParaBuilder(SimplePara source, Builder<?> parent) {
        super(source, parent);
    }
}
