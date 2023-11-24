package com.alphasystem.docbook.handler.impl;

import com.alphasystem.openxml.builder.wml.RPrBuilder;

class ArabicBoldHandler extends ArabicHandler {

    public ArabicBoldHandler(){}

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        return super.applyStyle(rprBuilder).withB(true).withBCs(true);
    }
}
