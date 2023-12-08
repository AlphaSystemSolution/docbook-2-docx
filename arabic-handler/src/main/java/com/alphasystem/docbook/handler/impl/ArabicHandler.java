package com.alphasystem.docbook.handler.impl;

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
    static final String ARABIC_SMALL = "arabicSmall";
    static final String ARABIC_BOLD = "arabicBold";
    static final String TRANSLATION = "translation";
    static final String ARABIC_NORMAL_HIGHLIGHT = "arabicNormalHighlight";

    private final long size;
    private final String fontName;
    private final boolean rtl;


    ArabicHandler() {
        this(DEFAULT_SIZE);
    }

    ArabicHandler(long size) {
        this(size, ARABIC_FONT_NAME, true);
    }

    public ArabicHandler(long size, String fontName, boolean rtl) {
        this.size = (size <= 0) ? DEFAULT_SIZE : size;
        this.fontName = fontName;
        this.rtl = rtl;
    }

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        final var rFonts = getRFontsBuilder().withAscii(fontName).withHAnsi(fontName)
                .withCs(fontName).getObject();
        return rprBuilder.withRFonts(rFonts).withSz(size).withSzCs(size).withRtl(rtl);
    }
}
