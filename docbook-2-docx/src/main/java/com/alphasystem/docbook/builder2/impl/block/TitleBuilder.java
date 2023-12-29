package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.openxml.builder.wml.WmlAdapter;
import org.docbook.model.Title;
import org.docx4j.wml.P;


public class TitleBuilder extends AbstractParaBuilder<Title> {

    private final String parentId;

    public TitleBuilder(Title title, String titleStyle, String parentId) {
        super(title, titleStyle);
        this.parentId = parentId;
    }

    @Override
    public P process() {
        final var p = super.process();
        if (parentId != null) {
            WmlAdapter.addBookMark(p, parentId);
        }
        return p;
    }
}
