package com.alphasystem.docbook.builder.impl;

import com.alphasystem.docbook.ScriptHandler;
import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.model.FunctionInput;

import java.util.Collections;
import java.util.List;

public abstract class JavaScriptBasedBuilder<S, T> extends BlockBuilder<S> implements ScriptHandler<T> {


    protected JavaScriptBasedBuilder(S source, Builder<?> parent) {
        this("getContent", source, parent);
    }

    protected JavaScriptBasedBuilder(String childContentMethodName, S source, Builder<?> parent) {
        super(childContentMethodName, source, parent);
    }

    protected abstract FunctionInput<T> initFunctionInputs(List<Object> processedChildContent);

    protected T initTarget(List<Object> processedChildContent) {
        return execute(initFunctionInputs(processedChildContent));
    }

    @Override
    protected List<Object> doProcess(List<Object> processedChildContent) {
        return Collections.singletonList(initTarget(processedChildContent));
    }

}
