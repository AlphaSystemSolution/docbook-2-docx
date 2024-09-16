package com.alphasystem.docbook.handler;

import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docbook.handler.impl.ColorHandler;
import com.alphasystem.docbook.handler.impl.HighlightHandler;
import com.alphasystem.docbook.handler.impl.StyleHandler;
import com.alphasystem.docbook.model.ColorCode;

/**
 * @author sali
 */
public final class InlineHandlerFactory extends HandlerFactory<InlineStyleHandler> {

    public static final String ITALIC = "italic";
    public static final String LITERAL = "literal";
    public static final String SUBSCRIPT = "subscript";
    public static final String SUPERSCRIPT = "superscript";
    public static final String HYPERLINK = "hyperlink";
    private static final String HIGHLIGHT_STYLE_PREFIX = "highlight-";

    private static final InlineHandlerFactory instance;

    static {
        instance = new InlineHandlerFactory();
    }

    /**
     * Do not let any one instantiate this class.
     */
    private InlineHandlerFactory() {
    }

    public static InlineHandlerFactory getInstance() {
        return instance;
    }

    @Override
    public InlineStyleHandler getHandler(String style) {
        InlineStyleHandler handler = handlers.get(style);
        if (handler == null) {
            final var colorCode = ColorCode.getByName(style);
            if (colorCode != null) {
                handler = new ColorHandler(colorCode);
                handlers.put(colorCode.getName(), handler);
            } else if (style.startsWith(HIGHLIGHT_STYLE_PREFIX)) {
                var index = style.indexOf(HIGHLIGHT_STYLE_PREFIX);
                var colorName = style.substring(index + HIGHLIGHT_STYLE_PREFIX.length());
                handler = new HighlightHandler(colorName);
                handlers.put(style, handler);
            } else if (ApplicationController.getContext().getDocumentStyles().contains(style)) {
                handler = new StyleHandler(style);
                handlers.put(style, handler);
            }
        }
        return handler;
    }
}
