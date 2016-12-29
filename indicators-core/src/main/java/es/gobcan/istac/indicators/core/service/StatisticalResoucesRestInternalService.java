package es.gobcan.istac.indicators.core.service;

import java.util.List;

import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Queries;

public interface StatisticalResoucesRestInternalService {

    public Queries findQueries(String query, String orderBy, String limit, String offset, List<String> lang);

    public Queries retrieveQuery(String query, String orderBy, String limit, String offset, List<String> lang);

}
