package com.alphasystem.xml;

import org.docbook.model.*;

import java.util.function.Function;

public class UnmarshallerUtils {

    private UnmarshallerUtils() {
    }

    public static Integer toInt(String value, int defaultValue) {
        final var intValue = convert(value, Integer::parseInt);
        return intValue == null ? defaultValue : intValue;
    }

    public static Frame toFrame(String value) {
        return convert(value, Frame::fromValue);
    }

    public static Choice toChoice(String value) {
        return convert(value, Choice::fromValue);
    }

    public static Align toAlign(String value) {
        return convert(value, Align::fromValue);
    }

    public static BasicVerticalAlign toBasicVerticalAlign(String value) {
        return convert(value, BasicVerticalAlign::fromValue);
    }

    public static VerticalAlign toVerticalAlign(String value) {
        return convert(value, VerticalAlign::fromValue);
    }

    public static Numeration toNumeration(String value, Numeration defaultValue) {
        final var numeration = convert(value, Numeration::fromValue);
        return numeration == null ? defaultValue : numeration;
    }

    private static <T> T convert(String value, Function<String, T> toTarget) {
        if (value == null) {
            return null;
        }
        try {
            return toTarget.apply(value);
        } catch (Exception ex) {
            return null;
        }
    }

}
