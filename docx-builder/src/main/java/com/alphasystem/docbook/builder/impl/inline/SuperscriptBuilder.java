package com.alphasystem.docbook.builder.impl.inline;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.InlineBuilder;
import org.docbook.model.Superscript;

import static com.alphasystem.docbook.builder.DocumentBuilderHelper.SUPERSCRIPT;

/**
 * @author sali
 */
public class SuperscriptBuilder extends InlineBuilder<Superscript> {

    public SuperscriptBuilder(Builder<?> parent, Superscript superscript, int indexInParent) {
        super(parent, superscript, indexInParent);
    }

    @Override
    protected void initContent() {
        styles = new String[]{SUPERSCRIPT};
        content = source.getContent();
    }
}
