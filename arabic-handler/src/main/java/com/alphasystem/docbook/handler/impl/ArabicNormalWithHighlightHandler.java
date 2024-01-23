package com.alphasystem.docbook.handler.impl;

import com.alphasystem.docx4j.builder.wml.RPrBuilder;
/**
 * @author sali
 */
public class ArabicNormalWithHighlightHandler extends ArabicHandler {

    public ArabicNormalWithHighlightHandler() {
    }

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        return super.applyStyle(rprBuilder).withHighlight("yellow");
    }
}
