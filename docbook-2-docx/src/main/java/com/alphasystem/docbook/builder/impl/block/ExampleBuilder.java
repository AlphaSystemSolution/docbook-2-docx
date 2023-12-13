package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import org.docbook.model.Example;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Tr;

/**
 * @author sali
 */
public class ExampleBuilder extends TableBasedBlockBuilder<Example> {

    public ExampleBuilder(Builder<?> parent, Example example, int indexInParent) {
        super(parent, example, indexInParent);
    }

    @Override
    protected void initContent() {
        titleContent = source.getTitleContent();
        content = source.getContent();
    }

    @Override
    protected void preProcess() {
        tbl = applicationController.getExampleTable();
        final Tr tr = (Tr) tbl.getContent().get(0);
        tc = (Tc) tr.getContent().get(0);
    }

}
