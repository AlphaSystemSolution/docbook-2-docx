package com.alphasystem.docbook.handler;

/**
 * @author sali
 */
public class BuilderHandlerFactory extends HandlerFactory<BuilderHandler<?, ?>> {

    private static BuilderHandlerFactory instance;

    /**
     * Do not let any one instantiate this class.
     */
    private BuilderHandlerFactory() {
    }

    public synchronized static BuilderHandlerFactory getInstance() {
        if (instance == null) {
            instance = new BuilderHandlerFactory();
        }
        return instance;
    }

    @Override
    public BuilderHandler<?, ?> getHandler(String key) {
        return handlers.get(key);
    }
}
