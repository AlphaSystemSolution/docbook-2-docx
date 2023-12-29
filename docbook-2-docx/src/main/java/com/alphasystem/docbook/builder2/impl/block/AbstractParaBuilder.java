package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.builder2.impl.AbstractBuilder;
import com.alphasystem.openxml.builder.wml.WmlBuilderFactory;
import org.apache.commons.lang3.StringUtils;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;

public abstract class AbstractParaBuilder<S> extends AbstractBuilder<S, P> {

    protected String role;
    protected PPr paraProperties;


    protected AbstractParaBuilder(S source, String role) {
        super(source);
        this.role = role;
        if (StringUtils.isNotBlank(this.role)) {
            paraProperties = WmlBuilderFactory.getPPrBuilder().withPStyle(this.role).getObject();
        }
    }

    protected P createPara() {
        return WmlBuilderFactory.getPBuilder().withParaId(getId(source)).withPPr(paraProperties).getObject();
    }

    @Override
    public P process() {
        return createPara();
    }
}
