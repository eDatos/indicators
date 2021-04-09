package es.gobcan.istac.indicators.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;

public class FreeMarkerUtil {

    private static final String UTF8_HTML_ENCODING = "UTF-8";

    public static String importHTMLFromUrl(String stringUrl) throws UnsupportedEncodingException, IOException {
        if (StringUtils.isEmpty(stringUrl)) {
            return StringUtils.EMPTY;
        }

        URL url = new URL(stringUrl);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), UTF8_HTML_ENCODING))) {
            for (String line; (line = reader.readLine()) != null;) {
                builder.append(line);
            }
        }
        return builder.toString();
    }

}
