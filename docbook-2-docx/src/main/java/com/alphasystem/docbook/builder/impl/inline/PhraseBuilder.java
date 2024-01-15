package com.alphasystem.docbook.builder.impl.inline;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.InlineBuilder;
import org.docbook.model.Phrase;


public class PhraseBuilder extends InlineBuilder<Phrase> {

    public PhraseBuilder(Phrase source, Builder<?> parent) {
        super(null, source, parent);
    }
}
