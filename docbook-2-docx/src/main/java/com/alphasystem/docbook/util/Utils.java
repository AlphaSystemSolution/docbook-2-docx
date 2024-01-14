package com.alphasystem.docbook.util;

import com.alphasystem.SystemException;
import com.alphasystem.util.AppUtil;
import com.alphasystem.util.nio.NIOFileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;

public class Utils {

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
