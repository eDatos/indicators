package es.gobcan.istac.indicators.rest.clients;

import java.util.List;

import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dataset;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Query;

import es.gobcan.istac.indicators.core.service.StatisticalResoucesRestExternalService;

public interface StatisticalResourceRestExternalFacade {

    public static final String BEAN_ID = "statisticalResourceRestExternalFacade";

    Query retrieveQueryByUrn(String queryUrn, List<String> lang, StatisticalResoucesRestExternalService.QueryFetchEnum onlyMetadata);

    Dataset retrieveDatasetByUrn(String datasetUrn, List<String> lang, StatisticalResoucesRestExternalService.QueryFetchEnum onlyMetadata);

}

