package com.alphasystem.docbook.builder2.impl.inline;

import com.alphasystem.docbook.builder2.impl.InlineBuilder;
import org.docbook.model.Literal;

import static com.alphasystem.docbook.builder.DocumentBuilderHelper.LITERAL;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class LiteralBuilder extends InlineBuilder<Literal> {

    @Override
    protected void doInit(Literal source) {
        super.doInit(source);
        final var role = source.getRole();
        styles = isBlank(role) ? new String[]{LITERAL} : role.split(" ");
    }
}
