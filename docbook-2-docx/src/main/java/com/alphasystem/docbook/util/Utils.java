package com.alphasystem.docbook.util;

import com.alphasystem.SystemException;
import com.alphasystem.util.AppUtil;
import com.alphasystem.util.nio.NIOFileUtils;
import org.docbook.model.Emphasis;
import org.docbook.model.Phrase;
import org.docbook.model.Superscript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.alphasystem.xml.UnmarshallerConstants.*;
import static com.alphasystem.xml.UnmarshallerConstants.isSuperscriptType;

public class Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger("com.alphasystem.docbook");

    private Utils() {
    }

    public static File readResource(String resourceName) throws IOException {
        try (var is = AppUtil.getResourceAsStream(resourceName)) {
            var file = Files.createTempFile("system-defaults-", ".properties").toFile();
            file.deleteOnExit();
            final var os = new FileOutputStream(file);
            NIOFileUtils.fastCopy(is, os);
            os.close();
            return file;
        }
    }

    public static Object initObject(String fullQualifiedClassName) throws SystemException {
        return initObject(fullQualifiedClassName, null, null);
    }

    public static Object initObject(String fullQualifiedClassName,
                                    Class<?>[] parameterTypes,
                                    Object[] args) throws SystemException {
        try {
            return initObject(Class.forName(fullQualifiedClassName), parameterTypes, args);
        } catch (ClassNotFoundException ex) {
            throw new SystemException(String.format("Could not initialize class of type \"%s\".", fullQualifiedClassName), ex);
        }
    }

    public static Object initObject(Class<?> clazz, Class<?>[] parameterTypes, Object[] args) throws SystemException {
        try {
            return clazz.getConstructor(parameterTypes).newInstance(args);
        } catch (Exception ex) {
            throw new SystemException(String.format("Could not initialize class of type \"%s\".", clazz.getName()), ex);
        }
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

    public static String getLinkText(List<Object> contents) {
        return getLinkText("", contents);
    }

    private static String getLinkText(String result, List<Object> contents) {
        if (Objects.isNull(contents) || contents.isEmpty()) {
            return result;
        }
        final var collectedText = contents.stream().map(content -> {
            if (isStringType(content)) {
                return (String) content;
            } else if (isEmphasisType(content)) {
                return getLinkText(result, ((Emphasis) content).getContent());
            } else if (isPhraseType(content)) {
                return getLinkText(result, ((Phrase) content).getContent());
            } else if (isSuperscriptType(content)) {
                return getLinkText(result, ((Superscript) content).getContent());
            } else {
                LOGGER.warn("Not sure how to get text from: ");
                return "";
            }
        }).collect(Collectors.joining(""));

        return result + collectedText;
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
}
