package com.alphasystem.docbook.builder2.impl.inline;

import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.docbook.builder2.impl.InlineBuilder;
import org.docbook.model.Literal;

import static com.alphasystem.docbook.builder.DocumentBuilderHelper.LITERAL;

public class LiteralBuilder extends InlineBuilder<Literal> {

    public LiteralBuilder(Literal source, Builder<?> parent) {
        super(LITERAL, source, parent);
    }
}
