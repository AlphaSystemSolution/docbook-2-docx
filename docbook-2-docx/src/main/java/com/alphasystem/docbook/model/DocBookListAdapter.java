package com.alphasystem.docbook.model;

import org.docbook.model.ItemizedList;
import org.docbook.model.ListItem;
import org.docbook.model.OrderedList;

public class DocBookListAdapter {

    private final OrderedList orderedList;
    private final ItemizedList itemizedList;

    private DocBookListAdapter(OrderedList orderedList, ItemizedList itemizedList) {
        if (orderedList != null && itemizedList != null) {
            throw new IllegalArgumentException("Only one of table or informalTable is allowed.");
        }
        this.orderedList = orderedList;
        this.itemizedList = itemizedList;
    }

    public OrderedList getOrderedList() {
        return orderedList;
    }

    public ItemizedList getItemizedList() {
        return itemizedList;
    }

    public void addListItem(ListItem listItem) {
        if (orderedList != null) {
            orderedList.getListItem().add(listItem);
        } else if (itemizedList != null) {
            itemizedList.getListItem().add(listItem);
        }
    }

    public static DocBookListAdapter fromOrderedList(OrderedList orderedList) {
        return new DocBookListAdapter(orderedList, null);
    }

    public static DocBookListAdapter fromItemizedList(ItemizedList itemizedList) {
        return new DocBookListAdapter(null, itemizedList);
    }
}
