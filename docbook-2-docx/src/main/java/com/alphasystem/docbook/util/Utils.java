package com.alphasystem.docbook.util;

import com.alphasystem.commons.util.AppUtil;
import org.docbook.model.Emphasis;
import org.docbook.model.Phrase;
import org.docbook.model.Superscript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.alphasystem.xml.UnmarshallerConstants.*;

public class Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger("com.alphasystem.docbook");

    private Utils() {
    }


    public static String getId(Object source) {
        return (String) AppUtil.invokeMethod(source, "getId");
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
}
