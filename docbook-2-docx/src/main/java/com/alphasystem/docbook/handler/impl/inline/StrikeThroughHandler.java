package com.alphasystem.docbook.handler.impl.inline;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.openxml.builder.wml.RPrBuilder;

/**
 * Handles "strike through" style.
 *
 * @author sali
 */
class StrikeThroughHandler implements InlineStyleHandler {

    StrikeThroughHandler() {
    }

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        return rprBuilder.withStrike(true);
    }
}
