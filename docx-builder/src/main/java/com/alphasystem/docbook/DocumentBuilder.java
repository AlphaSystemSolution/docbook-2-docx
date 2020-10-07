package com.alphasystem.docbook;

import com.alphasystem.SystemException;
import com.alphasystem.asciidoc.model.AsciiDocumentInfo;
import com.alphasystem.docbook.builder.BuilderFactory;
import com.alphasystem.docbook.util.ConfigurationUtils;
import com.alphasystem.docbook.util.FileUtil;
import com.alphasystem.openxml.builder.wml.WmlPackageBuilder;
import com.alphasystem.util.nio.NIOFileUtils;
import com.alphasystem.xml.UnmarshallerTool;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.ast.StructuredDocument;
import org.docbook.model.Article;
import org.docbook.model.Book;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.WordprocessingML.StyleDefinitionsPart;
import org.docx4j.wml.Style;
import org.docx4j.wml.Styles;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import static com.alphasystem.asciidoc.model.Backend.DOC_BOOK;
import static com.alphasystem.docbook.builder.model.DocumentCaption.EXAMPLE;
import static com.alphasystem.docbook.builder.model.DocumentCaption.TABLE;
import static com.alphasystem.openxml.builder.wml.WmlAdapter.addTableOfContent;
import static com.alphasystem.openxml.builder.wml.WmlAdapter.save;
import static java.nio.file.Files.exists;
import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author sali
 */
public class DocumentBuilder {

    private final static Asciidoctor asciiDoctor = Asciidoctor.Factory.create();
    private final static ConfigurationUtils configurationUtils = ConfigurationUtils.getInstance();

    static {
        // initialize Application controller
        ApplicationController.getInstance();
    }

    public static Path buildDocument(String content, AsciiDocumentInfo documentInfo) throws SystemException {
        if (isBlank(content) || documentInfo == null) {
            throw new SystemException("No content or document info");
        }
        String docBookContent = convertToDocBook(content, documentInfo);
        UnmarshallerTool unmarshallerTool = new UnmarshallerTool();

        final Path docxPath = FileUtil.getDocxFile(documentInfo.getSrcFile().toPath());
        final Object document = getDocument(docBookContent, unmarshallerTool);
        final DocumentContext documentContext = new DocumentContext(documentInfo, document);
        buildDocument(docxPath, documentContext);
        return docxPath;
    }

    public static Path buildDocument(Path srcPath, Path docxPath) throws SystemException {
        buildDocument(docxPath, createContext(srcPath));
        return docxPath;
    }

    public static Path buildDocument(Path srcPath) throws SystemException {
        return buildDocument(srcPath,  FileUtil.getDocxFile(srcPath));
    }

    public static DocumentContext createContext(Path srcPath) throws SystemException {
        final File srcFile = srcPath.toFile();
        if (!exists(srcPath)) {
            throw new NullPointerException("Source file does not exists.");
        }
        final Path fileNamePath = srcPath.getFileName();
        final String fileName = fileNamePath.toString();
        final String extension = getExtension(fileName);
        String docBookContent = null;
        AsciiDocumentInfo documentInfo = null;
        if ("adoc".endsWith(extension)) {
            // load ascii document info
            documentInfo = new AsciiDocumentInfo();
            documentInfo.setSrcFile(srcFile);
            StructuredDocument structuredDocument = asciiDoctor.readDocumentStructure(srcFile, new HashMap<>());
            documentInfo.populateAttributes(structuredDocument.getHeader().getAttributes());
            docBookContent = convertToDocBook(documentInfo);
        } else if ("xml".endsWith(extension)) {
            try {
                try (InputStream inputStream = Files.newInputStream(srcPath);
                     ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    NIOFileUtils.fastCopy(inputStream, outputStream);
                    docBookContent = new String(outputStream.toByteArray());
                }
            } catch (IOException e) {
                throw new SystemException(e.getMessage(), e);
            }
        }

        UnmarshallerTool unmarshallerTool = new UnmarshallerTool();
        Object document = getDocument(docBookContent, unmarshallerTool);
        if (documentInfo == null) {
            documentInfo = unmarshallerTool.getDocumentInfo();
        }
        return new DocumentContext(documentInfo, document);
    }

    public static WordprocessingMLPackage buildDocument(final DocumentContext documentContext) throws SystemException {
        ApplicationController.startContext(documentContext);
        WordprocessingMLPackage wordprocessingMLPackage = null;
        try {
            WmlPackageBuilder wmlPackageBuilder = WmlPackageBuilder.createPackage(configurationUtils.getTemplate())
                    .styles(configurationUtils.getStyles());
            wordprocessingMLPackage = wmlPackageBuilder.getPackage();
            MainDocumentPart mainDocumentPart = wordprocessingMLPackage.getMainDocumentPart();
            final StyleDefinitionsPart styleDefinitionsPart = mainDocumentPart.getStyleDefinitionsPart();
            final Styles styles = styleDefinitionsPart.getContents();
            final List<Style> list = styles.getStyle();
            list.forEach(style -> documentContext.getDocumentStyles().add(style.getStyleId()));

            AsciiDocumentInfo documentInfo = documentContext.getDocumentInfo();
            if (documentInfo.isSectionNumbers()) {
                wmlPackageBuilder.multiLevelHeading();
            }
            if (documentInfo.getExampleCaption() != null) {
                wmlPackageBuilder.multiLevelHeading(EXAMPLE);
            }
            if (documentInfo.getTableCaption() != null) {
                wmlPackageBuilder.multiLevelHeading(TABLE);
            }
            documentContext.setMainDocumentPart(mainDocumentPart);

            final List<Object> content = BuilderFactory.getInstance().buildDocument();
            if (content == null || content.isEmpty()) {
                ApplicationController.endContext();
                return wordprocessingMLPackage;
            }

            content.forEach(mainDocumentPart::addObject);

            if (documentInfo.isToc()) {
                addTableOfContent(mainDocumentPart, documentInfo.getTocTitle(), 5);
            }
        } catch (Docx4JException e) {
            throw new SystemException(e.getMessage(), e);
        } finally {
            ApplicationController.endContext();
        }
        return wordprocessingMLPackage;
    }

    public static void buildDocument(final Path docxPath, final DocumentContext documentContext) throws SystemException {
        try {
            save(docxPath.toFile(), buildDocument(documentContext));
        } catch (Docx4JException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    private static Object getDocument(String docBookContent, UnmarshallerTool unmarshallerTool) throws SystemException {
        Object document;
        try {
            document = unmarshallerTool.unmarshal(docBookContent, Article.class);
        } catch (SystemException e) {
            document = unmarshallerTool.unmarshal(docBookContent, Book.class);
        }
        return document;
    }

    private static String convertToDocBook(AsciiDocumentInfo asciiDocumentInfo) throws SystemException {
        String docBookContent;
        AsciiDocumentInfo docBook = new AsciiDocumentInfo(asciiDocumentInfo);
        docBook.setBackend(DOC_BOOK.getValue());
        OptionsBuilder optionsBuilder = docBook.getOptionsBuilder().headerFooter(true);
        try {
            try (Reader reader = Files.newBufferedReader(asciiDocumentInfo.getSrcFile().toPath());
                 StringWriter writer = new StringWriter()) {
                asciiDoctor.convert(reader, writer, optionsBuilder);
                docBookContent = writer.toString();
            }
        } catch (IOException e) {
            throw new SystemException(e.getMessage(), e);
        }
        return docBookContent;
    }

    private static String convertToDocBook(String content, AsciiDocumentInfo asciiDocumentInfo) {
        final OptionsBuilder optionsBuilder = asciiDocumentInfo.getOptionsBuilder();
        optionsBuilder.toFile(false).inPlace(false).backend(DOC_BOOK.getValue()).headerFooter(true);
        return asciiDoctor.convert(content, optionsBuilder);
    }
}
