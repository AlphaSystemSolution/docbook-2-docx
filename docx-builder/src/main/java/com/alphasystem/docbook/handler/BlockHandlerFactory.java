package com.alphasystem.docbook.handler;

/**
 * @author sali
 */
public class BlockHandlerFactory extends HandlerFactory<BlockHandler> {

    public static final String EXAMPLE_KEY = "example";
    public static final String INFORMAL_EXAMPLE_KEY = "informal_example";
    public static final String SIDE_BAR_KEY = "side_bar";
    public static final String SCREEN_KEY = "screen";

    private static BlockHandlerFactory instance;

    /**
     * Do not let any one instantiate this class.
     */
    private BlockHandlerFactory() {
    }

    public synchronized static BlockHandlerFactory getInstance() {
        if (instance == null) {
            instance = new BlockHandlerFactory();
        }
        return instance;
    }

    @Override
    public BlockHandler getHandler(String key) {
        return handlers.get(key);
    }
}
