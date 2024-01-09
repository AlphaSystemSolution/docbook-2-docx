package com.alphasystem.docbook.model;

/**
 * @author sali
 */
public enum Admonition {

    CAUTION("Caution"), IMPORTANT("Important"), NOTE("Note"), TIP("Tip"), WARNING("Warning");

    private final String value;

    Admonition(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
