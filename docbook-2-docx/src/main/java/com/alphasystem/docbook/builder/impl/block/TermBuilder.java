package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import org.docbook.model.Term;

public class TermBuilder extends AbstractParaBuilder<Term> {

    public TermBuilder(Term source, Builder<?> parent) {
        super(source, parent);
    }
}
