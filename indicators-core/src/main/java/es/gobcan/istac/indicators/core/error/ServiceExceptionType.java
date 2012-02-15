package es.gobcan.istac.indicators.core.error;

import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;

public class ServiceExceptionType extends CommonServiceExceptionType {

    // Indicators systems
    public static final CommonServiceExceptionType INDICATORS_SYSTEM_NOT_FOUND                             = create("exception.indicators.indicators_system.not_found");
    public static final CommonServiceExceptionType INDICATORS_SYSTEM_VERSION_NOT_FOUND                     = create("exception.indicators.indicators_system.vesion_not_found");
    public static final CommonServiceExceptionType INDICATORS_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED         = create("exception.indicators.indicators_system.already_exists.code_duplicated");
    public static final CommonServiceExceptionType INDICATORS_SYSTEM_ALREADY_EXIST_URI_GOPESTAT_DUPLICATED = create("exception.indicators.indicators_system.already_exists.uri_gopestat_duplicated");
    public static final CommonServiceExceptionType INDICATORS_SYSTEM_IN_PRODUCTION_NOT_FOUND               = create("exception.indicators.indicators_system.production_not_found");
    public static final CommonServiceExceptionType INDICATORS_SYSTEM_IN_DIFFUSION_NOT_FOUND                = create("exception.indicators.indicators_system.diffusion_not_found");
    public static final CommonServiceExceptionType INDICATORS_SYSTEM_WRONG_STATE                           = create("exception.indicators.indicators_system.wrong_state");

    // Dimensions
    public static final CommonServiceExceptionType DIMENSION_NOT_FOUND                                     = create("exception.indicators.dimension.not_found");
    public static final CommonServiceExceptionType DIMENSION_NOT_FOUND_IN_INDICATORS_SYSTEM                = create("exception.indicators.dimension.not_found_in_indicators_system");

    // Indicators
    public static final CommonServiceExceptionType INDICATOR_NOT_FOUND                                     = create("exception.indicators.indicator.not_found");
    public static final CommonServiceExceptionType INDICATOR_VERSION_NOT_FOUND                             = create("exception.indicators.indicator.vesion_not_found");
    public static final CommonServiceExceptionType INDICATOR_ALREADY_EXIST_CODE_DUPLICATED                 = create("exception.indicators.indicator.already_exists.code_duplicated");
    public static final CommonServiceExceptionType INDICATOR_IN_PRODUCTION_NOT_FOUND                       = create("exception.indicators.indicator.production_not_found");
    public static final CommonServiceExceptionType INDICATOR_IN_DIFFUSION_NOT_FOUND                        = create("exception.indicators.indicator.diffusion_not_found");
    public static final CommonServiceExceptionType INDICATOR_WRONG_STATE                                   = create("exception.indicators.indicator.wrong_state");
    public static final CommonServiceExceptionType INDICATOR_VERSION_WRONG_STATE                           = create("exception.indicators.indicator.version_wrong_state");

    // Datasources
    public static final CommonServiceExceptionType DATA_SOURCE_NOT_FOUND                                   = create("exception.indicators.data_source.not_found");
}
