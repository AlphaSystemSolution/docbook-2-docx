package com.alphasystem.docbook.builder2.impl.inline;

import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.docbook.builder2.impl.InlineBuilder;
import org.docbook.model.Superscript;

import static com.alphasystem.docbook.builder.DocumentBuilderHelper.SUPERSCRIPT;

public class SuperscriptBuilder extends InlineBuilder<Superscript> {

    public SuperscriptBuilder(Superscript source, Builder<?> parent) {
        super(SUPERSCRIPT, source, parent);
    }
}
