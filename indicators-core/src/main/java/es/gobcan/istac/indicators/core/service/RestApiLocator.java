package es.gobcan.istac.indicators.core.service;

import javax.annotation.PostConstruct;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.notices.rest.internal.v1_0.service.NoticesV1_0;
import org.siemac.metamac.statistical_operations.rest.internal.v1_0.service.StatisticalOperationsRestInternalFacadeV10;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.service.StatisticalResourcesV1_0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestApiLocator {

    private static final int                           STATISTICAL_RESOURCES_EXTERNAL_API_RECEIVE_TIMEOUT_VALUE = 300000;

    @Autowired
    private ConfigurationService                       configurationService;

    private StatisticalOperationsRestInternalFacadeV10 statisticalOperationsRestInternalFacadeV10               = null;

    private NoticesV1_0                                noticesV10                                               = null;

    private StatisticalResourcesV1_0                   statisticalResourcesRestExternalFacacadeV10              = null;

    @PostConstruct
    public void initService() throws MetamacException {
        String statisticalOperationsApiUrlBase = configurationService.retrieveStatisticalOperationsInternalApiUrlBase();
        statisticalOperationsRestInternalFacadeV10 = JAXRSClientFactory.create(statisticalOperationsApiUrlBase, StatisticalOperationsRestInternalFacadeV10.class, null, true); // true to do thread safe

        String noticesApiUrlBase = configurationService.retrieveNoticesInternalApiUrlBase();
        noticesV10 = JAXRSClientFactory.create(noticesApiUrlBase, NoticesV1_0.class, null, true); // true to do thread safe

        String statisticalResourcesExternalApiUrlBase = configurationService.retrieveStatisticalResourcesExternalApiUrlBase();
        statisticalResourcesRestExternalFacacadeV10 = JAXRSClientFactory.create(statisticalResourcesExternalApiUrlBase, StatisticalResourcesV1_0.class, null, true); // true to do thread safe
        setReceiveTimeout(statisticalResourcesRestExternalFacacadeV10, RestApiLocator.STATISTICAL_RESOURCES_EXTERNAL_API_RECEIVE_TIMEOUT_VALUE);
    }

    public StatisticalOperationsRestInternalFacadeV10 getStatisticalOperationsRestFacadeV10() {
        // reset thread context
        WebClient.client(statisticalOperationsRestInternalFacadeV10).reset();
        WebClient.client(statisticalOperationsRestInternalFacadeV10).accept("application/xml");

        return statisticalOperationsRestInternalFacadeV10;
    }

    public StatisticalResourcesV1_0 getStatisticalResourcesRestExternalFacacadeV10() {
        // reset thread context
        WebClient.client(statisticalResourcesRestExternalFacacadeV10).reset();
        WebClient.client(statisticalResourcesRestExternalFacacadeV10).accept("application/xml");

        return statisticalResourcesRestExternalFacacadeV10;
    }

    public NoticesV1_0 getNoticesRestInternalFacadeV10() {
        // reset thread context
        WebClient.client(noticesV10).reset();
        WebClient.client(noticesV10).accept("application/xml");

        return noticesV10;
    }

    private void setReceiveTimeout(Object client, long receiveTimeoutValue) {
        HTTPConduit httpConduit = WebClient.getConfig(client).getHttpConduit();
        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
        httpClientPolicy.setReceiveTimeout(receiveTimeoutValue);
        httpConduit.setClient(httpClientPolicy);
    }
}