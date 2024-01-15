package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.model.Admonition;
import org.docbook.model.Warning;

public class WarningBuilder extends AdmonitionBuilder<Warning> {

    public WarningBuilder(Warning source, Builder<?> parent) {
        super(Admonition.WARNING, source, parent);
    }
}
