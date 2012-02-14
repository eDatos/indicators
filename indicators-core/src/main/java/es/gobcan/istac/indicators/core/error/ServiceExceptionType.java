package es.gobcan.istac.indicators.core.error;

import org.siemac.metamac.core.common.error.CommonServiceExceptionType;

public class ServiceExceptionType extends CommonServiceExceptionType {
    
    // Indicators systems
    public static final CommonServiceExceptionType SERVICE_INDICATORS_SYSTEM_NOT_FOUND                                  = create("0501", "exception.service.validation.indicators_system.not_found");
    public static final CommonServiceExceptionType SERVICE_INDICATORS_SYSTEM_VERSION_NOT_FOUND                          = create("0502", "exception.service.validation.indicators_system.vesion_not_found");
    public static final CommonServiceExceptionType SERVICE_INDICATORS_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED              = create("0503", "exception.service.validation.indicators_system.already_exists.code_duplicated");
    public static final CommonServiceExceptionType SERVICE_INDICATORS_SYSTEM_ALREADY_EXIST_URI_GOPESTAT_DUPLICATED      = create("0504", "exception.service.validation.indicators_system.already_exists.uri_gopestat_duplicated");
    public static final CommonServiceExceptionType SERVICE_INDICATORS_SYSTEM_IN_PRODUCTION_NOT_FOUND                    = create("0505", "exception.service.validation.indicators_system.production_not_found");
    public static final CommonServiceExceptionType SERVICE_INDICATORS_SYSTEM_IN_DIFFUSION_NOT_FOUND                     = create("0506", "exception.service.validation.indicators_system.diffusion_not_found");
    public static final CommonServiceExceptionType SERVICE_INDICATORS_SYSTEM_WRONG_STATE                                = create("0507", "exception.service.validation.indicators_system.wrong_state");
    public static final CommonServiceExceptionType SERVICE_INDICATORS_SYSTEM_VERSION_WRONG_STATE                        = create("0508", "exception.service.validation.indicators_system.version_wrong_state");
    
    // Dimensions
    public static final CommonServiceExceptionType SERVICE_DIMENSION_NOT_FOUND                                          = create("0601", "exception.service.validation.dimension.not_found");
    public static final CommonServiceExceptionType SERVICE_DIMENSION_NOT_FOUND_IN_INDICATORS_SYSTEM                     = create("0602", "exception.service.validation.dimension.not_found_in_indicators_system");
    
    // Indicators
    public static final CommonServiceExceptionType SERVICE_INDICATOR_NOT_FOUND                                          = create("0701", "exception.service.validation.indicator.not_found");
    public static final CommonServiceExceptionType SERVICE_INDICATOR_VERSION_NOT_FOUND                                  = create("0702", "exception.service.validation.indicator.vesion_not_found");
    public static final CommonServiceExceptionType SERVICE_INDICATOR_ALREADY_EXIST_CODE_DUPLICATED                      = create("0703", "exception.service.validation.indicator.already_exists.code_duplicated");
    public static final CommonServiceExceptionType SERVICE_INDICATOR_IN_PRODUCTION_NOT_FOUND                            = create("0704", "exception.service.validation.indicator.production_not_found");
    public static final CommonServiceExceptionType SERVICE_INDICATOR_IN_DIFFUSION_NOT_FOUND                             = create("0705", "exception.service.validation.indicator.diffusion_not_found");
    public static final CommonServiceExceptionType SERVICE_INDICATOR_WRONG_STATE                                        = create("0706", "exception.service.validation.indicator.wrong_state");
    public static final CommonServiceExceptionType SERVICE_INDICATOR_VERSION_WRONG_STATE                                = create("0707", "exception.service.validation.indicator.version_wrong_state");
}
