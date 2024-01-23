package com.alphasystem.docbook.handler.impl;

import com.alphasystem.docx4j.builder.wml.RPrBuilder;

public class ArabicBoldHandler extends ArabicHandler {

    public ArabicBoldHandler(){}

    public ArabicBoldHandler(long size) {
        super(size);
    }

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        return super.applyStyle(rprBuilder).withB(true).withBCs(true);
    }
}
