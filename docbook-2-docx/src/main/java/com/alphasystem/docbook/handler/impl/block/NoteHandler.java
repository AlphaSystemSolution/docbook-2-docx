package com.alphasystem.docbook.handler.impl.block;

import static com.alphasystem.docbook.builder.model.Admonition.NOTE;

/**
 * @author sali
 */
public class NoteHandler extends AdmonitionBlockHandler {

    NoteHandler() {
        super(NOTE, 15);
    }
}
