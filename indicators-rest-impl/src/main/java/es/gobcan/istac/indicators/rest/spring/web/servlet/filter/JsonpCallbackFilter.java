package es.gobcan.istac.indicators.rest.spring.web.servlet.filter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonpCallbackFilter implements Filter {

    private static final Logger LOG           = LoggerFactory.getLogger(JsonpCallbackFilter.class);
    private static final String CALBACK_PARAM = "_callback";

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        @SuppressWarnings("unchecked")
        Map<String, String[]> parms = httpRequest.getParameterMap();

        if (parms.containsKey(CALBACK_PARAM)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Wrapping response with JSONP callback '" + parms.get(CALBACK_PARAM)[0] + "'");
            }

            OutputStream out = httpResponse.getOutputStream();

            GenericResponseWrapper wrapper = new GenericResponseWrapper(httpResponse);

            chain.doFilter(request, wrapper);

            byte[] startCallbackFunction = new String(parms.get(CALBACK_PARAM)[0] + "(").getBytes();
            byte[] endCallbackFunction = new String(");").getBytes();
            wrapper.setContentLength(startCallbackFunction.length + wrapper.getData().length + endCallbackFunction.length);

            out.write(startCallbackFunction);
            out.write(wrapper.getData());
            out.write(endCallbackFunction);

            wrapper.setContentType("text/javascript;charset=UTF-8");

            out.close();
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}