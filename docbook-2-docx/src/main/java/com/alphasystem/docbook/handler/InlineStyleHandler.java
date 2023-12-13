package com.alphasystem.docbook.handler;

import com.alphasystem.openxml.builder.wml.RPrBuilder;

/**
 * Interface for handling style on current inline text.
 *
 * @author sali
 */
public interface InlineStyleHandler extends Handler {

    /**
     * Apply the style on the current run.
     *
     * @param rprBuilder current running {@link RPrBuilder}
     * @return modified {@link RPrBuilder} after applying this style
     */
    RPrBuilder applyStyle(RPrBuilder rprBuilder);
}
