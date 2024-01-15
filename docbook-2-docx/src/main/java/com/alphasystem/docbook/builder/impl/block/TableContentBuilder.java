package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.AbstractBuilder;
import com.alphasystem.openxml.builder.wml.table.VerticalMergeType;
import com.alphasystem.util.AppUtil;
import com.alphasystem.xml.UnmarshallerUtils;
import org.apache.commons.lang3.StringUtils;
import org.docbook.model.Entry;
import org.docbook.model.ObjectFactory;
import org.docbook.model.Row;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class TableContentBuilder<S> extends AbstractBuilder<S> {

    private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();

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
                    final var vMerge = moreRows <= 0 ? VerticalMergeType.NONE : VerticalMergeType.RESTART;
                    final var startColumnName = entry.getNameStart();
                    final var endColumnName = entry.getNameEnd();
                    final var gridSpan = parent.getGridSpan(startColumnName, endColumnName);

                    // following columns will be overridden
                    // NameStart will be populated with - columnIndex
                    // NameEnd will be populated with - gridSpan
                    // morerows will be populated with - VerticalMergeType
                    // this is done so that we don't have to re-do calculation again

                    entry.setMoreRows(vMerge.name());
                    entry.setNameStart(String.valueOf(columnIndex));
                    entry.setNameEnd(String.valueOf(gridSpan));
                    entries[rowIndex][columnIndex] = entry;

                    // moreRows is gt 0 then fill corresponding column for subsequent rows
                    if (moreRows > 0) {
                        for (int i = 1; i <= moreRows; i++) {
                            final var nextEntry = OBJECT_FACTORY.createEntry().withMoreRows(VerticalMergeType.CONTINUE.name())
                                    .withNameStart(String.valueOf(columnIndex)).withNameEnd(String.valueOf(gridSpan));
                            entries[rowIndex + i][columnIndex] = nextEntry;
                            // if there is column span then no need to continue
                            if (StringUtils.isNotBlank(startColumnName) && StringUtils.isNotBlank(endColumnName)) {
                                break;
                            }
                        }
                    }
                    columnIndex += gridSpan;
                } else {
                    logger.warn("Content of type \"{}\" is not implemented", content.getClass().getName());
                }
            } // end of for loop for row.getContent
        } // end of for loop for row

        return Arrays.stream(entries).map (entry -> new Row().withContent(Arrays.stream(entry)
                .filter(Objects::nonNull).toArray())).collect(Collectors.toList());
    }

    private int getTotalNumberOfRows(List<Row> rows) {
        var totalRows = rows.size();

        for (Row row : rows) {
            for (Object obj : row.getContent()) {
                if (AppUtil.isInstanceOf(Entry.class, obj)) {
                    final var entry = (Entry) obj;
                    // +1 since we need to add current column as well
                    final var moreRows = UnmarshallerUtils.toInt(entry.getMoreRows(), -1) + 1;
                    totalRows = Math.max(totalRows, moreRows);
                }
            }
        }

        return totalRows;
    }
}
