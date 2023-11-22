package com.alphasystem.docbook.handler.impl;

import org.docx4j.wml.RFonts;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.openxml.builder.wml.RPrBuilder;

import static com.alphasystem.openxml.builder.wml.WmlBuilderFactory.getRFontsBuilder;

/**
 * @author sali
 */
abstract class ArabicHandler implements InlineStyleHandler {

    private static final String ARABIC_FONT_NAME = "KFGQPC Uthman Taha Naskh";
    private static final long DEFAULT_SIZE = 22;
    static final String ARABIC_HEADING_1 = "arabicHeading1";
    static final String ARABIC_TABLE_CAPTION = "arabicTableCaption";
    static final String ARABIC_NORMAL = "arabicNormal";
    static final String ARABIC_BOLD = "arabicBold";
    static final String ARABIC_NORMAL_HIGHLIGHT = "arabicNormalHighlight";

    private final long size;

    ArabicHandler() {
        this(DEFAULT_SIZE);
    }

    ArabicHandler(long size) {
        this.size = (size <= 0) ? DEFAULT_SIZE : size;
    }

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        final RFonts rFonts = getRFontsBuilder().withAscii(ARABIC_FONT_NAME).withHAnsi(ARABIC_FONT_NAME)
                .withCs(ARABIC_FONT_NAME).getObject();
        return rprBuilder.withRFonts(rFonts).withSz(size).withSzCs(size).withRtl(true);
    }
}
