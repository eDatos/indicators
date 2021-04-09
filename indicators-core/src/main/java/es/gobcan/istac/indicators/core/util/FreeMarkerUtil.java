package es.gobcan.istac.indicators.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;

/* Even when we opted for this approach, there are others if neccesary, see
 * http://freemarker.624813.n4.nabble.com/can-I-include-quot-http-some-page-way-over-here-html-quot-td624983.html
 */
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
