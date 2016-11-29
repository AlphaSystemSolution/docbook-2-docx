package com.alphasystem.docbook.handler.impl.block;

import com.alphasystem.docbook.handler.BlockHandler;
import com.alphasystem.openxml.builder.wml.TableAdapter;
import org.docx4j.wml.*;

import static com.alphasystem.openxml.builder.wml.WmlAdapter.getBorder;
import static com.alphasystem.openxml.builder.wml.WmlBuilderFactory.*;
import static org.docx4j.wml.STBorder.SINGLE;

/**
 * @author sali
 */
public class ExampleHandler implements BlockHandler<Tbl> {

    @Override
    public Tbl handleBlock() {
        final CTBorder border = getBorder(SINGLE, 4L, 0L, "E0E0DC");
        final TblBorders tblBorders = getTblBordersBuilder().withTop(border).withLeft(border)
                .withBottom(border).withRight(border).withInsideH(border).withInsideV(border).getObject();

        final TblPr tblPr = getTblPrBuilder().withTblBorders(tblBorders).getObject();

        final CTShd shade = getCTShdBuilder().withVal(STShd.CLEAR).withColor("auto")
                .withFill("FFFEF7").getObject();
        final TcPr tcPr = getTcPrBuilder().withShd(shade).getObject();

        return new TableAdapter(1).startTable(tblPr).startRow()
                .addColumn(0, null, tcPr, (Object[]) null).endRow().getTable();
    }
}
