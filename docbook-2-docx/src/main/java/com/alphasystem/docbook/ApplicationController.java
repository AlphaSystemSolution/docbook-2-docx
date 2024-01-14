package com.alphasystem.docbook;

import com.alphasystem.asciidoc.model.DocumentInfo;
import com.alphasystem.docbook.handler.InlineHandlerService;
import com.alphasystem.docbook.util.ConfigurationUtils;
import com.alphasystem.docbook.util.Utils;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;

import java.io.File;
import java.io.IOException;
import java.util.ServiceLoader;

import static java.util.ServiceLoader.load;

/**
 * @author sali
 */
public final class ApplicationController {

    private static final ThreadLocal<DocumentContext> CONTEXT = new ThreadLocal<>();

    private static final ConfigurationUtils configurationUtils = ConfigurationUtils.getInstance();
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
        ServiceLoader<InlineHandlerService> inlineHandlerServices = load(InlineHandlerService.class);
        inlineHandlerServices.forEach(InlineHandlerService::initializeHandlers);

        context = Context.newBuilder("js").allowAllAccess(true).build();

        // initialization of scripts
        configurationUtils.getScriptFiles().stream()
                .map(ApplicationController::readResource)
                .map(ApplicationController::loadSource)
                .forEach(context::eval);

        Runtime.getRuntime().addShutdownHook(new Thread(context::close));
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
