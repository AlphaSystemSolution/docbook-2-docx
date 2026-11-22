package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;
import org.docbook.model.MediaObject;

import java.util.List;

public class MediaObjectBuilder extends BlockBuilder<MediaObject> {

  public MediaObjectBuilder(MediaObject source, Builder<?> parent) {
    super(source, parent);
  }

  @Override
  protected List<Object> processChildContent(List<Object> childContent) {
    return super.processChildContent(childContent);
  }
}
