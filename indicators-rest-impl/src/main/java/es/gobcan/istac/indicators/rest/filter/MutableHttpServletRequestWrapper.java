package es.gobcan.istac.indicators.rest.filter;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public final class MutableHttpServletRequestWrapper extends HttpServletRequestWrapper {
    // holds custom header and value mapping
    private final Map<String, String> customHeaders;
    private String customUri;
    private StringBuffer customUrl;

    public MutableHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        customHeaders = new HashMap<>();
        customUri = request.getRequestURI();
        customUrl = request.getRequestURL();
    }

    public void putHeader(String name, String value) {
        this.customHeaders.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        // check the custom headers first
        String headerValue = customHeaders.get(name);

        if (headerValue != null) {
            return headerValue;
        }
        // else return from into the original wrapped object
        return ((HttpServletRequest) getRequest()).getHeader(name);
    }

    public Enumeration<String> getHeaderNames() {
        // create a set of the custom header names
        Set<String> set = new HashSet<>(customHeaders.keySet());

        // now add the headers from the wrapped request object
        @SuppressWarnings("unchecked") Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (e.hasMoreElements()) {
            // add the names of the request headers into the list
            String n = e.nextElement();
            set.add(n);
        }

        // create an enumeration from the set and return
        return Collections.enumeration(set);
    }

    public void removeTermination(String termination) {
        if (customUri.endsWith(termination)) {
            customUri = customUri.substring(0, customUri.length() - termination.length());
        }
        if (customUrl.toString().endsWith(termination)) {
            customUrl = new StringBuffer(customUrl.substring(0, customUrl.length() - termination.length()));
        }
    }

    @Override
    public String getRequestURI() {
        return customUri;
    }

    @Override
    public StringBuffer getRequestURL() {
        return customUrl;
    }
}