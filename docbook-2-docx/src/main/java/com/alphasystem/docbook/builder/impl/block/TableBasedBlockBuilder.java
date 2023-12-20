package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tc;

import java.util.ArrayList;
import java.util.List;

import static com.alphasystem.openxml.builder.wml.WmlAdapter.getEmptyParaNoSpacing;

/**
 * Common class for a block which is implemented as a table.
 *
 * @author sali
 * @see ExampleBuilder
 * @see SideBarBuilder
 * @see AdmonitionBuilder
 */
public abstract class TableBasedBlockBuilder<T> extends BlockBuilder<T> {

    protected Tbl tbl;
    protected Tc tc;

    protected TableBasedBlockBuilder(Builder<?> parent, T obj, int indexInParent) {
        super(parent, obj, indexInParent);
    }

    @Override
    protected List<Object> postProcess(List<Object> processedTitleContent, List<Object> processedChildContent) {
        processedChildContent.forEach(o -> tc.getContent().add(o));
        List<Object> result = new ArrayList<>();
        if (processedTitleContent.isEmpty()) {
            result.add(getEmptyParaNoSpacing());
        } else {
            result.addAll(processedTitleContent);
        }
        result.add(tbl);
        result.add(getEmptyParaNoSpacing());
        return result;
    }
}
