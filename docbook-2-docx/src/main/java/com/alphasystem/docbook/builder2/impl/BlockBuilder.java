package com.alphasystem.docbook.builder2.impl;

import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.docbook.util.Utils;
import com.alphasystem.util.AppUtil;
import org.docbook.model.Title;

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
        final var o = Utils.invokeMethod(source, "getTitle");
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
    public List<Object> process() {
        final var processed = super.process();
        final var result = new ArrayList<>(processTitle());
        result.addAll(processed);
        return result;
    }
}
