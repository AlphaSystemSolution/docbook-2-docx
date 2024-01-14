package com.alphasystem.docbook.builder.impl.inline;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.InlineBuilder;
import org.docbook.model.Emphasis;

import static com.alphasystem.docbook.handler.InlineHandlerFactory.ITALIC;

public class EmphasisBuilder extends InlineBuilder<Emphasis> {

    public EmphasisBuilder(Emphasis source, Builder<?> parent) {
        super(ITALIC, source, parent);
    }
}
