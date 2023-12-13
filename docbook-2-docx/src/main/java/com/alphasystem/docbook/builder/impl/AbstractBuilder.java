package com.alphasystem.docbook.builder.impl;


import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.BuilderFactory;
import com.alphasystem.docbook.util.ConfigurationUtils;
import org.docbook.model.Title;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static com.alphasystem.util.AppUtil.isInstanceOf;

/**
 * @author sali
 */
public abstract class AbstractBuilder<T> implements Builder<T> {

    private static Method getMethod(Object obj, String methodName) {
        Method method = null;
        try {
            method = obj.getClass().getMethod(methodName);
        } catch (NoSuchMethodException e) {
            // ignore
        }
        return method;
    }

    public static Object invokeMethod(Object obj, String methodName) {
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

    public static String getId(Object source) {
        return (String) invokeMethod(source, "getId");
    }

    @SuppressWarnings({"unchecked"})
    public static List<Object> getContent(Object source) {
        return (List<Object>) invokeMethod(source, "getContent");
    }

    @SuppressWarnings({"unchecked"})
    public static List<Object> getTitleContent(Object source) {
        return (List<Object>) invokeMethod(source, "getTitleContent");
    }

    public static Title getTitle(Object source) {
        List<Object> content = getContent(source);
        Title title = getContent(Title.class, content);
        if (title == null) {
            content = getTitleContent(source);
            title = getContent(Title.class, content);
        }
        return title;
    }

    @SuppressWarnings({"unchecked"})
    protected static <T> T getContent(Class<T> declaredType, List<Object> content) {
        T o = null;
        if (content == null) {
            return null;
        }
        for (Object obj : content) {
            if (isInstanceOf(declaredType, obj)) {
                o = (T) obj;
                break;
            }
        }
        return o;
    }

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final ApplicationController applicationController = ApplicationController.getInstance();
    protected final ConfigurationUtils configurationUtils = ConfigurationUtils.getInstance();
    protected final BuilderFactory factory = BuilderFactory.getInstance();
    protected final Builder<?> parent;
    protected T source;
    protected List<Object> titleContent;
    protected List<Object> content;
    protected int indexInParent;

    protected AbstractBuilder(Builder<?> parent, T source, int indexInParent) {
        if (source == null) {
            throw new NullPointerException(String.format("Source object is null in \"%s\"", getClass().getName()));
        }
        this.parent = parent;
        this.source = source;
        this.indexInParent = indexInParent;
        initContent();
    }

    public int getIndexInParent() {
        return indexInParent;
    }

    public void setIndexInParent(int indexInParent) {
        this.indexInParent = indexInParent;
    }

    protected abstract void initContent();

    @Override
    public T getSource() {
        return source;
    }

    @Override
    public Builder<?> getParent() {
        return parent;
    }

    protected boolean hasParent(Class<? extends Builder<?>> builderClass) {
        boolean result = false;
        var currentParent = parent;
        while (currentParent != null) {
            if (isInstanceOf(builderClass, currentParent)) {
                result = true;
                break;
            }
            currentParent = currentParent.getParent();
        }
        return result;
    }

    @SuppressWarnings({"unchecked"})
    protected <B extends BlockBuilder<?>> B getParent(Class<B> builderClass) {
        B result = null;
        var currentParent = parent;
        while (currentParent != null) {
            if (isInstanceOf(builderClass, currentParent)) {
                result = (B) currentParent;
                break;
            }
            currentParent = currentParent.getParent();
        }
        return result;
    }

    protected void logUnhandledContentWarning(Object o) {
        logger.warn("Unhandled type \"{}\".", o.getClass().getName());
    }


}
