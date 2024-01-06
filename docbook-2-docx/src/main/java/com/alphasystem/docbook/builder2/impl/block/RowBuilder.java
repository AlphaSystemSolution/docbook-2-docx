package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.docbook.builder2.impl.AbstractBuilder;
import com.alphasystem.openxml.builder.wml.WmlBuilderFactory;
import org.docbook.model.Row;

import java.util.Collections;
import java.util.List;

public class RowBuilder extends AbstractBuilder<Row> {

    private int columnIndex = 0;

    public RowBuilder(Row source, Builder<?> parent) {
        super(source, parent);
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void updateColumnIndex(int value) {
        columnIndex = value;
    }

    @Override
    protected List<Object> doProcess(List<Object> processedChildContent) {
        final var trBuilder = WmlBuilderFactory.getTrBuilder().addContent(processedChildContent.toArray());
        columnIndex = 0;
        return Collections.singletonList(trBuilder.getObject());
    }
}
