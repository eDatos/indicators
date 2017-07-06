package es.gobcan.istac.indicators.web.filter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Automatically determines the encoding of the request parameters, as given in
 * the query string.
 * <p>
 * If the query string of a request cannot be decoded, the filter chain is not processed further, but a status code of 400 is returned instead.
 * <p>
 * The filter can be configured using the following parameters:
 * <ul>
 * <li>{@code encodings}: The comma-separated list of encodings (see {@link Charset#forName(String)}) that are tried in order. The first one that can decode the complete query string is taken.
 * <p>
 * Default value: {@code UTF-8}
 * <p>
 * Example: {@code UTF-8,EUC-KR,ISO-8859-15}.
 * <li>{@code inputEncodingParameterName}: When this parameter is defined and a query parameter of that name is provided by the client, and that parameter's value contains only non-escaped characters
 * and the server knows an encoding of that name, then it is used exclusively, overriding the {@code encodings} parameter for this request.
 * <p>
 * Default value: {@code null}
 * <p>
 * Example: {@code ie} (as used by Google).
 * </ul>
 */
public class URIEncodingFilter implements Filter {

    protected Log               log                        = LogFactory.getLog(URIEncodingFilter.class);

    private String              inputEncodingParameterName = null;
    private final List<Charset> encodings                  = new ArrayList<Charset>();

    @Override
    public void init(FilterConfig config) throws ServletException {
        String encodingsStr = "UTF-8";

        Enumeration<String> en = config.getInitParameterNames();
        while (en.hasMoreElements()) {
            final String name = en.nextElement();
            final String value = config.getInitParameter(name);
            if (name.equals("encodings")) {
                encodingsStr = value;
            } else if (name.equals("inputEncodingParameterName")) {
                inputEncodingParameterName = value;
            } else {
                throw new IllegalArgumentException("Unknown parameter: " + name);
            }
        }

        for (String encoding : encodingsStr.split(",\\s*")) {
            Charset charset = Charset.forName(encoding);
            encodings.add(charset);
        }
    }

    @Override
    public void doFilter(ServletRequest sreq, ServletResponse sres, FilterChain fc) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) sreq;
        final HttpServletResponse res = (HttpServletResponse) sres;

        Map<String, String[]> params;
        try {
            params = Util.decodeQueryString(req.getQueryString(), encodings, inputEncodingParameterName);
        } catch (IOException e) {
            log.debug(e);
            res.sendError(400, e.getMessage());
            return;
        }
        HttpServletRequest wrapper = new ParametersWrapper(req, params);
        fc.doFilter(wrapper, res);
    }

    @Override
    public void destroy() {
        // nothing to do
    }

    static class Util {

        private Util() {

        }

        static CharsetDecoder strictDecoder(Charset cs) {
            CharsetDecoder dec = cs.newDecoder();
            dec.onMalformedInput(CodingErrorAction.REPORT);
            dec.onUnmappableCharacter(CodingErrorAction.REPORT);
            return dec;
        }

        static int[] toCodePoints(String str) {
            final int len = str.length();
            int[] codePoints = new int[len];
            int i = 0, j = 0;
            while (i < len) {
                int cp = Character.codePointAt(str, i);
                codePoints[j++] = cp;
                i += Character.charCount(cp);
            }
            return j == len ? codePoints : ArrayUtils.subarray(codePoints, 0, len);
        }

        private static int hexValue(char c) {
            if ('0' <= c && c <= '9') {
                return c - '0';
            }
            if ('A' <= c && c <= 'F') {
                return 10 + c - 'A';
            }
            if ('a' <= c && c <= 'f') {
                return 10 + c - 'a';
            }
            return -1;
        }

        public static String decodeUrl(String encoded, CharsetDecoder decoder) throws IOException {
            byte[] bytes = new byte[encoded.length()];
            int bytescount = 0;

            for (int i = 0; i < encoded.length(); i++) {
                char c = encoded.charAt(i);
                if (c == '%') {
                    if (!(i + 2 < encoded.length())) {
                        throw new IOException("Invalid hex sequence at the end: " + encoded.substring(i));
                    }

                    int hi = hexValue(encoded.charAt(i + 1));
                    int lo = hexValue(encoded.charAt(i + 2));
                    if (hi == -1 || lo == -1) {
                        throw new IOException("Invalid hex sequence: " + encoded.substring(i, i + 3));
                    }

                    bytes[bytescount++] = (byte) (16 * hi + lo);
                    i += 2;
                    continue;
                }

                if (c == '+') {
                    bytes[bytescount++] = '\u0020';
                    continue;
                }

                if (!('\u0020' <= c && c <= '\u007E')) {
                    throw new IOException("Invalid character: #" + (int) c);
                }
                bytes[bytescount++] = (byte) c;
            }

            CharBuffer cbuf = decoder.decode(ByteBuffer.wrap(bytes, 0, bytescount));
            return cbuf.toString();
        }

        static String ensureDefinedUnicode(String s) throws IOException {
            for (int cp : toCodePoints(s)) {
                if (!Character.isDefined(cp)) {
                    throw new IOException("Undefined unicode code point: " + cp);
                }
            }
            return s;
        }

        static Map<String, String[]> decodeQueryString(String queryString, List<Charset> charsets, String ieName) throws IOException {
            List<String> queryNames = new ArrayList<String>();
            List<String> queryValues = new ArrayList<String>();

            Map<String, String[]> params = new LinkedHashMap<String, String[]>();
            if (queryString == null) {
                return params;
            }

            Charset ie = null;
            for (String pair : queryString.split("&")) {
                int eq = pair.indexOf('=');
                String name = eq == -1 ? pair : pair.substring(0, eq);
                String value = eq == -1 ? "" : pair.substring(eq + 1);
                queryNames.add(name);
                queryValues.add(value);
                if (name.equals(ieName) && value.indexOf('%') == -1) {
                    try {
                        ie = Charset.forName(value);
                    } catch (IllegalCharsetNameException e) {
                        throw new IOException("Illegal input encoding name: " + value);
                    } catch (UnsupportedCharsetException e) {
                        throw new IOException("Unsupported input encoding: " + value);
                    }
                }
            }

            Charset[] css = (ie != null) ? new Charset[]{ie} : charsets.toArray(new Charset[charsets.size()]);
            for (Charset charset : css) {
                try {
                    params.clear();
                    CharsetDecoder decoder = strictDecoder(charset);
                    for (int i = 0; i < queryNames.size(); i++) {
                        String name = ensureDefinedUnicode(Util.decodeUrl(queryNames.get(i), decoder));
                        String value = ensureDefinedUnicode(Util.decodeUrl(queryValues.get(i), decoder));
                        String[] oldValues = params.get(name);
                        String[] newValues;
                        if (oldValues == null) {
                            newValues = new String[1];
                        } else {
                            newValues = (String[]) ArrayUtils.add(oldValues, "0");
                        }
                        newValues[newValues.length - 1] = value;
                        params.put(name, newValues);
                    }
                    return params;
                } catch (IOException e) {
                    continue;
                }
            }
            throw new IOException("Could not decode the query string: " + queryString);
        }
    }

    @SuppressWarnings("unchecked")
    static class ParametersWrapper extends HttpServletRequestWrapper {

        private final Map<String, String[]> queryParams;

        public ParametersWrapper(HttpServletRequest request, Map<String, String[]> queryParams) {
            super(request);
            this.queryParams = queryParams;
        }

        @Override
        public String getParameter(String name) {
            String[] values = queryParams.get(name);
            if (values != null) {
                if (values.length == 0) {
                    return StringUtils.EMPTY;
                }
                return values[0];
            } else {
                return super.getParameter(name);
            }
        }

        @Override
        public Map getParameterMap() {
            queryParams.putAll(super.getParameterMap());
            return Collections.unmodifiableMap(queryParams);
        }

        @Override
        public String[] getParameterValues(String name) {
            return queryParams.get(name) != null ? queryParams.get(name) : super.getParameterValues(name);
        }

    }
}