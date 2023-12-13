package com.alphasystem.docbook.builder.impl.inline;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.InlineBuilder;
import org.docbook.model.Subscript;

import static com.alphasystem.docbook.builder.DocumentBuilderHelper.SUBSCRIPT;

/**
 * @author sali
 */
public class SubscriptBuilder extends InlineBuilder<Subscript> {

    public SubscriptBuilder(Builder<?> parent, Subscript subscript, int indexInParent) {
        super(parent, subscript, indexInParent);
    }

    @Override
    protected void initContent() {
        styles = new String[]{SUBSCRIPT};
        content = source.getContent();
    }

}
