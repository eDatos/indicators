package es.gobcan.istac.indicators.rest.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

public final class RequestUtil {

    private RequestUtil() {
    }

    private static final String SPACE = " ";
    private static final String PLUS  = "+";

    @SuppressWarnings("unchecked")
    public static Map<String, List<String>> parseParamExpression(String paramExpression) {
        if (StringUtils.isBlank(paramExpression)) {
            return MapUtils.EMPTY_MAP;
        }

        // dimExpression =
        // MOTIVOS_ESTANCIA[000|001|002]:ISLAS_DESTINO_PRINCIPAL[005|006]
        // Pattern patternDimension = Pattern.compile("(\\w+)\\[((\\w\\|?)+)\\]");
        // Pattern patternCode = Pattern.compile("(\\w+)\\|?");
        // Pattern patternCode = Pattern.compile("(" + SDMXCommonRegExpV2_1.OBSERVATIONAL_TIME_PERIOD + "|" + SDMXCommonRegExpV2_1.IDTYPE + "\\|?)");
        // Pattern patternDimension = Pattern.compile("(\\w+)\\[" + "((" + SDMXCommonRegExpV2_1.OBSERVATIONAL_TIME_PERIOD + "|" + SDMXCommonRegExpV2_1.IDTYPE + "\\|?" + ")+)" + "\\]");
        Pattern patternDimension = getPatternDimension();
        Pattern patternCode = getPatternCode();

        Matcher matcherDimension = patternDimension.matcher(paramExpression);

        Map<String, List<String>> selectedDimension = new HashMap<String, List<String>>();
        while (matcherDimension.find()) {
            String dimIdentifier = matcherDimension.group(1);
            String codes = matcherDimension.group(2);

            List<String> codeDimensions = selectedDimension.get(dimIdentifier);

            if (codeDimensions == null) {
                codeDimensions = new ArrayList<>();
                selectedDimension.put(dimIdentifier, codeDimensions);
            }

            codeDimensions.addAll(parseCodes(patternCode, codes));
        }
        return selectedDimension;
    }

    public static Set<String> parseFields(String fields) {
        Set<String> result = new HashSet<String>();
        if (!StringUtils.isEmpty(fields)) {
            String[] fieldsParts = fields.split(",");
            for (String fieldPart : fieldsParts) {
                // WORKAROUND: INDISTAC-899
                if (fieldPart.startsWith(SPACE)) {
                    fieldPart = fieldPart.replace(SPACE, PLUS);
                }
                result.add(fieldPart.trim());
            }
        }
        return result;
    }

    protected static Pattern getPatternCode() {
        return Pattern.compile("^(" + ExpandedSDMXCommonRegExpV2_1.OBSERVATIONAL_TIME_PERIOD + "|" + ExpandedSDMXCommonRegExpV2_1.IDTYPE + ")" + "$");
    }

    protected static Pattern getPatternDimension() {
        return Pattern.compile("(\\w+)\\[(.*+)\\]");
    }

    public static List<String> parseCodes(Pattern patternCode, String codes) {
        List<String> codeDimensions = new ArrayList<>();

        if (!StringUtils.isBlank(codes)) {
            List<String> splittedCodes = Arrays.asList(StringUtils.split(codes, "|"));

            for (String splittedCode : splittedCodes) {
                Matcher matcherCode = patternCode.matcher(splittedCode);
                while (matcherCode.find()) {
                    codeDimensions.add(matcherCode.group(1));
                }
            }
        }
        return codeDimensions;
    }
}
