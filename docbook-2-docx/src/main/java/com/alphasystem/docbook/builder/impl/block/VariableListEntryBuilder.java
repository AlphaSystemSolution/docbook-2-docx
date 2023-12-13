package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;
import org.docbook.model.VariableListEntry;

import java.util.ArrayList;

/**
 * @author sali
 */
public class VariableListEntryBuilder extends BlockBuilder<VariableListEntry> {

    public VariableListEntryBuilder(Builder<?> parent, VariableListEntry source, int indexInParent) {
        super(parent, source, indexInParent);
    }

    @Override
    protected void initContent() {
        content = new ArrayList<>();
        content.addAll(source.getTerm());
        content.add(source.getListItem());
    }

}
