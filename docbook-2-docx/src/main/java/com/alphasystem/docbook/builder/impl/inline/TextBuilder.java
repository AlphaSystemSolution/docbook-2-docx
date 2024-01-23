package com.alphasystem.docbook.builder.impl.inline;

import com.alphasystem.commons.util.IdGenerator;
import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.InlineBuilder;
import com.alphasystem.docx4j.builder.wml.WmlAdapter;
import com.alphasystem.docx4j.builder.wml.WmlBuilderFactory;

public class TextBuilder extends InlineBuilder<String> {

    public TextBuilder(String source, Builder<?> parent) {
        super(null,null, source, parent);
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
}
