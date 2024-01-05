package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.builder2.Builder;
import org.docbook.model.TableFooter;

import java.util.ArrayList;
import java.util.List;

public class TableFooterBuilder extends TableContentBuilder<TableFooter> {

    public TableFooterBuilder(TableFooter source, Builder<?> parent) {
        super(source, parent);
    }

    @Override
    protected List<Object> getChildContent() {
        return new ArrayList<>(source.getRow());
    }
}
