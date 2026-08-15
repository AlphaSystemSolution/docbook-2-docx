package com.alphasystem.docbook.handler.impl;

import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.docx4j.builder.wml.RPrBuilder;
import org.docx4j.wml.RPr;

/**
 * JavaScript-based inline style handler. The handler would take styleName (role), which would be
 * the name of the function as well.
 */
public class JavaScriptBasedStyleHandler implements InlineStyleHandler {

  private final RPr runProperties;

  /**
   * Initialize the handler with the given style name / role / function name.
   *
   * @param runProperties Run properties to be applied to the current run.
   */
  public JavaScriptBasedStyleHandler(RPr runProperties) {
    this.runProperties = runProperties;
  }

  @Override
  public RPrBuilder applyStyle(RPrBuilder rprBuilder) {
    return new RPrBuilder(rprBuilder.getObject(), runProperties);
  }
}
