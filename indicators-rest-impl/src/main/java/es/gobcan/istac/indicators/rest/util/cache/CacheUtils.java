package es.gobcan.istac.indicators.rest.util.cache;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CacheUtils {

    public static String findGeographicalValuesByCodesKey(List<String> codes) {
        List<String> keyCodes = new ArrayList<String>(codes);
        Collections.sort(keyCodes);
        return StringUtils.join(keyCodes, ',');
    }

}
