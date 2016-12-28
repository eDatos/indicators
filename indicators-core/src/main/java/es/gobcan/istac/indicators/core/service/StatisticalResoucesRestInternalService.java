package es.gobcan.istac.indicators.core.service;

import java.util.List;

import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Dataset;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Queries;

public interface StatisticalResoucesRestInternalService {

    public Dataset retrieveDataset(String agencyID, String resourceID, String version, List<String> lang, String fields, String dimensionSelection);

    public Queries findQueries(String query, String orderBy, String limit, String offset, List<String> lang);

}
