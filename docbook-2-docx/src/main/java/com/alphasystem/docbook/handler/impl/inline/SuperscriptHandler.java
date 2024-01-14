package com.alphasystem.docbook.handler.impl.inline;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.openxml.builder.wml.RPrBuilder;

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
