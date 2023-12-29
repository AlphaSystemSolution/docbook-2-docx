package com.alphasystem.docbook.builder2.impl.block;

import org.docx4j.wml.P;

public abstract class NullBuilder<S> extends AbstractParaBuilder<S> {

    protected NullBuilder(S source) {
        super(source, null);
    }

    @Override
    public P process() {
        return null;
    }
}
