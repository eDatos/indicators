package es.gobcan.istac.indicators.rest.clients;

import javax.annotation.PostConstruct;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical_operations.rest.internal.v1_0.service.StatisticalOperationsRestInternalFacadeV10;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;

public class RestApiLocatorInternal {

    @Autowired
    private IndicatorsConfigurationService             configurationService;

    private StatisticalOperationsRestInternalFacadeV10 statisticalOperationsRestInternalFacadeV10 = null;

    @PostConstruct
    public void initService() throws MetamacException {
        String baseApi = configurationService.retrieveStatisticalOperationsInternalApiUrlBase();
        // true to do thread safe
        statisticalOperationsRestInternalFacadeV10 = JAXRSClientFactory.create(baseApi, StatisticalOperationsRestInternalFacadeV10.class, null, true);
    }

    public StatisticalOperationsRestInternalFacadeV10 getStatisticalOperationsRestFacadeV10() {
        // reset thread context
        WebClient.client(statisticalOperationsRestInternalFacadeV10).reset();
        WebClient.client(statisticalOperationsRestInternalFacadeV10).accept("application/xml");

        return statisticalOperationsRestInternalFacadeV10;
    }
}