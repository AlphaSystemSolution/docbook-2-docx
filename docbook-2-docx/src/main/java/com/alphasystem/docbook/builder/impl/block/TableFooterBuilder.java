package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import java.util.List;
import org.docbook.model.TableFooter;

public class TableFooterBuilder extends TableContentBuilder<TableFooter> {

  public TableFooterBuilder(TableFooter source, Builder<?> parent) {
    super(source, parent);
  }

  @Override
  protected List<Object> getChildContent() {
    return sanitizeRows(source.getRow());
  }
}
