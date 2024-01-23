package com.alphasystem.docbook.handler.impl;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.docbook.model.ColorCode;
import com.alphasystem.docx4j.builder.wml.RPrBuilder;
import com.alphasystem.docx4j.builder.wml.WmlAdapter;

/**
 * Handles style with given color name.
 *
 * @author sali
 */
public class ColorHandler implements InlineStyleHandler {

    private final ColorCode colorCode;

    public ColorHandler(ColorCode colorCode) {
        this.colorCode = colorCode;
    }

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        return rprBuilder.withColor(WmlAdapter.getColor(colorCode.getCode()));
    }
}
