package es.gobcan.istac.indicators.web.diffusion.urlrewrite;

import javax.servlet.ServletException;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.core.common.util.urlrewrite.AbstractRewriteMatch;

import es.gobcan.istac.indicators.rest.IndicatorsRestConstants;

class IndicatorsRewriteMatch extends AbstractRewriteMatch {

    private ConfigurationService configurationService = null;

    @Override
    protected String[] getAcceptedApiPrefixes() {
        return new String[]{"indicators"};
    }

    @Override
    protected String getLatestApiVersion() {
        return IndicatorsRestConstants.API_VERSION_1_0;
    }

    @Override
    protected String getApiBaseUrl() throws ServletException {
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
