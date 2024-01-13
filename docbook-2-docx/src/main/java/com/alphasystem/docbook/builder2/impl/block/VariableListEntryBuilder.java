package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.docbook.builder2.impl.BlockBuilder;
import org.docbook.model.Term;
import org.docbook.model.Title;
import org.docbook.model.VariableListEntry;

import java.util.ArrayList;
import java.util.List;

public class VariableListEntryBuilder extends BlockBuilder<VariableListEntry> {

    public VariableListEntryBuilder(VariableListEntry source, Builder<?> parent) {
        super(null, source, parent);
    }

    @Override
    protected Title getTitle() {
        return null;
    }

    @Override
    protected List<Object> getChildContent() {
        // combined all terms into one term separated by comma
        final var term =
                io.vavr.collection.List.ofAll(source.getTerm()).foldLeft(new Term(), (acc, current) -> {
                    final var content = acc.getContent();
                    if (!content.isEmpty()) {
                        content.add(", ");
                    }
                    content.addAll(current.getContent());
                    return acc;
                });
        final var result = new ArrayList<>();
        result.add(term);
        result.add(source.getListItem());
        return result;
    }
}
