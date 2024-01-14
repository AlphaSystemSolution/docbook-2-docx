package com.alphasystem.docbook.builder;

import com.alphasystem.SystemException;
import com.alphasystem.docbook.util.ConfigurationUtils;
import com.alphasystem.docbook.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BuilderFactory {

    private static final BuilderFactory instance;

    static {
        instance = new BuilderFactory();
    }

    public static BuilderFactory getInstance() {
        return instance;
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ConfigurationUtils configurationUtils = ConfigurationUtils.getInstance();

    private final Map<String, Class<?>> buildersClassMap = new HashMap<>();

    /*
     * Do not let any one instantiate this class
     */
    private BuilderFactory() {
        loadBuilders();
    }

    private Builder<?> getBuilder(Object o, Builder<?> parent) {
        if (Objects.isNull(o)) {
            throw new NullPointerException("Object cannot be null.");
        }
        final var name = o.getClass().getName();
        final var builderClass = buildersClassMap.get(name);
        try {
            return (Builder<?>) Utils.initObject(builderClass, new Class<?>[]{o.getClass(), Builder.class}, new Object[]{o, parent});
        } catch (SystemException e) {
            logger.warn("No builder found for: {}", name);
            throw new RuntimeException(e);
        }
    }

    private void loadBuilders() {
        final var config = configurationUtils.getConfig("docbook-docx.builders");
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

    public List<Object> process(Object o, Builder<?> parent) {
        return getBuilder(o, parent).process();
    }
}
