package com.alphasystem.docbook.handler.impl;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.openxml.builder.wml.RPrBuilder;

import static com.alphasystem.openxml.builder.wml.WmlBuilderFactory.getRStyleBuilder;

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
        return rprBuilder.withRStyle(getRStyleBuilder().withVal(styleName).getObject());
    }
}
