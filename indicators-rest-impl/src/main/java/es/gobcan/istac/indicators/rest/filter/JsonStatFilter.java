package es.gobcan.istac.indicators.rest.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonStatFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonStatFilter.class);
    public static final String JSONSTAT_MIME_TYPE = "application/jsonstat+json";
    public static final String REQUEST_PARAMETER_TYPE = "_type";
    public static final String JSONSTAT_PARAMETER_VALUE = "jsonstat";
    public static final String JSONSTAT_URL_TERMINATION = ".jsonstat";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        String acceptType = request.getParameter(REQUEST_PARAMETER_TYPE);
        if (acceptType != null && acceptType.equals(JSONSTAT_PARAMETER_VALUE)) {
            LOGGER.debug("JSON-stat MIME type detected on query parameter");
            request = getRequestWrapperForParameter(request);
        }

        String url = ((HttpServletRequest) request).getRequestURL().toString();
        if (url.endsWith(JSONSTAT_URL_TERMINATION)) {
            LOGGER.debug("JSON-stat termination detected on url");
            request = getRequestWrapperForUrl(request);
        }

        chain.doFilter(request, response);
    }

    private ServletRequest getRequestWrapperForParameter(ServletRequest request) {
        MutableHttpServletRequestWrapper wrapper = new MutableHttpServletRequestWrapper((HttpServletRequest) request);
        wrapper.putHeader("Accept", JSONSTAT_MIME_TYPE);
        return wrapper;
    }

    private ServletRequest getRequestWrapperForUrl(ServletRequest request) {
        MutableHttpServletRequestWrapper wrapper = new MutableHttpServletRequestWrapper((HttpServletRequest) request);
        wrapper.putHeader("Accept", JSONSTAT_MIME_TYPE);
        wrapper.removeTermination(JSONSTAT_URL_TERMINATION);
        return wrapper;
    }

    @Override
    public void destroy() {
    }
}