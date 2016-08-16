package es.gobcan.istac.indicators.rest.component;

import javax.annotation.PostConstruct;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.utils.RestUtils;
import es.gobcan.istac.indicators.rest.IndicatorsRestConstants;

public class UriLinksInternal extends UriLinks {
    
    @Override
    @PostConstruct
    public void init() throws MetamacException {
        String indicatorsApiInternalEndpoint = configurationService.retrieveIndicatorsInternalApiUrlBase();
        indicatorsApiEndpointV10 = RestUtils.createLink(indicatorsApiInternalEndpoint, IndicatorsRestConstants.API_VERSION_1_0);
    }
}
