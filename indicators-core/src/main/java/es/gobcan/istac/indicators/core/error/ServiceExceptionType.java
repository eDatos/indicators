package es.gobcan.istac.indicators.core.error;

import org.siemac.metamac.core.common.error.CommonServiceExceptionType;

public class ServiceExceptionType extends CommonServiceExceptionType {
    
    // Indicators systems
    public static final CommonServiceExceptionType SERVICE_INDICATORS_SYSTEM_NOT_FOUND                                  = create("1001", "exception.service.indicators_system.not_found");
    public static final CommonServiceExceptionType SERVICE_INDICATORS_SYSTEM_VERSION_NOT_FOUND                          = create("1002", "exception.service.indicators_system.vesion_not_found");
    public static final CommonServiceExceptionType SERVICE_INDICATORS_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED              = create("1003", "exception.service.indicators_system.already_exists.code_duplicated");
    public static final CommonServiceExceptionType SERVICE_INDICATORS_SYSTEM_ALREADY_EXIST_URI_GOPESTAT_DUPLICATED      = create("1004", "exception.service.indicators_system.already_exists.uri_gopestat_duplicated");
    public static final CommonServiceExceptionType SERVICE_INDICATORS_SYSTEM_IN_PRODUCTION_NOT_FOUND                    = create("1005", "exception.service.indicators_system.production_not_found");
    public static final CommonServiceExceptionType SERVICE_INDICATORS_SYSTEM_IN_DIFFUSION_NOT_FOUND                     = create("1006", "exception.service.indicators_system.diffusion_not_found");
    public static final CommonServiceExceptionType SERVICE_INDICATORS_SYSTEM_WRONG_STATE                                = create("1007", "exception.service.indicators_system.wrong_state");
    public static final CommonServiceExceptionType SERVICE_INDICATORS_SYSTEM_VERSION_WRONG_STATE                        = create("1008", "exception.service.indicators_system.version_wrong_state");
    
    // Dimensions
    public static final CommonServiceExceptionType SERVICE_DIMENSION_NOT_FOUND                                          = create("1101", "exception.service.dimension.not_found");
    public static final CommonServiceExceptionType SERVICE_DIMENSION_NOT_FOUND_IN_INDICATORS_SYSTEM                     = create("1102", "exception.service.dimension.not_found_in_indicators_system");
    
    // Indicators
    public static final CommonServiceExceptionType SERVICE_INDICATOR_NOT_FOUND                                          = create("1201", "exception.service.indicator.not_found");
    public static final CommonServiceExceptionType SERVICE_INDICATOR_VERSION_NOT_FOUND                                  = create("1202", "exception.service.indicator.vesion_not_found");
    public static final CommonServiceExceptionType SERVICE_INDICATOR_ALREADY_EXIST_CODE_DUPLICATED                      = create("1203", "exception.service.indicator.already_exists.code_duplicated");
    public static final CommonServiceExceptionType SERVICE_INDICATOR_IN_PRODUCTION_NOT_FOUND                            = create("1204", "exception.service.indicator.production_not_found");
    public static final CommonServiceExceptionType SERVICE_INDICATOR_IN_DIFFUSION_NOT_FOUND                             = create("1205", "exception.service.indicator.diffusion_not_found");
    public static final CommonServiceExceptionType SERVICE_INDICATOR_WRONG_STATE                                        = create("1206", "exception.service.indicator.wrong_state");
    public static final CommonServiceExceptionType SERVICE_INDICATOR_VERSION_WRONG_STATE                                = create("1207", "exception.service.indicator.version_wrong_state");
    
    // Datasources
    public static final CommonServiceExceptionType SERVICE_DATA_SOURCE_NOT_FOUND                                        = create("1301", "exception.service.data_source.not_found");
}
