package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import java.util.List;
import org.docbook.model.TableHeader;

public class TableHeaderBuilder extends TableContentBuilder<TableHeader> {

  public TableHeaderBuilder(TableHeader source, Builder<?> parent) {
    super(source, parent);
  }

  @Override
  protected List<Object> getChildContent() {
    return sanitizeRows(source.getRow());
  }
}
