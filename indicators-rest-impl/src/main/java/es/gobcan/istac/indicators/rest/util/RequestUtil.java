package es.gobcan.istac.indicators.rest.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

public class RequestUtil {

    @SuppressWarnings("unchecked")
    public static Map<String, List<String>> parseParamExpression(String paramExpression) {
        if (StringUtils.isBlank(paramExpression)) {
            return MapUtils.EMPTY_MAP;
        }

        // dimExpression = MOTIVOS_ESTANCIA[000|001|002]:ISLAS_DESTINO_PRINCIPAL[005|006]
        Pattern patternDimension = Pattern.compile("(\\w+)\\[((\\w\\|?)+)\\]");
        Pattern patternCode = Pattern.compile("(\\w+)\\|?");

        Matcher matcherDimension = patternDimension.matcher(paramExpression);

        Map<String, List<String>> selectedDimension = new HashMap<String, List<String>>();
        while (matcherDimension.find()) {
            String dimIdentifier = matcherDimension.group(1);
            String codes = matcherDimension.group(2);
            Matcher matcherCode = patternCode.matcher(codes);
            while (matcherCode.find()) {
                List<String> codeDimensions = selectedDimension.get(dimIdentifier);
                if (codeDimensions == null) {
                    codeDimensions = new ArrayList<String>();
                    selectedDimension.put(dimIdentifier, codeDimensions);
                }
                String codeIdentifier = matcherCode.group(1);
                codeDimensions.add(codeIdentifier);
            }
        }
        return selectedDimension;
    }
}
