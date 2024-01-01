package com.alphasystem.docbook.builder2.impl.block;

import org.docbook.model.ItemizedList;

import java.util.ArrayList;
import java.util.List;

public class ItemizedListBuilder extends ListBuilder<ItemizedList> {

    @Override
    protected List<Object> getChildContent() {
        return new ArrayList<>(source.getListItem());
    }
}
