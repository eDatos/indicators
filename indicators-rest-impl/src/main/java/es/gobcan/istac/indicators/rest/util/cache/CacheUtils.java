package es.gobcan.istac.indicators.rest.util.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public final class CacheUtils {

    private CacheUtils() {
    }

    public static String findGeographicalValuesByCodesKey(List<String> codes) {
        List<String> keyCodes = new ArrayList<String>(codes);
        Collections.sort(keyCodes);
        return StringUtils.join(keyCodes, ',');
    }

}
