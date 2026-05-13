package com.alphasystem.docbook.handler.impl;

import static org.docx4j.wml.STVerticalAlignRun.SUBSCRIPT;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.docx4j.builder.wml.RPrBuilder;

/**
 * Handles "subscript" style.
 *
 * @author sali
 */
public class SubscriptHandler implements InlineStyleHandler {

  @Override
  public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
    return rprBuilder.withVertAlign(SUBSCRIPT);
  }
}
