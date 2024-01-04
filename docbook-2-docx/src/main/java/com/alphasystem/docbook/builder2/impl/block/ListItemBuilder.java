package com.alphasystem.docbook.builder2.impl.block;


import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.docbook.builder2.impl.AbstractBuilder;
import com.alphasystem.docbook.util.Utils;
import com.alphasystem.xml.UnmarshallerConstants;
import org.docbook.model.ListItem;

import java.util.List;

public class ListItemBuilder extends AbstractBuilder<ListItem> {

    /*
     * In a list if there are multiple paragraphs, then we only need to set "NumPr" on the first para.
     * The id is the id of first such para.
     */
    protected String firstParaId = null;

    public ListItemBuilder(ListItem source, Builder<?> parent) {
        super(source, parent);
    }

    public String getFirstParaId() {
        return firstParaId;
    }

    private void setFirstParaId(Object o) {
        this.firstParaId = Utils.getId(o);
    }

    @Override
    protected List<Object> processChildContent(List<Object> childContent) {
        childContent.stream().filter(UnmarshallerConstants::isParaTypes).findFirst()
                .ifPresent(this::setFirstParaId);
        return super.processChildContent(childContent);
    }
}
