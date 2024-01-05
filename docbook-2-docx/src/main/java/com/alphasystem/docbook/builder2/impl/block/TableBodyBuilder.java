package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.builder2.Builder;
import org.docbook.model.TableBody;

import java.util.ArrayList;
import java.util.List;

public class TableBodyBuilder extends TableContentBuilder<TableBody> {

    public TableBodyBuilder(TableBody source, Builder<?> parent) {
        super(source, parent);
    }

    @Override
    protected List<Object> getChildContent() {
        return new ArrayList<>(source.getRow());
    }
}
