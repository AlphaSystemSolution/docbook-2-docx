package com.alphasystem.docbook.builder.impl.block;

import static com.alphasystem.docx4j.builder.wml.WmlBuilderFactory.BOOLEAN_DEFAULT_TRUE_TRUE;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.AbstractBuilder;
import com.alphasystem.docx4j.builder.wml.WmlBuilderFactory;
import java.util.Collections;
import java.util.List;
import org.docbook.model.Row;
import org.docx4j.jaxb.Context;

public class RowBuilder extends AbstractBuilder<Row> {

  public RowBuilder(Row source, Builder<?> parent) {
    super(source, parent);
  }

  @Override
  protected List<Object> doProcess(List<Object> processedChildContent) {
    final var tableBuilder = getParent(AbstractTableBuilder.class);
    final var trBuilder = WmlBuilderFactory.getTrBuilder();
    if (tableBuilder != null && tableBuilder.isKeepTableTogether()) {
      final var factory = Context.getWmlObjectFactory();
      var trPr = trBuilder.getObject().getTrPr();
      if (trPr == null) {
        trPr = WmlBuilderFactory.getTrPrBuilder().getObject();
      }
      trPr.getCnfStyleOrDivIdOrGridBefore()
          .add(factory.createCTTrPrBaseCantSplit(BOOLEAN_DEFAULT_TRUE_TRUE));
      trBuilder.withTrPr(trPr);
    }

    trBuilder.addContent(processedChildContent.toArray());
    return Collections.singletonList(trBuilder.getObject());
  }
}
