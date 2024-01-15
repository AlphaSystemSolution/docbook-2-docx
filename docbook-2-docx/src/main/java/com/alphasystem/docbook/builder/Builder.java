package com.alphasystem.docbook.builder;

import java.util.List;

public interface Builder<S> {

    S getSource();
    Builder<?> getParent();
    String getRole();
    List<Object> process();
}
