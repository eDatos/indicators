package es.gobcan.istac.indicators.rest.facadeapi;

import es.gobcan.istac.indicators.rest.types.DataType;
import es.gobcan.istac.indicators.rest.types.IndicatorBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;
import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;

public interface IndicatorRestFacade {

    public IndicatorType retrieveIndicator(final String baseUrl, final String indicatorCode) throws Exception;
    public DataType retrieveIndicatorData(final String baseUrl, final String indicatorCode) throws Exception;
    public PagedResultType<IndicatorBaseType> findIndicators(final String baseUrl, final RestCriteriaPaginator paginator) throws Exception;

}
