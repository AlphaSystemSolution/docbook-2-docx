package com.alphasystem.docbook.handler.impl.inline;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.openxml.builder.wml.RPrBuilder;

import static org.docx4j.wml.STVerticalAlignRun.SUBSCRIPT;

/**
 * Handles "subscript" style.
 *
 * @author sali
 */
class SubscriptHandler implements InlineStyleHandler {

    SubscriptHandler() {
    }

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        return rprBuilder.withVertAlign(SUBSCRIPT);
    }
}
