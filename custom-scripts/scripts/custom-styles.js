var ConfigurationUtils = Java.type("com.alphasystem.docbook.util.ConfigurationUtils")
var WmlAdapter = Java.type("com.alphasystem.docx4j.builder.wml.WmlAdapter");
var WmlBuilderFactory = Java.type("com.alphasystem.docx4j.builder.wml.WmlBuilderFactory");

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