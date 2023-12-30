package com.alphasystem.docbook.handler.impl.block;

import com.alphasystem.docbook.handler.BlockHandlerFactory;
import com.alphasystem.docbook.handler.BlockHandlerService;

import static com.alphasystem.docbook.model.Admonition.*;
import static com.alphasystem.docbook.handler.BlockHandlerFactory.*;

/**
 * @author sali
 */
public class BaseBlockHandlerService extends BlockHandlerService {

    @Override
    public void initializeHandlers() {
        final BlockHandlerFactory instance = BlockHandlerFactory.getInstance();
        instance.registerHandler(CAUTION.name(), new CautionHandler());
        instance.registerHandler(IMPORTANT.name(), new ImportantHandler());
        instance.registerHandler(NOTE.name(), new NoteHandler());
        instance.registerHandler(TIP.name(), new TipHandler());
        instance.registerHandler(WARNING.name(), new WarningHandler());
        instance.registerHandler(EXAMPLE_KEY, new ExampleHandler());
        instance.registerHandler(INFORMAL_EXAMPLE_KEY, new InformalExampleHandler());
        instance.registerHandler(SIDE_BAR_KEY, new SideBarHandler());
        instance.registerHandler(SCREEN_KEY, new ScreenHandler());
    }
}
