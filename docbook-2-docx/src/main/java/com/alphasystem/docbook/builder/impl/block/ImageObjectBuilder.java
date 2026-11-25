package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;
import java.util.Collections;
import java.util.List;
import org.docbook.model.Imageobject;

public class ImageObjectBuilder extends BlockBuilder<Imageobject> {

  public ImageObjectBuilder(Imageobject source, Builder<?> parent) {
    super(null, source, parent);
  }

  @Override
  protected List<Object> getChildContent() {
    final var imagedata = source.getImagedata();
    return imagedata == null ? Collections.emptyList() : Collections.singletonList(imagedata);
  }
}
