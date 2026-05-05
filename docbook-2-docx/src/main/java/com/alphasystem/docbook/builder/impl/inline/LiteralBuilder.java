package com.alphasystem.docbook.builder.impl.inline;

import static com.alphasystem.docbook.handler.InlineHandlerFactory.LITERAL;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.InlineBuilder;
import org.docbook.model.Literal;

public class LiteralBuilder extends InlineBuilder<Literal> {

  public LiteralBuilder(Literal source, Builder<?> parent) {
    super(LITERAL, source, parent);
  }
}
