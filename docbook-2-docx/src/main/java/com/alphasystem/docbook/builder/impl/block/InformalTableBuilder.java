package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.model.DocBookTableAdapter;
import org.docbook.model.InformalTable;

public class InformalTableBuilder extends AbstractTableBuilder<InformalTable> {

    public InformalTableBuilder(InformalTable source, Builder<?> parent) {
        super(source, parent);
        this.docBookTableAdapter = DocBookTableAdapter.fromInformalTable(source);
    }
}
