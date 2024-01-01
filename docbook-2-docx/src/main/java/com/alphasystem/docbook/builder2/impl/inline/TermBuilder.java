package com.alphasystem.docbook.builder2.impl.inline;

import com.alphasystem.docbook.builder2.impl.InlineBuilder;
import org.docbook.model.Term;

import static com.alphasystem.docbook.builder.DocumentBuilderHelper.STRONG;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class TermBuilder extends InlineBuilder<Term> {

    @Override
    protected void doInit(Term source) {
        super.doInit(source);
        final var role = source.getRole();
        styles = isBlank(role) ? new String[]{STRONG} : role.split(" ");
    }
}
