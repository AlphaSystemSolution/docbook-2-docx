package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.model.DocBookTableAdapter;
import org.docbook.model.InformalTable;

public class InformalTableBuilder extends AbstractTableBuilder<InformalTable> {

    public InformalTableBuilder(InformalTable source, int level) {
        super(source, level);
        this.docBookTableAdapter = DocBookTableAdapter.fromInformalTable(source);
    }
}
