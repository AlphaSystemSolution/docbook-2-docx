package com.alphasystem.docbook.handler.impl;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.docx4j.builder.wml.RPrBuilder;

/**
 * Handler to handle case where because of misconfiguration system is unable to find handler class.
 *
 * @author sali
 */
public class NullHandler implements InlineStyleHandler {

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        return rprBuilder;
    }
}
