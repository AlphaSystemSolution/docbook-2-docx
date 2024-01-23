package com.alphasystem.docbook.handler.impl;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.docx4j.builder.wml.RPrBuilder;

import static org.docx4j.wml.STVerticalAlignRun.SUPERSCRIPT;

/**
 * Handles "superscript" style.
 *
 * @author sali
 */
public class SuperscriptHandler implements InlineStyleHandler {

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        return rprBuilder.withVertAlign(SUPERSCRIPT);
    }
}
