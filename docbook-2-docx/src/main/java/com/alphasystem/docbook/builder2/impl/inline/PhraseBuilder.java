package com.alphasystem.docbook.builder2.impl.inline;

import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.docbook.builder2.impl.InlineBuilder;
import org.docbook.model.Phrase;


public class PhraseBuilder extends InlineBuilder<Phrase> {

    public PhraseBuilder(Phrase source, Builder<?> parent) {
        super(null, source, parent);
    }
}
