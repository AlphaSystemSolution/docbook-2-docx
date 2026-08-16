package com.alphasystem.docbook;

import com.alphasystem.asciidoc.model.DocumentInfo;
import com.alphasystem.commons.SystemException;
import com.alphasystem.commons.util.AppUtil;
import com.alphasystem.docbook.handler.InlineHandlerFactory;
import com.alphasystem.docbook.handler.InlineStyleHandler;
import com.alphasystem.docbook.handler.impl.JavaScriptBasedStyleHandler;
import com.alphasystem.docbook.util.ConfigurationUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.alphasystem.docx4j.builder.wml.StylesBuilder;
import com.alphasystem.docx4j.builder.wml.WmlBuilderFactory;
import org.docx4j.wml.RPr;
import org.docx4j.wml.Style;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Context context;
    private final Map<String, Object> jsFunctions = new HashMap<>();

    /**
     * Do not let anyone instantiate this class
     */
    private ApplicationController() {
        // initialization of scripts
        context = Context.newBuilder("js").allowAllAccess(true).build();
        loadScripts();
        executeScripts();
        loadHandlers();

        Runtime.getRuntime().addShutdownHook(new Thread(context::close));
    }

    public Object getFunction(String name) {
        return jsFunctions.get(name);
    }

    private void loadScripts() {
        final var consumer =
                (Function<Path, Void>)
                        path -> {
                            logger.debug("Loading script: {}", path);
                            context.eval(loadSource(path.toFile()));
                            return null;
                        };
        try {
            AppUtil.processResourceDirectory("META-INF/scripts", consumer);
            final var customDirPath = System.getProperty("docbook-docx.customDirPath");
            if (customDirPath != null) {
                final var scripts = String.format("%s%s%s", customDirPath, File.separator, "scripts");
                logger.info("Loading custom scripts: {}", scripts);
                AppUtil.processDirectory(Paths.get(scripts), consumer);
            }
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeScripts() {
        final var configList = configurationUtils.getAppConfig().getConfigList("scripts");
        configList.forEach(config -> {
            final var name = config.getString("name");
            final var type = config.getString("type");
            Class<?> scriptClass;
            try {
                scriptClass = Class.forName(type);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            final var js = context.getBindings("js").getMember(name).execute().as(scriptClass);
            if (type.endsWith("org.docx4j.wml.Style")) {
                final var styles = (StylesBuilder) jsFunctions.getOrDefault("styles", WmlBuilderFactory.getStylesBuilder());
                styles.addStyle((Style) js);
                jsFunctions.put("styles", styles);
            } else {
                jsFunctions.put(name, js);
            }
        });
    }

    private void loadHandlers() {
        final var config = configurationUtils.getConfig("docbook-docx.style-handlers");
        config
                .entrySet()
                .forEach(
                        entry -> {
                            final var key = entry.getKey();
                            final var handlerClassName = entry.getValue().unwrapped().toString();
                            InlineStyleHandler handler;
                            if (handlerClassName.equals(JavaScriptBasedStyleHandler.class.getName())) {
                                handler = new JavaScriptBasedStyleHandler((RPr) jsFunctions.get(key));
                            } else {
                                try {
                                    final var obj = AppUtil.initObject(handlerClassName);
                                    if (!AppUtil.isInstanceOf(InlineStyleHandler.class, obj)) {
                                        throw new RuntimeException(
                                                String.format(
                                                        "Type \"%s\" is not subclass of \"InlineStyleHandler\".",
                                                        handlerClassName));
                                    }
                                    handler = (InlineStyleHandler) obj;
                                } catch (SystemException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            inlineHandlerFactory.registerHandler(key, handler);
                        });
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
