package com.alphasystem.docbook.handler.impl.inline;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.openxml.builder.wml.RPrBuilder;

/**
 * Handles "bold" style.
 *
 * @author sali
 */
class BoldHandler implements InlineStyleHandler {

    BoldHandler() {
    }

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        return rprBuilder.withB(true).withBCs(true);
    }
}
