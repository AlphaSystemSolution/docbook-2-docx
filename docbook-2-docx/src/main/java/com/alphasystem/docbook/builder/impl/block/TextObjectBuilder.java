package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;
import org.docbook.model.TextObject;

public class TextObjectBuilder extends BlockBuilder<TextObject> {

    public TextObjectBuilder(TextObject source, Builder<?> parent) {
        super(source, parent);
    }
}
