package com.alphasystem.docbook.handler.impl.inline;

import com.alphasystem.docbook.handler.InlineHandlerFactory;
import com.alphasystem.docbook.handler.InlineHandlerService;

/**
 * @author sali
 */
public class BaseInlineInlineHandlerService extends InlineHandlerService {

    @Override
    public void initializeHandlers() {
        final BoldHandler handler = new BoldHandler();
        final InlineHandlerFactory instance = InlineHandlerFactory.getInstance();
        instance.registerHandler(InlineHandlerFactory.STRONG, handler);
        instance.registerHandler(InlineHandlerFactory.BOLD, handler);
        instance.registerHandler(InlineHandlerFactory.HYPERLINK, new HyperlinkHandler());
        instance.registerHandler(InlineHandlerFactory.ITALIC, new ItalicHandler());
        instance.registerHandler(InlineHandlerFactory.MARKED, new HighlightHandler());
        instance.registerHandler(InlineHandlerFactory.LITERAL, new LiteralHandler());
        instance.registerHandler(InlineHandlerFactory.LINE_THROUGH, new StrikeThroughHandler());
        instance.registerHandler(InlineHandlerFactory.SUBSCRIPT, new SubscriptHandler());
        instance.registerHandler(InlineHandlerFactory.SUPERSCRIPT, new SuperscriptHandler());
        instance.registerHandler(InlineHandlerFactory.UNDERLINE, new UnderlineHandler());
    }
}
