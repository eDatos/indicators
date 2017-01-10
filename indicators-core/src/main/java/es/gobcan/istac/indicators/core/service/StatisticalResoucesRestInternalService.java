package es.gobcan.istac.indicators.core.service;

import java.util.List;

import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Queries;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Query;

public interface StatisticalResoucesRestInternalService {

    public static final String BEAN_ID = "statisticalResoucesRestInternalService";
    
    public enum QueryFetchEnum {
        ALL, ONLY_METADATA, ONLY_DATA
    }
    
    public Queries findQueries(String query, String orderBy, String limit, String offset, List<String> lang);

    public Query retrieveQueryByUrn(String queryUrn, List<String> lang, QueryFetchEnum onlyMetadata);

    public Query retrieveQueryByUrnInDefaultLang(String queryUrn, QueryFetchEnum onlyMetadata);

}
