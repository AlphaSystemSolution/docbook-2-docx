package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.docbook.builder2.impl.JavaScriptBasedBuilder;
import com.alphasystem.docbook.model.Admonition;
import org.docx4j.wml.Tbl;

import java.util.List;

public abstract class AdmonitionBuilder<S> extends JavaScriptBasedBuilder<S, Tbl> {

    private final Admonition admonition;

    protected AdmonitionBuilder(Admonition admonition, S source, Builder<?> parent) {
        super(source, parent);
        this.admonition = admonition;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected FunctionInput<Tbl> initFunctionInputs(List<Object> processedChildContent) {
        final var parent = getParent(ListBuilder.class);
        var level = -1;
        if (parent != null) {
            level = (int) parent.getListInfo().getLevel();
        }
        return new FunctionInput<>(configurationUtils.getString("admonition.functionName"),
                Tbl.class,
                new Object[]{admonition.getValue(),
                        configurationUtils.getAdmonitionCaption(admonition),
                        configurationUtils.getAdmonitionCaptionColor(admonition),
                        level,
                        processedChildContent.toArray()});
    }
}
