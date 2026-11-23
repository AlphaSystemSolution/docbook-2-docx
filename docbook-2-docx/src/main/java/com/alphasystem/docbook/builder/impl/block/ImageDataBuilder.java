package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.inline.InlineMediaObjectBuilder;
import com.alphasystem.docx4j.builder.wml.WmlBuilderFactory;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.docbook.model.Align;
import org.docbook.model.BasicVerticalAlign;
import org.docbook.model.Imagedata;
import org.docx4j.dml.wordprocessingDrawing.STAlignH;
import org.docx4j.dml.wordprocessingDrawing.STAlignV;
import org.docx4j.dml.wordprocessingDrawing.STRelFromH;
import org.docx4j.dml.wordprocessingDrawing.STRelFromV;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.R;

public class ImageDataBuilder extends AbstractParaBuilder<Imagedata> {

  private STAlignH horizontalAlign;
  private STAlignV verticalAlign;

  public ImageDataBuilder(Imagedata source, Builder<?> parent) {
    super(source, parent);
  }

  @Override
  protected void preProcess() {
    super.preProcess();

    final var jc = toJc(source.getAlign());
    if (Objects.nonNull(jc)) {
      paraProperties.setJc(WmlBuilderFactory.getJcBuilder().withVal(jc).getObject());
    }

    final var verticalJc = toVerticalAlign(source.getValign());
    if (Objects.nonNull(verticalJc)) {
      // a floating (anchored) image is not affected by paragraph justification, so the horizontal
      // alignment has to be expressed on the anchor itself
      verticalAlign = verticalJc;
      horizontalAlign = toHorizontalAlign(source.getAlign());
    }
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
      var imageAdapter =
          ApplicationController.getContext()
              .getImageAdapter()
              .withUrl(URI.create(fileRef).toURL())
              .withAltText(altText)
              .withWidth(contentWidth)
              .withHeight(contentHeight);
      if (Objects.nonNull(verticalAlign)) {
        imageAdapter =
            imageAdapter
                .withHorizontalAlignment(STRelFromH.COLUMN, horizontalAlign)
                .withVerticalAlignment(STRelFromV.LINE, verticalAlign);
      }
      run = imageAdapter.insertImage();
    } catch (Exception e) {
      logger.error("Failed to insert image", e);
    }
    return run == null ? Collections.emptyList() : Collections.singletonList(run);
  }

  @Override
  protected List<Object> doProcess(List<Object> processedChildContent) {
    if (Objects.nonNull(getParent(InlineMediaObjectBuilder.class))) {
      // the paragraph is flattened back into runs by InlineMediaObjectBuilder, a bookmark would
      // break that path
      final var p =
          WmlBuilderFactory.getPBuilder()
              .withPPr(paraProperties)
              .addContent(processedChildContent.toArray())
              .getObject();
      return Collections.singletonList(p);
    }
    return super.doProcess(processedChildContent);
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

  private static JcEnumeration toJc(Align align) {
    if (align == null) {
      return null;
    }
    return switch (align) {
      case LEFT -> JcEnumeration.LEFT;
      case CENTER -> JcEnumeration.CENTER;
      case RIGHT -> JcEnumeration.RIGHT;
      case JUSTIFY -> JcEnumeration.BOTH;
      default -> null;
    };
  }

  private static STAlignH toHorizontalAlign(Align align) {
    if (align == null) {
      return STAlignH.LEFT;
    }
    return switch (align) {
      case CENTER -> STAlignH.CENTER;
      case RIGHT -> STAlignH.RIGHT;
      default -> STAlignH.LEFT;
    };
  }

  private static STAlignV toVerticalAlign(BasicVerticalAlign valign) {
    if (valign == null) {
      return null;
    }
    return switch (valign) {
      case TOP -> STAlignV.TOP;
      case MIDDLE -> STAlignV.CENTER;
      case BOTTOM -> STAlignV.BOTTOM;
    };
  }
}
