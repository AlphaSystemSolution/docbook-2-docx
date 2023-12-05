package com.alphasystem.docbook.handler.impl;

import com.alphasystem.openxml.builder.wml.RPrBuilder;

/**
 * @author sali
 */
class ArabicNormalWithHighlightHandler extends ArabicHandler {

    ArabicNormalWithHighlightHandler() {
    }

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        return super.applyStyle(rprBuilder).withHighlight("yellow");
    }
}
