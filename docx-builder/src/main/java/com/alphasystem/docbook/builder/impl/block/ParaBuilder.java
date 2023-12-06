package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import org.docbook.model.Para;

/**
 * @author sali
 */
public class ParaBuilder extends AbstractParaBuilder<Para> {

    public ParaBuilder(Builder<?> parent, Para obj, int indexInParent) {
        super(parent, obj, indexInParent);
        this.role = source.getRole();
    }

    @Override
    protected void initContent() {
        content = source.getContent();
    }

}
