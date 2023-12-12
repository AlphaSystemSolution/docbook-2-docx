package com.alphasystem.asciidoc.util;

import com.alphasystem.SystemException;
import com.alphasystem.asciidoc.model.AsciiDocumentInfo;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.OptionsBuilder;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.alphasystem.asciidoc.model.Backend.DOC_BOOK;
import static java.nio.file.Files.exists;

public class DocumentConverter {

    private final static Asciidoctor asciiDoctor = Asciidoctor.Factory.create();

    private DocumentConverter() {
    }

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
        String docBookContent;
        final var docBook = new AsciiDocumentInfo(convertDocument(srcPath));
        docBook.setBackend(DOC_BOOK.getValue());
        OptionsBuilder optionsBuilder = docBook.getOptionsBuilder().standalone(true);
        try {
            try (Reader reader = Files.newBufferedReader(docBook.getSrcFile().toPath());
                 StringWriter writer = new StringWriter()) {
                asciiDoctor.convert(reader, writer, optionsBuilder.build());
                docBookContent = writer.toString();
            }
        } catch (IOException e) {
            throw new SystemException(e.getMessage(), e);
        }
        docBook.setContent(docBookContent);
        docBook.setDocument(null);
        return docBook;
    }
}
