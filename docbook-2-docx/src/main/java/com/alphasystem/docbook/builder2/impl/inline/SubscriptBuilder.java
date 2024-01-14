package com.alphasystem.docbook.builder2.impl.inline;

import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.docbook.builder2.impl.InlineBuilder;
import org.docbook.model.Subscript;

import static com.alphasystem.docbook.handler.InlineHandlerFactory.SUBSCRIPT;

public class SubscriptBuilder extends InlineBuilder<Subscript> {

    public SubscriptBuilder(Subscript source, Builder<?> parent) {
        super(SUBSCRIPT, source, parent);
    }
}
