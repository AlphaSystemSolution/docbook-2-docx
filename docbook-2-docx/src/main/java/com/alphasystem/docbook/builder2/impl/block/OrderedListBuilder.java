package com.alphasystem.docbook.builder2.impl.block;

import org.docbook.model.OrderedList;

import java.util.ArrayList;
import java.util.List;

public class OrderedListBuilder extends ListBuilder<OrderedList> {

    @Override
    protected List<Object> getChildContent() {
        return new ArrayList<>(source.getListItem());
    }
}
