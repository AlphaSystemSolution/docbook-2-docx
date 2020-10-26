package com.alphasystem.docbook.handler.impl.block;

import com.alphasystem.docbook.handler.BlockHandler;
import com.alphasystem.openxml.builder.wml.TableAdapter;
import org.docx4j.wml.Tbl;

/**
 * @author sali
 */
public class ScreenHandler implements BlockHandler<Tbl> {

    @Override
    public Tbl handleBlock() {
        return new TableAdapter(1)
                .startTable("SourceCode")
                .startRow()
                .addColumn(0, null, null, (Object[]) null)
                .endRow()
                .getTable();
    }
}
