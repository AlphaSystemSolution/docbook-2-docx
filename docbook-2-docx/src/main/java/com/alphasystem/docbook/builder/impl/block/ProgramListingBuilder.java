package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;
import com.alphasystem.openxml.builder.wml.table.ColumnData;
import com.alphasystem.openxml.builder.wml.table.TableAdapter;
import org.docbook.model.ProgramListing;

import java.util.Collections;
import java.util.List;

public class ProgramListingBuilder extends BlockBuilder<ProgramListing> {

    public ProgramListingBuilder(ProgramListing source, Builder<?> parent) {
        super(source, parent);
    }

    @Override
    protected List<Object> doProcess(List<Object> processedChildContent) {
        processedChildContent = super.doProcess(processedChildContent);
        final var tbl = new TableAdapter()
                .withTableStyle("ProgramListingCode")
                .withWidths(100.0)
                .startTable()
                .startRow()
                .addColumn(new ColumnData(0).withContent(processedChildContent.toArray()))
                .endRow()
                .getTable();
        return Collections.singletonList(tbl);
    }
}
