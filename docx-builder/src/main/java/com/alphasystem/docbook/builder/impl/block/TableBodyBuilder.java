package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import org.docbook.model.TableBody;

import java.util.ArrayList;

/**
 * @author sali
 */
public class TableBodyBuilder extends TableContentBuilder<TableBody> {

    public TableBodyBuilder(Builder<?> parent, TableBody source, int indexInParent) {
        super(parent, source, indexInParent);
    }

    @Override
    protected void initContent() {
        content = new ArrayList<>(source.getRow());
    }
}
