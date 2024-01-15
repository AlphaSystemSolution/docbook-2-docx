package com.alphasystem.docbook.handler.impl;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.openxml.builder.wml.RPrBuilder;
import org.docx4j.wml.RFonts;

import static com.alphasystem.openxml.builder.wml.WmlBuilderFactory.getRFontsBuilder;

/**
 * @author sali
 */
public abstract class ArabicHandler implements InlineStyleHandler {

    private static final String ARABIC_FONT_NAME = "Arabic Typesetting";
    private static final long DEFAULT_SIZE = 36;

    private final long size;

    protected ArabicHandler() {
        this(DEFAULT_SIZE);
    }

    protected ArabicHandler(long size) {
        this.size = (size <= 0) ? DEFAULT_SIZE : size;
    }

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        final RFonts rFonts = getRFontsBuilder().withAscii(ARABIC_FONT_NAME).withHAnsi(ARABIC_FONT_NAME)
                .withCs(ARABIC_FONT_NAME).getObject();
        return rprBuilder.withRFonts(rFonts).withSz(size).withSzCs(size).withRtl(true);
    }
}
