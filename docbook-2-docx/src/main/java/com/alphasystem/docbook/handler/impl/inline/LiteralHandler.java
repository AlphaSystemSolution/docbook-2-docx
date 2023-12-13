package com.alphasystem.docbook.handler.impl.inline;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.openxml.builder.wml.RPrBuilder;

import static com.alphasystem.openxml.builder.wml.WmlBuilderFactory.getRFontsBuilder;

/**
 * Handles "literal" style.
 *
 * @author sali
 */
class LiteralHandler implements InlineStyleHandler {

    private static final String COURIER_NEW = "Courier New";

    LiteralHandler() {
    }

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        return rprBuilder.withRFonts(getRFontsBuilder().withAscii(COURIER_NEW).withHAnsi(COURIER_NEW).withCs(COURIER_NEW)
                .getObject());
    }
}
