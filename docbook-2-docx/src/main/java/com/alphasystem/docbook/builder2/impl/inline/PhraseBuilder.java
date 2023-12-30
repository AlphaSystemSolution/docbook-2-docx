package com.alphasystem.docbook.builder2.impl.inline;

import com.alphasystem.docbook.builder2.impl.InlineBuilder;
import org.apache.commons.lang3.StringUtils;
import org.docbook.model.Phrase;

import java.util.List;


public class PhraseBuilder extends InlineBuilder<Phrase> {

    @Override
    protected void doInit(Phrase source) {
        super.doInit(source);
        final var role = source.getRole();
        if(StringUtils.isNotBlank(role)) {
            styles = role.split(" ");
        }
    }

    @Override
    protected List<Object> getChildContent() {
        return source.getContent();
    }
}
