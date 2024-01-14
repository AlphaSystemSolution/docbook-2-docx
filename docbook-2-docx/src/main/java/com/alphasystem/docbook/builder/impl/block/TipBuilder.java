package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.model.Admonition;
import org.docbook.model.Tip;

public class TipBuilder extends AdmonitionBuilder<Tip> {

    public TipBuilder(Tip source, Builder<?> parent) {
        super(Admonition.TIP, source, parent);
    }
}
