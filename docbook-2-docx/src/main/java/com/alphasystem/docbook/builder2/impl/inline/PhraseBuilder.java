package com.alphasystem.docbook.builder2.impl.inline;

import com.alphasystem.docbook.builder2.impl.InlineBuilder;
import com.alphasystem.openxml.builder.wml.RBuilder;
import com.alphasystem.openxml.builder.wml.WmlAdapter;
import org.apache.commons.lang3.StringUtils;
import org.docbook.model.Phrase;
import org.docx4j.wml.R;

import java.util.stream.Collectors;


public class PhraseBuilder extends InlineBuilder<Phrase> {

    public PhraseBuilder(Phrase source) {
        super(source);
        String role = source.getRole();
        if(StringUtils.isNotBlank(role)) {
            styles = role.split(" ");
        }
    }

    @Override
    protected void createRunBuilder() {
        super.createRunBuilder();
        final var text = source.getContent().stream().map(c -> (String) c).collect(Collectors.joining());
        final var r = new TextBuilder(text, source.getId()).process();
        runBuilder = new RBuilder(r, runBuilder.getObject());
    }
}
