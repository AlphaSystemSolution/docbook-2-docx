package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.docbook.model.Admonition;
import org.docbook.model.Caution;

public class CautionBuilder extends AdmonitionBuilder<Caution> {

    public CautionBuilder(Caution source, Builder<?> parent) {
        super(Admonition.CAUTION, source, parent);
    }
}
