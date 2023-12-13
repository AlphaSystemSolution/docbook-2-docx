package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;
import com.alphasystem.openxml.builder.wml.WmlAdapter;
import org.docbook.model.Title;
import org.docx4j.wml.P;

import java.util.Collections;
import java.util.List;

import static com.alphasystem.openxml.builder.wml.WmlBuilderFactory.getPPrBuilder;

/**
 * @author sali
 */
public class TitleBuilder extends BlockBuilder<Title> {

    public TitleBuilder(Builder<?> parent, Title title, int indexInParent) {
        super(parent, title, indexInParent);
    }

    @Override
    protected void initContent() {
        content = source.getContent();
    }

    @Override
    protected void preProcess() {
        paraProperties = getPPrBuilder().withPStyle(configurationUtils.getTitleStyle(parent)).getObject();
    }

    @Override
    protected List<Object> postProcess(List<Object> processedTitleContent, List<Object> processedChildContent) {
        final P p = (P) processedChildContent.get(0);
        if (paraProperties != null) {
            p.setPPr(paraProperties);
        }
        if (parent != null) {
            WmlAdapter.addBookMark(p, getId(parent.getSource()));
        }
        return Collections.singletonList(p);
    }

}
