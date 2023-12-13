package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.openxml.builder.wml.ListItem;
import org.docbook.model.Numeration;
import org.docbook.model.OrderedList;

import java.util.ArrayList;

/**
 * @author sali
 */
public class OrderedListBuilder extends ListBuilder<OrderedList> {

    public OrderedListBuilder(Builder<?> parent, OrderedList orderedlist, int indexInParent) {
        super(parent, orderedlist, indexInParent);
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
        return com.alphasystem.openxml.builder.wml.OrderedList.getByStyleName(styleName);
    }

    @Override
    protected void preProcess() {
        final Numeration numeration = source.getNumeration();
        String styleName = (numeration == null) ? null : numeration.value();
        parseStyleAndLevel(styleName);
    }

}
