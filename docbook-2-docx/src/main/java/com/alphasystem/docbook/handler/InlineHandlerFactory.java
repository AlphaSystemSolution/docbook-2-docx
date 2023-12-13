package com.alphasystem.docbook.handler;

import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docbook.handler.impl.inline.ColorHandler;
import com.alphasystem.docbook.handler.impl.inline.StyleHandler;
import com.alphasystem.docbook.model.ColorCode;

/**
 * @author sali
 */
public final class InlineHandlerFactory extends HandlerFactory<InlineStyleHandler> {

    public static final String BOLD = "bold";
    public static final String STRONG = "strong";
    public static final String ITALIC = "italic";

    public static final String MARKED = "marked";
    public static final String UNDERLINE = "underline";
    public static final String LINE_THROUGH = "line-through";
    public static final String LITERAL = "literal";
    public static final String SUBSCRIPT = "subscript";
    public static final String SUPERSCRIPT = "superscript";
    public static final String HYPERLINK = "hyperlink";

    private static InlineHandlerFactory instance;

    /**
     * Do not let any one instantiate this class.
     */
    private InlineHandlerFactory() {
    }

    public static synchronized InlineHandlerFactory getInstance() {
        if (instance == null) {
            instance = new InlineHandlerFactory();
        }
        return instance;
    }

    @Override
    public InlineStyleHandler getHandler(String style) {
        InlineStyleHandler handler = handlers.get(style);
        if (handler == null) {
            final ColorCode colorCode = ColorCode.getByName(style);
            if (colorCode != null) {
                handler = new ColorHandler(colorCode);
                handlers.put(colorCode.getName(), handler);
            } else if (ApplicationController.getContext().getDocumentStyles().contains(style)) {
                handler = new StyleHandler(style);
                handlers.put(style, handler);
            }
        }
        return handler;
    }
}
