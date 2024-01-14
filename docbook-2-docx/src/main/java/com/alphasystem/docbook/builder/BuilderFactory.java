package com.alphasystem.docbook.builder;

import com.alphasystem.docbook.util.ConfigurationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

public class BuilderFactory {

    private static final BuilderFactory instance;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ConfigurationUtils configurationUtils = ConfigurationUtils.getInstance();

    static {
        instance = new BuilderFactory();
    }

    public static BuilderFactory getInstance() {
        return instance;
    }

    /*
     * Do not let any one instantiate this class
     */
    private BuilderFactory() {
    }

    private Builder<?> getBuilder(Object o, Builder<?> parent) {
        if (Objects.isNull(o)) {
            throw new NullPointerException("Object cannot be null.");
        }
        try {
            final var builderClass = configurationUtils.getBuilderClass(o);
            final var constructor = builderClass.getConstructor(o.getClass(), Builder.class);
            return (Builder<?>) constructor.newInstance(o, parent);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException
                 | IllegalAccessException e) {
            logger.warn("No builder found for: {}", o.getClass().getName());
            throw new RuntimeException(e);
        }
    }

    public List<Object> process(Object o, Builder<?> parent) {
        return getBuilder(o, parent).process();
    }
}
