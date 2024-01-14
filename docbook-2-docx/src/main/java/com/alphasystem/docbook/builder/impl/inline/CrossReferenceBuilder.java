package com.alphasystem.docbook.builder.impl.inline;

import com.alphasystem.docbook.builder.Builder;
import org.docbook.model.CrossReference;

public class CrossReferenceBuilder extends LinkSupportBuilder<CrossReference> {

    public CrossReferenceBuilder(CrossReference source, Builder<?> parent) {
        super(null, source, parent);
    }
}
