package com.alphasystem.docbook.handler.impl;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.docx4j.builder.wml.RPrBuilder;
import com.alphasystem.docx4j.builder.wml.WmlBuilderFactory;

/**
 * Handles "literal" style.
 *
 * @author sali
 */
public class LiteralHandler implements InlineStyleHandler {

    private static final String COURIER_NEW = "Courier New";

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        return rprBuilder.withRFonts(WmlBuilderFactory.getRFontsBuilder().withAscii(COURIER_NEW).withHAnsi(COURIER_NEW)
                .withCs(COURIER_NEW).getObject());
    }
}
