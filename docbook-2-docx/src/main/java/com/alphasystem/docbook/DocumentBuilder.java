package com.alphasystem.docbook;

import com.alphasystem.SystemException;
import com.alphasystem.asciidoc.model.DocumentInfo;
import com.alphasystem.docbook.builder.BuilderFactory;
import com.alphasystem.docbook.util.ConfigurationUtils;
import com.alphasystem.docbook.util.FileUtil;
import com.alphasystem.openxml.builder.wml.TocGenerator;
import com.alphasystem.openxml.builder.wml.WmlPackageBuilder;
import com.alphasystem.util.nio.NIOFileUtils;
import com.alphasystem.xml.UnmarshallerTool;
import org.apache.commons.lang3.StringUtils;
import org.docbook.model.Article;
import org.docbook.model.Book;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.WordprocessingML.StyleDefinitionsPart;
import org.docx4j.wml.Style;
import org.docx4j.wml.Styles;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.alphasystem.docbook.model.DocumentCaption.EXAMPLE;
import static com.alphasystem.docbook.model.DocumentCaption.TABLE;
import static com.alphasystem.openxml.builder.wml.WmlAdapter.save;
import static java.nio.file.Files.exists;

/**
 * @author sali
 */
public class DocumentBuilder {

    private final static ConfigurationUtils configurationUtils = ConfigurationUtils.getInstance();

    static {
        // initialize Application controller
        ApplicationController.getInstance();
    }

    public static Path buildDocument(final DocumentInfo documentInfo, Path docxPath) throws SystemException {
        final var unmarshallerTool = new UnmarshallerTool(documentInfo);
        try {
            save(docxPath.toFile(), unmarshallerTool.unmarshal(documentInfo.getContent()));
        } catch (Docx4JException e) {
            throw new SystemException(e.getMessage(), e);
        }
        return docxPath;
    }

    public static Path buildDocument(final DocumentInfo documentInfo) throws SystemException {
        return buildDocument(documentInfo, FileUtil.getDocxFile(documentInfo.getSrcFile().toPath()));
    }

    public static DocumentContext createContext(final DocumentInfo documentInfo) throws SystemException {
        final var content = documentInfo.getContent();
        if (StringUtils.isBlank(content)) {
            throw new IllegalArgumentException("Content not provided");
        }
        final var unmarshallerTool = new UnmarshallerTool(documentInfo);
        Object document = getDocument(content, unmarshallerTool);
        var documentInfo1 = unmarshallerTool.getDocumentInfo();
        return new DocumentContext(documentInfo1, document);
    }

    public static DocumentContext createContext(Path srcPath) throws SystemException {
        if (!exists(srcPath)) {
            throw new NullPointerException("Source file does not exists.");
        }
        String docBookContent;
        try {
            try (InputStream inputStream = Files.newInputStream(srcPath);
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                NIOFileUtils.fastCopy(inputStream, outputStream);
                docBookContent = outputStream.toString();
            }
        } catch (IOException e) {
            throw new SystemException(e.getMessage(), e);
        }

        final var documentInfo = new DocumentInfo();
        documentInfo.setContent(docBookContent);

        return createContext(documentInfo);
    }


    public static WordprocessingMLPackage buildDocument(final DocumentContext documentContext) throws SystemException {
        ApplicationController.startContext(documentContext);
        WordprocessingMLPackage wordprocessingMLPackage;
        try {
            WmlPackageBuilder wmlPackageBuilder = WmlPackageBuilder.createPackage(configurationUtils.getTemplate())
                    .styles(configurationUtils.getStyles());
            wordprocessingMLPackage = wmlPackageBuilder.getPackage();
            MainDocumentPart mainDocumentPart = wordprocessingMLPackage.getMainDocumentPart();

            final StyleDefinitionsPart styleDefinitionsPart = mainDocumentPart.getStyleDefinitionsPart();
            final Styles styles = styleDefinitionsPart.getContents();
            final List<Style> list = styles.getStyle();
            list.forEach(style -> documentContext.getDocumentStyles().add(style.getStyleId()));

            final var documentInfo = documentContext.getDocumentInfo();
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

            if (documentInfo.isToc() && documentInfo.isSectionNumbers()) {
                new TocGenerator().level(5).tocHeading(documentInfo.getTocTitle()).level(5)
                        .mainDocumentPart(mainDocumentPart).generateToc();
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
}
