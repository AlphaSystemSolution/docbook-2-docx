package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.JavaScriptBasedBuilder;
import com.alphasystem.openxml.builder.wml.WmlAdapter;
import org.docx4j.wml.Tbl;

import java.util.ArrayList;
import java.util.List;

public abstract class TableBasedBlockBuilder<S> extends JavaScriptBasedBuilder<S, Tbl> {

    protected TableBasedBlockBuilder(S source, Builder<?> parent) {
        super(source, parent);
    }

    protected TableBasedBlockBuilder(String childContentMethodName, S source, Builder<?> parent) {
        super(childContentMethodName, source, parent);
    }

    @Override
    protected List<Object> doProcess(List<Object> processedChildContent) {
        final var result = new ArrayList<>();
        // add empty para before and after, in case previous and next elements are tables then MS will merge tables
        result.add(WmlAdapter.getEmptyParaNoSpacing());
        result.addAll(super.doProcess(processedChildContent));
        result.add(WmlAdapter.getEmptyParaNoSpacing());
        return result;
    }
}
