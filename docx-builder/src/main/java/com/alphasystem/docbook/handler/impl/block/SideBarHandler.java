package com.alphasystem.docbook.handler.impl.block;

import com.alphasystem.docbook.handler.BlockHandler;
import com.alphasystem.openxml.builder.wml.WmlAdapter;
import com.alphasystem.openxml.builder.wml.table.TableAdapter;
import org.docx4j.wml.*;

import static com.alphasystem.openxml.builder.wml.WmlBuilderFactory.*;
import static org.docx4j.wml.STBorder.SINGLE;

/**
 * @author sali
 */
public class SideBarHandler implements BlockHandler<Tbl> {

    @Override
    public Tbl handleBlock() {
        final CTBorder border = WmlAdapter.getBorder(SINGLE, 4L, 0L, "E0E0DC");
        final TblBorders tblBorders = getTblBordersBuilder().withTop(border).withLeft(border)
                .withBottom(border).withRight(border).withInsideH(border).withInsideV(border).getObject();

        final TblPr tblPr = getTblPrBuilder().withTblBorders(tblBorders).getObject();

        final CTShd shade = getCTShdBuilder().withVal(STShd.CLEAR).withColor("auto").withFill("F8F8F7").getObject();
        final TcPr tcPr = getTcPrBuilder().withShd(shade).getObject();

        return new TableAdapter().startTable(tblPr, 100.0)
                .startRow().addColumn(0, null, tcPr, (Object[]) null)
                .endRow()
                .getTable();
    }
}
