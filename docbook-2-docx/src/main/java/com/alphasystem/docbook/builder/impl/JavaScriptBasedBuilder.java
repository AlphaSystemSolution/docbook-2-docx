package com.alphasystem.docbook.builder.impl;

import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.model.FunctionInput;

import java.util.Collections;
import java.util.List;

public abstract class JavaScriptBasedBuilder<S, T> extends BlockBuilder<S> {


    protected JavaScriptBasedBuilder(S source, Builder<?> parent) {
        this("getContent", source, parent);
    }

    protected JavaScriptBasedBuilder(String childContentMethodName, S source, Builder<?> parent) {
        super(childContentMethodName, source, parent);

    }

    protected abstract FunctionInput<T> initFunctionInputs(List<Object> processedChildContent);

    protected T initTarget(List<Object> processedChildContent) {
        final var functionInput = initFunctionInputs(processedChildContent);
        final var binding = ApplicationController.getInstance().getScriptEngine().getBindings("js");
        return binding.getMember(functionInput.functionName()).execute(functionInput.args())
                .as(functionInput.targetType());
    }

    @Override
    protected List<Object> doProcess(List<Object> processedChildContent) {
        return Collections.singletonList(initTarget(processedChildContent));
    }

}
