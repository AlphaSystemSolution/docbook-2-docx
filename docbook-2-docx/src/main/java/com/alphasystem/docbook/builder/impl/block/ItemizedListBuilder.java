package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.openxml.builder.wml.ListItem;
import com.alphasystem.openxml.builder.wml.UnorderedList;
import org.docbook.model.ItemizedList;

import java.util.ArrayList;

/**
 * @author sali
 */
public class ItemizedListBuilder extends ListBuilder<ItemizedList> {

    public ItemizedListBuilder(Builder<?> parent, ItemizedList obj, int indexInParent) {
        super(parent, obj, indexInParent);
    }

    @Override
    protected void initContent() {
        titleContent = source.getTitleContent();
        content = new ArrayList<>();
        content.addAll(source.getContent());
        content.addAll(source.getListItem());
    }

    @Override
    protected ListItem<?> getItemByName(String styleName) {
        return UnorderedList.getByStyleName(styleName);
    }

    @Override
    protected void preProcess() {
        parseStyleAndLevel(source.getMark());
    }

}
