package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.docbook.builder2.impl.AbstractBuilder;
import com.alphasystem.openxml.builder.wml.table.VerticalMergeType;
import com.alphasystem.util.AppUtil;
import com.alphasystem.xml.UnmarshallerUtils;
import org.docbook.model.Entry;
import org.docbook.model.ObjectFactory;
import org.docbook.model.Row;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TableContentBuilder<S> extends AbstractBuilder<S> {

    private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();

    // flag to indicate that one or table entries has row span, so we need to sanitize rows
    private boolean hasRowSpan = false;

    protected TableContentBuilder(S source, Builder<?> parent) {
        super(null, source, parent);
    }

    /*
     * Row span in DocBook works using property "morerows" in "Entry" object, which indicates how many more rows this
     * column will span. For this reason, one row can have more of entries than others.
     *
     * In Docx has row span works using "VMerge" property. First column must have the value "restart" and subsequent columns
     * must have "VMerge" property without any value. For this reason, number of columns in Docx are always same in each row.
     *
     * We need to scan "rows" and sanitize data to have exact number "entries" in order to row span to work correctly.
     *
     * More over it is possible in the DocBook number of "rows" less if each column of last row is spanning. First step
     * is to find out total number of actual rows, second step to fill missing columns.
     *
     */
    protected List<Object> sanitizeRows(List<Row> rows) {
        final var totalRows = getTotalNumberOfRows(rows);

        if (!hasRowSpan) {
            // no need to do any sanitization
            return new ArrayList<>(rows);
        }

        final var parent = (AbstractTableBuilder<?>) getParent();
        final var numOfColumns = parent.getColumnInfoList().size();

        // final data
        final var entries = new Object[totalRows][numOfColumns];

        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            var columnIndex = 0;
            final var row = rows.get(rowIndex);
            final var contents = row.getContent();

            for (Object content : contents) {
                // find the index of first null value from the result
                while (true) {
                    final var obj = entries[rowIndex][columnIndex];
                    if (obj == null) {
                        break;
                    } else {
                        columnIndex += 1;
                    }
                }

                // fill corresponding value in the result
                if (AppUtil.isInstanceOf(Entry.class, content)) {
                    final var entry = (Entry) content;
                    final var moreRows = UnmarshallerUtils.toInt(entry.getMoreRows(), 0);

                    // we are populating with our value for "morerows" field
                    final var vMerge = moreRows <= 0 ? VerticalMergeType.NONE : VerticalMergeType.RESTART;
                    entry.setMoreRows(vMerge.name());
                    entries[rowIndex][columnIndex] = entry;

                    // moreRows is gt 0 then fill corresponding column for subsequent rows
                    if (moreRows > 0) {
                        final String startColumnName = entry.getNameStart();
                        final String endColumnName = entry.getNameEnd();
                        for (int i = 1; i <= moreRows; i++) {
                            final var nextEntry = OBJECT_FACTORY.createEntry().withMoreRows(VerticalMergeType.CONTINUE.name())
                                    .withNameStart(startColumnName).withNameEnd(endColumnName);
                            entries[rowIndex + i][columnIndex] = nextEntry;
                        }

                    }
                } else {
                    logger.warn("Content of type \"{}\" is not implemented", content.getClass().getName());
                }
            } // end of for loop for row.getContent
        } // end of for loop for row

        for (int i = 0; i < entries.length; i++) {
            final var entry = entries[i];
            for (int j = 0; j < entry.length; j++) {
                final var obj = entries[i][j];
                if (obj == null) {
                    logger.warn("Index {}:{} is not filled", i, j);
                }
            }
        }

        return Arrays.stream(entries).map (entry -> new Row().withContent(entry)).collect(Collectors.toList());
    }

    private int getTotalNumberOfRows(List<Row> rows) {
        var totalRows = rows.size();

        for (Row row : rows) {
            for (Object obj : row.getContent()) {
                if (AppUtil.isInstanceOf(Entry.class, obj)) {
                    final var entry = (Entry) obj;
                    // +1 since we need to add current column as well
                    final var moreRows = UnmarshallerUtils.toInt(entry.getMoreRows(), -1) + 1;
                    if (!hasRowSpan) {
                        hasRowSpan = moreRows >= 1;
                    }
                    totalRows = Math.max(totalRows, moreRows);
                }
            }
        }

        return totalRows;
    }
}
