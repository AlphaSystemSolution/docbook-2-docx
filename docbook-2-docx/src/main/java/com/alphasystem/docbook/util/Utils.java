package com.alphasystem.docbook.util;

import com.alphasystem.util.AppUtil;
import com.alphasystem.util.nio.NIOFileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private Utils() {
    }

    public static List<File> readResource(String resourceName) throws IOException {
        final var files = new ArrayList<File>();
        File file = null;
        final var resources = AppUtil.getResources(resourceName);
        if (resources.hasMoreElements()) {
            final var url = resources.nextElement();
            try (InputStream ins = url.openStream()) {
                file = Files.createTempFile("system-defaults-", ".properties").toFile();
                file.deleteOnExit();
                files.add(file);
                final var os = new FileOutputStream(file);
                NIOFileUtils.fastCopy(ins, os);
                os.close();
            }
        }
        return files;
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
