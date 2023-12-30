package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.model.DocBookTableAdapter;
import org.docbook.model.InformalTable;

public class InformalTableBuilder extends AbstractTableBuilder<InformalTable> {

    @Override
    protected void doInit(InformalTable source) {
        super.doInit(source);
        this.docBookTableAdapter = DocBookTableAdapter.fromInformalTable(source);
    }
}
