package com.alphasystem.docbook.handler.impl.inline;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.openxml.builder.wml.RPrBuilder;

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
