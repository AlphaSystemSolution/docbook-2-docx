package com.alphasystem.docbook;

import com.alphasystem.asciidoc.model.DocumentInfo;
import com.alphasystem.commons.SystemException;
import com.alphasystem.docbook.model.DocumentCaption;
import com.alphasystem.docbook.util.ConfigurationUtils;
import com.alphasystem.docx4j.builder.wml.NumberingHelper;
import com.alphasystem.docx4j.builder.wml.WmlPackageBuilder;
import org.apache.commons.lang3.StringUtils;
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

/**
 * @author sali
 */
public final class DocumentContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentContext.class);
    private final Map<String, String> listNumbersMap = new HashMap<>();
    private final Map<String, String> idToLinkMap = new HashMap<>();
    private final ConfigurationUtils configurationUtils = ConfigurationUtils.getInstance();
    private final List<String> documentStyles;
    private final DocumentInfo documentInfo;
    private final boolean article;
    private WmlPackageBuilder wmlPackageBuilder;
    private WordprocessingMLPackage wordprocessingMLPackage;
    private MainDocumentPart mainDocumentPart;
    private final NumberingHelper numberingHelper = NumberingHelper.getInstance();

    public DocumentContext(final DocumentInfo documentInfo) {
        this.documentInfo = documentInfo;
        this.documentStyles = new ArrayList<>();
        article = true; // TODO:
        buildPackage();
    }


    private void buildPackage() {
        try {
            final var inputs = new WmlPackageBuilder.WmlPackageInputs().withTemplatePath(configurationUtils.getTemplate());
            wmlPackageBuilder = new WmlPackageBuilder(inputs).styles(configurationUtils.getStyles());
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
        } catch (Docx4JException | SystemException e) {
            throw new RuntimeException(e);
        }
    }

    public DocumentInfo getDocumentInfo() {
        return documentInfo;
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
        if (listItem == null && level >= 0) {
            // for var list
            listNumbersMap.put(styleName, styleName);
        }
        if (listItem == null || level < 0) {
            return -1;
        }

        long numberId = listItem.getNumberId();
        final var o = listNumbersMap.get(styleName);
        if (o == null) {
            listNumbersMap.put(styleName, styleName);
        } else {
            numberId = mainDocumentPart.getNumberingDefinitionsPart().restart(numberId, level, 1);
            numberingHelper.update(styleName, numberId);
        }
        return numberId;
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
