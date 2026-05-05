package com.alphasystem.docbook.builder.impl.inline;

import static com.alphasystem.docbook.handler.InlineHandlerFactory.SUBSCRIPT;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.InlineBuilder;
import org.docbook.model.Subscript;

public class SubscriptBuilder extends InlineBuilder<Subscript> {

  public SubscriptBuilder(Subscript source, Builder<?> parent) {
    super(SUBSCRIPT, source, parent);
  }
}
