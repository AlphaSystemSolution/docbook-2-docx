package com.alphasystem.docbook.model;

public class ListInfo {

    private long number;
    private long level;

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

    public ListInfo withNumber(long number) {
        this.number = number;
        return this;
    }

    public ListInfo withLevel(long level) {
        this.level = level;
        return this;
    }
}
