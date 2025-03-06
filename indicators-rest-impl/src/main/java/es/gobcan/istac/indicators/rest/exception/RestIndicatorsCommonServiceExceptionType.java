package es.gobcan.istac.indicators.rest.exception;

import org.siemac.metamac.rest.exception.RestCommonServiceExceptionType;

public class RestIndicatorsCommonServiceExceptionType extends RestCommonServiceExceptionType {

    public static final RestCommonServiceExceptionType INDICATOR_NOT_FOUND                        = create("exception.indicators.dataset.not_found");
    public static final RestCommonServiceExceptionType COLLECTION_NOT_FOUND                       = create("exception.indicators.collection.not_found");
    public static final RestCommonServiceExceptionType QUERY_NOT_FOUND                            = create("exception.indicators.query.not_found");
    public static final RestCommonServiceExceptionType INDICATOR_OBSERVATIONS_EXCEED_MAX_FOR_XLSX = create("exception.indicators.indicator.observations_exceed_max_for_xlsx");
}