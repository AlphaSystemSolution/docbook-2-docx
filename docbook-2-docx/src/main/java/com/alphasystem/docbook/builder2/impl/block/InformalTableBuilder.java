package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.docbook.model.DocBookTableAdapter;
import org.docbook.model.InformalTable;

public class InformalTableBuilder extends AbstractTableBuilder<InformalTable> {

    @Override
    protected void doInit(InformalTable source, Builder<?> parent) {
        super.doInit(source, parent);
        this.docBookTableAdapter = DocBookTableAdapter.fromInformalTable(source);
    }
}
