package es.gobcan.istac.indicators.core.service;

import java.util.List;

import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Queries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class StatisticalResoucesRestInternalServiceImpl implements StatisticalResoucesRestInternalService {

    private static Logger  logger = LoggerFactory.getLogger(StatisticalResoucesRestInternalService.class);

    @Autowired
    private RestApiLocator restApiLocator;

    @Override
    public Queries findQueries(String query, String orderBy, String limit, String offset, List<String> lang) {
        try {
            return restApiLocator.getStatisticalResourcesRestInternalFacacadeV10().findQueries(query, orderBy, limit, offset, lang);
        } catch (Exception e) {
            logger.error("Unable to find Queries", e);
            throw toRestException(e);
        }
    }

    @Override
    public Queries retrieveQuery(String query, String orderBy, String limit, String offset, List<String> lang) {
        try {
            return restApiLocator.getStatisticalResourcesRestInternalFacacadeV10().findQueries(query, orderBy, limit, offset, lang);
        } catch (Exception e) {
            logger.error("Unable to find Queries", e);
            throw toRestException(e);
        }
    }

    private RestException toRestException(Exception e) {
        throw toRestException(e);
    }
}
