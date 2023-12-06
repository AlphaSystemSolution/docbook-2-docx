package com.alphasystem.docbook.handler.impl.inline;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.openxml.builder.wml.RPrBuilder;

/**
 *  Handles highlighted text
 * @author  sali
 */
public class HighlightHandler implements InlineStyleHandler {
    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        return rprBuilder.withHighlight("yellow");
    }
}
