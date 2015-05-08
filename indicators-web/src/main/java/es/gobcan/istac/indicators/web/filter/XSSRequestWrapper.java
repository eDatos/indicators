package es.gobcan.istac.indicators.web.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class XSSRequestWrapper extends HttpServletRequestWrapper {

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

    private String stripXSS(String value) {
        if (value == null) {
            return null;
        }

        String stripXSS = value;

        // Avoid null characters
        stripXSS = stripXSS.replaceAll("\0", "");

        // Avoid HTML events
        stripXSS = stripXSS.replaceAll("(?i)" + HtmlEventsEnum.getRegexp(), StringUtils.EMPTY);
        stripXSS = stripXSS.replaceAll("(?i)" + ForbiddenTagsEnum.getRegexp(), StringUtils.EMPTY);
        
        // Clean out HTML
        if (stripXSS.startsWith("{") && isValidJSON(stripXSS)){ 
            return stripXSS;
        }
        stripXSS = Jsoup.clean(stripXSS, Whitelist.none());

        return stripXSS;
    }
}
