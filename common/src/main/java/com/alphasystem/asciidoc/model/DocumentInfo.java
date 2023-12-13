package com.alphasystem.asciidoc.model;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

import static com.alphasystem.util.AppUtil.USER_HOME_DIR;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author sali
 */
public class DocumentInfo {

    private static String getFailSafeString(final String src, final String defaultValue) {
        return StringUtils.isBlank(src) ? defaultValue : src;
    }

    private String content;
    private String documentType;
    private String documentName;
    private String documentTitle;
    private String backend;
    private String stylesDir;
    private File customStyleSheetFile;
    private boolean linkCss;
    private String includeDir;
    private String docInfoDir;
    private String imagesDir;
    private String iconsDir;
    private String icons;
    private String iconFontName;
    private String idPrefix;
    private String idSeparator;
    private String docInfo;
    private boolean docInfo2;
    private String sourceLanguage;
    private String lastUpdateLabel;
    private boolean omitLastUpdatedTimeStamp;
    private boolean compact;
    private boolean experimental;
    private boolean toc;
    private String tocClass;
    private String tocPlacement;
    private String tocPosition;
    private String tocTitle;
    private String appendixCaption;
    private String cautionCaption;
    private String exampleCaption;
    private String figureCaption;
    private String importantCaption;
    private String noteCaption;
    private String tableCaption;
    private String tipCaption;
    private String untitledLabel;
    private String versionLabel;
    private String warningCaption;
    private boolean sectionNumbers;
    private boolean hideUriSchema;
    private File srcFile;

    public DocumentInfo() {
        setDocumentType(null);
        setBackend(null);
        setStylesDir(null);
        setIcons(null);
        setLinkCss(false);
        setOmitLastUpdatedTimeStamp(true);
        setCompact(true);
    }

    /**
     * Copy Constructor
     *
     * @param src source object, cannot be null.
     * @throws IllegalArgumentException if src is null
     */
    public DocumentInfo(DocumentInfo src) throws IllegalArgumentException {
        this();
        if (src == null) {
            throw new IllegalArgumentException("source object cannot be null");
        }
        setContent(src.getContent());
        setDocumentType(getFailSafeString(src.getDocumentType(), getDocumentType()));
        setDocumentName(getFailSafeString(src.getDocumentName(), getDocumentName()));
        setDocumentTitle(getFailSafeString(src.getDocumentTitle(), getDocumentTitle()));
        setDocInfo2(src.isDocInfo2());
        setIncludeDir(getFailSafeString(src.getIncludeDir(), getIncludeDir()));
        setDocInfoDir(getFailSafeString(src.getDocInfoDir(), getDocInfoDir()));
        setDocInfo(getFailSafeString(src.getDocInfo(), getDocInfo()));
        setImagesDir(getFailSafeString(src.getImagesDir(), getImagesDir()));
        setIconsDir(getFailSafeString(src.getIconsDir(), getIconsDir()));
        setIcons(getFailSafeString(src.getIcons(), getIcons()));
        setIconFontName(getFailSafeString(src.getIconFontName(), getIconFontName()));
        setLinkCss(src.isLinkCss());
        setSrcFile(src.getSrcFile() == null ? getSrcFile() : src.getSrcFile());
        setStylesDir(getFailSafeString(src.getStylesDir(), getStylesDir()));
        setCustomStyleSheetFile(src.getCustomStyleSheetFile() == null ? getCustomStyleSheetFile() : src.getCustomStyleSheetFile());
        setSourceLanguage(getFailSafeString(src.getSourceLanguage(), getSourceLanguage()));
        setLastUpdateLabel(getFailSafeString(src.getLastUpdateLabel(), getLastUpdateLabel()));
        setOmitLastUpdatedTimeStamp(src.isOmitLastUpdatedTimeStamp());
        setCompact(src.isCompact());
        setIdSeparator(getFailSafeString(src.getIdSeparator(), getIdSeparator()));
        setIdPrefix(getFailSafeString(src.getIdPrefix(), getIdPrefix()));
        setSectionNumbers(src.isSectionNumbers());
        setExperimental(src.isExperimental());
        setToc(src.isToc());
        setTocTitle(getFailSafeString(src.getTocTitle(), getTocTitle()));
        setTocPlacement(getFailSafeString(src.getTocPlacement(), getTocPlacement()));
        setTocPosition(getFailSafeString(src.getTocPosition(), getTocPosition()));
        setTocClass(getFailSafeString(src.getTocClass(), getTocClass()));
        setAppendixCaption(getFailSafeString(src.getAppendixCaption(), getAppendixCaption()));
        setCautionCaption(getFailSafeString(src.getCautionCaption(), getCautionCaption()));
        setExampleCaption(getFailSafeString(src.getExampleCaption(), getExampleCaption()));
        setFigureCaption(getFailSafeString(src.getFigureCaption(), getFigureCaption()));
        setImportantCaption(getFailSafeString(src.getImportantCaption(), getImportantCaption()));
        setNoteCaption(getFailSafeString(src.getNoteCaption(), getNoteCaption()));
        setTableCaption(getFailSafeString(src.getTableCaption(), getTableCaption()));
        setTipCaption(getFailSafeString(src.getTipCaption(), getTipCaption()));
        setUntitledLabel(getFailSafeString(src.getUntitledLabel(), getUntitledLabel()));
        setVersionLabel(getFailSafeString(src.getVersionLabel(), getVersionLabel()));
        setWarningCaption(getFailSafeString(src.getWarningCaption(), getWarningCaption()));
        setBackend(getFailSafeString(src.getBackend(), getBackend()));
        setHideUriSchema(src.isHideUriSchema());
    }

    public String getDocumentType() {
        return documentType;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDocumentType(String documentType) {
        this.documentType = isBlank(documentType) ? "article" : documentType;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getBackend() {
        return backend;
    }

    public void setBackend(String backend) {
        this.backend = (backend == null) ? Backend.HTML.getValue() : backend;
    }

    public String getStylesDir() {
        return stylesDir;
    }

    public void setStylesDir(String stylesDir) {
        this.stylesDir = stylesDir;
    }

    public File getCustomStyleSheetFile() {
        return customStyleSheetFile;
    }

    public void setCustomStyleSheetFile(File customStyleSheetFile) {
        this.customStyleSheetFile = customStyleSheetFile;
    }

    public boolean isLinkCss() {
        return linkCss;
    }

    public void setLinkCss(boolean linkCss) {
        this.linkCss = linkCss;
    }

    public String getIncludeDir() {
        return includeDir;
    }

    public void setIncludeDir(String includeDir) {
        this.includeDir = includeDir;
    }

    public String getDocInfoDir() {
        return docInfoDir;
    }

    public void setDocInfoDir(String docInfoDir) {
        this.docInfoDir = docInfoDir;
    }

    public String getImagesDir() {
        return imagesDir;
    }

    public void setImagesDir(String imagesDir) {
        this.imagesDir = imagesDir;
    }

    public String getIconsDir() {
        return iconsDir;
    }

    public void setIconsDir(String iconsDir) {
        this.iconsDir = iconsDir;
    }

    public String getIcons() {
        return icons;
    }

    public void setIcons(String icons) {
        this.icons = icons;
    }

    public String getIconFontName() {
        return iconFontName;
    }

    public void setIconFontName(String iconFontName) {
        this.iconFontName = iconFontName;
    }

    public String getIdPrefix() {
        return idPrefix;
    }

    public void setIdPrefix(String idPrefix) {
        this.idPrefix = idPrefix;
    }

    public String getIdSeparator() {
        return idSeparator;
    }

    public void setIdSeparator(String idSeparator) {
        this.idSeparator = idSeparator;
    }

    public String getDocInfo() {
        return docInfo;
    }

    public void setDocInfo(String docInfo) {
        this.docInfo = docInfo;
    }

    public boolean isDocInfo2() {
        return docInfo2;
    }

    public void setDocInfo2(boolean docInfo2) {
        this.docInfo2 = docInfo2;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(String sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public String getLastUpdateLabel() {
        return lastUpdateLabel;
    }

    public void setLastUpdateLabel(String lastUpdateLabel) {
        this.lastUpdateLabel = lastUpdateLabel;
    }

    public boolean isOmitLastUpdatedTimeStamp() {
        return omitLastUpdatedTimeStamp;
    }

    public void setOmitLastUpdatedTimeStamp(boolean omitLastUpdatedTimeStamp) {
        this.omitLastUpdatedTimeStamp = omitLastUpdatedTimeStamp;
    }

    public boolean isCompact() {
        return compact;
    }

    public void setCompact(boolean compact) {
        this.compact = compact;
    }

    public boolean isExperimental() {
        return experimental;
    }

    public void setExperimental(boolean experimental) {
        this.experimental = experimental;
    }

    public boolean isSectionNumbers() {
        return sectionNumbers;
    }

    public void setSectionNumbers(boolean sectionNumbers) {
        this.sectionNumbers = sectionNumbers;
    }

    public boolean isToc() {
        return toc;
    }

    public void setToc(boolean toc) {
        this.toc = toc;
    }

    public String getTocClass() {
        return tocClass;
    }

    public void setTocClass(String tocClass) {
        this.tocClass = tocClass;
    }

    public String getTocPlacement() {
        return tocPlacement;
    }

    public void setTocPlacement(String tocPlacement) {
        this.tocPlacement = tocPlacement;
    }

    public String getTocPosition() {
        return tocPosition;
    }

    public void setTocPosition(String tocPosition) {
        this.tocPosition = tocPosition;
    }

    public String getTocTitle() {
        return tocTitle;
    }

    public void setTocTitle(String tocTitle) {
        this.tocTitle = tocTitle;
    }

    public String getUntitledLabel() {
        return untitledLabel;
    }

    public void setUntitledLabel(String untitledLabel) {
        this.untitledLabel = untitledLabel;
    }

    public String getAppendixCaption() {
        return appendixCaption;
    }

    public void setAppendixCaption(String appendixCaption) {
        this.appendixCaption = appendixCaption;
    }

    public String getCautionCaption() {
        return cautionCaption;
    }

    public void setCautionCaption(String cautionCaption) {
        this.cautionCaption = cautionCaption;
    }

    public String getExampleCaption() {
        return exampleCaption;
    }

    public void setExampleCaption(String exampleCaption) {
        this.exampleCaption = exampleCaption;
    }

    public String getFigureCaption() {
        return figureCaption;
    }

    public void setFigureCaption(String figureCaption) {
        this.figureCaption = figureCaption;
    }

    public String getImportantCaption() {
        return importantCaption;
    }

    public void setImportantCaption(String importantCaption) {
        this.importantCaption = importantCaption;
    }

    public String getNoteCaption() {
        return noteCaption;
    }

    public void setNoteCaption(String noteCaption) {
        this.noteCaption = noteCaption;
    }

    public String getTableCaption() {
        return tableCaption;
    }

    public void setTableCaption(String tableCaption) {
        this.tableCaption = tableCaption;
    }

    public String getTipCaption() {
        return tipCaption;
    }

    public void setTipCaption(String tipCaption) {
        this.tipCaption = tipCaption;
    }

    public String getVersionLabel() {
        return versionLabel;
    }

    public void setVersionLabel(String versionLabel) {
        this.versionLabel = versionLabel;
    }

    public String getWarningCaption() {
        return warningCaption;
    }

    public void setWarningCaption(String warningCaption) {
        this.warningCaption = warningCaption;
    }

    public boolean isHideUriSchema() {
        return hideUriSchema;
    }

    public void setHideUriSchema(boolean hideUriSchema) {
        this.hideUriSchema = hideUriSchema;
    }

    public File getSrcFile() {
        return srcFile;
    }

    public void setSrcFile(File srcFile) {
        this.srcFile = (srcFile == null) ? USER_HOME_DIR : srcFile;
    }

    @Override
    public String toString() {
        return "AsciiDocumentInfo{" +
                "documentType='" + documentType + '\'' +
                ", documentName='" + documentName + '\'' +
                ", documentTitle='" + documentTitle + '\'' +
                ", backend='" + backend + '\'' +
                ", stylesDir='" + stylesDir + '\'' +
                ", customStyleSheetFile=" + customStyleSheetFile +
                ", linkCss=" + linkCss +
                ", includeDir='" + includeDir + '\'' +
                ", docInfoDir='" + docInfoDir + '\'' +
                ", imagesDir='" + imagesDir + '\'' +
                ", iconsDir='" + iconsDir + '\'' +
                ", icons='" + icons + '\'' +
                ", iconFontName='" + iconFontName + '\'' +
                ", idPrefix='" + idPrefix + '\'' +
                ", idSeparator='" + idSeparator + '\'' +
                ", docInfo='" + docInfo + '\'' +
                ", docInfo2=" + docInfo2 +
                ", sourceLanguage='" + sourceLanguage + '\'' +
                ", lastUpdateLabel='" + lastUpdateLabel + '\'' +
                ", omitLastUpdatedTimeStamp=" + omitLastUpdatedTimeStamp +
                ", compact=" + compact +
                ", experimental=" + experimental +
                ", toc=" + toc +
                ", tocClass='" + tocClass + '\'' +
                ", tocPlacement='" + tocPlacement + '\'' +
                ", tocPosition='" + tocPosition + '\'' +
                ", tocTitle='" + tocTitle + '\'' +
                ", appendixCaption='" + appendixCaption + '\'' +
                ", cautionCaption='" + cautionCaption + '\'' +
                ", exampleCaption='" + exampleCaption + '\'' +
                ", figureCaption='" + figureCaption + '\'' +
                ", importantCaption='" + importantCaption + '\'' +
                ", noteCaption='" + noteCaption + '\'' +
                ", tableCaption='" + tableCaption + '\'' +
                ", tipCaption='" + tipCaption + '\'' +
                ", untitledLabel='" + untitledLabel + '\'' +
                ", versionLabel='" + versionLabel + '\'' +
                ", warningCaption='" + warningCaption + '\'' +
                ", sectionNumbers=" + sectionNumbers +
                ", hideUriSchema=" + hideUriSchema +
                ", srcFile=" + srcFile +
                '}';
    }
}
