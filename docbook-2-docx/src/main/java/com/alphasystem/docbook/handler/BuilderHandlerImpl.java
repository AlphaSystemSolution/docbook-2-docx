package com.alphasystem.docbook.handler;

import com.alphasystem.docbook.builder.Builder;

import static com.alphasystem.util.AppUtil.initGenericClass;

/**
 * @author sali
 */
public abstract class BuilderHandlerImpl<O, B extends Builder<O>> implements BuilderHandler<O, B> {

    protected Class<O> modelClass;
    protected Class<B> builderClass;

    @SuppressWarnings({"unchecked"})
    protected BuilderHandlerImpl() {
        final Class<?>[] classes = initGenericClass(getClass());
        modelClass = (Class<O>) classes[0];
        builderClass = (Class<B>) classes[1];
    }

    @Override
    public Class<O> getModelClass() {
        return modelClass;
    }

    @Override
    public Class<B> getBuilderClass() {
        return builderClass;
    }
}
