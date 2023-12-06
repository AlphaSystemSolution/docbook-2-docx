package com.alphasystem.docbook.handler.impl.block;

import com.alphasystem.docbook.handler.BlockHandler;
import com.alphasystem.openxml.builder.wml.table.ColumnData;
import com.alphasystem.openxml.builder.wml.table.TableAdapter;
import org.docx4j.wml.Tbl;

/**
 * @author sali
 */
public class ScreenHandler implements BlockHandler<Tbl> {

    @Override
    public Tbl handleBlock() {
        return new TableAdapter()
                .withTableStyle("SourceCode")
                .withWidths(100.0)
                .startTable()
                .startRow()
                .addColumn(new ColumnData(0))
                .endRow()
                .getTable();
    }
}
