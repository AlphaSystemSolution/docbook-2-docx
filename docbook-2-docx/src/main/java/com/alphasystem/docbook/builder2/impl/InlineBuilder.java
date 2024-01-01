package com.alphasystem.docbook.builder2.impl;

import com.alphasystem.docbook.handler.InlineHandlerFactory;
import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.docbook.handler.impl.inline.NullHandler;
import com.alphasystem.openxml.builder.wml.RBuilder;
import com.alphasystem.openxml.builder.wml.RPrBuilder;
import com.alphasystem.openxml.builder.wml.WmlBuilderFactory;
import org.docx4j.wml.R;
import org.docx4j.wml.RPr;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ArrayUtils.isEmpty;

public abstract class InlineBuilder<S> extends AbstractBuilder<S> {

    protected String[] styles = null;
    protected RBuilder runBuilder;
    protected InlineHandlerFactory handlerFactory = InlineHandlerFactory.getInstance();

    protected InlineBuilder() {
        super();
    }

    protected InlineBuilder(String childContentMethodName) {
        super(childContentMethodName);
    }

    protected RPr handleStyles() {
        if (isEmpty(styles)) {
            return WmlBuilderFactory.getRPrBuilder().getObject();
        }
        RPrBuilder rPrBuilder;
        RPr rPr = null;
        for (String style : styles) {
            rPrBuilder = new RPrBuilder(handleStyle(style), rPr);
            rPr = rPrBuilder.getObject();
        }
        return rPr;
    }

    /**
     * Creates Run properties with given style.
     *
     * @param style name of style to apply
     * @return run properties with specified style applied
     */
    protected RPr handleStyle(String style) {
        if (style == null) {
            return null;
        }
        InlineStyleHandler handler = handlerFactory.getHandler(style);
        if (handler == null) {
            logger.warn("Not sure how to handle style \"{}\" in builder \"{}\".", style, getClass().getSimpleName());
            handler = new NullHandler();
        }
        return handler.applyStyle(WmlBuilderFactory.getRPrBuilder()).getObject();
    }

    protected void createRunBuilder() {
        runBuilder = WmlBuilderFactory.getRBuilder().withRsidR(getId());
    }

    @Override
    protected List<Object> doProcess(List<Object> processedChildContent) {
        createRunBuilder();
        final var rPr = new RPrBuilder(handleStyles(), runBuilder.getObject().getRPr()).getObject();
        final var r = runBuilder.withRPr(rPr).getObject();
        final var result = new ArrayList<>();
        result.add(r);

        final var updatedChildContent =
                processedChildContent.stream().map(content -> {
                    final var childR = (R) content;
                    childR.setRPr(new RPrBuilder(rPr, childR.getRPr()).getObject());
                    return childR;
                }).collect(Collectors.toList());

        result.addAll(updatedChildContent);
        return result;
    }
}
