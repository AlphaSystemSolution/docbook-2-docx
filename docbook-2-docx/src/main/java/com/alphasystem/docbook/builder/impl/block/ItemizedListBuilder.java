package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import org.docbook.model.ItemizedList;

import java.util.ArrayList;
import java.util.List;

public class ItemizedListBuilder extends ListBuilder<ItemizedList> {

    public ItemizedListBuilder(ItemizedList source, Builder<?> parent) {
        super(source, parent);
    }

    @Override
    protected void setListStyleName() {
        this.listStyleName = source.getMark();
    }

    @Override
    protected List<Object> getChildContent() {
        return new ArrayList<>(source.getListItem());
    }
}
