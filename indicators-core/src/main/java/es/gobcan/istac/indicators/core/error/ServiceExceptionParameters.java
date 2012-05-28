package es.gobcan.istac.indicators.core.error;


public class ServiceExceptionParameters {

    public static final String UUID                                                                          = "uuid";
    public static final String CODE                                                                          = "code";

    // Indicators systems
    public static final String INDICATORS_SYSTEM                                                             = "indicators_system";
    public static final String INDICATORS_SYSTEM_UUID                                                        = "indicators_system.uuid";
    public static final String INDICATORS_SYSTEM_VERSION_NUMBER                                              = "indicators_system.version_number";
    public static final String INDICATORS_SYSTEM_VERSION_TYPE                                                = "indicators_system.version_type";
    public static final String INDICATORS_SYSTEM_CODE                                                        = "indicators_system.code";
    public static final String INDICATORS_SYSTEM_PROC_STATUS_DRAFT                                           = "indicator_system.draft";
    public static final String INDICATORS_SYSTEM_PROC_STATUS_PRODUCTION_VALIDATION                           = "indicator_system.production_validation";
    public static final String INDICATORS_SYSTEM_PROC_STATUS_DIFFUSION_VALIDATION                            = "indicator_system.diffusion_validation";
    public static final String INDICATORS_SYSTEM_PROC_STATUS_VALIDATION_REJECTED                             = "indicator_system.validation_rejected";
    public static final String INDICATORS_SYSTEM_PROC_STATUS_PUBLISHED                                       = "indicator_system.published";
    public static final String INDICATORS_SYSTEM_PROC_STATUS_ARCHIVED                                        = "indicator_system.archived";

    // Dimensions
    public static final String DIMENSION                                                                     = "dimension";
    public static final String DIMENSION_UUID                                                                = "dimension.uuid";
    public static final String DIMENSION_ORDER_IN_LEVEL                                                      = "dimension.order_in_level";
    public static final String DIMENSION_TITLE                                                               = "dimension.title";
    public static final String DIMENSION_PARENT_UUID                                                         = "dimension.parent_uuid";

    // Indicators instances
    public static final String INDICATOR_INSTANCE                                                            = "indicator_instance";
    public static final String INDICATOR_INSTANCE_UUID                                                       = "indicator_instance.uuid";
    public static final String INDICATOR_INSTANCE_CODE                                                       = "indicator_instance.code";
    public static final String INDICATOR_INSTANCE_ORDER_IN_LEVEL                                             = "indicator_instance.order_in_level";
    public static final String INDICATOR_INSTANCE_TITLE                                                      = "indicator_instance.title";
    public static final String INDICATOR_INSTANCE_INDICATOR_UUID                                             = "indicator_instance.indicator_uuid";
    public static final String INDICATOR_INSTANCE_TIME_GRANULARITY                                           = "indicator_instance.time_granularity";
    public static final String INDICATOR_INSTANCE_TIME_VALUE                                                 = "indicator_instance.time_value";
    public static final String INDICATOR_INSTANCE_GEOGRAPHICAL_VALUE_UUID                                    = "indicator_instance.geographical_value_uuid";
    public static final String INDICATOR_INSTANCE_PARENT_UUID                                                = "indicator_instance.parent_uuid";

    // Indicators
    public static final String INDICATOR                                                                     = "indicator";
    public static final String INDICATOR_UUID                                                                = "indicator.uuid";
    public static final String INDICATOR_VERSION_NUMBER                                                      = "indicator.version_number";
    public static final String INDICATOR_VERSION_TYPE                                                        = "indicator.version_type";
    public static final String INDICATOR_CODE                                                                = "indicator.code";
    public static final String INDICATOR_SUBJECT_CODE                                                        = "indicator.subject_code";
    public static final String INDICATOR_SUBJECT_TITLE                                                       = "indicator.subject_title";
    public static final String INDICATOR_TITLE                                                               = "indicator.title";
    public static final String INDICATOR_ACRONYM                                                             = "indicator.acronym";
    public static final String INDICATOR_COMMENTS                                                            = "indicator.comments";
    public static final String INDICATOR_CONCEPT_DESCRIPTION                                                 = "indicator.concept_description";
    public static final String INDICATOR_NOTES                                                               = "indicator.notes";
    public static final String INDICATOR_DATA_REPOSITORY_ID                                                  = "indicator.data_repository_id";
    public static final String INDICATOR_DATA_REPOSITORY_TABLE_NAME                                          = "indicator.data_repository_table_name";
    public static final String INDICATOR_PROC_STATUS_DRAFT                                                   = "indicator.draft";
    public static final String INDICATOR_PROC_STATUS_PRODUCTION_VALIDATION                                   = "indicator.production_validation";
    public static final String INDICATOR_PROC_STATUS_DIFFUSION_VALIDATION                                    = "indicator.diffusion_validation";
    public static final String INDICATOR_PROC_STATUS_VALIDATION_REJECTED                                     = "indicator.validation_rejected";
    public static final String INDICATOR_PROC_STATUS_PUBLICATION_FAILED                                      = "indicator.publication_failed";
    public static final String INDICATOR_PROC_STATUS_PUBLISHED                                               = "indicator.published";
    public static final String INDICATOR_PROC_STATUS_ARCHIVED                                                = "indicator.archived";

    // Indicator quantities
    public static final String INDICATOR_QUANTITY                                                            = "indicator.quantity";
    public static final String INDICATOR_QUANTITY_TYPE                                                       = "indicator.quantity.type";
    public static final String INDICATOR_QUANTITY_UNIT_UUID                                                  = "indicator.quantity.unit_uuid";
    public static final String INDICATOR_QUANTITY_UNIT_MULTIPLIER                                            = "indicator.quantity.unit_multiplier";
    public static final String INDICATOR_QUANTITY_DECIMAL_PLACES                                             = "indicator.quantity.decimal_places";
    public static final String INDICATOR_QUANTITY_IS_PERCENTAGE                                              = "indicator.quantity.is_percentage";
    public static final String INDICATOR_QUANTITY_PERCENTAGE_OF                                              = "indicator.quantity.percentage_of";
    public static final String INDICATOR_QUANTITY_BASE_VALUE                                                 = "indicator.quantity.base_value";
    public static final String INDICATOR_QUANTITY_BASE_TIME                                                  = "indicator.quantity.base_time";
    public static final String INDICATOR_QUANTITY_BASE_LOCATION_UUID                                         = "indicator.quantity.base_location_uuid";
    public static final String INDICATOR_QUANTITY_BASE_QUANTITY_INDICATOR_UUID                               = "indicator.quantity.base_quantity_indicator_uuid";
    public static final String INDICATOR_QUANTITY_MINIMUM                                                    = "indicator.quantity.minimum";
    public static final String INDICATOR_QUANTITY_MAXIMUM                                                    = "indicator.quantity.maximum";
    public static final String INDICATOR_QUANTITY_NUMERATOR_INDICATOR_UUID                                   = "indicator.quantity.numerator_indicator_uuid";
    public static final String INDICATOR_QUANTITY_DENOMINATOR_INDICATOR_UUID                                 = "indicator.quantity.denominator_indicator_uuid";

    // Indicator Data
    public static final String INDICATOR_DATA_DIMENSION_TYPE_GEOGRAPHICAL                                    = "indicator.data_dimension_type.geographical";
    public static final String INDICATOR_DATA_DIMENSION_TYPE_TIME                                            = "indicator.data_dimension_type.time";
    public static final String INDICATOR_DATA_DIMENSION_TYPE_MEASURE                                         = "indicator.data_dimension_type.measure";

    // Datasources
    public static final String DATA_SOURCE                                                                   = "data_source";
    public static final String DATA_SOURCE_UUID                                                              = "data_source.uuid";
    public static final String DATA_SOURCE_DATA_GPE_UUID                                                     = "data_source.data_gpe_uuid";
    public static final String DATA_SOURCE_PX_URI                                                            = "data_source.px_uri";
    public static final String DATA_SOURCE_SOURCE_SURVEY_CODE                                                = "data_source.source_survey_code";
    public static final String DATA_SOURCE_SOURCE_SURVEY_TITLE                                               = "data_source.source_survey_title";
    public static final String DATA_SOURCE_SOURCE_SURVEY_ACRONYM                                             = "data_source.source_survey_acronym";
    public static final String DATA_SOURCE_PUBLISHERS                                                        = "data_source.publishers";
    public static final String DATA_SOURCE_ABSOLUTE_METHOD                                                   = "data_source.absolute_method";
    public static final String DATA_SOURCE_TIME_VARIABLE                                                     = "data_source.time_variable";
    public static final String DATA_SOURCE_TIME_VALUE                                                        = "data_source.time_value";
    public static final String DATA_SOURCE_GEOGRAPHICAL_VARIABLE                                             = "data_source.geographical_variable";
    public static final String DATA_SOURCE_GEOGRAPHICAL_VALUE_UUID                                           = "data_source.geographical_value_uuid";
    public static final String DATA_SOURCE_OTHER_VARIABLE_VARIABLE                                           = "data_source.other_variable.variable";
    public static final String DATA_SOURCE_OTHER_VARIABLE_CATEGORY                                           = "data_source.other_variable.category";
    // Rates
    // Annual puntual
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE                                               = "data_source.annual_puntual_rate";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_METHOD_TYPE                                   = "data_source.annual_puntual_rate.method_type";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_METHOD                                        = "data_source.annual_puntual_rate.method";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_ROUNDING                                      = "data_source.annual_puntual_rate.rounding";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY                                      = "data_source.annual_puntual_rate.quantity";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_TYPE                                 = "data_source.annual_puntual_rate.quantity.type";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_UNIT_UUID                            = "data_source.annual_puntual_rate.quantity.unit_uuid";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_UNIT_MULTIPLIER                      = "data_source.annual_puntual_rate.quantity.unit_multiplier";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_DECIMAL_PLACES                       = "data_source.annual_puntual_rate.quantity.decimal_places";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_IS_PERCENTAGE                        = "data_source.annual_puntual_rate.quantity.is_percentage";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_PERCENTAGE_OF                        = "data_source.annual_puntual_rate.quantity.percentage_of";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_BASE_VALUE                           = "data_source.annual_puntual_rate.quantity.base_value";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_BASE_TIME                            = "data_source.annual_puntual_rate.quantity.base_time";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_BASE_LOCATION_UUID                   = "data_source.annual_puntual_rate.quantity.base_location_uuid";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_BASE_QUANTITY_INDICATOR_UUID         = "data_source.annual_puntual_rate.quantity.base_quantity_indicator_uuid";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_MINIMUM                              = "data_source.annual_puntual_rate.quantity.minimum";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_MAXIMUM                              = "data_source.annual_puntual_rate.quantity.maximum";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_NUMERATOR_INDICATOR_UUID             = "data_source.annual_puntual_rate.quantity.numerator_indicator_uuid";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_DENOMINATOR_INDICATOR_UUID           = "data_source.annual_puntual_rate.quantity.denominator_indicator_uuid";
    // Annual percentage
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE                                            = "data_source.annual_percentage_rate";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_METHOD_TYPE                                = "data_source.annual_percentage_rate.method_type";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_METHOD                                     = "data_source.annual_percentage_rate.method";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_ROUNDING                                   = "data_source.annual_percentage_rate.rounding";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY                                   = "data_source.annual_percentage_rate.quantity";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_TYPE                              = "data_source.annual_percentage_rate.quantity.type";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_UNIT_UUID                         = "data_source.annual_percentage_rate.quantity.unit_uuid";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_UNIT_MULTIPLIER                   = "data_source.annual_percentage_rate.quantity.unit_multiplier";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_DECIMAL_PLACES                    = "data_source.annual_percentage_rate.quantity.decimal_places";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_IS_PERCENTAGE                     = "data_source.annual_percentage_rate.quantity.is_percentage";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_PERCENTAGE_OF                     = "data_source.annual_percentage_rate.quantity.percentage_of";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_BASE_VALUE                        = "data_source.annual_percentage_rate.quantity.base_value";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_BASE_TIME                         = "data_source.annual_percentage_rate.quantity.base_time";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_BASE_LOCATION_UUID                = "data_source.annual_percentage_rate.quantity.base_location_uuid";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_BASE_QUANTITY_INDICATOR_UUID      = "data_source.annual_percentage_rate.quantity.base_quantity_indicator_uuid";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_MINIMUM                           = "data_source.annual_percentage_rate.quantity.minimum";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_MAXIMUM                           = "data_source.annual_percentage_rate.quantity.maximum";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_NUMERATOR_INDICATOR_UUID          = "data_source.annual_percentage_rate.quantity.numerator_indicator_uuid";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_DENOMINATOR_INDICATOR_UUID        = "data_source.annual_percentage_rate.quantity.denominator_indicator_uuid";
    // Interperiod puntual
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE                                          = "data_source.interperiod_puntual_rate";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_METHOD_TYPE                              = "data_source.interperiod_puntual_rate.method_type";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_METHOD                                   = "data_source.interperiod_puntual_rate.method";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_ROUNDING                                 = "data_source.interperiod_puntual_rate.rounding";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY                                 = "data_source.interperiod_puntual_rate.quantity";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_TYPE                            = "data_source.interperiod_puntual_rate.quantity.type";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_UNIT_UUID                       = "data_source.interperiod_puntual_rate.quantity.unit_uuid";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_UNIT_MULTIPLIER                 = "data_source.interperiod_puntual_rate.quantity.unit_multiplier";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_DECIMAL_PLACES                  = "data_source.interperiod_puntual_rate.quantity.decimal_places";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_IS_PERCENTAGE                   = "data_source.interperiod_puntual_rate.quantity.is_percentage";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_PERCENTAGE_OF                   = "data_source.interperiod_puntual_rate.quantity.percentage_of";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_BASE_VALUE                      = "data_source.interperiod_puntual_rate.quantity.base_value";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_BASE_TIME                       = "data_source.interperiod_puntual_rate.quantity.base_time";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_BASE_LOCATION_UUID              = "data_source.interperiod_puntual_rate.quantity.base_location_uuid";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_BASE_QUANTITY_INDICATOR_UUID    = "data_source.interperiod_puntual_rate.quantity.base_quantity_indicator_uuid";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_MINIMUM                         = "data_source.interperiod_puntual_rate.quantity.minimum";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_MAXIMUM                         = "data_source.interperiod_puntual_rate.quantity.maximum";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_NUMERATOR_INDICATOR_UUID        = "data_source.interperiod_puntual_rate.quantity.numerator_indicator_uuid";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_DENOMINATOR_INDICATOR_UUID      = "data_source.interperiod_puntual_rate.quantity.denominator_indicator_uuid";
    // Interperiod percentage
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE                                       = "data_source.interperiod_percentage_rate";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_METHOD_TYPE                           = "data_source.interperiod_percentage_rate.method_type";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_METHOD                                = "data_source.interperiod_percentage_rate.method";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_ROUNDING                              = "data_source.interperiod_percentage_rate.rounding";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY                              = "data_source.interperiod_percentage_rate.quantity";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_TYPE                         = "data_source.interperiod_percentage_rate.quantity.type";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_UNIT_UUID                    = "data_source.interperiod_percentage_rate.quantity.unit_uuid";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_UNIT_MULTIPLIER              = "data_source.interperiod_percentage_rate.quantity.unit_multiplier";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_DECIMAL_PLACES               = "data_source.interperiod_percentage_rate.quantity.decimal_places";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_IS_PERCENTAGE                = "data_source.interperiod_percentage_rate.quantity.is_percentage";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_PERCENTAGE_OF                = "data_source.interperiod_percentage_rate.quantity.percentage_of";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_BASE_VALUE                   = "data_source.interperiod_percentage_rate.quantity.base_value";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_BASE_TIME                    = "data_source.interperiod_percentage_rate.quantity.base_time";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_BASE_LOCATION_UUID           = "data_source.interperiod_percentage_rate.quantity.base_location_uuid";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_BASE_QUANTITY_INDICATOR_UUID = "data_source.interperiod_percentage_rate.quantity.base_quantity_indicator_uuid";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_MINIMUM                      = "data_source.interperiod_percentage_rate.quantity.minimum";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_MAXIMUM                      = "data_source.interperiod_percentage_rate.quantity.maximum";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_NUMERATOR_INDICATOR_UUID     = "data_source.interperiod_percentage_rate.quantity.numerator_indicator_uuid";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_DENOMINATOR_INDICATOR_UUID   = "data_source.interperiod_percentage_rate.quantity.denominator_indicator_uuid";

    // Granularities and values
    public static final String GEOGRAPHICAL_GRANULARITY_UUID                                                 = "geographical_granularity.uuid";
    public static final String TIME_VALUE                                                                    = "time_value";
    public static final String TIME_GRANULARITY                                                              = "time_granularity";
    public static final String MEASURE_VALUE                                                                 = "measure_value";
}
