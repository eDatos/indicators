package es.gobcan.istac.indicators.rest.component;

import javax.annotation.PostConstruct;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.utils.RestUtils;
import es.gobcan.istac.indicators.rest.IndicatorsRestConstants;

public class UriLinksExternal extends UriLinks {
    
    @Override
    @PostConstruct
    public void init() throws MetamacException {
        String indicatorsApiExternalEndpoint = configurationService.retrieveIndicatorsExternalApiUrlBase();
        indicatorsApiEndpointV10 = RestUtils.createLink(indicatorsApiExternalEndpoint, IndicatorsRestConstants.API_VERSION_1_0);
    }
}
