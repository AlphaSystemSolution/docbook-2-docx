package com.alphasystem.docbook.handler.impl;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.docx4j.builder.wml.RPrBuilder;
import com.alphasystem.docx4j.builder.wml.WmlBuilderFactory;

import static org.docx4j.wml.UnderlineEnumeration.SINGLE;

/**
 * Handles "underline" style.
 *
 * @author sali
 */
public class UnderlineHandler implements InlineStyleHandler {

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        return rprBuilder.withU(WmlBuilderFactory.getUBuilder().withVal(SINGLE).getObject());
    }
}
