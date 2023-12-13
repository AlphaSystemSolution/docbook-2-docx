package com.alphasystem.docbook.handler.impl.block;

import static com.alphasystem.docbook.builder.model.Admonition.CAUTION;

/**
 * @author sali
 */
public class CautionHandler extends AdmonitionBlockHandler {

    CautionHandler() {
        super(CAUTION, 15);
    }
}
