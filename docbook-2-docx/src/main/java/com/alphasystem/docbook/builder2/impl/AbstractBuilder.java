package com.alphasystem.docbook.builder2.impl;

import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.docbook.builder2.BuilderFactory;
import com.alphasystem.docbook.util.ConfigurationUtils;
import com.alphasystem.docbook.util.Utils;
import com.alphasystem.util.AppUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public abstract class AbstractBuilder<S> implements Builder<S> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final ConfigurationUtils configurationUtils = ConfigurationUtils.getInstance();
    protected BuilderFactory builderFactory;
    protected String id;
    protected Builder<?> parent;
    protected S source;
    private final String childContentMethodName;

    protected AbstractBuilder(S source, Builder<?> parent) {
        this("getContent", source, parent);
    }

    protected AbstractBuilder(String childContentMethodName, S source, Builder<?> parent) {
        this.childContentMethodName = childContentMethodName;
        doInit(source, parent);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Builder<?> getParent() {
        return parent;
    }

    @Override
    public S getSource() {
        return source;
    }

    @Override
    public List<Object> process() {
        if (source == null) {
            throw new NullPointerException(String.format("Source object is null in \"%s\"", getClass().getName()));
        }
        return doProcess(processChildContent(getChildContent()));
    }

    protected void doInit(S source, Builder<?> parent) {
        builderFactory = BuilderFactory.getInstance();
        this.source = source;
        this.parent = parent;
        this.id = Utils.getId(source);
    }

    @SuppressWarnings("unchecked")
    protected List<Object> getChildContent() {
        if (StringUtils.isNotBlank(childContentMethodName)) {
            return (List<Object>) Utils.invokeMethod(source, childContentMethodName);
        } else {
            return Collections.emptyList();
        }
    }

    protected String getRole() {
        return (String) Utils.invokeMethod(source, "getRole");
    }

    protected List<Object> processChildContent(List<Object> childContent) {
        return childContent.stream().map(content -> builderFactory.process(content, this))
                .flatMap(Collection::stream).collect(Collectors.toList());
    }

    protected abstract List<Object> doProcess(List<Object> processedChildContent);

    protected <B extends Builder<B>> boolean hasParent(Class<B> parentBuilderClass) {
        return getParent(parentBuilderClass) != null;
    }

    @SuppressWarnings({"unchecked"})
    protected <B extends Builder<B>> B getParent(Class<B> parentBuilderClass) {
        B result = null;
        var currentParent = parent;
        while (currentParent != null) {
            if (AppUtil.isInstanceOf(parentBuilderClass, currentParent)) {
                result = (B) currentParent;
                break;
            }
            currentParent = currentParent.getParent();
        }
        return result;
    }
}
