package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;
import org.docbook.model.Entry;
import org.docbook.model.ObjectFactory;
import org.docbook.model.Row;

import java.util.List;

import static com.alphasystem.util.AppUtil.isInstanceOf;
import static java.lang.String.format;

/**
 * @author sali
 */
public abstract class TableContentBuilder<T> extends BlockBuilder<T> {

    private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();

    private static String getNextMoreRows(String moreRows) {
        if (moreRows == null) {
            return null;
        }
        if (moreRows.endsWith("*")) {
            moreRows = moreRows.substring(0, moreRows.length() - 1);
        }
        final int i = Integer.parseInt(moreRows) - 1;
        if (i < 0) {
            return null;
        }
        moreRows = format("%s*", i);
        return moreRows;
    }

    protected TableContentBuilder(Builder<?> parent, T source, int indexInParent) {
        super(parent, source, indexInParent);
    }

    @Override
    protected void preProcess() {
        super.preProcess();
        final var tableBuilder = getParent(AbstractTableBuilder.class);
        Row nextRow = null;
        boolean hasNextRow = false;
        boolean hasMoreRows = false;
        final int size = content.size();
        for (int i = 0; i < size; i++) {
            final Row currentRow = (Row) content.get(i);
            final int nextIndex = i + 1;
            hasNextRow = nextIndex < size;
            if (hasNextRow) {
                nextRow = (Row) content.get(nextIndex);
            } else {
                nextRow = OBJECT_FACTORY.createRow();
            }
            int nextColumnIndex = 0;
            final List<Object> rowContent = currentRow.getContent();
            for (final Object o : rowContent) {
                if (isInstanceOf(Entry.class, o)) {
                    final Entry entry = (Entry) o;
                    final String moreRows = getNextMoreRows(entry.getMoreRows());
                    hasMoreRows = (moreRows != null);
                    final String startColumnName = entry.getNameStart();
                    final String endColumnName = entry.getNameEnd();
                    if (hasMoreRows) {
                        Entry nextEntry = OBJECT_FACTORY.createEntry().withMoreRows(moreRows)
                                .withNameStart(startColumnName).withNameEnd(endColumnName);
                        updateNextRow(nextRow, nextEntry, nextColumnIndex, tableBuilder);
                    } // end of if "hasMoreRows"
                    nextColumnIndex += tableBuilder.getGridSpan(startColumnName, endColumnName);
                } // end of Entry check
            } // end of inner for loop
        } // end of outer for loop
        if (hasMoreRows && !hasNextRow) {
            content.add(nextRow);
        }
    }

    private void updateNextRow(Row row, Entry entry, int columnIndex, AbstractTableBuilder<?> tableBuilder) {
        final List<Object> content = row.getContent();
        int index = 0;
        final int size = content.size();
        for (int i = 0; i < size; i++) {
            if (index == columnIndex) {
                index = i;
                break;
            }
            final Object o = content.get(i);
            if (isInstanceOf(Entry.class, o)) {
                Entry currentEntry = (Entry) o;
                index += tableBuilder.getGridSpan(currentEntry.getNameStart(), currentEntry.getNameEnd());
            }
        }
        if (index > size) {
            content.add(entry);
        } else {
            content.add(index, entry);
        }
    }


}
