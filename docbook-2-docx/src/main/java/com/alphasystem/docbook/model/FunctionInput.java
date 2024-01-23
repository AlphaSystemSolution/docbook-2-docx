package com.alphasystem.docbook.model;

import java.util.Objects;

public record FunctionInput<T>(String functionName, Class<T> targetType, Object[] args) {
    public FunctionInput(String functionName, Class<T> targetType, Object[] args) {
        this.functionName = functionName;
        this.targetType = targetType;
        this.args = Objects.isNull(args) ? new Object[0] : args;
    }
}
