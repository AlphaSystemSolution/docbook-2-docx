package com.alphasystem.docbook.builder.impl.inline;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.InlineBuilder;
import com.alphasystem.docbook.util.Utils;
import java.util.List;
import java.util.stream.Collectors;
import org.docbook.model.Imageobject;
import org.docbook.model.Imageobjectco;
import org.docbook.model.Inlinemediaobject;
import org.docbook.model.TextObject;
import org.docx4j.wml.P;

public class InlineMediaObjectBuilder extends InlineBuilder<Inlinemediaobject> {

  public InlineMediaObjectBuilder(Inlinemediaobject source, Builder<?> parent) {
    super(null, source, parent);
  }

  @Override
  protected List<Object> getChildContent() {
    return source.getVideoobjectOrAudioobjectOrImageobject().stream()
        .filter(content -> content instanceof Imageobject || content instanceof Imageobjectco)
        .collect(Collectors.toList());
  }

  @Override
  protected List<Object> doProcess(List<Object> processedChildContent) {
    final var runs =
        processedChildContent.stream()
            .flatMap(
                content -> (content instanceof P p ? p.getContent() : List.of(content)).stream())
            .toList();
    return super.doProcess(runs);
  }

  public String getAltText() {
    return source.getVideoobjectOrAudioobjectOrImageobject().stream()
        .filter(TextObject.class::isInstance)
        .map(TextObject.class::cast)
        .findFirst()
        .map(textObject -> Utils.getLinkText(textObject.getContent()))
        .orElse(null);
  }
}
