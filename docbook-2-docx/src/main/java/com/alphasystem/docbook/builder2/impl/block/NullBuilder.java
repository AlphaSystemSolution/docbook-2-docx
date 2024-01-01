package com.alphasystem.docbook.builder2.impl.block;

import java.util.Collections;
import java.util.List;

public abstract class NullBuilder<S> extends AbstractParaBuilder<S> {

    protected NullBuilder() {
        super(null);
    }

    @Override
    public List<Object> process(S source) {
        return Collections.emptyList();
    }
}
