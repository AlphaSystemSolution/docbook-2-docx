package com.alphasystem.docbook.model;

public class ListInfo {

    private final long number;
    private final long level;

    public ListInfo() {
        this(-1, -1);
    }

    public ListInfo(long number, long level) {
        this.number = number;
        this.level = level;
    }

    public long getNumber() {
        return number;
    }

    public long getLevel() {
        return level;
    }
}
