package es.gobcan.istac.indicators.rest.exception;

import org.siemac.metamac.rest.exception.RestCommonServiceExceptionType;

public class RestIndicatorsCommonServiceExceptionType extends RestCommonServiceExceptionType {

    public static final RestCommonServiceExceptionType INDICATOR_OBSERVATIONS_EXCEED_MAX_FOR_XLSX = create("exception.indicators.indicator.observations_exceed_max_for_xlsx");
}