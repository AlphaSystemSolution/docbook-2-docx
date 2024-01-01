package com.alphasystem.docbook.util;

import com.alphasystem.docbook.builder2.BuilderFactory;
import com.alphasystem.docbook.builder2.impl.block.AbstractTableBuilder;
import com.alphasystem.openxml.builder.wml.PPrBuilder;
import com.alphasystem.openxml.builder.wml.WmlBuilderFactory;
import com.alphasystem.openxml.builder.wml.table.ColumnData;
import com.alphasystem.openxml.builder.wml.table.VerticalMergeType;
import com.alphasystem.xml.UnmarshallerConstants;
import com.alphasystem.xml.UnmarshallerUtils;
import org.docbook.model.Align;
import org.docbook.model.BasicVerticalAlign;
import org.docbook.model.Entry;
import org.docbook.model.VerticalAlign;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.P;
import org.docx4j.wml.STVerticalJc;

public class ColumnBuilder {

    private ColumnBuilder() {
    }

    /**
     * @param parentBuilder       table builder
     * @param entry               DocBook column entry
     * @param columnIndex         current columnIndex
     * @param parentVerticalAlign Table VerticalAlign
     * @return next column info
     */
    public static NextColumnInfo build(AbstractTableBuilder<?> parentBuilder, BuilderFactory builderFactory,
                                       Entry entry, int columnIndex, VerticalAlign parentVerticalAlign) {
        final var tcPr = WmlBuilderFactory.getTcPrBuilder().withVAlign(getVerticalAlign(entry.getValign(), parentVerticalAlign))
                .getObject();
        final var gridSpan = parentBuilder.getGridSpan(entry.getNameStart(), entry.getNameEnd());
        final var moreRows = UnmarshallerUtils.toInt(entry.getMoreRows(), 0);
        VerticalMergeType vMergeType = moreRows <= 0 ? VerticalMergeType.CONTINUE : VerticalMergeType.RESTART;

        final var columnContent = entry.getContent().stream().map(content -> {
            var align = getAlign(entry.getAlign());
            if (UnmarshallerConstants.isParaTypes(content)){
                var p = (P) builderFactory.process(content).get(0);
                final var ppr = new PPrBuilder(WmlBuilderFactory.getPPrBuilder().withJc(align).getObject(), p.getPPr()).getObject();
                p.setPPr(ppr);
                return p;
            } else {
                return builderFactory.process(content);
            }
        }).toArray();

        var columnData = new ColumnData(columnIndex).withColumnProperties(tcPr).withGridSpanValue(gridSpan)
                .withVerticalMergeType(vMergeType).withContent(columnContent);
        parentBuilder.getTableAdapter().addColumn(columnData);

        return new NextColumnInfo(moreRows, columnIndex, columnIndex + gridSpan);
    }

    private static STVerticalJc getVerticalAlign(BasicVerticalAlign vAlign, VerticalAlign parentVerticalAlign) {
        if (vAlign == null && parentVerticalAlign != null) {
            try {
                vAlign = BasicVerticalAlign.fromValue(parentVerticalAlign.value());
            } catch (Exception e) {
                // ignore, if the parent value is BASELINE, then there is no corresponding value for BasicVerticalAlign
            }
        }

        STVerticalJc val = null;
        if (vAlign != null) {
            switch (vAlign) {
                case BOTTOM:
                    val = STVerticalJc.BOTTOM;
                    break;
                case MIDDLE:
                    val = STVerticalJc.CENTER;
                    break;
                case TOP:
                    val = STVerticalJc.TOP;
                    break;
            }
        }
        return val;
    }

    private static JcEnumeration getAlign(Align align) {
        JcEnumeration jcEnumeration = null;
        if (align != null) {
            switch (align) {
                case LEFT:
                    jcEnumeration = JcEnumeration.LEFT;
                    break;
                case CENTER:
                    jcEnumeration = JcEnumeration.CENTER;
                    break;
                case RIGHT:
                    jcEnumeration = JcEnumeration.RIGHT;
                    break;
                case JUSTIFY:
                    jcEnumeration = JcEnumeration.BOTH;
                    break;
            }
        }
        return jcEnumeration;
    }

    public static class NextColumnInfo {

        private final int moreRows;
        private final int currentColumnIndex;
        private final int nextColumnIndex;

        public NextColumnInfo(int moreRows, int currentColumnIndex, int nextColumnIndex) {
            this.moreRows = Math.max(moreRows, 0);
            this.currentColumnIndex = Math.max(currentColumnIndex, 0);
            this.nextColumnIndex = Math.max(nextColumnIndex, 0);
        }

        public int getMoreRows() {
            return moreRows;
        }

        public int getCurrentColumnIndex() {
            return currentColumnIndex;
        }

        public int getNextColumnIndex() {
            return nextColumnIndex;
        }

        public NextColumnInfo decrementMoreRows() {
            return new NextColumnInfo(moreRows - 1, currentColumnIndex, nextColumnIndex);
        }

        @Override
        public String toString() {
            return "NextColumnInfo (" +
                    "moreRows=" + moreRows +
                    ", currentColumnIndex=" + currentColumnIndex +
                    ", nextColumnIndex=" + nextColumnIndex +
                    ")";
        }
    }
}
