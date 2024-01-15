package com.alphasystem.docbook.builder.impl.inline;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.InlineBuilder;
import org.docbook.model.Superscript;

import static com.alphasystem.docbook.handler.InlineHandlerFactory.SUPERSCRIPT;

public class SuperscriptBuilder extends InlineBuilder<Superscript> {

    public SuperscriptBuilder(Superscript source, Builder<?> parent) {
        super(SUPERSCRIPT, source, parent);
    }
}
