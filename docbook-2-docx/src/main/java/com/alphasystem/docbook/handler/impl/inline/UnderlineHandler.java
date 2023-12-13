package com.alphasystem.docbook.handler.impl.inline;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.openxml.builder.wml.RPrBuilder;

import static com.alphasystem.openxml.builder.wml.WmlBuilderFactory.getUBuilder;
import static org.docx4j.wml.UnderlineEnumeration.SINGLE;

/**
 * Handles "underline" style.
 *
 * @author sali
 */
class UnderlineHandler implements InlineStyleHandler {

    UnderlineHandler() {
    }

    @Override
    public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
        return rprBuilder.withU(getUBuilder().withVal(SINGLE).getObject());
    }
}
