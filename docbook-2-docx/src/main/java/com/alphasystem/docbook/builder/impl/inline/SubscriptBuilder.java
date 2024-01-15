package com.alphasystem.docbook.builder.impl.inline;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.InlineBuilder;
import org.docbook.model.Subscript;

import static com.alphasystem.docbook.handler.InlineHandlerFactory.SUBSCRIPT;

public class SubscriptBuilder extends InlineBuilder<Subscript> {

    public SubscriptBuilder(Subscript source, Builder<?> parent) {
        super(SUBSCRIPT, source, parent);
    }
}
