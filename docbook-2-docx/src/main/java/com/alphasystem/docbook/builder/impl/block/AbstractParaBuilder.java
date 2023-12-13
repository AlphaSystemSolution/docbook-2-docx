package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;
import com.alphasystem.openxml.builder.wml.PPrBuilder;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;

import static com.alphasystem.util.AppUtil.isInstanceOf;

/**
 * @author sali
 */
public abstract class AbstractParaBuilder<T> extends BlockBuilder<T> {

    protected String role;

    protected AbstractParaBuilder(Builder<?> parent, T obj, int indexInParent) {
        super(parent, obj, indexInParent);
    }

    @Override
    protected void preProcess() {
        final PPr ppr = ((BlockBuilder<?>) parent).getParaProperties();
        if (ppr != null) {
            paraProperties = new PPrBuilder(ppr, paraProperties).getObject();
        }
        if(role != null){
            final PPrBuilder pPrBuilder = new PPrBuilder(paraProperties).withPStyle(role);
            paraProperties = pPrBuilder.getObject();
        }
    }

    @Override
    protected Object postProcessContent(Object o) {
        if (isInstanceOf(P.class, o)) {
            ((P) o).setPPr(paraProperties);
        }
        return super.postProcessContent(o);
    }
}
