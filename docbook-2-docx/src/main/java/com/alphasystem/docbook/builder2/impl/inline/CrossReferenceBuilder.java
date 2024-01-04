package com.alphasystem.docbook.builder2.impl.inline;

import com.alphasystem.docbook.builder2.Builder;
import org.docbook.model.CrossReference;

public class CrossReferenceBuilder extends LinkSupportBuilder<CrossReference> {

    public CrossReferenceBuilder(CrossReference source, Builder<?> parent) {
        super(null, source, parent);
    }
}
