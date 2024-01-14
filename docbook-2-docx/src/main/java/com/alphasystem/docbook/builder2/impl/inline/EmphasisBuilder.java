package com.alphasystem.docbook.builder2.impl.inline;

import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.docbook.builder2.impl.InlineBuilder;
import org.docbook.model.Emphasis;

import static com.alphasystem.docbook.handler.InlineHandlerFactory.ITALIC;

public class EmphasisBuilder extends InlineBuilder<Emphasis> {

    public EmphasisBuilder(Emphasis source, Builder<?> parent) {
        super(ITALIC, source, parent);
    }
}
