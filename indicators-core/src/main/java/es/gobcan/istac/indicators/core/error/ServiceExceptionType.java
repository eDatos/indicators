package es.gobcan.istac.indicators.core.error;

import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;

public class ServiceExceptionType extends CommonServiceExceptionType {

    // Indicators systems
    public static final CommonServiceExceptionType INDICATORS_SYSTEM_NOT_FOUND                             = create("exception.indicators.indicators_system.not_found");
    public static final CommonServiceExceptionType INDICATORS_SYSTEM_VERSION_NOT_FOUND                     = create("exception.indicators.indicators_system.vesion_not_found");
    public static final CommonServiceExceptionType INDICATORS_SYSTEM_NOT_FOUND_WITH_CODE                   = create("exception.indicators.indicators_system.not_found_code");
    public static final CommonServiceExceptionType INDICATORS_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED         = create("exception.indicators.indicators_system.already_exists.code_duplicated");
    public static final CommonServiceExceptionType INDICATORS_SYSTEM_ALREADY_EXIST_URI_GOPESTAT_DUPLICATED = create("exception.indicators.indicators_system.already_exists.uri_gopestat_duplicated");
    public static final CommonServiceExceptionType INDICATORS_SYSTEM_IN_PRODUCTION_NOT_FOUND               = create("exception.indicators.indicators_system.production_not_found");
    public static final CommonServiceExceptionType INDICATORS_SYSTEM_IN_DIFFUSION_NOT_FOUND                = create("exception.indicators.indicators_system.diffusion_not_found");
    public static final CommonServiceExceptionType INDICATORS_SYSTEM_WRONG_STATE                           = create("exception.indicators.indicators_system.wrong_state");
    public static final CommonServiceExceptionType INDICATORS_SYSTEM_MUST_HAVE_INDICATOR_INSTANCE          = create("exception.indicators.indicators_system.must_have_indicator_instance");

    // Dimensions
    public static final CommonServiceExceptionType DIMENSION_NOT_FOUND                                     = create("exception.indicators.dimension.not_found");
    public static final CommonServiceExceptionType DIMENSION_NOT_FOUND_IN_INDICATORS_SYSTEM                = create("exception.indicators.dimension.not_found_in_indicators_system");

    // Indicator instances
    public static final CommonServiceExceptionType INDICATOR_INSTANCE_NOT_FOUND                            = create("exception.indicators.indicator_instance.not_found");
    public static final CommonServiceExceptionType INDICATOR_NOT_FOUND_WITH_CODE                           = create("exception.indicators.indicator.not_found_code");
    public static final CommonServiceExceptionType INDICATOR_INSTANCE_ALREADY_EXIST_INDICATOR_SAME_LEVEL   = create("exception.indicators.indicator_instance.already_exists.indicator_in_same_level");

    // Indicators
    public static final CommonServiceExceptionType INDICATOR_NOT_FOUND                                     = create("exception.indicators.indicator.not_found");
    public static final CommonServiceExceptionType INDICATOR_VERSION_NOT_FOUND                             = create("exception.indicators.indicator.version_not_found");
    public static final CommonServiceExceptionType INDICATOR_ALREADY_EXIST_CODE_DUPLICATED                 = create("exception.indicators.indicator.already_exists.code_duplicated");
    public static final CommonServiceExceptionType INDICATOR_IN_PRODUCTION_NOT_FOUND                       = create("exception.indicators.indicator.production_not_found");
    public static final CommonServiceExceptionType INDICATOR_IN_DIFFUSION_NOT_FOUND                        = create("exception.indicators.indicator.diffusion_not_found");
    public static final CommonServiceExceptionType INDICATOR_WRONG_STATE                                   = create("exception.indicators.indicator.wrong_state");
    public static final CommonServiceExceptionType INDICATOR_MUST_HAVE_DATA_SOURCES                        = create("exception.indicators.indicator.must_have_data_sources");
    public static final CommonServiceExceptionType INDICATOR_MUST_NOT_BE_IN_INDICATORS_SYSTEMS             = create("exception.indicators.indicator.must_not_be_in_indicators_systems");

    // Datasources
    public static final CommonServiceExceptionType DATA_SOURCE_NOT_FOUND                                   = create("exception.indicators.data_source.not_found");
}
