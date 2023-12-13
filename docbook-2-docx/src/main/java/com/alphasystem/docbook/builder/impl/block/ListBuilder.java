package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;

import java.util.List;

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
        final var listItemBuilder = getParent(ListItemBuilder.class);
        if (listItemBuilder != null) {
            // we have nested list, get the current list item and pass it down to
            number = listItemBuilder.getNumber();
            level = listItemBuilder.getLevel() + 1;
        } else {
            number = getItemByName(styleName).getNumberId();
            level = 0;
            number = getContext().getListNumber(number, level);
        }
        ApplicationController.getContext().setCurrentListLevel(level);
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

    @Override
    protected List<Object> postProcess(List<Object> processedTitleContent, List<Object> processedChildContent) {
        ApplicationController.getContext().setCurrentListLevel(-1);
        return super.postProcess(processedTitleContent, processedChildContent);
    }
}
