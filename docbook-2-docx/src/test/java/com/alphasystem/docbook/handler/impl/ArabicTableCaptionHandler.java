package com.alphasystem.docbook.handler.impl;

import com.alphasystem.docx4j.builder.wml.RPrBuilder;
import com.alphasystem.docx4j.builder.wml.WmlBuilderFactory;
import org.docx4j.wml.Color;

import static org.docx4j.wml.STThemeColor.TEXT_2;

/**
 * @author sali
 */
public class ArabicTableCaptionHandler extends ArabicHandler {

    public ArabicTableCaptionHandler() {
        super(40);
    }

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        final Color color = WmlBuilderFactory.getColorBuilder().withVal("099BDD").withThemeColor(TEXT_2).getObject();
        return super.applyStyle(rprBuilder).withColor(color);
    }
}
