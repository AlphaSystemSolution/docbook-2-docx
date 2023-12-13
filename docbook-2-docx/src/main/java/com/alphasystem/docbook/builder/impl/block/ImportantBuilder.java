package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.model.Admonition;
import org.docbook.model.Important;

/**
 * @author sali
 */
public class ImportantBuilder extends AdmonitionBuilder<Important> {

    public ImportantBuilder(Builder<?> parent, Important important, int indexInParent) {
        super(parent, important, indexInParent, Admonition.IMPORTANT);
    }

    @Override
    protected void initContent() {
        titleContent = source.getTitleContent();
        content = source.getContent();
    }
}
