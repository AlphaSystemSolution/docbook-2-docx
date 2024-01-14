package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.model.DocBookTableAdapter;
import org.docbook.model.Table;

public class TableBuilder extends AbstractTableBuilder<Table> {

    public TableBuilder(Table source, Builder<?> parent) {
        super(source, parent);
        this.docBookTableAdapter = DocBookTableAdapter.fromTable(source);
    }
}
