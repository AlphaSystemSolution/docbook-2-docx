package com.alphasystem.xml;

import com.alphasystem.docbook.util.ConfigurationUtils;

/**
 * @author sali
 */
public final class DocumentInfo<T> {

    private static ConfigurationUtils configurationUtils = ConfigurationUtils.getInstance();

    private T document;

    private boolean toc;

    private String tableOfContentCaption;

    private boolean numbered;

    private String exampleCaption;

    public DocumentInfo() {
        setTableOfContentCaption(configurationUtils.getTableOfContentCaption());
        setExampleCaption(configurationUtils.getExampleCaption());
    }

    public T getDocument() {
        return document;
    }

    public void setDocument(T document) {
        this.document = document;
    }

    public boolean isToc() {
        return toc;
    }

    public void setToc(boolean toc) {
        this.toc = toc;
    }

    public String getTableOfContentCaption() {
        return tableOfContentCaption;
    }

    public void setTableOfContentCaption(String tableOfContentCaption) {
        this.tableOfContentCaption = tableOfContentCaption;
    }

    public boolean isNumbered() {
        return numbered;
    }

    public void setNumbered(boolean numbered) {
        this.numbered = numbered;
    }

    public String getExampleCaption() {
        return exampleCaption;
    }

    public void setExampleCaption(String exampleCaption) {
        this.exampleCaption = exampleCaption;
    }

    @Override
    public String toString() {
        String objectType = (document == null) ? "None" : document.getClass().getName();
        return String.format("{\n\"Object\": \"%s\",\n\"TOC\": %s,\n\"Numbered\": %s\n}", objectType, toc, numbered);
    }
}
