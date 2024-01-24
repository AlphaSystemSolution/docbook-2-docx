var ConfigurationUtils = Java.type("com.alphasystem.docbook.util.ConfigurationUtils")
var WmlAdapter = Java.type("com.alphasystem.docx4j.builder.wml.WmlAdapter");
var WmlBuilderFactory = Java.type("com.alphasystem.docx4j.builder.wml.WmlBuilderFactory");
var RPrBuilder = Java.type("com.alphasystem.docx4j.builder.wml.RPrBuilder")
var STThemeColor = Java.type("org.docx4j.wml.STThemeColor")

function arabicHandler(rPrBuilder, configPath, rtl) {
    var config = ConfigurationUtils.getInstance().getConfig(configPath);
    var fontName = config.getString("font-name");
    var fontSize = config.getInt("font-size");
    var rFonts = WmlBuilderFactory.getRFontsBuilder().withAscii(fontName).withHAnsi(fontName)
                    .withCs(fontName).getObject();
    return rPrBuilder.withRFonts(rFonts).withSz(fontSize).withSzCs(fontSize).withRtl(rtl);
}

function arabicNormal(rPrBuilder) {
    return arabicHandler(rPrBuilder, "arabic.normal", true);
}

function arabicSmall(rPrBuilder) {
    return arabicHandler(rPrBuilder, "arabic.small", true);
}

function translation(rPrBuilder) {
    return arabicHandler(rPrBuilder, "arabic.translation", false);
}

function arabicBold(rPrBuilder) {
    var updateValue = arabicHandler(rPrBuilder, fontName, fontSize, true);
    return updateValue.withB(true).withBCs(true);
}

function arabicHeading1(rPrBuilder) {
    return arabicHandler(rPrBuilder, "arabic.heading", true);
}

function arabicTableCaption(rPrBuilder) {
    var config = ConfigurationUtils.getInstance().getConfig("arabic.caption");
    var colorValue = config.getString("color");
    var color = WmlBuilderFactory.getColorBuilder().withVal(colorValue).withThemeColor(STThemeColor.TEXT_2).getObject();
    var updateValue = arabicHandler(rPrBuilder, "arabic.caption", true);
    return updateValue.withColor(color).withB(true).withBCs(true);
}
