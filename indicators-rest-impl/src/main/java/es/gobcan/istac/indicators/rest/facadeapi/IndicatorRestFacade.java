package es.gobcan.istac.indicators.rest.facadeapi;

import es.gobcan.istac.indicators.rest.types.*;

import java.util.List;
import java.util.Map;

public interface IndicatorRestFacade {

    public IndicatorType retrieveIndicator(final String baseUrl, final String indicatorCode) throws Exception;
    public DataType retrieveIndicatorData(final String baseUrl, final String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities) throws Exception;
    public PagedResultType<IndicatorBaseType> findIndicators(String baseUrl, String q, String order, final RestCriteriaPaginator paginator, String fields) throws Exception;
}
