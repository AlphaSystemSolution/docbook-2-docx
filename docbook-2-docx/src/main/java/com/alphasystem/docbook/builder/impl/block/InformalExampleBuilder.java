package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.JavaScriptBasedBuilder;
import com.alphasystem.docbook.model.FunctionInput;
import java.util.List;
import org.docbook.model.InformalExample;
import org.docx4j.wml.Tbl;

public class InformalExampleBuilder extends JavaScriptBasedBuilder<InformalExample, Tbl> {

  public InformalExampleBuilder(InformalExample source, Builder<?> parent) {
    super(source, parent);
  }

  @Override
  protected FunctionInput<Tbl> initFunctionInputs(List<Object> processedChildContent) {
    return new FunctionInput<>(
        configurationUtils.getExampleFunctionName(),
        Tbl.class,
        new Object[] {processedChildContent});
  }
}
