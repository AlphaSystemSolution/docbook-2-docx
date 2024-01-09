package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.docbook.model.Admonition;
import org.docbook.model.Note;

public class NoteBuilder extends AdmonitionBuilder<Note> {

    public NoteBuilder(Note source, Builder<?> parent) {
        super(Admonition.NOTE, source, parent);
    }
}
