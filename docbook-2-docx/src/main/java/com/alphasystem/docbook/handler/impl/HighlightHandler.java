package com.alphasystem.docbook.handler.impl;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.docx4j.builder.wml.RPrBuilder;

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
