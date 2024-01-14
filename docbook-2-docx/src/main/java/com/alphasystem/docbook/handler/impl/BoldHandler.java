package com.alphasystem.docbook.handler.impl;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.openxml.builder.wml.RPrBuilder;

/**
 * Handles "bold" style.
 *
 * @author sali
 */
public class BoldHandler implements InlineStyleHandler {

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        return rprBuilder.withB(true).withBCs(true);
    }
}
