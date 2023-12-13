package com.alphasystem.docbook.builder.impl.inline;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.InlineBuilder;
import com.alphasystem.openxml.builder.wml.RBuilder;
import com.alphasystem.openxml.builder.wml.WmlAdapter;
import com.alphasystem.openxml.builder.wml.WmlBuilderFactory;
import org.docx4j.wml.R;

import java.util.Collections;
import java.util.List;

/**
 * @author sali
 */
public class TextBuilder extends InlineBuilder<String> {

    public TextBuilder(Builder<?> parent, String text, int indexInParent) {
        super(parent, text, indexInParent);
    }

    @Override
    protected void initContent() {
    }

    @Override
    protected List<R> processContent() {
        final String[] lines = source.split(System.lineSeparator());
        final RBuilder rBuilder = WmlBuilderFactory.getRBuilder();
        List<R> result;
        if (lines.length == 0) {
            result = Collections.emptyList();
        } else {
            final String firstLine = lines[0];
            rBuilder.addContent(WmlAdapter.getText(firstLine, "preserve"));
            for (int i = 1; i < lines.length; i++) {
                final String line = lines[i];
                rBuilder.addContent(WmlBuilderFactory.getBrBuilder().getObject())
                        .addContent(WmlAdapter.getText(line, "preserve"));
            }
            result = Collections.singletonList(rBuilder.getObject());
        }

        return result;
    }

}
