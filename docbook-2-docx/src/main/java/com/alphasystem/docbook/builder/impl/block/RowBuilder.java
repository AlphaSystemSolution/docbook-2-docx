package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;
import com.alphasystem.openxml.builder.wml.TrBuilder;
import com.alphasystem.openxml.builder.wml.WmlBuilderFactory;
import org.docbook.model.Row;

import java.util.List;

import static java.util.Collections.singletonList;

/**
 * @author sali
 */
public class RowBuilder extends BlockBuilder<Row> {

    protected int nextColumnIndex = 0;

    public RowBuilder(Builder<?> parent, Row source, int indexInParent) {
        super(parent, source, indexInParent);
    }

    @Override
    protected void initContent() {
        content = source.getContent();
    }

    @Override
    protected Builder<?> getChildBuilder(Object o, int index) {
        // In case where column is span more than one column "nextColumnIndex" will be different from "index"
        return super.getChildBuilder(o, nextColumnIndex);
    }

    @Override
    protected List<Object> postProcess(List<Object> processedTitleContent, List<Object> processedChildContent) {
        final TrBuilder trBuilder = WmlBuilderFactory.getTrBuilder();
        processedChildContent.forEach(trBuilder::addContent);
        return singletonList(trBuilder.getObject());
    }

    void updateNextColumnIndex(int gridSpan) {
        nextColumnIndex += gridSpan;
    }
}
