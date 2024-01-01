package com.alphasystem.docbook.builder2.impl.inline;

import com.alphasystem.docbook.builder2.impl.InlineBuilder;
import org.docbook.model.Term;

import static com.alphasystem.docbook.builder.DocumentBuilderHelper.STRONG;

public class TermBuilder extends InlineBuilder<Term> {

    public TermBuilder() {
        super(STRONG);
    }
}
