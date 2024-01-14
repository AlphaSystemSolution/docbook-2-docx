package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.JavaScriptBasedBuilder;
import com.alphasystem.xml.UnmarshallerConstants;
import org.docbook.model.Example;
import org.docbook.model.Title;
import org.docx4j.wml.Tbl;

import java.util.List;

public class ExampleBuilder extends JavaScriptBasedBuilder<Example, Tbl> {

    public ExampleBuilder(Example source, Builder<?> parent) {
        super(source, parent);
    }

    @Override
    protected Title getTitle() {
        return (Title) source.getTitleContent().stream().filter(UnmarshallerConstants::isTitleType)
                .findFirst().orElse(null);
    }

    @Override
    protected FunctionInput<Tbl> initFunctionInputs(List<Object> processedChildContent) {
        return new FunctionInput<>(configurationUtils.getExampleFunctionName(), Tbl.class, new Object[]{processedChildContent});
    }
}
