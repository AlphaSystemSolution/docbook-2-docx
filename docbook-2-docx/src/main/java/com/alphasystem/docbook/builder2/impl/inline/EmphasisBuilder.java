package com.alphasystem.docbook.builder2.impl.inline;

import com.alphasystem.docbook.builder2.impl.InlineBuilder;
import org.docbook.model.Emphasis;

import static com.alphasystem.docbook.builder.DocumentBuilderHelper.ITALIC;


public class EmphasisBuilder extends InlineBuilder<Emphasis> {

    public EmphasisBuilder() {
        super(ITALIC);
    }
}
