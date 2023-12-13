package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;
import org.docbook.model.FormalPara;

import java.util.Collections;

/**
 * @author sali
 */
public class FormalParaBuilder extends BlockBuilder<FormalPara> {

    public FormalParaBuilder(Builder<?> parent, FormalPara obj, int indexInParent) {
        super(parent, obj, indexInParent);
    }

    @Override
    protected void initContent() {
        titleContent = source.getTitleContent();
        content = Collections.singletonList(source.getPara());
    }
}
