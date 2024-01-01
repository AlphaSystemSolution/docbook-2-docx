package com.alphasystem.docbook.builder2.impl.inline;

import com.alphasystem.docbook.builder2.impl.InlineBuilder;
import org.docbook.model.Subscript;

import static com.alphasystem.docbook.builder.DocumentBuilderHelper.SUBSCRIPT;

public class SubscriptBuilder extends InlineBuilder<Subscript> {

    public SubscriptBuilder() {
        super(SUBSCRIPT);
    }
}
