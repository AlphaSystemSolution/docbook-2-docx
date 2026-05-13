package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import java.util.List;
import org.docbook.model.TableBody;

public class TableBodyBuilder extends TableContentBuilder<TableBody> {

  public TableBodyBuilder(TableBody source, Builder<?> parent) {
    super(source, parent);
  }

  @Override
  protected List<Object> getChildContent() {
    return sanitizeRows(source.getRow());
  }
}
