package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.docbook.builder2.impl.AbstractBuilder;

public abstract class TableContentBuilder<S> extends AbstractBuilder<S> {

    protected TableContentBuilder(S source, Builder<?> parent) {
        super(null, source, parent);
    }
}
