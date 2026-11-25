package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;
import org.docbook.model.Informalfigure;

public class InformalFigureBuilder extends BlockBuilder<Informalfigure> {

  public InformalFigureBuilder(Informalfigure source, Builder<?> parent) {
    super("getItemizedlistOrOrderedlistOrProcedure", source, parent);
  }
}
