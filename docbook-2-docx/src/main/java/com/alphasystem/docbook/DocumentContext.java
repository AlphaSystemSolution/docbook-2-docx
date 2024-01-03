package com.alphasystem.docbook;

import com.alphasystem.asciidoc.model.DocumentInfo;
import com.alphasystem.docbook.model.DocumentCaption;
import com.alphasystem.docbook.model.ListInfo;
import com.alphasystem.docbook.util.ConfigurationUtils;
import com.alphasystem.openxml.builder.wml.NumberingHelper;
import com.alphasystem.openxml.builder.wml.WmlPackageBuilder;
import org.apache.commons.lang3.StringUtils;
import org.docbook.model.Article;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.DocumentSettingsPart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.CTCompat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alphasystem.util.AppUtil.isInstanceOf;

/**
 * @author sali
 */
public final class DocumentContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentContext.class);
    private final Map<String, String> listNumbersMap = new HashMap<>();
    private final Map<String, String> idToLinkMap = new HashMap<>();
    private final ConfigurationUtils configurationUtils = ConfigurationUtils.getInstance();
    private final List<String> documentStyles;
    private final Object document;
    private final DocumentInfo documentInfo;
    private final boolean article;
    private WmlPackageBuilder wmlPackageBuilder;
    private WordprocessingMLPackage wordprocessingMLPackage;
    private MainDocumentPart mainDocumentPart;
    private final NumberingHelper numberingHelper = NumberingHelper.getInstance();

    private ListInfo currentListInfo;

    public DocumentContext(final DocumentInfo documentInfo) {
        this(documentInfo, null);
    }

    public DocumentContext(final DocumentInfo documentInfo, final Object document) {
        this.documentInfo = documentInfo;
        this.document = document;
        this.documentStyles = new ArrayList<>();
        article = isInstanceOf(Article.class, document);
        buildPackage();
    }

    private void buildPackage() {
        try {
            wmlPackageBuilder = WmlPackageBuilder.createPackage(configurationUtils.getTemplate())
                    .styles(configurationUtils.getStyles());
            wordprocessingMLPackage = wmlPackageBuilder.getPackage();
            mainDocumentPart = wordprocessingMLPackage.getMainDocumentPart();

            final var styleDefinitionsPart = mainDocumentPart.getStyleDefinitionsPart();
            final var styles = styleDefinitionsPart.getContents();
            final var list = styles.getStyle();
            list.forEach(style -> documentStyles.add(style.getStyleId()));

            if (documentInfo.isSectionNumbers()) {
                wmlPackageBuilder.multiLevelHeading();
            }
            if (documentInfo.getExampleCaption() != null) {
                wmlPackageBuilder.multiLevelHeading(DocumentCaption.EXAMPLE);
            }
            if (documentInfo.getTableCaption() != null) {
                wmlPackageBuilder.multiLevelHeading(DocumentCaption.TABLE);
            }

            updateDocumentCompatibility();
        } catch (Docx4JException e) {
            throw new RuntimeException(e);
        }
    }

    public DocumentInfo getDocumentInfo() {
        return documentInfo;
    }

    public Object getDocument() {
        return document;
    }

    public List<String> getDocumentStyles() {
        return documentStyles;
    }

    public boolean isArticle() {
        return article;
    }

    public WmlPackageBuilder getWmlPackageBuilder() {
        return wmlPackageBuilder;
    }

    public WordprocessingMLPackage getWordprocessingMLPackage() {
        return wordprocessingMLPackage;
    }

    public MainDocumentPart getMainDocumentPart() {
        return mainDocumentPart;
    }

    public long getListNumber(String styleName, long level) {
        final var listItem = numberingHelper.getListItem(styleName);
        if (listItem == null || level < 0) {
            return -1;
        }

        long numberId = listItem.getNumberId();
        final var o = listNumbersMap.get(styleName);
        if (o == null) {
            listNumbersMap.put(styleName, styleName);
        } else {
            numberId = mainDocumentPart.getNumberingDefinitionsPart().restart(numberId, level, 1);
        }
        return numberId;
    }

    public long getCurrentListLevel() {
        return getCurrentListInfo().getLevel();
    }

    public void putLabel(String id, String label) {
        final var value = idToLinkMap.get(id);
        if (StringUtils.isBlank(value)) {
            idToLinkMap.put(id, label);
        }
    }

    public String getLabel(String id) {
        var label = idToLinkMap.get(id);
        if (StringUtils.isBlank(label)) {
            // if no id -> label mapping is defined, then word "here" will be return for link display.
            label = "here";
        }
        return label;
    }

    @Deprecated
    public void setCurrentListLevel(long level) {
    }

    public ListInfo getCurrentListInfo() {
        if (currentListInfo == null) {
            this.currentListInfo = new ListInfo(0, -1);
        }
        return currentListInfo;
    }

    public void setCurrentListInfo(ListInfo currentListInfo) {
        this.currentListInfo = currentListInfo;
    }

    private void updateDocumentCompatibility() {
        try {
            final DocumentSettingsPart dsp = mainDocumentPart.getDocumentSettingsPart(true);
            final CTCompat compat = Context.getWmlObjectFactory().createCTCompat();
            compat.setCompatSetting("compatibilityMode", "http://schemas.microsoft.com/office/word", "15");
            dsp.getContents().setCompat(compat);
        } catch (Exception ex) {
            // ignore
            LOGGER.warn("Unable to update document compatibility.", ex);
        }
    }

}
