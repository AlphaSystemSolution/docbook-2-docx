package com.alphasystem.docbook.builder.impl;

import com.alphasystem.commons.util.AppUtil;
import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docx4j.builder.wml.WmlBuilderFactory;
import org.docbook.model.Title;
import org.docx4j.wml.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class BlockBuilder<S> extends AbstractBuilder<S> {

    protected BlockBuilder(S source, Builder<?> parent) {
        this("getContent", source, parent);
    }

    protected BlockBuilder(String childContentMethodName, S source, Builder<?> parent) {
        super(childContentMethodName, source, parent);
    }

    protected Title getTitle() {
        final var o = AppUtil.invokeMethod(source, "getTitle");
        if (Objects.nonNull(o) && AppUtil.isInstanceOf(Title.class, o)) {
            return (Title) o;
        }
        return null;
    }

    protected List<Object> processTitle() {
        final var title = getTitle();
        if (Objects.nonNull(title)) {
            return builderFactory.process(title, this);
        }
        return Collections.emptyList();
    }

    @Override
    protected List<Object> doProcess(List<Object> processedChildContent) {
        // at this stage child content should have been wrapped in one of block element, if not wrap in "P" object
        final var onlyRunType = io.vavr.collection.List.ofAll(processedChildContent).forAll(BlockBuilder::isRunType);
        if (onlyRunType) {
            processedChildContent = Collections.singletonList(WmlBuilderFactory.getPBuilder()
                    .addContent(processedChildContent.toArray()).getObject());
        }
        return super.doProcess(processedChildContent);
    }

    @Override
    public List<Object> process() {
        final var processed = super.process();
        final var result = new ArrayList<>(processTitle());
        result.addAll(processed);
        return result;
    }

    private static boolean isRunType(Object o) {
        return AppUtil.isInstanceOf(R.class, o);
    }
}
