package com.alphasystem.xml;

import com.alphasystem.commons.SystemException;
import com.alphasystem.docbook.ApplicationController;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

/**
 * @author sali
 */
public class UnmarshallerTool {
  public WordprocessingMLPackage unmarshal(String source) throws SystemException {
    final var context = ApplicationController.getContext();
    try {
      final var parsed = new DocBookParser().parse(source);
      new DocBookModelNormalizer(parsed).normalize();
      final var renderer = new DocBookRenderer(parsed);
      context.setDocBookRenderer(renderer);
      renderer.render();
      return context.getWordprocessingMLPackage();
    } catch (Exception ex) {
      throw new SystemException(ex.getMessage(), ex);
    } finally {
      context.setDocBookRenderer(null);
      context.restTableWidth();
      context.resetKeepTogether();
    }
  }
}
