package com.alphasystem.docbook.model;


import com.alphasystem.commons.util.IdGenerator;
import com.alphasystem.docx4j.builder.wml.AbstractListItem;

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
