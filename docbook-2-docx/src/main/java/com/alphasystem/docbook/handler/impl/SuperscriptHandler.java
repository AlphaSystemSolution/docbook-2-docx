package com.alphasystem.docbook.handler.impl;

import static org.docx4j.wml.STVerticalAlignRun.SUPERSCRIPT;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.docx4j.builder.wml.RPrBuilder;

/**
 * Handles "superscript" style.
 *
 * @author sali
 */
public class SuperscriptHandler implements InlineStyleHandler {

  @Override
  public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
    return rprBuilder.withVertAlign(SUPERSCRIPT);
  }
}
