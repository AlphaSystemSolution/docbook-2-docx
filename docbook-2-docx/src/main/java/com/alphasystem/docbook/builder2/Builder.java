package com.alphasystem.docbook.builder2;

import java.util.List;

public interface Builder<S> {

    String getId();
    S getSource();
    List<Object> process(S source);
}
