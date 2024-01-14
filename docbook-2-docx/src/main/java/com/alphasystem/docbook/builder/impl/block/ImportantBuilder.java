package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.model.Admonition;
import org.docbook.model.Important;

public class ImportantBuilder extends AdmonitionBuilder<Important> {

    public ImportantBuilder(Important source, Builder<?> parent) {
        super(Admonition.IMPORTANT, source, parent);
    }
}
