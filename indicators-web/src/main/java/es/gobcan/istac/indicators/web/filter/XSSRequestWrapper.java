package es.gobcan.istac.indicators.web.filter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XSSRequestWrapper extends HttpServletRequestWrapper {

    public static final String BLANK  = " ";

    protected Logger           logger = LoggerFactory.getLogger(XSSRequestWrapper.class);

    public XSSRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);

        if (values == null) {
            return values;
        }

        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = stripXSS(values[i]);
        }

        return encodedValues;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);

        return stripXSS(value);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return stripXSS(value);
    }

    private String stripXSS(String value) {
        String stripXSS = value;

        if (StringUtils.isBlank(stripXSS)) {
            return null;
        }

        // Avoid null characters
        stripXSS = stripXSS.replaceAll("\0", "");

        // Allow json on params
        if (stripXSS.startsWith("{") && isValidJSON(stripXSS)) {
            return stripXSS;
        }

        // Avoid HTML events
        stripXSS = replaceInside(HtmlEventsEnum.getPattern(), stripXSS);
        stripXSS = replaceInside(ForbiddenTagsEnum.getPattern(), stripXSS);

        // Clean out HTML
        int countLeadingWithespace = countLeadingWithespace(stripXSS);
        stripXSS = Jsoup.clean(stripXSS, Whitelist.none());
        stripXSS = applyLeadingWhitspaces(stripXSS, countLeadingWithespace); // Preserve whitespaces

        if (StringUtils.isBlank(stripXSS)) {
            return null;
        }

        return stripXSS;
    }

    public static String replaceInside(Pattern pattern, String stripXSS) {
        Matcher matcher = pattern.matcher(stripXSS);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            int previousChar = matcher.start() - 1;
            int nextChar = matcher.end();

            boolean needreplace = false;
            //@formatter:off
            if (
                    ((previousChar < 0) || (!StringUtils.isAlphanumeric(StringUtils.EMPTY + stripXSS.charAt(previousChar))))
                &&
                    ((nextChar >= stripXSS.length()) || (!StringUtils.isAlphanumeric(StringUtils.EMPTY + stripXSS.charAt(nextChar))))
                ){
                needreplace = true;
            }
            //@formatter:on

            if (needreplace) {
                matcher.appendReplacement(result, StringUtils.EMPTY); // Delete region matched
            } else {
                // result.append(matcher.group());
            }
        }
        matcher.appendTail(result);
        return result.toString();
    }

    private String applyLeadingWhitspaces(String str, int count) {
        for (int i = 0; i < count; i++) {
            str = BLANK + str;
        }
        return str;
    }

    private int countLeadingWithespace(String str) {
        if (str == null || (str.length()) == 0) {
            return 0;
        }

        int start = 0;
        int strLen = str.length();
        while ((start != strLen) && Character.isWhitespace(str.charAt(start))) {
            start++;
        }
        return start;
    }

    public boolean isValidJSON(final String json) {
        boolean valid = false;
        try {
            final JsonParser parser = new ObjectMapper().getJsonFactory().createJsonParser(json);
            while (parser.nextToken() != null) {
            }
            valid = true;
        } catch (JsonParseException jpe) {
            jpe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return valid;
    }
}