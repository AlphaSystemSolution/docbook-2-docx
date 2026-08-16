var ConfigurationUtils = Java.type("com.alphasystem.docbook.util.ConfigurationUtils")
var WmlAdapter = Java.type("com.alphasystem.docx4j.builder.wml.WmlAdapter");
var WmlBuilderFactory = Java.type("com.alphasystem.docx4j.builder.wml.WmlBuilderFactory");
var RPrBuilder = Java.type("com.alphasystem.docx4j.builder.wml.RPrBuilder")
var STThemeColor = Java.type("org.docx4j.wml.STThemeColor")

function arabicHandler(rPrBuilder, configPath, rtl) {
    const config = ConfigurationUtils.getInstance().getAppConfig().getConfig(configPath);
    const fontName = config.getString("font-name");
    const fontSize = config.getInt("font-size");
    const rFonts = WmlBuilderFactory.getRFontsBuilder().withAscii(fontName).withHAnsi(fontName)
        .withCs(fontName).getObject();
    return rPrBuilder.withRFonts(rFonts).withSz(fontSize).withSzCs(fontSize).withRtl(rtl);
}

function arabicNormal() {
    return arabicHandler(WmlBuilderFactory.getRPrBuilder(), "arabic.normal", true).getObject();
}

function arabicSmall() {
    return arabicHandler(WmlBuilderFactory.getRPrBuilder(), "arabic.small", true).getObject();
}

function arabicSmallGray() {
    const color = WmlBuilderFactory.getColorBuilder().withVal("gray").getObject();
    return arabicHandler(WmlBuilderFactory.getRPrBuilder().withColor(color), "arabic.small", true).getObject();
}

function arabicBold() {
    const rPrBuilder = arabicHandler(WmlBuilderFactory.getRPrBuilder(), "arabic.bold", true);
    return rPrBuilder.withB(true).withBCs(true).getObject();
}

function arabicHeading1() {
    return arabicHandler(WmlBuilderFactory.getRPrBuilder(), "arabic.heading", true).getObject();
}

function arabicTableCaption() {
    const config = ConfigurationUtils.getInstance().getAppConfig().getConfig("arabic.caption");
    const colorValue = config.getString("color");
    const color = WmlBuilderFactory.getColorBuilder().withVal(colorValue).withThemeColor(STThemeColor.TEXT_2).getObject();
    const rPrBuilder = arabicHandler(WmlBuilderFactory.getRPrBuilder(), "arabic.caption", true);
    return rPrBuilder.withColor(color).withB(true).withBCs(true).getObject();
}

function translation() {
    return arabicHandler(WmlBuilderFactory.getRPrBuilder(), "arabic.translation", false).getObject();
}

function arabicParagraph() {
    var styleId = "ArabicParagraph";
    var appConfig = ConfigurationUtils.getInstance().getAppConfig();
    var fontName = appConfig.getString("arabic.normal.font-name");
    var fontSize = appConfig.getLong("arabic.normal.font-size");
    var pPr = WmlBuilderFactory.getPPrBuilder().withBidi(true).getObject();
    var rFonts = WmlBuilderFactory.getRFontsBuilder().withAscii(fontName).withHAnsi(fontName).getObject();
    var rPr = WmlBuilderFactory.getRPrBuilder().withRFonts(rFonts).withSz(fontSize).getObject();
    return WmlBuilderFactory.getStyleBuilder().withType("paragraph").withCustomStyle(true).withBasedOn("Normal").withStyleId(styleId)
        .withName(styleId).withRsid("00A401CB").withPPr(pPr).withRPr(rPr).getObject();
}