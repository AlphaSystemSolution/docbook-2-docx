package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;
import com.alphasystem.docbook.model.VariableListType;
import com.alphasystem.openxml.builder.wml.WmlAdapter;
import com.alphasystem.openxml.builder.wml.WmlBuilderFactory;
import com.alphasystem.commons.util.AppUtil;
import org.docx4j.wml.PPr;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class AbstractParaBuilder<S> extends BlockBuilder<S> {

    protected PPr paraProperties;
    private final VariableListType variableListType = new VariableListType();

    protected AbstractParaBuilder(S source, Builder<?> parent) {
        this("getContent", source, parent);
    }

    protected AbstractParaBuilder(String childContentMethodName, S source, Builder<?> parent) {
        super(childContentMethodName, source, parent);
    }

    @Override
    protected void preProcess() {
        super.preProcess();

        paraProperties = WmlBuilderFactory.getPPrBuilder().withPStyle(role).getObject();
        if (AppUtil.isInstanceOf(TermBuilder.class, this)) {
            role = configurationUtils.getVarTermStyle();
            final var pPrBuilder = WmlBuilderFactory.getPPrBuilder().withPStyle(role);
            final var variableListBuilder = getParent(VariableListBuilder.class);
            if (Objects.nonNull(variableListBuilder)) {
                final var listInfo = variableListBuilder.getListInfo();
                final var level = (int) listInfo.getLevel();
                if (level > 0) {
                    final var leftIndent = variableListType.getLeftIndent(level);
                    final var indent = WmlBuilderFactory.getPPrBaseBuilder().getIndBuilder().withLeft(leftIndent).getObject();
                    pPrBuilder.withInd(indent);
                }
            }
            paraProperties = pPrBuilder.getObject();
        } else if (AppUtil.isInstanceOf(ListItemBuilder.class, parent)) {
            // this para is within a list item
            final var listItemBuilder = (ListItemBuilder) parent;
            final var grandParent = listItemBuilder.getParent();

            var numberId = -1L;
            var level = -1L;
            var applyNumbering = false;
            if (AppUtil.isInstanceOf(ListBuilder.class, grandParent)) {
                final var listBuilder = ((ListBuilder<?>) grandParent);
                final var _role = listBuilder.getRole();
                if (Objects.nonNull(_role)) {
                    role = _role;
                } else {
                    role = configurationUtils.getDefaultListStyle();
                }
                final var listInfo = listBuilder.getListInfo();
                numberId = listInfo.getNumber();
                level = listInfo.getLevel();
                applyNumbering = id.equals(listItemBuilder.getFirstParaId()) && numberId > -1;
            }
            paraProperties = WmlAdapter.getListParagraphProperties(numberId, level, role, applyNumbering);
        }
    }

    protected List<Object> createPara(List<Object> processedChildContent) {
        final var id = getId();
        final var p = WmlBuilderFactory.getPBuilder().withParaId(id).withPPr(paraProperties)
                .addContent(processedChildContent.toArray()).getObject();
        WmlAdapter.addBookMark(p, id);
        return Collections.singletonList(p);
    }

    @Override
    protected List<Object> doProcess(List<Object> processedChildContent) {
        return createPara(processedChildContent);
    }
}
