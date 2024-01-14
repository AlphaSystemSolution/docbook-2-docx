package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;

import java.util.Collections;
import java.util.List;

public abstract class NullBuilder<S> extends AbstractParaBuilder<S> {

    protected NullBuilder(S source, Builder<?> parent) {
        super(null, source, parent);
    }

    @Override
    public List<Object> process() {
        return Collections.emptyList();
    }
}
