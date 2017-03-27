package es.gobcan.istac.indicators.web.filter;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public enum ForbiddenTagsEnum {

    // @formatter:off
    SCRIPT("SCRIPT"),
    EMBED("EMBED"),
    OBJECT("OBJECT"),
    LAYER("LAYER"),
    STYLE("STYLE"),
    META("META"),
    IFRAME("IFRAME"),
    FRAME("FRAME"),
    LINK("LINK"),
    IMPORT("IMPORT"),
    XML("XML");
    // @formatter:on

    private String                                      field;

    private static final Map<String, ForbiddenTagsEnum> LOOKUP_MAP   = new HashMap<String, ForbiddenTagsEnum>();
    private static final Set<String>                    CONTAINS_SET = new HashSet<String>();
    private static String                               REGEXP       = null;
    private static Pattern                              PATTERN      = null;

    static {
        for (ForbiddenTagsEnum s : EnumSet.allOf(ForbiddenTagsEnum.class)) {
            String key = s.field.toLowerCase();
            LOOKUP_MAP.put(key, s);
            CONTAINS_SET.add(key);
            if (REGEXP == null) {
                REGEXP = key;
            } else {
                REGEXP += "|" + key;
            }
        }
        PATTERN = Pattern.compile("(?i)(?:" + REGEXP + ")");
    }

    public static ForbiddenTagsEnum fromEventString(String campo) {
        return LOOKUP_MAP.get(campo.toLowerCase());
    }

    public static boolean containsField(String campo) {
        return CONTAINS_SET.contains(campo.toLowerCase());
    }

    public static String getRegexp() {
        return REGEXP;
    }

    public static Pattern getPattern() {
        return PATTERN;
    }

    private ForbiddenTagsEnum(String event) {
        field = event;
    }

}