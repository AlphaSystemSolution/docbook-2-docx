package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;
import com.alphasystem.docbook.builder.impl.inline.InlineMediaObjectBuilder;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.docbook.model.Imagedata;
import org.docx4j.wml.R;

public class ImageDataBuilder extends BlockBuilder<Imagedata> {

  public ImageDataBuilder(Imagedata source, Builder<?> parent) {
    super(source, parent);
  }

  @Override
  protected List<Object> processChildContent(List<Object> childContent) {
    final var fileRef = source.getFileref();
    if (StringUtils.isBlank(fileRef)) {
      throw new IllegalArgumentException("File reference is required");
    }

    final var contentWidth = parseSize(source.getContentwidth());
    final var contentHeight = parseSize(source.getContentdepth());

    final var inlineMediaObjectBuilder = getParent(InlineMediaObjectBuilder.class);
    final var mediaObjectBuilder = getParent(MediaObjectBuilder.class);
    final var altText =
        inlineMediaObjectBuilder != null
            ? inlineMediaObjectBuilder.getAltText()
            : mediaObjectBuilder == null ? null : mediaObjectBuilder.getAltText();

    R run = null;
    try {
      run =
          ApplicationController.getContext()
              .getImageAdapter()
              .withUrl(URI.create(fileRef).toURL())
              .withAltText(altText)
              .withWidth(contentWidth)
              .withHeight(contentHeight)
              .insertImage();
    } catch (Exception e) {
      logger.error("Failed to insert image", e);
    }
    return Collections.singletonList(run);
  }

  private Double parseSize(String size) {
    if (StringUtils.isBlank(size)) {
      return null;
    }
    try {
      return Double.parseDouble(size);
    } catch (NumberFormatException e) {
      return null;
    }
  }
}
