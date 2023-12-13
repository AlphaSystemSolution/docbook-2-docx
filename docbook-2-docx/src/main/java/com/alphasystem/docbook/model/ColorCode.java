package com.alphasystem.docbook.model;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author sali
 */
public enum ColorCode {

    AQUA("aqua", "00FFFF"),
    BLACK("black", "000000"),
    BLUE("blue", "0000FF"),
    FUCHSIA("fuchsia", "FF0080"),
    GRAY("gray", "808080"),
    GREY("grey", "808080"),
    GREEN("green", "00FF00"),
    LIME("lime", "32CD32"),
    MAROON("maroon", "800000"),
    NAVY("navy", "000080"),
    OLIVE("olive", "808000"),
    PURPLE("purple", "800080"),
    RED("red", "FF0000"),
    SILVER("silver", "C0C0C0"),
    TEAL("teal", "008080"),
    WHITE("white", "FFFFFF"),
    YELLOW("yellow", "FFFF00");

    private static Map<String, ColorCode> codeMap = new LinkedHashMap<>();
    private static Map<String, ColorCode> nameMap = new LinkedHashMap<>();

    static {
        for (ColorCode colorCode : values()) {
            codeMap.put(colorCode.getCode(), colorCode);
            nameMap.put(colorCode.getName(), colorCode);
        }
    }

    public static ColorCode getByCode(String code) {
        return isBlank(code) ? null : codeMap.get(code);
    }

    public static ColorCode getByName(String name) {
        return isBlank(name) ? null : nameMap.get(name);
    }

    private final String name;
    private final String code;

    ColorCode(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
