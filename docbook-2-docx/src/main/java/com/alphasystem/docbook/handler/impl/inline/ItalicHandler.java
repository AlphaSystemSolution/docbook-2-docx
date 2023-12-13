package com.alphasystem.docbook.handler.impl.inline;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.openxml.builder.wml.RPrBuilder;

/**
 * Handles "italic" style.
 *
 * @author sali
 */
class ItalicHandler implements InlineStyleHandler {

    ItalicHandler() {
    }

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        return rprBuilder.withI(true).withICs(true);
    }
}
