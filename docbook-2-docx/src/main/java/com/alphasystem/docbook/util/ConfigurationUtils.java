package com.alphasystem.docbook.util;

import com.alphasystem.docbook.model.Admonition;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.apache.commons.lang3.StringUtils;
import org.docbook.model.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.lang.String.format;

/**
 * @author sali
 */
public class ConfigurationUtils {

    private static final ConfigurationUtils instance;

    static {
        instance = new ConfigurationUtils();
    }

    public static synchronized ConfigurationUtils getInstance() {
        return instance;
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Map<String, Class<?>> buildersClassMap = new HashMap<>();
    private final Map<String, String> titlesMap = new HashMap<>();
    private final Map<String, String> functionNames = new HashMap<>();
    private final Map<Admonition, Tuple2<String, String>> admonitions = new HashMap<>();
    private final Config mainConfig;
    private final Config appConfig;
    private String defaultListStyle;
    private String tocCaption;
    private String tableCaption;
    private String exampleCaption;
    private String varTermStyle;

    /**
     * Do not let any one instantiate this class.
     */
    private ConfigurationUtils() {
        mainConfig = ConfigFactory.load();
        appConfig = mainConfig.getConfig("docbook-docx");
        loadBuilders();
        loadTitles();
        loadAdmonitions();
        loadFunctionNames();
    }

    private void loadBuilders() {
        final var config = appConfig.getConfig("builders");
        config.entrySet().forEach(entry -> {
            final var key = entry.getKey();
            final var builderClassName = entry.getValue().unwrapped().toString();
            logger.info("Loading builder \"{}\" for \"{}\".", builderClassName, key);
            try {
                buildersClassMap.put(key, Class.forName(builderClassName));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void loadTitles() {
        final var config = appConfig.getConfig("titles");
        config.entrySet().forEach(entry -> {
            final var key = entry.getKey();
            final var value = entry.getValue().unwrapped().toString();
            titlesMap.put(key, value);
        });
    }

    private void loadAdmonitions() {
        final var config = appConfig.getConfig("admonitions");
        Arrays.stream(Admonition.values()).forEach(admonition -> {
            final var c = config.getConfig(admonition.name().toLowerCase());
            admonitions.put(admonition, Tuple.of(c.getString("caption"), c.getString("color")));
        });
    }

    private void loadFunctionNames() {
        final var config = appConfig.getConfig("script-function-names");
        config.entrySet().forEach(entry -> {
            final var key = entry.getKey();
            final var value = entry.getValue().unwrapped().toString();
            functionNames.put(key, value);
        });
    }

    public Class<?> getBuilderClass(Object o) throws ClassNotFoundException {
        final var className = o.getClass().getName();
        var clazz = buildersClassMap.get(className);
        if (Objects.isNull(clazz)) {
            throw new RuntimeException(format("No builder found for: %s", className));
        }
        return clazz;
    }

    public String getTitleStyle(String titleKey) {
        return titlesMap.getOrDefault(titleKey, titlesMap.get("default"));
    }

    public String getTitleStyle(int level, Class<?> parentClass) {
        var titleKey = parentClass.getName();
        if (parentClass.equals(Section.class)) {
            titleKey = format("%s.%s", titleKey, level);
        }
        return getTitleStyle(titleKey);
    }

    public String getDefaultListStyle() {
        if (Objects.isNull(defaultListStyle)) {
            defaultListStyle = getString("list-style.default");
        }
        return defaultListStyle;
    }

    public String getVarTermStyle() {
        if (Objects.isNull(varTermStyle)) {
            varTermStyle = getString("list-style.var-term");
        }
        return varTermStyle;
    }

    public Tuple2<String, String> getAdmonitionConfig(Admonition admonition) {
        return admonitions.get(admonition);
    }

    public String getAdmonitionFunctionName() {
        return functionNames.get("admonition");
    }

    public String getSideBarFunctionName() {
        return functionNames.get("sidebar");
    }

    public String getExampleFunctionName() {
        return functionNames.get("example");
    }

    public String getExampleCaption() {
        if (Objects.isNull(exampleCaption)) {
            exampleCaption = appConfig.getString("captions.example");
        }
        return exampleCaption;
    }

    public String getTableCaption() {
        if (Objects.isNull(tableCaption)) {
            tableCaption = appConfig.getString("captions.table");
        }
        return tableCaption;
    }

    public String getTableOfContentCaption() {
        if (Objects.isNull(tocCaption)) {
            tocCaption = appConfig.getString("captions.toc");
        }
        return tocCaption;
    }

    public String getTableStyle(String ts) {
        return getString(String.format("table.%s", ts));
    }

    public String getTemplate() {
        return getString("template");
    }

    public String[] getStyles() {
        final var defaultStyles = "default-styles.xml";
        var _styles = getString("styles");
        _styles = StringUtils.isBlank(_styles) ? defaultStyles : defaultStyles + "," + _styles;
        return _styles.split(",");
    }

    public List<String> getScriptFiles() {
        final var defaultJsFiles = appConfig.getStringList("scripts");
        final var results = new ArrayList<>(defaultJsFiles);
        try {
            final var customScripts = getString("custom-scripts");
            if (Objects.nonNull(customScripts)) {
                results.addAll(Arrays.asList(customScripts.split(",")));
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
        return results;
    }

    public Config getConfig(String path) {
        return mainConfig.getConfig(path);
    }

    private String getString(String key) {
        return appConfig.hasPath(key) ? appConfig.getString(key) : null;
    }
}
