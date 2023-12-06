package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import org.docbook.model.InformalTable;
import org.docbook.model.TableGroup;

import java.util.List;

/**
 * @author sali
 */
public class InformalTableBuilder extends AbstractTableBuilder<InformalTable> {

    public InformalTableBuilder(Builder<?> parent, InformalTable informalTable, int indexInParent) {
        super(parent, informalTable, indexInParent);
    }

    @Override
    protected void initContent() {
        final List<TableGroup> tableGroups = source.getTableGroup();
        final TableGroup tableGroup = ((tableGroups != null) && !tableGroups.isEmpty()) ? tableGroups.get(0) : null;
        if (tableGroup != null) {
            initializeTableAdapter(tableGroup, source.getFrame(), source.getRowSep(), source.getColSep(), source.getTableStyle());
            initializeContent(tableGroup);
        }
    }

}
