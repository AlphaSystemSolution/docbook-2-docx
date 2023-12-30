package com.alphasystem.docbook.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Utils {

    private Utils() {
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
