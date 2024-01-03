package com.alphasystem.docbook;

import com.alphasystem.asciidoc.model.DocumentInfo;
import com.alphasystem.docbook.handler.BlockHandlerFactory;
import com.alphasystem.docbook.handler.BlockHandlerService;
import com.alphasystem.docbook.handler.BuilderHandlerService;
import com.alphasystem.docbook.handler.InlineHandlerService;
import com.alphasystem.docbook.model.Admonition;
import com.alphasystem.docbook.util.ConfigurationUtils;
import org.docx4j.wml.Tbl;

import java.nio.file.Path;
import java.util.ServiceLoader;

import static com.alphasystem.docbook.handler.BlockHandlerFactory.*;
import static com.alphasystem.util.nio.NIOFileUtils.USER_DIR;
import static java.nio.file.Paths.get;
import static java.util.ServiceLoader.load;

/**
 * @author sali
 */
public final class ApplicationController {

    private static final String CONF = "conf";
    private static final String CONF_DIR = System.getProperty("conf.path", USER_DIR);
    private static final Path CONF_PATH = get(CONF_DIR, CONF);
    public static final String CONF_PATH_VALUE = CONF_PATH.toString();
    private static final ThreadLocal<DocumentContext> CONTEXT = new ThreadLocal<>();

    private static final ConfigurationUtils configurationUtils = ConfigurationUtils.getInstance();
    private static ApplicationController instance;

    @Deprecated
    public static void startContext(DocumentContext documentContext) {
        CONTEXT.set(documentContext);
    }

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
        documentInfo.setCautionCaption(configurationUtils.getAdmonitionCaption(Admonition.CAUTION));
        documentInfo.setImportantCaption(configurationUtils.getAdmonitionCaption(Admonition.IMPORTANT));
        documentInfo.setNoteCaption(configurationUtils.getAdmonitionCaption(Admonition.NOTE));
        documentInfo.setTipCaption(configurationUtils.getAdmonitionCaption(Admonition.TIP));
        documentInfo.setWarningCaption(configurationUtils.getAdmonitionCaption(Admonition.WARNING));
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

    private final BlockHandlerFactory blockHandlerFactory;

    /**
     * Do not let anyone instantiate this class
     */
    private ApplicationController() {
        ServiceLoader<BlockHandlerService> blockHandlerServices = load(BlockHandlerService.class);
        blockHandlerServices.forEach(BlockHandlerService::initializeHandlers);

        ServiceLoader<InlineHandlerService> inlineHandlerServices = load(InlineHandlerService.class);
        inlineHandlerServices.forEach(InlineHandlerService::initializeHandlers);

        ServiceLoader<BuilderHandlerService> builderHandlerServices = load(BuilderHandlerService.class);
        builderHandlerServices.forEach(BuilderHandlerService::initializeHandlers);

        blockHandlerFactory = BlockHandlerFactory.getInstance();
    }

    public Tbl getExampleTable() {
        return (Tbl) blockHandlerFactory.getHandler(EXAMPLE_KEY).handleBlock();
    }

    public Tbl getInformalExampleTable() {
        return (Tbl) blockHandlerFactory.getHandler(INFORMAL_EXAMPLE_KEY).handleBlock();
    }

    public Tbl getSideBarTable() {
        return (Tbl) blockHandlerFactory.getHandler(SIDE_BAR_KEY).handleBlock();
    }

    public Tbl getScreenTable() {
        return (Tbl) blockHandlerFactory.getHandler(SCREEN_KEY).handleBlock();
    }

    public Tbl getAdmonitionTable(Admonition admonition) {
        return (Tbl) blockHandlerFactory.getHandler(admonition.name()).handleBlock();
    }

}
