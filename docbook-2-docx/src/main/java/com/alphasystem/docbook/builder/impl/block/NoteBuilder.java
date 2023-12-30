package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.model.Admonition;
import org.docbook.model.Note;

/**
 * @author sali
 */
public class NoteBuilder extends AdmonitionBuilder<Note> {

    public NoteBuilder(Builder<?> parent, Note note, int indexInParent) {
        super(parent, note, indexInParent, Admonition.NOTE);
    }

    @Override
    protected void initContent() {
        titleContent = source.getTitleContent();
        content = source.getContent();
    }
}
