package com.alphasystem.asciidoc.util;

import static java.nio.file.Files.exists;

import com.alphasystem.asciidoc.model.AsciiDocumentInfo;
import com.alphasystem.asciidoc.model.Backend;
import com.alphasystem.commons.SystemException;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.OptionsBuilder;

public class DocumentConverter {

  private static final Asciidoctor asciiDoctor = Asciidoctor.Factory.create();

  private DocumentConverter() {}

  public static AsciiDocumentInfo convertDocument(final Path srcPath) {
    final File srcFile = srcPath.toFile();
    if (!exists(srcPath)) {
      throw new NullPointerException("Source file does not exists.");
    }
    final var documentInfo = new AsciiDocumentInfo();
    documentInfo.setSrcFile(srcFile);

    final var document = asciiDoctor.loadFile(srcFile, Options.builder().build());
    documentInfo.populateAttributes(document);

    return documentInfo;
  }

  public static AsciiDocumentInfo convertToDocBook(final Path srcPath) throws SystemException {
    return convert(Backend.DOC_BOOK, srcPath);
  }

  public static AsciiDocumentInfo convertToXhtml(final Path srcPath) throws SystemException {
    return convert(Backend.XHTML, srcPath);
  }

  public static AsciiDocumentInfo convertToHtml(final Path srcPath) throws SystemException {
    return convert(Backend.HTML, srcPath);
  }

  private static AsciiDocumentInfo convert(final Backend backend, final Path srcPath) throws SystemException {
    String content;
    final var documentInfo = new AsciiDocumentInfo(convertDocument(srcPath));
    documentInfo.setBackend(backend.getValue());
    OptionsBuilder optionsBuilder = documentInfo.getOptionsBuilder().standalone(true);
    try {
      try (Reader reader =
                   Files.newBufferedReader(documentInfo.getDocumentInfo().getSrcFile().toPath());
           StringWriter writer = new StringWriter()) {
        asciiDoctor.convert(reader, writer, optionsBuilder.build());
        content = writer.toString();
      }
    } catch (IOException e) {
      throw new SystemException(e.getMessage(), e);
    }
    documentInfo.setContent(content);
    documentInfo.setDocument(null);
    return documentInfo;
  }
}
