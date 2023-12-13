package com.alphasystem.docbook.handler.impl.block;

import static com.alphasystem.docbook.builder.model.Admonition.IMPORTANT;

/**
 * @author sali
 */
public class ImportantHandler extends AdmonitionBlockHandler {

    ImportantHandler() {
        super(IMPORTANT, 20);
    }
}
