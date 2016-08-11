package es.gobcan.istac.indicators.rest.facadeapi;

import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.rest.types.DataType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemHistoryType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;
import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;

public interface IndicatorSystemRestFacade {

    IndicatorsSystemType retrieveIndicatorsSystem(final String idIndicatorSystem) throws MetamacException;
    PagedResultType<IndicatorsSystemBaseType> findIndicatorsSystems(final RestCriteriaPaginator paginator) throws MetamacException;
    List<IndicatorsSystemHistoryType> findIndicatorsSystemHistoryByCode(final String code, final int maxResults) throws MetamacException;
    PagedResultType<IndicatorInstanceBaseType> retrievePaginatedIndicatorsInstances(final String idIndicatorSystem, String q, String order, Integer limit, Integer offset, String fields,
            Map<String, List<String>> representation, Map<String, List<String>> selectedGranularities) throws MetamacException;
    IndicatorInstanceType retrieveIndicatorInstanceByCode(final String idIndicatorSystem, final String idIndicatorInstance) throws MetamacException;
    DataType retrieveIndicatorInstanceDataByCode(final String idIndicatorSystem, final String idIndicatorInstance, Map<String, List<String>> selectedRepresentations,
            Map<String, List<String>> selectedGranularities, boolean includeObservations) throws MetamacException;
}
