package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.docbook.builder2.impl.AbstractBuilder;
import com.alphasystem.docbook.model.NextColumnInfo;
import com.alphasystem.openxml.builder.wml.WmlBuilderFactory;
import org.docbook.model.Row;

import java.util.*;
import java.util.stream.Collectors;

public class RowBuilder extends AbstractBuilder<Row> {

    private int columnIndex = 0;
    private final Map<Integer, NextColumnInfo> nextColumnInfoMap = new HashMap<>();

    public RowBuilder(Row source, Builder<?> parent) {
        super(source, parent);
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void updateNextColumnInfo(NextColumnInfo nextColumnInfo) {
        if (nextColumnInfo.getMoreRows() > 0) {
            nextColumnInfoMap.put(nextColumnInfo.getCurrentColumnIndex(), nextColumnInfo);
        } else {
            nextColumnInfoMap.remove(nextColumnInfo.getCurrentColumnIndex());
        }
        columnIndex = nextColumnInfo.getNextColumnIndex();
    }

    @Override
    protected List<Object> processChildContent(List<Object> childContent) {
        return childContent.stream().map(content -> {
            final var existingValue = nextColumnInfoMap.get(columnIndex);
            if (existingValue != null && existingValue.getMoreRows() > 0) {
                columnIndex = existingValue.getNextColumnIndex();
                final var updatedValue = existingValue.decrementMoreRows();
                if (updatedValue.getMoreRows() > 0) {
                    nextColumnInfoMap.put(updatedValue.getCurrentColumnIndex(), updatedValue);
                } else {
                    nextColumnInfoMap.remove(updatedValue.getCurrentColumnIndex());
                }
            }
            return builderFactory.process(content, this);
        }).flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    protected List<Object> doProcess(List<Object> processedChildContent) {
        final var trBuilder = WmlBuilderFactory.getTrBuilder().addContent(processedChildContent.toArray());
        nextColumnInfoMap.clear();
        columnIndex = 0;
        return Collections.singletonList(trBuilder.getObject());
    }
}
