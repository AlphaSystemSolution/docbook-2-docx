package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.builder2.Builder;
import org.docbook.model.VariableList;

import java.util.ArrayList;
import java.util.List;

public class VariableListBuilder extends ListBuilder<VariableList> {

    public VariableListBuilder(VariableList source, Builder<?> parent) {
        super(source, parent);
    }

    @Override
    protected void setListStyleName() {
        listStyleName = "var-list";
    }

    @Override
    protected List<Object> getChildContent() {
        return new ArrayList<>(source.getVariableListEntry());
    }
}
