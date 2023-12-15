package com.alphasystem.asciidoc.util;

import com.alphasystem.SystemException;
import com.alphasystem.asciidoc.model.AsciiDocumentInfo;
import com.alphasystem.asciidoc.model.Backend;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.OptionsBuilder;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;

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
        return convert(Backend.DOC_BOOK, srcPath);
    }

    public static AsciiDocumentInfo convertToXhtml(final Path srcPath) throws SystemException {
        return convert(Backend.XHTML, srcPath);
    }

    private static AsciiDocumentInfo convert(final Backend backend, final Path srcPath) throws SystemException {
        String content;
        final var asciiDocumentInfo = new AsciiDocumentInfo(convertDocument(srcPath));
        asciiDocumentInfo.setBackend(backend.getValue());
        OptionsBuilder optionsBuilder = asciiDocumentInfo.getOptionsBuilder().standalone(true);
        try {
            try (Reader reader = Files.newBufferedReader(asciiDocumentInfo.getDocumentInfo().getSrcFile().toPath());
                 StringWriter writer = new StringWriter()) {
                asciiDoctor.convert(reader, writer, optionsBuilder.build());
                content = writer.toString();
            }
        } catch (IOException e) {
            throw new SystemException(e.getMessage(), e);
        }
        asciiDocumentInfo.setContent(content);
        asciiDocumentInfo.setDocument(null);
        return asciiDocumentInfo;
    }
}
