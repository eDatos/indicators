package es.gobcan.istac.indicators.rest.clients;

import javax.annotation.PostConstruct;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.siemac.edatos.core.common.constants.CoreCommonConstants;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.rest.external.v1_0.service.SrmRestExternalFacadeV10;
import org.siemac.metamac.statistical_operations.rest.external.v1_0.service.StatisticalOperationsV1_0;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;

public class RestApiLocatorExternal {

    @Autowired
    private IndicatorsConfigurationService configurationService;

    private StatisticalOperationsV1_0      statisticalOperationsRestInternalFacadeV10 = null;

    private SrmRestExternalFacadeV10       srmRestExternalFacadeV10                   = null;
    private String                         apiKey;

    @PostConstruct
    public void initService() throws MetamacException {
        apiKey = configurationService.retrieveIndicatorsApiKey();
        String baseApi = configurationService.retrieveStatisticalOperationsExternalApiUrlBase();
        // true to do thread safe
        statisticalOperationsRestInternalFacadeV10 = JAXRSClientFactory.create(baseApi, StatisticalOperationsV1_0.class, null, true);

        String srmBaseApi = configurationService.retrieveSrmExternalApiUrlBase();
        srmRestExternalFacadeV10 = JAXRSClientFactory.create(srmBaseApi, SrmRestExternalFacadeV10.class, null, true);
    }

    public StatisticalOperationsV1_0 getStatisticalOperationsRestFacadeV10() {
        // reset thread context
        WebClient.client(statisticalOperationsRestInternalFacadeV10).reset();
        WebClient.client(statisticalOperationsRestInternalFacadeV10).accept("application/xml").header(CoreCommonConstants.API_KEY_PARAMETER, apiKey);

        return statisticalOperationsRestInternalFacadeV10;
    }

    public SrmRestExternalFacadeV10 getSrmRestInternalFacadeV10() {
        // reset thread context
        WebClient.client(srmRestExternalFacadeV10).reset();
        WebClient.client(srmRestExternalFacadeV10).accept("application/xml").header(CoreCommonConstants.API_KEY_PARAMETER, apiKey);

        return srmRestExternalFacadeV10;
    }
}