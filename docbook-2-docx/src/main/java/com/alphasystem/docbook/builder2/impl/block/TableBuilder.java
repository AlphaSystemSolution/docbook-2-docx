package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.docbook.model.DocBookTableAdapter;
import org.docbook.model.Table;

public class TableBuilder extends AbstractTableBuilder<Table> {

    public TableBuilder(Table source, Builder<?> parent) {
        super(source, parent);
        this.docBookTableAdapter = DocBookTableAdapter.fromTable(source);
    }
}
