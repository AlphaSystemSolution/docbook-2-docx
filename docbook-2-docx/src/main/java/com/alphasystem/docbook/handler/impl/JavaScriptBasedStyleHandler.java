package com.alphasystem.docbook.handler.impl;

import com.alphasystem.docbook.ScriptHandler;
import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.docbook.model.FunctionInput;
import com.alphasystem.docx4j.builder.wml.RPrBuilder;

/**
 * Javascript based inline style handler. The handler would take styleName (role), which would be the name of the
 * function as well.
 */
public class JavaScriptBasedStyleHandler implements InlineStyleHandler, ScriptHandler<RPrBuilder> {

    private final String styleName;

    /**
     * Initialize handler with the given style name / role / function name.
     *
     * @param styleName Name of the style or role, this would be the name of JavaScriptFunction as well.
     */
    public JavaScriptBasedStyleHandler(String styleName) {
        this.styleName = styleName;
    }

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        return execute(new FunctionInput<>(styleName, RPrBuilder.class, new Object[]{rprBuilder}));
    }
}
