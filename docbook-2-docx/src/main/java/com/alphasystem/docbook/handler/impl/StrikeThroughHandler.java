package com.alphasystem.docbook.handler.impl;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.docx4j.builder.wml.RPrBuilder;

/**
 * Handles "strike through" style.
 *
 * @author sali
 */
public class StrikeThroughHandler implements InlineStyleHandler {

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        return rprBuilder.withStrike(true);
    }
}
