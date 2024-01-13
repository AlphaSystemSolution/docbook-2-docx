package com.alphasystem.docbook.model;

import com.alphasystem.openxml.builder.wml.AbstractListItem;
import com.alphasystem.util.IdGenerator;

public class VariableListType extends AbstractListItem<VariableListType> {

    private static final int LEFT_INDENT_VALUE = 360;
    private static final int INCREMENT_VALUE = 144;

    public VariableListType() {
        super("VarListParagraph", IdGenerator.nextId());
    }

    @Override
    public long getLeftIndent(int level) {
        return LEFT_INDENT_VALUE + ((long) INCREMENT_VALUE * level);
    }
}
