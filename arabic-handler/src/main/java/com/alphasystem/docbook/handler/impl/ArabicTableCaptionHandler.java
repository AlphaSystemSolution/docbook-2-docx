package com.alphasystem.docbook.handler.impl;

import org.docx4j.wml.Color;

import com.alphasystem.openxml.builder.wml.RPrBuilder;

import static com.alphasystem.openxml.builder.wml.WmlBuilderFactory.getColorBuilder;
import static org.docx4j.wml.STThemeColor.TEXT_2;

/**
 * @author sali
 */
public class ArabicTableCaptionHandler extends ArabicBoldHandler {

    public ArabicTableCaptionHandler() {
        super(24);
    }

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        final Color color = getColorBuilder().withVal("099BDD").withThemeColor(TEXT_2).getObject();
        return super.applyStyle(rprBuilder).withColor(color);
    }
}
