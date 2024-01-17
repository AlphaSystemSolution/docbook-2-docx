package com.alphasystem.docbook;

import com.alphasystem.commons.SystemException;
import com.alphasystem.asciidoc.model.DocumentInfo;
import com.alphasystem.docbook.handler.InlineHandlerFactory;
import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.docbook.util.ConfigurationUtils;
import com.alphasystem.docbook.util.Utils;
import com.alphasystem.commons.util.AppUtil;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;

import java.io.File;
import java.io.IOException;

/**
 * @author sali
 */
public final class ApplicationController {

    private static final ThreadLocal<DocumentContext> CONTEXT = new ThreadLocal<>();

    private static final ConfigurationUtils configurationUtils = ConfigurationUtils.getInstance();
    private final InlineHandlerFactory inlineHandlerFactory = InlineHandlerFactory.getInstance();
    private static ApplicationController instance;

    public static void startContext(final DocumentInfo documentInfo) {
        CONTEXT.set(new DocumentContext(createDocumentInfo(documentInfo)));
    }

    public static DocumentContext getContext() {
        return CONTEXT.get();
    }

    public static void endContext() {
        CONTEXT.remove();
    }

    private static DocumentInfo createDocumentInfo(final DocumentInfo src) {
        var documentInfo = new DocumentInfo(src);
        documentInfo.setTocTitle(configurationUtils.getTableOfContentCaption());
        // TODO: revisit whether we need these to be set here
        /*documentInfo.setCautionCaption(configurationUtils.getAdmonitionCaption(Admonition.CAUTION));
        documentInfo.setImportantCaption(configurationUtils.getAdmonitionCaption(Admonition.IMPORTANT));
        documentInfo.setNoteCaption(configurationUtils.getAdmonitionCaption(Admonition.NOTE));
        documentInfo.setTipCaption(configurationUtils.getAdmonitionCaption(Admonition.TIP));
        documentInfo.setWarningCaption(configurationUtils.getAdmonitionCaption(Admonition.WARNING));*/
        documentInfo.setExampleCaption(configurationUtils.getExampleCaption());
        documentInfo.setTableCaption(configurationUtils.getTableCaption());
        return documentInfo;
    }

    public static synchronized ApplicationController getInstance() {
        if (instance == null) {
            instance = new ApplicationController();
        }
        return instance;
    }

    private final Context context;

    /**
     * Do not let anyone instantiate this class
     */
    private ApplicationController() {
        loadHandlers();
        context = Context.newBuilder("js").allowAllAccess(true).build();

        // initialization of scripts
        configurationUtils.getScriptFiles().stream()
                .map(ApplicationController::readResource)
                .map(ApplicationController::loadSource)
                .forEach(context::eval);

        Runtime.getRuntime().addShutdownHook(new Thread(context::close));
    }

    private void loadHandlers() {
        final var config = configurationUtils.getConfig("docbook-docx.style-handlers");
        config.entrySet().forEach(entry -> {
            final var key = entry.getKey();
            final var handlerClassName = entry.getValue().unwrapped().toString();
            try {
                final var obj = Utils.initObject(handlerClassName);
                if (!AppUtil.isInstanceOf(InlineStyleHandler.class, obj)) {
                    throw new RuntimeException(String.format("Type \"%s\" is not subclass of \"InlineStyleHandler\".", handlerClassName));
                }
                inlineHandlerFactory.registerHandler(key, (InlineStyleHandler) obj);
            } catch (SystemException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static File readResource(String resourceName) {
        try {
            return Utils.readResource(resourceName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Source loadSource(File file) {
        try {
            return Source.newBuilder("js", file).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Context getScriptEngine() {
        return context;
    }
}
