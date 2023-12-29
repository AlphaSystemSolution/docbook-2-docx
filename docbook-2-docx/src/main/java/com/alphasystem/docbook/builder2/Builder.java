package com.alphasystem.docbook.builder2;

public interface Builder<S, T> {

    String getId();
    S getSource();
    T process();
}
