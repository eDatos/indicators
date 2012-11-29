package es.gobcan.istac.indicators.rest.util;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static Set<String> parseFields(String fields) {
        Set<String> result = new HashSet<String>();
        if(!org.siemac.metamac.core.common.util.shared.StringUtils.isEmpty(fields)) {
            String[] fieldsParts = fields.split(",");
            for(String fieldPart : fieldsParts) {
                result.add(fieldPart.trim());
            }
        }
        return result;
    }
}
