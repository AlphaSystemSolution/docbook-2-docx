package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.builder2.Builder;

import java.util.Collections;
import java.util.List;

public abstract class NullBuilder<S> extends AbstractParaBuilder<S> {

    protected NullBuilder() {
        super(null);
    }

    @Override
    public List<Object> process(S source, Builder<?> parent) {
        return Collections.emptyList();
    }
}
