package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.openxml.builder.wml.WmlAdapter;
import org.docbook.model.Title;
import org.docx4j.wml.P;

import java.util.List;


public class TitleBuilder extends AbstractParaBuilder<Title> {

    @Override
    protected List<Object> doProcess(List<Object> processedChildContent) {
        final var result = super.doProcess(processedChildContent);
        WmlAdapter.addBookMark((P) result.get(0), getId());
        return result;
    }

    @Override
    protected List<Object> getChildContent() {
        return source.getContent();
    }
}
