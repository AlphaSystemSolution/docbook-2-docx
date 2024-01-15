package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import org.docbook.model.Title;


public class TitleBuilder extends AbstractParaBuilder<Title> {

    public TitleBuilder(Title source, Builder<?> parent) {
        super(source, parent);
    }
}
