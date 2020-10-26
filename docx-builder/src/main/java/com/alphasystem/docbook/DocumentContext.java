package com.alphasystem.docbook;

import com.alphasystem.asciidoc.model.AsciiDocumentInfo;
import org.docbook.model.Article;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.parts.WordprocessingML.DocumentSettingsPart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.docx4j.relationships.ObjectFactory;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.CTCompat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alphasystem.util.AppUtil.isInstanceOf;
import static org.docx4j.openpackaging.parts.relationships.Namespaces.HYPERLINK;

/**
 * @author sali
 */
public final class DocumentContext {

    private static final ObjectFactory RELATIONSHIP_OBJECT_FACTORY = new ObjectFactory();
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentContext.class);
    private static final Object DUMMY = new Object();

    private final Map<Long, Object> listNumbersMap = new HashMap<>();
    private final List<String> documentStyles;
    private final Object document;
    private final AsciiDocumentInfo documentInfo;
    private final boolean article;
    private MainDocumentPart mainDocumentPart;
    private NumberingDefinitionsPart numberingDefinitionsPart;

    public DocumentContext(final AsciiDocumentInfo documentInfo, final Object document) {
        this.documentInfo = documentInfo;
        this.document = document;
        this.documentStyles = new ArrayList<>();
        article = isInstanceOf(Article.class, document);
    }

    public AsciiDocumentInfo getDocumentInfo() {
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

    public MainDocumentPart getMainDocumentPart() {
        return mainDocumentPart;
    }

    public void setMainDocumentPart(MainDocumentPart mainDocumentPart) {
        this.mainDocumentPart = mainDocumentPart;
        this.numberingDefinitionsPart = getMainDocumentPart().getNumberingDefinitionsPart();
        updateDocumentCompatibility();
    }

    public String addHyperlink(String url) {
        final Relationship relationship = RELATIONSHIP_OBJECT_FACTORY.createRelationship();
        relationship.setType(HYPERLINK);
        relationship.setTarget(url);
        relationship.setTargetMode("External");
        mainDocumentPart.getRelationshipsPart().addRelationship(relationship);
        return relationship.getId();
    }

    public long getListNumber(long numberId, long level) {
        if (numberId < 0 || level < 0) {
            return -1;
        }
        final Object o = listNumbersMap.get(numberId);
        if (o == null) {
            listNumbersMap.put(numberId, DUMMY);
        } else {
            numberId = numberingDefinitionsPart.restart(numberId, level, 1);
        }
        return numberId;
    }

    private void updateDocumentCompatibility() {
        try {
            final DocumentSettingsPart dsp = mainDocumentPart.getDocumentSettingsPart(true);
            final CTCompat compat = Context.getWmlObjectFactory().createCTCompat();
            compat.setCompatSetting("compatibilityMode", "http://schemas.microsoft.com/office/word", "15");
            dsp.getContents().setCompat(compat);
        }catch (Exception ex) {
            // ignore
            LOGGER.warn("Unable to update document compatibility.", ex);
        }
    }

}
