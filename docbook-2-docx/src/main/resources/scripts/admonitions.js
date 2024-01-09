var WmlAdapter = Java.type("com.alphasystem.openxml.builder.wml.WmlAdapter");
var WmlBuilderFactory = Java.type("com.alphasystem.openxml.builder.wml.WmlBuilderFactory");
var TableAdapter = Java.type("com.alphasystem.openxml.builder.wml.table.TableAdapter");
var ColumnData = Java.type("com.alphasystem.openxml.builder.wml.table.ColumnData");
var TableType = Java.type("com.alphasystem.openxml.builder.wml.table.TableType");
var IdGenerator = Java.type("com.alphasystem.util.IdGenerator");
var STBorder = Java.type("org.docx4j.wml.STBorder");
var STThemeColor = Java.type("org.docx4j.wml.STThemeColor");
var STShd = Java.type("org.docx4j.wml.STShd");

var STYLE_SUFFIX = "Caption";
var BORDER_SIZE = 8;
var FILL_COLOR = "F2F2F2";

function getAdmonitionStyleId(admonitionType) {
    return admonitionType + STYLE_SUFFIX;
};

function getTableBorders(colorValue) {
    return WmlBuilderFactory.getTblBordersBuilder()
                    .withTop(WmlAdapter.getBorder(STBorder.SINGLE, BORDER_SIZE, 0, colorValue))
                    .withBottom(WmlAdapter.getBorder(STBorder.SINGLE, BORDER_SIZE, 0, colorValue))
                    .withLeft(WmlAdapter.getBorder(STBorder.SINGLE, BORDER_SIZE, 0, colorValue))
                    .withRight(WmlAdapter.getBorder(STBorder.SINGLE, BORDER_SIZE, 0, colorValue))
                    .withInsideH(WmlAdapter.getBorder(STBorder.SINGLE, BORDER_SIZE, 0, colorValue))
                    .withInsideV(WmlAdapter.getBorder(STBorder.SINGLE, BORDER_SIZE, 0, colorValue))
                    .getObject();
};

function getCaptionColumnProperties(colorValue) {
    return WmlBuilderFactory.getTcPrBuilder()
                    .withTcBorders(WmlBuilderFactory.getTcPrInnerBuilder().getTcBordersBuilder()
                            .withBottom(WmlAdapter.getNilBorder())
                            .withRight(WmlAdapter.getBorder(STBorder.SINGLE, BORDER_SIZE, 0, colorValue))
                            .getObject())
                    .withShd(WmlBuilderFactory.getCTShdBuilder().withVal(STShd.CLEAR).withColor("auto")
                            .withFill(FILL_COLOR).withThemeFill(STThemeColor.BACKGROUND_1)
                            .withThemeFillShade("F2").getObject())
                    .getObject();
};

function getEmptyColumnProperties(colorValue) {
    return WmlBuilderFactory.getTcPrBuilder()
                .withTcBorders(WmlBuilderFactory.getTcPrInnerBuilder().getTcBordersBuilder()
                        .withBottom(WmlAdapter.getBorder(STBorder.SINGLE, BORDER_SIZE, 0, colorValue))
                        .withLeft(WmlAdapter.getBorder(STBorder.SINGLE, BORDER_SIZE, 0, colorValue))
                        .withTop(WmlAdapter.getNilBorder())
                        .withRight(WmlAdapter.getNilBorder())
                        .getObject())
                .getObject();
};

function getContentColumnProperties() {
    return WmlBuilderFactory.getTcPrBuilder()
                    .withTcBorders(WmlBuilderFactory.getTcPrInnerBuilder().getTcBordersBuilder()
                            .withTop(WmlAdapter.getNilBorder())
                            .getObject())
                    .withShd(WmlBuilderFactory.getCTShdBuilder().withVal(STShd.CLEAR).withColor("auto")
                            .withFill(FILL_COLOR).withThemeFill(STThemeColor.BACKGROUND_1)
                            .withThemeFillShade("F2").getObject())
                    .getObject();
};

function getAdmonitionCaption(admonitionType, captionText) {
     return WmlAdapter.getParagraphWithStyle(getAdmonitionStyleId(admonitionType), captionText);
};

function admonitionTable(admonitionType, captionText, colorValue, indentLevel, content) {
    var tblPr = WmlBuilderFactory.getTblPrBuilder().withTblBorders(getTableBorders(colorValue)).getObject();
    var row1Column0 = new ColumnData(0).withColumnProperties(getCaptionColumnProperties(colorValue))
                .withContent(getAdmonitionCaption(admonitionType, captionText));
    var row1Column1 = new ColumnData(1).withColumnProperties(getEmptyColumnProperties(colorValue))
                .withContent(WmlAdapter.getEmptyParaNoSpacing());
    var row2Column0 = new ColumnData(0).withColumnProperties(getContentColumnProperties())
                .withGridSpanValue(2).withContent(content);
    return new TableAdapter()
                .withTableType(TableType.AUTO)
                .withWidths(30.0, 70.0)
                .withIndentLevel(indentLevel)
                .withTableProperties(tblPr)
                .startTable()
                .startRow()
                .addColumn(row1Column0)
                .addColumn(row1Column1)
                .endRow()
                .startRow()
                .addColumn(row2Column0)
                .endRow()
                .getTable();
};
