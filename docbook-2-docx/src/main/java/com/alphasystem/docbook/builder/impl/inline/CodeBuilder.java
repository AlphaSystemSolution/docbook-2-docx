package com.alphasystem.docbook.builder.impl.inline;

import static com.alphasystem.docbook.handler.InlineHandlerFactory.LITERAL;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.InlineBuilder;
import org.docbook.model.Code;

public class CodeBuilder extends InlineBuilder<Code> {

  public CodeBuilder(Code source, Builder<?> parent) {
    super(LITERAL, source, parent);
  }
}
