package com.alphasystem.docbook.builder2.impl;

import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.docbook.builder2.BuilderFactory;
import com.alphasystem.docbook.util.ConfigurationUtils;
import com.alphasystem.docbook.util.Utils;
import com.alphasystem.util.AppUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;


public abstract class AbstractBuilder<S> implements Builder<S> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final ConfigurationUtils configurationUtils = ConfigurationUtils.getInstance();
    protected final BuilderFactory builderFactory = BuilderFactory.getInstance();
    protected String id;
    protected String role;
    protected Builder<?> parent;
    protected S source;
    private final String childContentMethodName;

    protected AbstractBuilder(S source, Builder<?> parent) {
        this("getContent", source, parent);
    }

    protected AbstractBuilder(String childContentMethodName, S source, Builder<?> parent) {
        if (source == null) {
            throw new NullPointerException(String.format("Source object is null in \"%s\"", getClass().getName()));
        }
        this.source = source;
        this.parent = parent;
        this.id = Utils.getId(source);
        this.role = (String) Utils.invokeMethod(source, "getRole");
        this.childContentMethodName = childContentMethodName;
    }

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
        preProcess();
        return doProcess(processChildContent(getChildContent()));
    }

    protected void preProcess() {
    }

    @SuppressWarnings("unchecked")
    protected List<Object> getChildContent() {
        if (StringUtils.isNotBlank(childContentMethodName)) {
            return (List<Object>) Utils.invokeMethod(source, childContentMethodName);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public String getRole() {
        if (Objects.isNull(role)) {
            role = (String) Utils.invokeMethod(source, "getRole");
        }
        return role;
    }

    protected List<Object> processChildContent(List<Object> childContent) {
        return childContent.stream().map(content -> builderFactory.process(content, this))
                .flatMap(Collection::stream).collect(Collectors.toList());
    }

    protected List<Object> doProcess(List<Object> processedChildContent) {
        return processedChildContent;
    }

    @SuppressWarnings({"unchecked"})
    protected <B extends Builder<?>> B getParent(Class<B> parentBuilderClass) {
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

    /**
     * Returns list of parent builders of this builder, will return empty list if there is no parent.
     * @return list of parents of this builder.
     */
    protected List<Builder<?>> getParents() {
        final var parents = new ArrayList<Builder<?>>();
        var currentParent = parent;
        while (currentParent != null) {
            parents.add(currentParent);
            currentParent = currentParent.getParent();
        }
        return parents;
    }
}
