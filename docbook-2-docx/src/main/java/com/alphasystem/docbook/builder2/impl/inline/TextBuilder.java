package com.alphasystem.docbook.builder2.impl.inline;

import com.alphasystem.docbook.builder2.impl.InlineBuilder;
import com.alphasystem.openxml.builder.wml.WmlAdapter;
import com.alphasystem.openxml.builder.wml.WmlBuilderFactory;
import com.alphasystem.util.IdGenerator;

import java.util.Collections;
import java.util.List;

public class TextBuilder extends InlineBuilder<String> {

    public TextBuilder() {
        this.id = IdGenerator.nextId();
    }

    @Override
    protected void createRunBuilder() {
         super.createRunBuilder();
        final var lines = source.split(System.lineSeparator());
        if (lines.length == 0) {
            return;
        }

        runBuilder.addContent(WmlAdapter.getText(lines[0], "preserve"));
        for (int i = 1; i < lines.length; i++) {
            runBuilder.addContent(WmlBuilderFactory.getBrBuilder().getObject())
                    .addContent(WmlAdapter.getText(lines[i], "preserve"));
        }

    }

    @Override
    protected List<Object> getChildContent() {
        return Collections.emptyList();
    }
}
