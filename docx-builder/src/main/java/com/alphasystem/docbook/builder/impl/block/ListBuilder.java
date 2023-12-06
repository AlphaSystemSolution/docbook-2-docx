package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;

import static com.alphasystem.docbook.ApplicationController.getContext;
import static com.alphasystem.util.AppUtil.isInstanceOf;

/**
 * @author sali
 */
public abstract class ListBuilder<T> extends BlockBuilder<T> {

    protected long number;
    protected long level;

    protected ListBuilder(Builder<?> parent, T obj, int indexInParent) {
        super(parent, obj, indexInParent);
    }

    protected abstract com.alphasystem.openxml.builder.wml.ListItem<?> getItemByName(String styleName);

    protected void parseStyleAndLevel(String styleName) {
        final Builder<?> parent = getParent();
        if (hasParent(ListItemBuilder.class)) {
            // we have nested list, get the current list item and pass it down to
            ListItemBuilder listItemBuilder = (ListItemBuilder) parent;
            number = listItemBuilder.getNumber();
            level = listItemBuilder.getLevel() + 1;
        } else {
            number = getItemByName(styleName).getNumberId();
            level = 0;
            number = getContext().getListNumber(number, level);
        }
    }

    @Override
    protected Builder<?> getChildBuilder(Object o, int index) {
        final var builder = super.getChildBuilder(o, index);
        if (isInstanceOf(ListItemBuilder.class, builder)) {
            ListItemBuilder listItemBuilder = (ListItemBuilder) builder;
            listItemBuilder.setNumber(number);
            listItemBuilder.setLevel(level);
        }
        return builder;
    }

}
