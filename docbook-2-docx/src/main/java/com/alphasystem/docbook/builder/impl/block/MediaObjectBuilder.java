package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;
import com.alphasystem.docbook.util.Utils;
import java.util.List;
import java.util.stream.Collectors;
import org.docbook.model.Imageobject;
import org.docbook.model.Imageobjectco;
import org.docbook.model.MediaObject;
import org.docbook.model.TextObject;

public class MediaObjectBuilder extends BlockBuilder<MediaObject> {

  public MediaObjectBuilder(MediaObject source, Builder<?> parent) {
    super(source, parent);
  }

  @Override
  protected List<Object> getChildContent() {
    return source.getContent().stream()
        .filter(content -> content instanceof Imageobject || content instanceof Imageobjectco)
        .collect(Collectors.toList());
  }

  public String getAltText() {
    return source.getContent().stream()
        .filter(TextObject.class::isInstance)
        .map(TextObject.class::cast)
        .findFirst()
        .map(textObject -> Utils.getLinkText(textObject.getContent()))
        .orElse(null);
  }
}
