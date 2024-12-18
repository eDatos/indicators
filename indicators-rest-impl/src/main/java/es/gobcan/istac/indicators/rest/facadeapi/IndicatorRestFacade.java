package es.gobcan.istac.indicators.rest.facadeapi;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.rest.types.DataType;
import es.gobcan.istac.indicators.rest.types.IndicatorBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorType;
import es.gobcan.istac.indicators.rest.types.JsonStatDataType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;
import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;

public interface IndicatorRestFacade {

    IndicatorType retrieveIndicator(final String indicatorCode) throws MetamacException;
    JsonStatDataType retrieveJsonStatIndicator(String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities) throws MetamacException;
    DataType retrieveIndicatorData(final String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities, boolean includeObservationMetadata)
            throws MetamacException;
    PagedResultType<IndicatorBaseType> findIndicators(String q, String order, final RestCriteriaPaginator paginator, String fields, Map<String, List<String>> representation) throws MetamacException;

    Response retrieveIndicatorDataXLSX(final String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities) throws MetamacException;
    Response retrieveIndicatorDataCSV(final String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities) throws MetamacException;
    Response retrieveIndicatorDataTSV(final String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities) throws MetamacException;

}
