package es.gobcan.istac.indicators.rest.facadeapi;

import es.gobcan.istac.indicators.rest.types.IndicatorInstanceBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceDataType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;
import es.gobcan.istac.indicators.rest.types.NoPagedResultType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;
import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;

public interface IndicatorSystemRestFacade {

    public IndicatorsSystemType retrieveIndicatorsSystem(final String baseUrl, final String idIndicatorSystem) throws Exception;
    public PagedResultType<IndicatorsSystemBaseType> findIndicatorsSystems(final String baseUrl, final RestCriteriaPaginator paginator) throws Exception;
    public NoPagedResultType<IndicatorInstanceBaseType> retrieveIndicatorsInstances(final String baseUrl, final String idIndicatorSystem) throws Exception;
    public IndicatorInstanceType retrieveIndicatorsInstance(final String baseUrl, final String idIndicatorSystem, final String uuidIndicatorInstance) throws Exception;
    public IndicatorInstanceDataType retrieveIndicatorsInstanceData(final String baseUrl, final String idIndicatorSystem, final String uuidIndicatorInstance) throws Exception;

}
