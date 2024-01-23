package com.alphasystem.docbook.handler.impl;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.docx4j.builder.wml.RPrBuilder;

/**
 * Handles "italic" style.
 *
 * @author sali
 */
public class ItalicHandler implements InlineStyleHandler {

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        return rprBuilder.withI(true).withICs(true);
    }
}
