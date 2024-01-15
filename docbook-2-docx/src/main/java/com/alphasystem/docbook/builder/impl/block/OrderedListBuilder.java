package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import org.docbook.model.OrderedList;

import java.util.ArrayList;
import java.util.List;

public class OrderedListBuilder extends ListBuilder<OrderedList> {

    public OrderedListBuilder(OrderedList source, Builder<?> parent) {
        super(source, parent);
    }

    @Override
    protected void setListStyleName() {
        this.listStyleName = source.getNumeration().value();
    }

    @Override
    protected List<Object> getChildContent() {
        return new ArrayList<>(source.getListItem());
    }
}
