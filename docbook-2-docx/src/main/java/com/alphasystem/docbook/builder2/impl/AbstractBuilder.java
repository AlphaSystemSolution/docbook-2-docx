package com.alphasystem.docbook.builder2.impl;

import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.docbook.builder2.BuilderFactory;
import com.alphasystem.docbook.util.ConfigurationUtils;
import com.alphasystem.docbook.util.Utils;
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
    protected S source;
    private final String childContentMethodName;

    protected AbstractBuilder() {
        this("getContent");
    }

    protected AbstractBuilder(String childContentMethodName) {
        this.childContentMethodName = childContentMethodName;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public S getSource() {
        return source;
    }

    @Override
    public List<Object> process(S source) {
        if (source == null) {
            throw new NullPointerException(String.format("Source object is null in \"%s\"", getClass().getName()));
        }
        doInit(source);
        return doProcess(processChildContent(getChildContent()));
    }

    protected void doInit(S source) {
        builderFactory = BuilderFactory.getInstance();
        this.source = source;
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
        return childContent.stream().map(builderFactory::process).flatMap(Collection::stream).collect(Collectors.toList());
    }

    protected abstract List<Object> doProcess(List<Object> processedChildContent);
}
