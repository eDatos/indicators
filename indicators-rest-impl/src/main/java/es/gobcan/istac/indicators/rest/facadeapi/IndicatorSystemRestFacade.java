package es.gobcan.istac.indicators.rest.facadeapi;

import java.util.List;

import org.springframework.web.util.UriComponentsBuilder;

import es.gobcan.istac.indicators.rest.types.IndicatorInstanceType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;
import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;

public interface IndicatorSystemRestFacade {

    public IndicatorsSystemType retrieveIndicatorsSystem(final UriComponentsBuilder uriComponentsBuilder, final String idIndicatorSystem) throws Exception;
    public PagedResultType<IndicatorsSystemBaseType> findIndicatorsSystems(final UriComponentsBuilder uriComponentsBuilder, final RestCriteriaPaginator paginator) throws Exception;
    public List<IndicatorInstanceType> retrieveIndicatorsInstances(final UriComponentsBuilder uriComponentsBuilder, final String idIndicatorSystem) throws Exception;
    public IndicatorInstanceType retrieveIndicatorsInstance(final UriComponentsBuilder uriComponentsBuilder, final String idIndicatorSystem, final String uuidIndicatorInstance) throws Exception;
}
