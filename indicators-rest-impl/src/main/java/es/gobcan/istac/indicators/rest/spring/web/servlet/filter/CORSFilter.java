package es.gobcan.istac.indicators.rest.spring.web.servlet.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Enabling CORS support - Access-Control-Allow-Origin
 * 
    <!-- Add this to your web.xml to enable "CORS" -->
    <filter>
      <filter-name>CORSFilter</filter-name>
      <filter-class>es.gobcan.istac.indicators.rest.spring.web.servlet.filter.CORSFilter</filter-class>
    </filter>
      
    <filter-mapping>
      <filter-name>CORSFilter</filter-name>
      <url-pattern>/api/*</url-pattern>
    </filter-mapping>
 * </code>
 */
// https://gist.github.com/zeroows/80bbe076d15cb8a4f0ad
public class CORSFilter extends OncePerRequestFilter {

    private static final Log LOG = LogFactory.getLog(CORSFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");

        if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(request.getMethod())) {
            LOG.trace("Sending Header....");
            // CORS "pre-flight" request
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type");
            response.addHeader("Access-Control-Max-Age", "1");
        }

        filterChain.doFilter(request, response);
    }

}