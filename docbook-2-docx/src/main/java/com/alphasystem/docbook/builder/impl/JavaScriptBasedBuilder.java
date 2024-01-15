package com.alphasystem.docbook.builder.impl;

import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docbook.builder.Builder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
        return binding.getMember(functionInput.getFunctionName()).execute(functionInput.getArgs())
                .as(functionInput.getTargetType());
    }

    @Override
    protected List<Object> doProcess(List<Object> processedChildContent) {
        return Collections.singletonList(initTarget(processedChildContent));
    }

    protected static class FunctionInput<T> {

        private final String functionName;
        private final Class<T> targetType;
        private final Object[] args;

        public FunctionInput(String functionName, Class<T> targetType, Object[] args) {
            this.functionName = functionName;
            this.targetType = targetType;
            this.args = Objects.isNull(args) ? new Object[0] : args;
        }

        public String getFunctionName() {
            return functionName;
        }

        public Class<T> getTargetType() {
            return targetType;
        }

        public Object[] getArgs() {
            return args;
        }
    }

}
