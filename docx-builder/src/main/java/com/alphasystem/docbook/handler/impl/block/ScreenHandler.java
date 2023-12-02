package com.alphasystem.docbook.handler.impl.block;

import com.alphasystem.docbook.handler.BlockHandler;
import com.alphasystem.openxml.builder.wml.table.TableAdapter;
import org.docx4j.wml.Tbl;

/**
 * @author sali
 */
public class ScreenHandler implements BlockHandler<Tbl> {

    @Override
    public Tbl handleBlock() {
        return new TableAdapter()
                .startTable("SourceCode", 100.0)
                .startRow()
                .addColumn(0, null, null, (Object[]) null)
                .endRow()
                .getTable();
    }
}
