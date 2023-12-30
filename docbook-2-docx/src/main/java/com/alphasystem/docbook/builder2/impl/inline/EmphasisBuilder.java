package com.alphasystem.docbook.builder2.impl.inline;

import com.alphasystem.docbook.builder2.impl.InlineBuilder;
import org.docbook.model.Emphasis;

import java.util.List;

import static com.alphasystem.docbook.builder.DocumentBuilderHelper.ITALIC;
import static org.apache.commons.lang3.StringUtils.isBlank;


public class EmphasisBuilder extends InlineBuilder<Emphasis> {

    @Override
    protected void doInit(Emphasis source) {
        super.doInit(source);
        final var role = source.getRole();
        styles = isBlank(role) ? new String[]{ITALIC} : role.split(" ");
    }

    @Override
    protected List<Object> getChildContent() {
        return source.getContent();
    }
}
