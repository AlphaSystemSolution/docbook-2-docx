package com.alphasystem.asciidoc.model;

import org.apache.commons.lang3.StringUtils;
import org.asciidoctor.Attributes;
import org.asciidoctor.Options;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.Placement;
import org.asciidoctor.ast.Document;

import java.io.File;
import java.util.Map;

import static com.alphasystem.commons.util.AppUtil.USER_HOME_DIR;
import static org.apache.commons.lang3.StringUtils.*;
import static org.asciidoctor.SafeMode.UNSAFE;

/**
 * @author sali
 */
public class AsciiDocumentInfo {

    private static String getFailSafeString(Map<String, Object> attributes, String key) {
        return stripToNull((String) attributes.get(key));
    }

    private static boolean getFailSafeBoolean(Map<String, Object> attributes, String key) {
        final var value = attributes.get(key);
        return value != null && (value.toString().trim().isBlank() || Boolean.parseBoolean(value.toString().trim().toLowerCase()));
    }

    private static String getFailSafeString(final String src, final String defaultValue) {
        return StringUtils.isBlank(src) ? defaultValue : src;
    }

    private final Attributes attributes;
    private final OptionsBuilder optionsBuilder;
    private final DocumentInfo documentInfo;
    private Document document;

    public AsciiDocumentInfo() {
        attributes = Attributes.builder().build();
        optionsBuilder = Options.builder().attributes(attributes);
        this.documentInfo = new DocumentInfo();
        setDocument(null);
        setDocumentType(null);
        setBackend(null);
        setStylesDir(null);
        setIcons(null);
        setLinkCss(false);
        setOmitLastUpdatedTimeStamp(true);
        setCompact(true);
        optionsBuilder.safe(UNSAFE);
    }

    /**
     * Copy Constructor
     *
     * @param src source object, cannot be null.
     * @throws IllegalArgumentException if src is null
     */
    public AsciiDocumentInfo(AsciiDocumentInfo src) throws IllegalArgumentException {
        this();
        if (src == null) {
            throw new IllegalArgumentException("source object cannot be null");
        }
        final var srcDocInfo = src.getDocumentInfo();

        setDocument(src.getDocument());
        setContent(srcDocInfo.getContent());
        setDocumentType(srcDocInfo.getDocumentType());
        setDocumentName(srcDocInfo.getDocumentName());
        setDocumentTitle(srcDocInfo.getDocumentTitle());
        setDocInfo2(srcDocInfo.isDocInfo2());
        setIncludeDir(srcDocInfo.getIncludeDir());
        setDocInfoDir(srcDocInfo.getDocInfoDir());
        setDocInfo(srcDocInfo.getDocInfo());
        setImagesDir(srcDocInfo.getImagesDir());
        setIconsDir(srcDocInfo.getIconsDir());
        setIcons(srcDocInfo.getIcons());
        setIconFontName(srcDocInfo.getIconFontName());
        setLinkCss(srcDocInfo.isLinkCss());
        setSrcFile(srcDocInfo.getSrcFile());
        setStylesDir(srcDocInfo.getStylesDir());
        setCustomStyleSheetFile(srcDocInfo.getCustomStyleSheetFile());
        setSourceLanguage(srcDocInfo.getSourceLanguage());
        setLastUpdateLabel(srcDocInfo.getLastUpdateLabel());
        setOmitLastUpdatedTimeStamp(srcDocInfo.isOmitLastUpdatedTimeStamp());
        setCompact(srcDocInfo.isCompact());
        setIdSeparator(srcDocInfo.getIdSeparator());
        setIdPrefix(srcDocInfo.getIdPrefix());
        setSectionNumbers(srcDocInfo.isSectionNumbers());
        setExperimental(srcDocInfo.isExperimental());
        setToc(srcDocInfo.isToc());
        setTocTitle(srcDocInfo.getTocTitle());
        setTocPlacement(srcDocInfo.getTocPlacement());
        setTocPosition(srcDocInfo.getTocPosition());
        setTocClass(srcDocInfo.getTocClass());
        setAppendixCaption(srcDocInfo.getAppendixCaption());
        setCautionCaption(srcDocInfo.getCautionCaption());
        setExampleCaption(srcDocInfo.getExampleCaption());
        setFigureCaption(srcDocInfo.getFigureCaption());
        setImportantCaption(srcDocInfo.getImportantCaption());
        setNoteCaption(srcDocInfo.getNoteCaption());
        setTableCaption(srcDocInfo.getTableCaption());
        setTipCaption(srcDocInfo.getTipCaption());
        setUntitledLabel(srcDocInfo.getUntitledLabel());
        setVersionLabel(srcDocInfo.getVersionLabel());
        setWarningCaption(srcDocInfo.getWarningCaption());
        setBackend(srcDocInfo.getBackend());
        setHideUriSchema(srcDocInfo.isHideUriSchema());
    }

    public OptionsBuilder getOptionsBuilder() {
        return optionsBuilder;
    }

    public DocumentInfo getDocumentInfo() {
        return documentInfo;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public void setContent(String content) {
        documentInfo.setContent(content);
    }

    public void setDocumentType(String documentType) {
        var dt = isBlank(documentType) ? "article" : documentType;
        documentInfo.setDocumentType(dt);
        attributes.setDocType(dt);
    }

    public void setDocumentName(String documentName) {
        documentInfo.setDocumentName(getFailSafeString(documentName, documentInfo.getDocumentName()));
    }

    public void setDocumentTitle(String documentTitle) {
        documentInfo.setDocumentTitle(getFailSafeString(documentTitle, documentInfo.getDocumentTitle()));
    }

    public void setBackend(String backend) {
        var value = getFailSafeString(backend, documentInfo.getBackend());
        value = (backend == null) ? Backend.HTML.getValue() : backend;
        documentInfo.setBackend(value);
        optionsBuilder.backend(value);
    }

    public void setStylesDir(String stylesDir) {
        final var value = getFailSafeString(stylesDir, documentInfo.getStylesDir());
        documentInfo.setStylesDir(value);
        if (documentInfo.isLinkCss()) {
            attributes.setStylesDir(documentInfo.getStylesDir());
        } else {
            attributes.setStylesDir(null);
        }
    }

    public void setCustomStyleSheetFile(File customStyleSheetFile) {
        final var value = customStyleSheetFile == null ? documentInfo.getCustomStyleSheetFile() : customStyleSheetFile;
        documentInfo.setCustomStyleSheetFile(value);
        if (value != null) {
            attributes.setStyleSheetName(value.getName());
        }
    }

    public void setLinkCss(boolean linkCss) {
        documentInfo.setLinkCss(linkCss);
        if (linkCss) {
            attributes.setLinkCss(linkCss);
            attributes.setStylesDir(documentInfo.getStylesDir());
        } else {
            attributes.setStylesDir(null);
        }
    }

    public void setIncludeDir(String includeDir) {
        documentInfo.setIncludeDir(getFailSafeString(includeDir, documentInfo.getIncludeDir()));
    }

    public void setDocInfoDir(String docInfoDir) {
        documentInfo.setDocInfoDir(getFailSafeString(docInfoDir, documentInfo.getDocInfoDir()));
    }

    public void setImagesDir(String imagesDir) {
        final var value = getFailSafeString(imagesDir, documentInfo.getImagesDir());
        documentInfo.setImagesDir(value);
        attributes.setImagesDir(value);
    }

    public void setIconsDir(String iconsDir) {
        final var value = getFailSafeString(iconsDir, documentInfo.getIconsDir());
        documentInfo.setIconsDir(value);
        attributes.setIconsDir(value);
    }

    public void setIcons(String icons) {
        final var value = getFailSafeString(icons, documentInfo.getIcons());
        documentInfo.setIcons(value);
        attributes.setIcons(value);
    }

    public void setIconFontName(String iconFontName) {
        final var value = getFailSafeString(iconFontName, documentInfo.getIconFontName());
        documentInfo.setIconFontName(value);
        attributes.setIconFontName(value);
        attributes.setIconFontRemote(!isNotBlank(value));
    }

    public void setIdPrefix(String idPrefix) {
        documentInfo.setIdSeparator(getFailSafeString(idPrefix, documentInfo.getIdPrefix()));
    }

    public void setIdSeparator(String idSeparator) {
        documentInfo.setIdSeparator(getFailSafeString(idSeparator, documentInfo.getIdSeparator()));
    }

    public void setDocInfo(String docInfo) {
        documentInfo.setDocInfo(getFailSafeString(docInfo, documentInfo.getDocInfo()));
    }

    public void setDocInfo2(boolean docInfo2) {
        documentInfo.setDocInfo2(docInfo2);
    }

    public void setSourceLanguage(String sourceLanguage) {
        final var value = getFailSafeString(sourceLanguage, documentInfo.getSourceLanguage());
        documentInfo.setSourceLanguage(value);
        attributes.setSourceLanguage(value);
    }

    public void setLastUpdateLabel(String lastUpdateLabel) {
        documentInfo.setLastUpdateLabel(getFailSafeString(lastUpdateLabel, documentInfo.getLastUpdateLabel()));
    }

    public void setOmitLastUpdatedTimeStamp(boolean omitLastUpdatedTimeStamp) {
        documentInfo.setOmitLastUpdatedTimeStamp(omitLastUpdatedTimeStamp);
    }

    public void setCompact(boolean compact) {
        documentInfo.setCompact(compact);
        optionsBuilder.compact(compact);
    }

    public void setExperimental(boolean experimental) {
        documentInfo.setExperimental(experimental);
        attributes.setExperimental(experimental);
    }

    public void setSectionNumbers(boolean sectionNumbers) {
        documentInfo.setSectionNumbers(sectionNumbers);
        attributes.setSectionNumbers(sectionNumbers);
    }

    public void setToc(boolean toc) {
        documentInfo.setToc(toc);
        attributes.setTableOfContents(toc);
    }

    public void setTocClass(String tocClass) {
        documentInfo.setTocClass(getFailSafeString(tocClass, documentInfo.getTocClass()));
    }

    public void setTocPlacement(String tocPlacement) {
        final var value = getFailSafeString(tocPlacement, documentInfo.getTocPlacement());
        documentInfo.setTocPlacement(value);
        attributes.setTableOfContents((isBlank(value) || value.equals("auto")) ? Placement.LEFT :
                Placement.valueOf(value.toUpperCase()));
    }

    public void setTocPosition(String tocPosition) {
        documentInfo.setTocPosition(getFailSafeString(tocPosition, documentInfo.getTocPosition()));
    }

    public void setTocTitle(String tocTitle) {
        documentInfo.setIdSeparator(getFailSafeString(tocTitle, documentInfo.getTocTitle()));
    }

    public void setUntitledLabel(String untitledLabel) {
        final var value = getFailSafeString(untitledLabel, documentInfo.getUntitledLabel());
        documentInfo.setUntitledLabel(value);
        attributes.setUntitledLabel(value);
    }

    public void setAppendixCaption(String appendixCaption) {
        documentInfo.setAppendixCaption(getFailSafeString(appendixCaption, documentInfo.getAppendixCaption()));
    }

    public void setCautionCaption(String cautionCaption) {
        documentInfo.setCautionCaption(getFailSafeString(cautionCaption, documentInfo.getCautionCaption()));
    }

    public void setExampleCaption(String exampleCaption) {
        documentInfo.setExampleCaption(getFailSafeString(exampleCaption, documentInfo.getExampleCaption()));
    }

    public void setFigureCaption(String figureCaption) {
        documentInfo.setFigureCaption(getFailSafeString(figureCaption, documentInfo.getFigureCaption()));
    }

    public void setImportantCaption(String importantCaption) {
        documentInfo.setImportantCaption(getFailSafeString(importantCaption, documentInfo.getImportantCaption()));
    }

    public void setNoteCaption(String noteCaption) {
        documentInfo.setNoteCaption(getFailSafeString(noteCaption, documentInfo.getNoteCaption()));
    }

    public void setTableCaption(String tableCaption) {
        documentInfo.setTableCaption(getFailSafeString(tableCaption, documentInfo.getTableCaption()));
    }

    public void setTipCaption(String tipCaption) {
        documentInfo.setTipCaption(getFailSafeString(tipCaption, documentInfo.getTipCaption()));
    }

    public void setVersionLabel(String versionLabel) {
        documentInfo.setVersionLabel(getFailSafeString(versionLabel, documentInfo.getVersionLabel()));
    }

    public void setWarningCaption(String warningCaption) {
        documentInfo.setWarningCaption(getFailSafeString(warningCaption, documentInfo.getWarningCaption()));
    }

    public void setHideUriSchema(boolean hideUriSchema) {
        documentInfo.setHideUriSchema(hideUriSchema);
    }

    public void setSrcFile(File srcFile) {
        final var value = (srcFile == null) ? USER_HOME_DIR : srcFile;
        documentInfo.setSrcFile(value);
        optionsBuilder.baseDir(value.getParentFile());
    }

    public void populateAttributes(final Document document) {
        setDocument(document);
        populateAttributes(document.getAttributes());
    }

    public void populateAttributes(Map<String, Object> attributes) {
        String s;
        s = getFailSafeString(attributes, "doctype");
        if (s != null) {
            setDocumentType(s);
        }
        s = getFailSafeString(attributes, "docname");
        if (s != null) {
            setDocumentName(s);
        }
        s = getFailSafeString(attributes, "doctitle");
        if (s != null) {
            setDocumentTitle(s);
        }
        s = getFailSafeString(attributes, "stylesdir");
        if (s != null) {
            setStylesDir(s);
        }
        s = getFailSafeString(attributes, "stylesheet");
        if (s != null) {
            File parent = new File(documentInfo.getSrcFile().getAbsoluteFile(), documentInfo.getStylesDir());
            setCustomStyleSheetFile(new File(parent, s));
        }
        s = getFailSafeString(attributes, "includedir");
        if (s != null) {
            setIncludeDir(s);
        }
        // setValue(this, attributes, "linkcss", "setLinkCss", Boolean.class);
        setLinkCss(getFailSafeBoolean(attributes, "linkcss"));
        setCompact(getFailSafeBoolean(attributes, "compact"));
        setExperimental(getFailSafeBoolean(attributes, "experimental"));
        setSectionNumbers(getFailSafeBoolean(attributes, "sectnums"));
        setHideUriSchema(getFailSafeBoolean(attributes, "hide-uri-scheme"));
        s = getFailSafeString(attributes, "iconsdir");
        if (s != null) {
            setIconsDir(s);
        }
        s = getFailSafeString(attributes, "includedir");
        if (s != null) {
            setIncludeDir(s);
        }
        s = getFailSafeString(attributes, "imagesdir");
        if (s != null) {
            setImagesDir(s);
        }
        s = getFailSafeString(attributes, "iconfont-name");
        if (s != null) {
            setIconFontName(s);
        }
        s = getFailSafeString(attributes, "idprefix");
        if (s != null) {
            setIdPrefix(s);
        }
        s = getFailSafeString(attributes, "idseparator");
        if (s != null) {
            setIdSeparator(s);
        }
        setToc(getFailSafeBoolean(attributes, "toc"));
        s = getFailSafeString(attributes, "toc-class");
        if (s != null) {
            setTocClass(s);
        }
        s = getFailSafeString(attributes, "toc-placement");
        if (s != null) {
            setTocPlacement(s);
        }
        s = getFailSafeString(attributes, "toc-position");
        if (s != null) {
            setTocPosition(s);
        }
        s = getFailSafeString(attributes, "toc-title");
        if (s != null) {
            setTocTitle(s);
        }
        s = getFailSafeString(attributes, "untitled-label");
        if (s != null) {
            setUntitledLabel(s);
        }
        s = getFailSafeString(attributes, "last-update-label");
        if (s != null) {
            setLastUpdateLabel(s);
        }
        s = getFailSafeString(attributes, "appendix-caption");
        if (s != null) {
            setAppendixCaption(s);
        }
        s = getFailSafeString(attributes, "caution-caption");
        if (s != null) {
            setCautionCaption(s);
        }
        s = getFailSafeString(attributes, "example-caption");
        if (s != null) {
            setExampleCaption(s);
        }
        s = getFailSafeString(attributes, "figure-caption");
        if (s != null) {
            setFigureCaption(s);
        }
        s = getFailSafeString(attributes, "important-caption");
        if (s != null) {
            setImportantCaption(s);
        }
        s = getFailSafeString(attributes, "note-caption");
        if (s != null) {
            setNoteCaption(s);
        }
        s = getFailSafeString(attributes, "table-caption");
        if (s != null) {
            setTableCaption(s);
        }
        s = getFailSafeString(attributes, "tip-caption");
        if (s != null) {
            setTipCaption(s);
        }
        s = getFailSafeString(attributes, "version-label");
        if (s != null) {
            setVersionLabel(s);
        }
        s = getFailSafeString(attributes, "warning-caption");
        if (s != null) {
            setWarningCaption(s);
        }
    }

    @Override
    public String toString() {
        return "AsciiDocumentInfo{" +
                "attributesBuilder=" + attributes +
                ", optionsBuilder=" + optionsBuilder +
                ", documentType='" + documentInfo.getDocumentType() + '\'' +
                ", documentName='" + documentInfo.getDocumentName() + '\'' +
                ", documentTitle='" + documentInfo.getDocumentTitle() + '\'' +
                ", backend='" + documentInfo.getBackend() + '\'' +
                ", stylesDir='" + documentInfo.getStylesDir() + '\'' +
                ", customStyleSheetFile=" + documentInfo.getCustomStyleSheetFile() +
                ", linkCss=" + documentInfo.isLinkCss() +
                ", includeDir='" + documentInfo.getIncludeDir() + '\'' +
                ", docInfoDir='" + documentInfo.getDocInfoDir() + '\'' +
                ", imagesDir='" + documentInfo.getImagesDir() + '\'' +
                ", iconsDir='" + documentInfo.getIconsDir() + '\'' +
                ", icons='" + documentInfo.getIcons() + '\'' +
                ", iconFontName='" + documentInfo.getIconFontName() + '\'' +
                ", idPrefix='" + documentInfo.getIdPrefix() + '\'' +
                ", idSeparator='" + documentInfo.getIdSeparator() + '\'' +
                ", docInfo='" + documentInfo.getDocInfo() + '\'' +
                ", docInfo2=" + documentInfo.isDocInfo2() +
                ", sourceLanguage='" + documentInfo.getSourceLanguage() + '\'' +
                ", lastUpdateLabel='" + documentInfo.getLastUpdateLabel() + '\'' +
                ", omitLastUpdatedTimeStamp=" + documentInfo.isOmitLastUpdatedTimeStamp() +
                ", compact=" + documentInfo.isCompact() +
                ", experimental=" + documentInfo.isExperimental() +
                ", toc=" + documentInfo.isToc() +
                ", tocClass='" + documentInfo.getTocClass() + '\'' +
                ", tocPlacement='" + documentInfo.getTocPlacement() + '\'' +
                ", tocPosition='" + documentInfo.getTocPosition() + '\'' +
                ", tocTitle='" + documentInfo.getTocTitle() + '\'' +
                ", appendixCaption='" + documentInfo.getAppendixCaption() + '\'' +
                ", cautionCaption='" + documentInfo.getCautionCaption() + '\'' +
                ", exampleCaption='" + documentInfo.getExampleCaption() + '\'' +
                ", figureCaption='" + documentInfo.getFigureCaption() + '\'' +
                ", importantCaption='" + documentInfo.getImportantCaption() + '\'' +
                ", noteCaption='" + documentInfo.getNoteCaption() + '\'' +
                ", tableCaption='" + documentInfo.getTableCaption() + '\'' +
                ", tipCaption='" + documentInfo.getTipCaption() + '\'' +
                ", untitledLabel='" + documentInfo.getUntitledLabel() + '\'' +
                ", versionLabel='" + documentInfo.getVersionLabel() + '\'' +
                ", warningCaption='" + documentInfo.getWarningCaption() + '\'' +
                ", sectionNumbers=" + documentInfo.isSectionNumbers() +
                ", hideUriSchema=" + documentInfo.isHideUriSchema() +
                ", srcFile=" + documentInfo.getSrcFile() +
                '}';
    }
}
