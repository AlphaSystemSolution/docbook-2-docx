package com.alphasystem.docbook.builder;

import java.util.List;

/**
 * @author sali
 */
public interface Builder<T> {

    List<Object> buildContent();

    T getSource();

    Builder<?> getParent();

}
