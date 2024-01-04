package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.openxml.builder.wml.WmlAdapter;
import org.docbook.model.Title;
import org.docx4j.wml.P;

import java.util.List;


public class TitleBuilder extends AbstractParaBuilder<Title> {

    public TitleBuilder(Title source, Builder<?> parent) {
        super(source, parent);
    }

    @Override
    protected List<Object> doProcess(List<Object> processedChildContent) {
        final var result = super.doProcess(processedChildContent);
        WmlAdapter.addBookMark((P) result.get(0), getId());
        return result;
    }
}
