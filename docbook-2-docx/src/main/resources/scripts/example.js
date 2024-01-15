var WmlAdapter = Java.type("com.alphasystem.openxml.builder.wml.WmlAdapter");
var WmlBuilderFactory = Java.type("com.alphasystem.openxml.builder.wml.WmlBuilderFactory");
var TableAdapter = Java.type("com.alphasystem.openxml.builder.wml.table.TableAdapter");
var ColumnData = Java.type("com.alphasystem.openxml.builder.wml.table.ColumnData");
var TableType = Java.type("com.alphasystem.openxml.builder.wml.table.TableType");
var STShd = Java.type("org.docx4j.wml.STShd");

function exampleTable(content) {
    var border = WmlAdapter.getBorder(STBorder.SINGLE, 4, 0, "E0E0DC");
    var tblBorders = WmlBuilderFactory.getTblBordersBuilder().withTop(border).withLeft(border)
                        .withBottom(border).withRight(border).withInsideH(border).withInsideV(border)
                        .getObject();
    var tblPr = WmlBuilderFactory.getTblPrBuilder().withTblBorders(tblBorders).getObject();
    var shade = WmlBuilderFactory.getCTShdBuilder().withVal(STShd.CLEAR).withColor("auto")
                    .withFill("FFFEF7").getObject();
    var tcPr = WmlBuilderFactory.getTcPrBuilder().withShd(shade).getObject();

    var shade = WmlBuilderFactory.getCTShdBuilder().withVal(STShd.CLEAR).withColor("auto").withFill("FFFFE0").getObject();
    var tcPr = WmlBuilderFactory.getTcPrBuilder().withShd(shade).withTcBorders(WmlAdapter.getNilBorders())
                    .withTcBorders(WmlAdapter.getNilBorders()).getObject();
    return new TableAdapter()
                .withWidths(100.0)
                .withTableProperties(tblPr)
                .startTable()
                .startRow()
                .addColumn(new ColumnData(0).withColumnProperties(tcPr).withContent(content))
                .endRow()
                .getTable();
}