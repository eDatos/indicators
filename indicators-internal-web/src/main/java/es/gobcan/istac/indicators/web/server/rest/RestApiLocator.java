package es.gobcan.istac.indicators.web.server.rest;

import javax.annotation.PostConstruct;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.statistical.operations.rest.internal.v1_0.service.StatisticalOperationsRestFacadeV10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestApiLocator {

    @Autowired
    private ConfigurationService               configurationService;

    private StatisticalOperationsRestFacadeV10 statisticalOperationsRestFacadeV10 = null;

    @PostConstruct
    public void initService() throws Exception {
        String baseApi = configurationService.getProperties().getProperty(RestApiConstants.STATISTICAL_OPERATIONS_REST_INTERNAL);
        statisticalOperationsRestFacadeV10 = JAXRSClientFactory.create(baseApi, StatisticalOperationsRestFacadeV10.class, null, true); // true to do thread safe
    }

    public StatisticalOperationsRestFacadeV10 getStatisticalOperationsRestFacadeV10() {
        // reset thread context
        WebClient.client(statisticalOperationsRestFacadeV10).reset();
        WebClient.client(statisticalOperationsRestFacadeV10).accept("application/xml");

        return statisticalOperationsRestFacadeV10;
    }
}