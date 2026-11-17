package com.alphasystem.docbook.handler.impl;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.docx4j.builder.wml.RPrBuilder;

public class SmallHandler implements InlineStyleHandler {

  @Override
  public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
    return rprBuilder.withSz(16L).withSzCs(16L);
  }
}
