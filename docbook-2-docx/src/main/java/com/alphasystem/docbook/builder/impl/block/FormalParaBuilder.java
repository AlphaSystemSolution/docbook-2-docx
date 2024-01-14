package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.xml.UnmarshallerConstants;
import org.docbook.model.FormalPara;
import org.docbook.model.Title;

import java.util.List;

public class FormalParaBuilder extends AbstractParaBuilder<FormalPara> {

    public FormalParaBuilder(FormalPara source, Builder<?> parent) {
        super(null, source, parent);
    }

    @Override
    protected Title getTitle() {
        return (Title) source.getTitleContent().stream().filter(UnmarshallerConstants::isTitleType).findFirst()
                .orElse(null);
    }

    @Override
    protected List<Object> createPara(List<Object> processedChildContent) {
        return builderFactory.process(source.getPara(), this);
    }
}
