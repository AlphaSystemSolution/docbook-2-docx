package com.alphasystem.docbook.util;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.block.SectionBuilder;
import com.alphasystem.docbook.model.Admonition;
import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.SystemConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.docbook.model.Section;

import java.io.IOException;
import java.util.*;

import static com.alphasystem.util.AppUtil.isInstanceOf;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author sali
 */
public class ConfigurationUtils {

    private static ConfigurationUtils instance;

    public static synchronized ConfigurationUtils getInstance() {
        if (instance == null) {
            try {
                instance = new ConfigurationUtils();
            } catch (ConfigurationException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return instance;
    }

    private final CompositeConfiguration configuration;
    private final Map<String, Class<?>> buildersClassMap = new HashMap<>();

    /**
     * Do not let any one instantiate this class.
     */
    private ConfigurationUtils() throws ConfigurationException {
        Parameters parameters = new Parameters();
        configuration = new CompositeConfiguration();

        try {
            final var builder = new FileBasedConfigurationBuilder<>(
                    PropertiesConfiguration.class).configure(parameters.fileBased()
                    .setFile(Utils.readResource("system-defaults.properties")));
            configuration.addConfiguration(builder.getConfiguration());
            configuration.addConfiguration(new SystemConfiguration());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Class<?> getBuilderClass(Object o) throws ClassNotFoundException {
        final var className = o.getClass().getName();
        var clazz = buildersClassMap.get(className);
        if (Objects.isNull(clazz)) {
            final var configValue = configuration.getString(format("%s.builder", className));
            if (Objects.isNull(configValue)) {
                throw new RuntimeException(format("No builder found for: %s", className));
            }
            clazz = Class.forName(configValue);
            buildersClassMap.put(className, clazz);
        }
        return clazz;
    }

    public String getTitleStyle(int level, Class<?> parentClass) {
        String defaultTitle = configuration.getString("default.title");
        var titleKey = parentClass.getName();
        if (parentClass.equals(Section.class)) {
            titleKey = format("%s.%s", titleKey, level);
        }
        titleKey = format("%s.title", titleKey);
        return configuration.getString(titleKey, defaultTitle);
    }

    public String getTitleStyle(Builder builder) {
        String defaultTitle = configuration.getString("default.title");
        String titleKey = format("%s", builder.getSource().getClass().getName());
        if (isInstanceOf(SectionBuilder.class, builder)) {
            SectionBuilder sectionBuilder = (SectionBuilder) builder;
            int level = sectionBuilder.getLevel();
            titleKey = format("%s.%s", titleKey, level);
        }
        titleKey = format("%s.title", titleKey);
        return configuration.getString(titleKey, defaultTitle);
    }

    public String getDefaultListStyle() {
        return getString("default.list.style");
    }

    public String getAdmonitionCaptionColor(Admonition admonition) {
        return getString(format("%s.color", admonition.name()));
    }

    public String getAdmonitionCaption(Admonition admonition) {
        return getString(format("%s.caption", admonition.name()));
    }

    public String getExampleCaption() {
        return configuration.getString("example.caption");
    }

    public String getTableCaption() {
        return configuration.getString("table.caption");
    }

    public String getTableOfContentCaption() {
        return configuration.getString("toc.caption");
    }

    public String getTableStyle(String ts) {
        return isBlank(ts) ? null : configuration.getString(ts);
    }

    public String getTemplate() {
        return configuration.getString("template");
    }

    public String[] getStyles() {
        final var defaultStyles = "default-styles.xml";
        var _styles = configuration.getString("styles");
        _styles = StringUtils.isBlank(_styles) ? defaultStyles : defaultStyles + "," + _styles;
        return _styles.split(",");
    }

    public List<String> getScriptFiles() {
        final var defaultJsFiles = Arrays.asList(configuration.getString("default.js.files").split(","));
        final var results = new ArrayList<>(defaultJsFiles);
        try {
            final var customJsFiles = configuration.getList(String.class, "customs.js.files");
            if (Objects.nonNull(customJsFiles)) {
                results.addAll(customJsFiles);
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
        return results;
    }

    public String getString(String key) {
        return configuration.getString(key);
    }
}
