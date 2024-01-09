package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.docbook.builder2.impl.BlockBuilder;
import com.alphasystem.openxml.builder.wml.WmlAdapter;
import com.alphasystem.openxml.builder.wml.WmlBuilderFactory;
import com.alphasystem.util.AppUtil;
import org.docx4j.wml.PPr;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class AbstractParaBuilder<S> extends BlockBuilder<S> {

    protected PPr paraProperties;

    protected AbstractParaBuilder(S source, Builder<?> parent) {
        this("getContent", source, parent);
    }

    protected AbstractParaBuilder(String childContentMethodName, S source, Builder<?> parent) {
        super(childContentMethodName, source, parent);
    }

    @Override
    protected void preProcess() {
        super.preProcess();
        if (AppUtil.isInstanceOf(ListItemBuilder.class, parent)) {
            // this para is within a list item
            final var listItemBuilder = (ListItemBuilder) parent;
            final var listBuilder = (ListBuilder<?>) listItemBuilder.getParent();

            final var listRole = listBuilder.getRole();
            if (Objects.isNull(listRole)) {
                this.role = configurationUtils.getDefaultListStyle();
            }
            final var listInfo = listBuilder.listInfo;
            paraProperties = WmlAdapter.getListParagraphProperties(listInfo.getNumber(), listInfo.getLevel(), role,
                    id.equals(listItemBuilder.getFirstParaId()));
        } else {
            paraProperties = WmlBuilderFactory.getPPrBuilder().withPStyle(role).getObject();
        }
    }

    @Override
    protected List<Object> doProcess(List<Object> processedChildContent) {
        final var id = getId();
        final var p = WmlBuilderFactory.getPBuilder().withParaId(id).withPPr(paraProperties)
                .addContent(processedChildContent.toArray()).getObject();
        WmlAdapter.addBookMark(p, id);
        return Collections.singletonList(p);
    }
}
