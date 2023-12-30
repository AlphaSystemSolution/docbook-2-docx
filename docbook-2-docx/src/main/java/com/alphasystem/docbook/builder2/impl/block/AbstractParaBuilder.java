package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.builder2.impl.AbstractBuilder;
import com.alphasystem.docbook.util.Utils;
import com.alphasystem.openxml.builder.wml.WmlBuilderFactory;
import org.apache.commons.lang3.StringUtils;
import org.docx4j.wml.PPr;

import java.util.Collections;
import java.util.List;

public abstract class AbstractParaBuilder<S> extends AbstractBuilder<S> {

    protected String role;
    protected PPr paraProperties;

    @Override
    protected void doInit(S source) {
        super.doInit(source);
        this.role = (String) Utils.invokeMethod(source,"role");
        if (StringUtils.isNotBlank(this.role)) {
            paraProperties = WmlBuilderFactory.getPPrBuilder().withPStyle(this.role).getObject();
        }
    }

    @Override
    protected List<Object> doProcess(List<Object> processedChildContent) {
        final var p = WmlBuilderFactory.getPBuilder().withParaId(getId()).withPPr(paraProperties)
                .addContent(processedChildContent.toArray()).getObject();
        return Collections.singletonList(p);
    }
}
