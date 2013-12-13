package es.gobcan.istac.indicators.rest.facadeapi;

import es.gobcan.istac.indicators.rest.types.*;

import java.util.List;
import java.util.Map;

public interface IndicatorSystemRestFacade {

    public IndicatorsSystemType retrieveIndicatorsSystem(final String baseUrl, final String idIndicatorSystem) throws Exception;
    public PagedResultType<IndicatorsSystemBaseType> findIndicatorsSystems(final String baseUrl, final RestCriteriaPaginator paginator) throws Exception;
    public List<IndicatorsSystemHistoryType> findIndicatorsSystemHistoryByCode(final String baseURL, final String code, final int maxResults) throws Exception;
    public ListResultType<IndicatorInstanceBaseType> retrieveIndicatorsInstances(final String baseUrl, final String idIndicatorSystem, String q, String order, String fields,
            Map<String, List<String>> representation, Map<String, List<String>> selectedGranularities) throws Exception;
    public PagedResultType<IndicatorInstanceBaseType> retrievePaginatedIndicatorsInstances(final String baseUrl, final String idIndicatorSystem, String q, String order, Integer limit, Integer offset,
            String fields, Map<String, List<String>> representation, Map<String, List<String>> selectedGranularities) throws Exception;
    public IndicatorInstanceType retrieveIndicatorInstanceByCode(final String baseUrl, final String idIndicatorSystem, final String idIndicatorInstance) throws Exception;
    public DataType retrieveIndicatorInstanceDataByCode(final String baseUrl, final String idIndicatorSystem, final String idIndicatorInstance, Map<String, List<String>> selectedRepresentations,
            Map<String, List<String>> selectedGranularities, boolean includeObservations) throws Exception;
}
