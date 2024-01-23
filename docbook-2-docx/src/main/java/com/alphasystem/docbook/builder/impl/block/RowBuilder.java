package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.AbstractBuilder;
import com.alphasystem.docx4j.builder.wml.WmlBuilderFactory;
import org.docbook.model.Row;

import java.util.Collections;
import java.util.List;

public class RowBuilder extends AbstractBuilder<Row> {

    public RowBuilder(Row source, Builder<?> parent) {
        super(source, parent);
    }


    @Override
    protected List<Object> doProcess(List<Object> processedChildContent) {
        final var trBuilder = WmlBuilderFactory.getTrBuilder().addContent(processedChildContent.toArray());
        return Collections.singletonList(trBuilder.getObject());
    }
}
