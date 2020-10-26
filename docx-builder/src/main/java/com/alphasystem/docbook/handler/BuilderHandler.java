package com.alphasystem.docbook.handler;

import com.alphasystem.docbook.builder.Builder;

/**
 * Specialized handler which give access to underlying {@link Builder}.
 *
 * @author sali
 */
public interface BuilderHandler<O, B extends Builder<O>> extends Handler {

    Class<O> getModelClass();
    Class<B> getBuilderClass();
}
