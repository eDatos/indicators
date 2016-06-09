package es.gobcan.istac.indicators.web.diffusion.urlrewrite;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.siemac.metamac.core.common.constants.CoreCommonConstants.API_LATEST;
import static org.siemac.metamac.core.common.constants.CoreCommonConstants.URL_SEPARATOR;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.tuckey.web.filters.urlrewrite.extend.RewriteMatch;

import es.gobcan.istac.indicators.rest.IndicatorsRestConstants;

class IndicatorsRewriteMatch extends RewriteMatch {

    private Pattern              urlPattern           = Pattern.compile(".*/api/indicators/" + API_LATEST + "(/(.*)?)?");

    private ConfigurationService configurationService = null;

    @Override
    public boolean execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = ((HttpServletRequest) request).getRequestURI();
        String queryString = ((HttpServletRequest) request).getQueryString();
        Matcher matcher = urlPattern.matcher(requestURI);
        if (matcher.matches() && matcher.groupCount() > 1) {
            String requestPathAfterVersion = matcher.group(1);
            String location = buildTargetLocation(IndicatorsRestConstants.API_VERSION_1_0, requestPathAfterVersion, queryString);
            response.sendRedirect(location);
        }
        return true;
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
