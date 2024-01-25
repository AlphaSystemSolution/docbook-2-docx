package com.alphasystem.docbook;

import com.alphasystem.docbook.model.FunctionInput;

public interface ScriptHandler<T> {

    default T execute(FunctionInput<T> functionInput) {
        final var binding = ApplicationController.getInstance().getScriptEngine().getBindings("js");
        return binding.getMember(functionInput.functionName()).execute(functionInput.args())
                .as(functionInput.targetType());
    }
}
