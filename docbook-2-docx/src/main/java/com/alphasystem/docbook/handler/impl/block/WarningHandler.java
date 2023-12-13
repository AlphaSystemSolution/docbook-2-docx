package com.alphasystem.docbook.handler.impl.block;

import static com.alphasystem.docbook.builder.model.Admonition.WARNING;

/**
 * @author sali
 */
public class WarningHandler extends AdmonitionBlockHandler {

    WarningHandler() {
        super(WARNING, 15);
    }
}
