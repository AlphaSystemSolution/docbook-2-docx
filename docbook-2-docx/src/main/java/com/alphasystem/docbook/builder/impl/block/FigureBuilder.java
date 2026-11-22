package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;
import com.alphasystem.docx4j.builder.wml.WmlAdapter;
import com.alphasystem.docx4j.builder.wml.WmlBuilderFactory;
import java.util.ArrayList;
import java.util.List;
import org.docbook.model.Figure;
import org.docx4j.wml.P;

public class FigureBuilder extends BlockBuilder<Figure> {

  public FigureBuilder(Figure source, Builder<?> parent) {
    super(source, parent);
  }

  @Override
  protected List<Object> getChildContent() {
    // render title underneath the figure content, per AsciiDoc convention
    final var content = new ArrayList<>(source.getItemizedlistOrOrderedlistOrProcedure());
    content.addAll(source.getTitleOrTitleabbrev());
    return content;
  }

  @Override
  protected List<Object> doProcess(List<Object> processedChildContent) {
    // merge figure content and its title into a single paragraph separated by a line break
    if (processedChildContent.size() < 2
        || !processedChildContent.stream().allMatch(P.class::isInstance)) {
      return super.doProcess(processedChildContent);
    }
    final var title = (P) processedChildContent.getLast();
    final var content = new ArrayList<>();
    processedChildContent.subList(0, processedChildContent.size() - 1).stream()
        .map(P.class::cast)
        .forEach(p -> content.addAll(p.getContent()));
    content.add(WmlAdapter.getLineBreakRun());
    content.addAll(title.getContent());
    final var merged =
        WmlBuilderFactory.getPBuilder()
            .withPPr(title.getPPr())
            .addContent(content.toArray())
            .getObject();
    return List.of(merged);
  }
}
