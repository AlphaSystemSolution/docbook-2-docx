package com.alphasystem.docbook.builder2.impl;

import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.docbook.util.ConfigurationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class AbstractBuilder<S, T> implements Builder<S, T> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final ConfigurationUtils configurationUtils = ConfigurationUtils.getInstance();
    protected String id;
    protected S source;

    protected AbstractBuilder(S source) {
        if (source == null) {
            throw new NullPointerException(String.format("Source object is null in \"%s\"", getClass().getName()));
        }
        logger.debug("Initializing builder: {}", getClass().getName());
        this.source = source;
        this.id = getId(source);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public S getSource() {
        return source;
    }

    private static Method getMethod(Object obj, String methodName) {
        Method method = null;
        try {
            method = obj.getClass().getMethod(methodName);
        } catch (NoSuchMethodException e) {
            // ignore
        }
        return method;
    }

    private static Object invokeMethod(Object obj, String methodName) {
        Object value = null;
        final Method method = getMethod(obj, methodName);
        if (method != null) {
            try {
                value = method.invoke(obj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                // ignore
            }
        }
        return value;
    }

    protected static String getId(Object source) {
        return (String) invokeMethod(source, "getId");
    }
}
