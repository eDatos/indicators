package es.gobcan.istac.indicators.web.diffusion.urlrewrite;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.siemac.metamac.core.common.constants.CoreCommonConstants.API_LATEST;
import static org.siemac.metamac.core.common.constants.CoreCommonConstants.URL_SEPARATOR;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.tuckey.web.filters.urlrewrite.extend.RewriteMatch;

import es.gobcan.istac.indicators.rest.IndicatorsRestConstants;

class IndicatorsRewriteMatch extends RewriteMatch {

    private Pattern              apiUrlPattern           = Pattern.compile(".*/api/indicators/(v\\d+\\.\\d+|" + API_LATEST + ")(/(.*)?)?");
    private Pattern              swaggerResourcesPattern = Pattern.compile(".*/api/indicators(/apidocs/.*)");

    private ConfigurationService configurationService    = null;

    @Override
    public boolean execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        Matcher apiUrlmatcher = apiUrlPattern.matcher(requestURI);
        if (apiUrlmatcher.matches() && apiUrlmatcher.groupCount() > 2) {
            String requestApiVersion = apiUrlmatcher.group(1);
            String requestPathAfterVersion = apiUrlmatcher.group(2);
            if (API_LATEST.equals(requestApiVersion)) {
                String location = buildTargetLocation(IndicatorsRestConstants.API_VERSION_1_0, requestPathAfterVersion, queryString);
                response.sendRedirect(location);
                return true;
            } else if (requestPathAfterVersion == null && requestURI.endsWith(requestApiVersion) && isBlank(queryString)) {
                String location = buildTargetLocation(requestApiVersion, requestPathAfterVersion, queryString);
                response.sendRedirect(location);
                return true;
            }
        }
        Matcher swaggerResourcesMatcher = swaggerResourcesPattern.matcher(requestURI);
        if (swaggerResourcesMatcher.matches() && apiUrlmatcher.groupCount() > 1) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(swaggerResourcesMatcher.group(2));
            requestDispatcher.forward(request, response);
            return true;
        }
        return false;
    }

    private String buildTargetLocation(String apiVersion, String requestPathAfterVersion, String queryString) throws ServletException {
        return getApiBaseUrl() + URL_SEPARATOR + apiVersion + (requestPathAfterVersion == null ? URL_SEPARATOR : requestPathAfterVersion) + (isBlank(queryString) ? EMPTY : "?" + queryString);
    }

    private String getApiBaseUrl() throws ServletException {
        try {
            return getConfigurationService().retrieveIndicatorsExternalApiUrlBase();
        } catch (MetamacException e) {
            throw new ServletException("Error retrieving configuration property of the external API URL base", e);
        }
    }

    private ConfigurationService getConfigurationService() {
        if (configurationService == null) {
            configurationService = ApplicationContextProvider.getApplicationContext().getBean(ConfigurationService.class);
        }
        return configurationService;
    }
}
