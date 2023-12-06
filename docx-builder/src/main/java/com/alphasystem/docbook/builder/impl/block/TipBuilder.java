package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.model.Admonition;
import org.docbook.model.Tip;

/**
 * @author sali
 */
public class TipBuilder extends AdmonitionBuilder<Tip> {

    public TipBuilder(Builder<?> parent, Tip tip, int indexInParent) {
        super(parent, tip, indexInParent, Admonition.TIP);
    }

    @Override
    protected void initContent() {
        titleContent = source.getTitleContent();
        content = source.getContent();
    }
}
