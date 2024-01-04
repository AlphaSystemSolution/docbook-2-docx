package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.builder2.Builder;
import org.docbook.model.ItemizedList;

import java.util.ArrayList;
import java.util.List;

public class ItemizedListBuilder extends ListBuilder<ItemizedList> {

    public ItemizedListBuilder(ItemizedList source, Builder<?> parent) {
        super(source, parent);
    }

    @Override
    protected List<Object> getChildContent() {
        return new ArrayList<>(source.getListItem());
    }
}
