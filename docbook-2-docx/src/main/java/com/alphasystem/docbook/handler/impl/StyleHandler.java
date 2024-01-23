package com.alphasystem.docbook.handler.impl;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.docx4j.builder.wml.RPrBuilder;
import com.alphasystem.docx4j.builder.wml.WmlBuilderFactory;

/**
 * Handles style with given name.
 *
 * @author sali
 */
public class StyleHandler implements InlineStyleHandler {

    protected final String styleName;

    public StyleHandler(String styleName) {
        this.styleName = styleName;
    }

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        return rprBuilder.withRStyle(WmlBuilderFactory.getRStyleBuilder().withVal(styleName).getObject());
    }
}
