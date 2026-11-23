package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docx4j.builder.wml.PPrBuilder;
import com.alphasystem.docx4j.builder.wml.WmlAdapter;
import com.alphasystem.docx4j.builder.wml.WmlBuilderFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.docbook.model.Figure;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.R;

public class FigureBuilder extends AbstractParaBuilder<Figure> {

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
    final var allParas =
        !processedChildContent.isEmpty()
            && processedChildContent.stream().allMatch(P.class::isInstance);
    if (processedChildContent.size() < 2 || !allParas) {
      // createPara would nest paragraph children inside a new paragraph, so only delegate for
      // run content; pass block content through unchanged
      if (processedChildContent.stream().allMatch(R.class::isInstance)) {
        return super.doProcess(processedChildContent);
      }
      return processedChildContent;
    }
    final var title = (P) processedChildContent.getLast();
    final var content = new ArrayList<>();
    // preserve any justification set on the figure content (e.g. imagedata align)
    final var jc =
        processedChildContent.subList(0, processedChildContent.size() - 1).stream()
            .map(P.class::cast)
            .map(P::getPPr)
            .filter(Objects::nonNull)
            .map(PPr::getJc)
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);
    processedChildContent.subList(0, processedChildContent.size() - 1).stream()
        .map(P.class::cast)
        .forEach(p -> content.addAll(p.getContent()));
    content.add(WmlAdapter.getLineBreakRun());
    content.addAll(title.getContent());
    // figure-level properties (role) override the title's, imagedata align wins over both
    var pPr = new PPrBuilder(paraProperties, title.getPPr()).getObject();
    if (Objects.nonNull(jc)) {
      pPr =
          new PPrBuilder(WmlBuilderFactory.getPPrBuilder().withJc(jc.getVal()).getObject(), pPr)
              .getObject();
    }
    final var merged =
        WmlBuilderFactory.getPBuilder().withPPr(pPr).addContent(content.toArray()).getObject();
    return List.of(merged);
  }
}
