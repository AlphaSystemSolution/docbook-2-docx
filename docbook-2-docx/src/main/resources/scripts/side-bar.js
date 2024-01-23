var WmlAdapter = Java.type("com.alphasystem.docx4j.builder.wml.WmlAdapter");
var WmlBuilderFactory = Java.type("com.alphasystem.docx4j.builder.wml.WmlBuilderFactory");
var TableAdapter = Java.type("com.alphasystem.docx4j.builder.wml.table.TableAdapter");
var ColumnData = Java.type("com.alphasystem.docx4j.builder.wml.table.ColumnData");
var TableType = Java.type("com.alphasystem.docx4j.builder.wml.table.TableType");
var STShd = Java.type("org.docx4j.wml.STShd");

function sideBarTable(title, content) {
    var shade = WmlBuilderFactory.getCTShdBuilder().withVal(STShd.CLEAR).withColor("auto").withFill("F2F2F2").getObject();
    var tcPr = WmlBuilderFactory.getTcPrBuilder().withShd(shade).withTcBorders(WmlAdapter.getNilBorders())
                    .withTcBorders(WmlAdapter.getNilBorders()).getObject();

    var row1Column0 = new ColumnData(0).withColumnProperties(tcPr).withContent(title);
    var row2Column0 = new ColumnData(0).withColumnProperties(tcPr).withContent(content);
    return new TableAdapter()
                .withNumOfColumns(1)
                .withTableType(TableType.AUTO)
                .startTable()
                .startRow()
                .addColumn(row1Column0)
                .endRow()
                .startRow()
                .addColumn(row2Column0)
                .endRow()
                .getTable();
}
