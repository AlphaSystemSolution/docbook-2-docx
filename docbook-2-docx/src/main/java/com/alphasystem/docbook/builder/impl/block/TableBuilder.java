package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import org.docbook.model.Table;
import org.docbook.model.TableGroup;
import org.docbook.model.Title;

import java.util.List;

import static java.util.Collections.singletonList;

/**
 * @author sali
 */
public class TableBuilder extends AbstractTableBuilder<Table> {

    public TableBuilder(Builder<?> parent, Table source, int indexInParent) {
        super(parent, source, indexInParent);
    }

    @Override
    protected void initContent() {
        final List<TableGroup> tableGroups = source.getTableGroup();
        final TableGroup tableGroup = ((tableGroups != null) && !tableGroups.isEmpty()) ? tableGroups.get(0) : null;
        if (tableGroup != null) {
            initializeTableAdapter(tableGroup, source.getFrame(), source.getRowSep(), source.getColSep(), source.getTableStyle());
            initializeContent(tableGroup);
        }
        final Title title = source.getTitle();
        if (title != null) {
            titleContent = singletonList(title);
        }
    }
}
