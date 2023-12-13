package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import org.docbook.model.InformalExample;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Tr;

/**
 * @author sali
 */
public class InformalExampleBuilder extends TableBasedBlockBuilder<InformalExample> {

    public InformalExampleBuilder(Builder<?> parent, InformalExample informalExample, int indexInParent) {
        super(parent, informalExample, indexInParent);
    }

    @Override
    protected void initContent() {
        content = source.getContent();
    }

    @Override
    protected void preProcess() {
        tbl = applicationController.getInformalExampleTable();
        final Tr tr = (Tr) tbl.getContent().get(0);
        tc = (Tc) tr.getContent().get(0);
    }

}
