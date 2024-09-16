package com.alphasystem.docbook.handler.impl;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.docx4j.builder.wml.RPrBuilder;

/**
 * Handles highlighted text
 *
 * @author sali
 */
public class HighlightHandler implements InlineStyleHandler {

    private final String highlightColor;

    public HighlightHandler() {
        this("yellow");
    }

    public HighlightHandler(String highlightColor) {
        this.highlightColor = highlightColor;
    }

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        return rprBuilder.withHighlight(highlightColor);
    }

    @Override
    public String toString() {
        return "HighlightHandler{" +
                "highlightColor='" + highlightColor + '\'' +
                '}';
    }
}
