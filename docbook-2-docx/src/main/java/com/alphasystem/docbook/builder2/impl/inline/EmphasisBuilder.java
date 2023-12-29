package com.alphasystem.docbook.builder2.impl.inline;

import com.alphasystem.docbook.builder2.impl.InlineBuilder;
import com.alphasystem.openxml.builder.wml.RBuilder;
import org.apache.commons.lang3.StringUtils;
import org.docbook.model.Emphasis;

import java.util.stream.Collectors;

import static com.alphasystem.docbook.builder.DocumentBuilderHelper.ITALIC;
import static org.apache.commons.lang3.StringUtils.isBlank;


public class EmphasisBuilder extends InlineBuilder<Emphasis> {

    public EmphasisBuilder(Emphasis source) {
        super(source);
        String role = source.getRole();
        styles = isBlank(role) ? new String[]{ITALIC} : role.split(" ");
    }

    @Override
    protected void createRunBuilder() {
        super.createRunBuilder();
        final var text = source.getContent().stream().map(c -> (String) c).collect(Collectors.joining());
        final var r = new TextBuilder(text, source.getId()).process();
        runBuilder = new RBuilder(r, runBuilder.getObject());
    }
}
