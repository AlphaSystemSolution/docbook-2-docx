package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.builder2.Builder;
import org.docbook.model.TableHeader;

import java.util.ArrayList;
import java.util.List;

public class TableHeaderBuilder extends TableContentBuilder<TableHeader> {

    public TableHeaderBuilder(TableHeader source, Builder<?> parent) {
        super(source, parent);
    }

    @Override
    protected List<Object> getChildContent() {
        return new ArrayList<>(source.getRow());
    }
}
