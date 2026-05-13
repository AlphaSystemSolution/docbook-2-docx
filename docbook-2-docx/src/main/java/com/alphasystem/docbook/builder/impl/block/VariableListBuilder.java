package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import java.util.ArrayList;
import java.util.List;
import org.docbook.model.VariableList;

public class VariableListBuilder extends ListBuilder<VariableList> {

  public VariableListBuilder(VariableList source, Builder<?> parent) {
    super(source, parent);
  }

  @Override
  protected void setListStyleName() {
    listStyleName = "var-list";
  }

  @Override
  protected List<Object> getChildContent() {
    return new ArrayList<>(source.getVariableListEntry());
  }
}
