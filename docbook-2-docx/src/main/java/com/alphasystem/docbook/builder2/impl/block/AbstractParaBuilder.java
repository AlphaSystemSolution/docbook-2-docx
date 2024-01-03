package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docbook.builder2.impl.AbstractBuilder;
import com.alphasystem.docbook.util.Utils;
import com.alphasystem.openxml.builder.wml.WmlAdapter;
import com.alphasystem.openxml.builder.wml.WmlBuilderFactory;
import org.apache.commons.lang3.StringUtils;
import org.docx4j.wml.PPr;

import java.util.Collections;
import java.util.List;

public abstract class AbstractParaBuilder<S> extends AbstractBuilder<S> {

    private static final String LIST_TYPE_PREFIX = "list_";
    private static final String LIST_TYPE_SUFFIX = "$";
    protected String role;
    protected PPr paraProperties;

    protected AbstractParaBuilder() {
        super();
    }

    protected AbstractParaBuilder(String childContentMethodName) {
        super(childContentMethodName);
    }

    @Override
    protected void doInit(S source) {
        super.doInit(source);
        this.role = (String) Utils.invokeMethod(source,"getRole");
        var listTypeWithPreviousParaObjects = false;
        var listType = false;
        var pprBuilder = WmlBuilderFactory.getPPrBuilder();
        if (StringUtils.isNotBlank(this.role)) {

            // prefix is to indicate that this role for list type
            if (this.role.startsWith(LIST_TYPE_PREFIX)) {
                listType = true;
                final var index = this.role.indexOf(LIST_TYPE_PREFIX);
                this.role = this.role.substring(index + LIST_TYPE_PREFIX.length());
            }

            // suffix is to indicate that this para is successive para in the list, we need to set "numPr" to null
            if (this.role.endsWith(LIST_TYPE_SUFFIX)) {
                listTypeWithPreviousParaObjects = true;
                this.role = this.role.substring(0, this.role.length() - 1);
            }
            pprBuilder = pprBuilder.withPStyle(this.role);
        }

        if (listType) {
            final var currentListInfo = ApplicationController.getContext().getCurrentListInfo();
            final var number = currentListInfo.getNumber();
            final var level = currentListInfo.getLevel();
            final var numPr = ((number < 0) || (level < 0) || listTypeWithPreviousParaObjects) ? null : WmlAdapter.getNumPr(number, level);
            pprBuilder.withNumPr(numPr);
        }

        paraProperties = pprBuilder.getObject();
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
