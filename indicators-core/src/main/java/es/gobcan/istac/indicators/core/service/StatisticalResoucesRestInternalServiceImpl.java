package es.gobcan.istac.indicators.core.service;

import java.util.List;

import org.apache.cxf.jaxrs.client.WebClient;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Dataset;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Queries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class StatisticalResoucesRestInternalServiceImpl implements StatisticalResoucesRestInternalService {

    private static Logger  logger = LoggerFactory.getLogger(StatisticalResoucesRestInternalService.class);

    @Autowired
    private RestApiLocator restApiLocator;

    @Override
    public Dataset retrieveDataset(String agencyID, String resourceID, String version, List<String> lang, String fields, String dimensionSelection) {
        try {
            return restApiLocator.getStatisticalResourcesRestInternalFacacadeV10().retrieveDataset(agencyID, resourceID, version, lang, fields, dimensionSelection);
        } catch (Exception e) {
            throw toRestException(e);
        }
    }

    @Override
    public Queries findQueries(String query, String orderBy, String limit, String offset, List<String> lang) {
        try {
            return restApiLocator.getStatisticalResourcesRestInternalFacacadeV10().findQueries(query, orderBy, limit, offset, lang);
        } catch (Exception e) {
            throw toRestException(e);
        }
    }

    private RestException toRestException(Exception e) {
        logger.error("Error", e);
        return RestExceptionUtils.toRestException(e, WebClient.client(restApiLocator.getStatisticalResourcesRestInternalFacacadeV10()));
    }
}
