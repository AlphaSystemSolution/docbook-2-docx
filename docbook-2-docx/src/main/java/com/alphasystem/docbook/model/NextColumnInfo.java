package com.alphasystem.docbook.model;

public class NextColumnInfo {

    private final int moreRows;
    private final int currentColumnIndex;
    private final int nextColumnIndex;


    public NextColumnInfo(int moreRows, int currentColumnIndex, int nextColumnIndex) {
        this.moreRows = Math.max(moreRows, 0);
        this.currentColumnIndex = Math.max(currentColumnIndex, 0);
        this.nextColumnIndex = Math.max(nextColumnIndex, 0);
    }

    public int getMoreRows() {
        return moreRows;
    }

    public int getCurrentColumnIndex() {
        return currentColumnIndex;
    }

    public int getNextColumnIndex() {
        return nextColumnIndex;
    }

    public NextColumnInfo decrementMoreRows() {
        return new NextColumnInfo(moreRows - 1, currentColumnIndex, nextColumnIndex);
    }

    @Override
    public String toString() {
        return "NextColumnInfo (" +
                "moreRows=" + moreRows +
                ", currentColumnIndex=" + currentColumnIndex +
                ", nextColumnIndex=" + nextColumnIndex +
                ")";
    }
}
