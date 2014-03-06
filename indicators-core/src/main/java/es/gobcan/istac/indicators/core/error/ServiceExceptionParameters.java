package es.gobcan.istac.indicators.core.error;

public class ServiceExceptionParameters {

    public static final String UUID                                                                          = "parameter.indicators.uuid";
    public static final String CODE                                                                          = "parameter.indicators.code";

    // Indicators systems
    public static final String INDICATORS_SYSTEM                                                             = "parameter.indicators.indicators_system";
    public static final String INDICATORS_SYSTEM_UUID                                                        = "parameter.indicators.indicators_system.uuid";
    public static final String INDICATORS_SYSTEM_VERSION_NUMBER                                              = "parameter.indicators.indicators_system.version_number";
    public static final String INDICATORS_SYSTEM_VERSION_TYPE                                                = "parameter.indicators.indicators_system.version_type";
    public static final String INDICATORS_SYSTEM_CODE                                                        = "parameter.indicators.indicators_system.code";
    public static final String INDICATORS_SYSTEM_PROC_STATUS_DRAFT                                           = "parameter.indicators.indicator_system.draft";
    public static final String INDICATORS_SYSTEM_PROC_STATUS_PRODUCTION_VALIDATION                           = "parameter.indicators.indicator_system.production_validation";
    public static final String INDICATORS_SYSTEM_PROC_STATUS_DIFFUSION_VALIDATION                            = "parameter.indicators.indicator_system.diffusion_validation";
    public static final String INDICATORS_SYSTEM_PROC_STATUS_VALIDATION_REJECTED                             = "parameter.indicators.indicator_system.validation_rejected";
    public static final String INDICATORS_SYSTEM_PROC_STATUS_PUBLISHED                                       = "parameter.indicators.indicator_system.published";
    public static final String INDICATORS_SYSTEM_PROC_STATUS_ARCHIVED                                        = "parameter.indicators.indicator_system.archived";

    // Dimensions
    public static final String DIMENSION                                                                     = "parameter.indicators.dimension";
    public static final String DIMENSION_UUID                                                                = "parameter.indicators.dimension.uuid";
    public static final String DIMENSION_ORDER_IN_LEVEL                                                      = "parameter.indicators.dimension.order_in_level";
    public static final String DIMENSION_TITLE                                                               = "parameter.indicators.dimension.title";
    public static final String DIMENSION_PARENT_UUID                                                         = "parameter.indicators.dimension.parent_uuid";

    // Indicators instances
    public static final String INDICATOR_INSTANCE                                                            = "parameter.indicators.indicator_instance";
    public static final String INDICATOR_INSTANCE_UUID                                                       = "parameter.indicators.indicator_instance.uuid";
    public static final String INDICATOR_INSTANCE_CODE                                                       = "parameter.indicators.indicator_instance.code";
    public static final String INDICATOR_INSTANCE_ORDER_IN_LEVEL                                             = "parameter.indicators.indicator_instance.order_in_level";
    public static final String INDICATOR_INSTANCE_TITLE                                                      = "parameter.indicators.indicator_instance.title";
    public static final String INDICATOR_INSTANCE_INDICATOR_UUID                                             = "parameter.indicators.indicator_instance.indicator_uuid";
    public static final String INDICATOR_INSTANCE_TIME_GRANULARITY                                           = "parameter.indicators.indicator_instance.time_granularity";
    public static final String INDICATOR_INSTANCE_TIME_VALUES                                                = "parameter.indicators.indicator_instance.time_values";
    public static final String INDICATOR_INSTANCE_GEOGRAPHICAL_VALUES                                        = "parameter.indicators.indicator_instance.geographical_values";
    public static final String INDICATOR_INSTANCE_PARENT_UUID                                                = "parameter.indicators.indicator_instance.parent_uuid";

    // Indicators
    public static final String INDICATOR                                                                     = "parameter.indicators.indicator";
    public static final String INDICATOR_UUID                                                                = "parameter.indicators.indicator.uuid";
    public static final String INDICATOR_VERSION_NUMBER                                                      = "parameter.indicators.indicator.version_number";
    public static final String INDICATOR_VERSION_TYPE                                                        = "parameter.indicators.indicator.version_type";
    public static final String INDICATOR_CODE                                                                = "parameter.indicators.indicator.code";
    public static final String INDICATOR_VIEW_CODE                                                           = "parameter.indicators.indicator.view_code";
    public static final String INDICATOR_SUBJECT_CODE                                                        = "parameter.indicators.indicator.subject_code";
    public static final String INDICATOR_SUBJECT_TITLE                                                       = "parameter.indicators.indicator.subject_title";
    public static final String INDICATOR_TITLE                                                               = "parameter.indicators.indicator.title";
    public static final String INDICATOR_ACRONYM                                                             = "parameter.indicators.indicator.acronym";
    public static final String INDICATOR_COMMENTS                                                            = "parameter.indicators.indicator.comments";
    public static final String INDICATOR_CONCEPT_DESCRIPTION                                                 = "parameter.indicators.indicator.concept_description";
    public static final String INDICATOR_NOTES                                                               = "parameter.indicators.indicator.notes";
    public static final String INDICATOR_DATA_REPOSITORY_ID                                                  = "parameter.indicators.indicator.data_repository_id";
    public static final String INDICATOR_DATA_REPOSITORY_TABLE_NAME                                          = "parameter.indicators.indicator.data_repository_table_name";
    public static final String INDICATOR_PROC_STATUS_DRAFT                                                   = "parameter.indicators.indicator.draft";
    public static final String INDICATOR_PROC_STATUS_PRODUCTION_VALIDATION                                   = "parameter.indicators.indicator.production_validation";
    public static final String INDICATOR_PROC_STATUS_DIFFUSION_VALIDATION                                    = "parameter.indicators.indicator.diffusion_validation";
    public static final String INDICATOR_PROC_STATUS_VALIDATION_REJECTED                                     = "parameter.indicators.indicator.validation_rejected";
    public static final String INDICATOR_PROC_STATUS_PUBLICATION_FAILED                                      = "parameter.indicators.indicator.publication_failed";
    public static final String INDICATOR_PROC_STATUS_PUBLISHED                                               = "parameter.indicators.indicator.published";
    public static final String INDICATOR_PROC_STATUS_ARCHIVED                                                = "parameter.indicators.indicator.archived";

    // Indicator quantities
    public static final String INDICATOR_QUANTITY                                                            = "parameter.indicators.indicator.quantity";
    public static final String INDICATOR_QUANTITY_TYPE                                                       = "parameter.indicators.indicator.quantity.type";
    public static final String INDICATOR_QUANTITY_UNIT_UUID                                                  = "parameter.indicators.indicator.quantity.unit_uuid";
    public static final String INDICATOR_QUANTITY_UNIT_MULTIPLIER                                            = "parameter.indicators.indicator.quantity.unit_multiplier";
    public static final String INDICATOR_QUANTITY_DECIMAL_PLACES                                             = "parameter.indicators.indicator.quantity.decimal_places";
    public static final String INDICATOR_QUANTITY_IS_PERCENTAGE                                              = "parameter.indicators.indicator.quantity.is_percentage";
    public static final String INDICATOR_QUANTITY_PERCENTAGE_OF                                              = "parameter.indicators.indicator.quantity.percentage_of";
    public static final String INDICATOR_QUANTITY_BASE_VALUE                                                 = "parameter.indicators.indicator.quantity.base_value";
    public static final String INDICATOR_QUANTITY_BASE_TIME                                                  = "parameter.indicators.indicator.quantity.base_time";
    public static final String INDICATOR_QUANTITY_BASE_LOCATION_UUID                                         = "parameter.indicators.indicator.quantity.base_location_uuid";
    public static final String INDICATOR_QUANTITY_BASE_QUANTITY_INDICATOR_UUID                               = "parameter.indicators.indicator.quantity.base_quantity_indicator_uuid";
    public static final String INDICATOR_QUANTITY_MINIMUM                                                    = "parameter.indicators.indicator.quantity.minimum";
    public static final String INDICATOR_QUANTITY_MAXIMUM                                                    = "parameter.indicators.indicator.quantity.maximum";
    public static final String INDICATOR_QUANTITY_NUMERATOR_INDICATOR_UUID                                   = "parameter.indicators.indicator.quantity.numerator_indicator_uuid";
    public static final String INDICATOR_QUANTITY_DENOMINATOR_INDICATOR_UUID                                 = "parameter.indicators.indicator.quantity.denominator_indicator_uuid";

    // Indicator Data
    public static final String INDICATOR_DATA_DIMENSION_TYPE_GEOGRAPHICAL                                    = "parameter.indicators.indicator.data_dimension_type.geographical";
    public static final String INDICATOR_DATA_DIMENSION_TYPE_TIME                                            = "parameter.indicators.indicator.data_dimension_type.time";
    public static final String INDICATOR_DATA_DIMENSION_TYPE_MEASURE                                         = "parameter.indicators.indicator.data_dimension_type.measure";

    // Datasources
    public static final String DATA_SOURCE                                                                   = "parameter.indicators.data_source";
    public static final String DATA_SOURCE_UUID                                                              = "parameter.indicators.data_source.uuid";
    public static final String DATA_SOURCE_DATA_GPE_UUID                                                     = "parameter.indicators.data_source.data_gpe_uuid";
    public static final String DATA_SOURCE_PX_URI                                                            = "parameter.indicators.data_source.px_uri";
    public static final String DATA_SOURCE_SOURCE_SURVEY_CODE                                                = "parameter.indicators.data_source.source_survey_code";
    public static final String DATA_SOURCE_SOURCE_SURVEY_TITLE                                               = "parameter.indicators.data_source.source_survey_title";
    public static final String DATA_SOURCE_SOURCE_SURVEY_ACRONYM                                             = "parameter.indicators.data_source.source_survey_acronym";
    public static final String DATA_SOURCE_PUBLISHERS                                                        = "parameter.indicators.data_source.publishers";
    public static final String DATA_SOURCE_ABSOLUTE_METHOD                                                   = "parameter.indicators.data_source.absolute_method";
    public static final String DATA_SOURCE_TIME_VARIABLE                                                     = "parameter.indicators.data_source.time_variable";
    public static final String DATA_SOURCE_TIME_VALUE                                                        = "parameter.indicators.data_source.time_value";
    public static final String DATA_SOURCE_GEOGRAPHICAL_VARIABLE                                             = "parameter.indicators.data_source.geographical_variable";
    public static final String DATA_SOURCE_GEOGRAPHICAL_VALUE_UUID                                           = "parameter.indicators.data_source.geographical_value_uuid";
    public static final String DATA_SOURCE_OTHER_VARIABLE_VARIABLE                                           = "parameter.indicators.data_source.other_variable.variable";
    public static final String DATA_SOURCE_OTHER_VARIABLE_CATEGORY                                           = "parameter.indicators.data_source.other_variable.category";
    // Rates
    // Annual puntual
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE                                               = "parameter.indicators.data_source.annual_puntual_rate";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_METHOD_TYPE                                   = "parameter.indicators.data_source.annual_puntual_rate.method_type";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_METHOD                                        = "parameter.indicators.data_source.annual_puntual_rate.method";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_ROUNDING                                      = "parameter.indicators.data_source.annual_puntual_rate.rounding";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY                                      = "parameter.indicators.data_source.annual_puntual_rate.quantity";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_TYPE                                 = "parameter.indicators.data_source.annual_puntual_rate.quantity.type";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_UNIT_UUID                            = "parameter.indicators.data_source.annual_puntual_rate.quantity.unit_uuid";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_UNIT_MULTIPLIER                      = "parameter.indicators.data_source.annual_puntual_rate.quantity.unit_multiplier";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_DECIMAL_PLACES                       = "parameter.indicators.data_source.annual_puntual_rate.quantity.decimal_places";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_IS_PERCENTAGE                        = "parameter.indicators.data_source.annual_puntual_rate.quantity.is_percentage";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_PERCENTAGE_OF                        = "parameter.indicators.data_source.annual_puntual_rate.quantity.percentage_of";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_BASE_VALUE                           = "parameter.indicators.data_source.annual_puntual_rate.quantity.base_value";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_BASE_TIME                            = "parameter.indicators.data_source.annual_puntual_rate.quantity.base_time";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_BASE_LOCATION_UUID                   = "parameter.indicators.data_source.annual_puntual_rate.quantity.base_location_uuid";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_BASE_QUANTITY_INDICATOR_UUID         = "parameter.indicators.data_source.annual_puntual_rate.quantity.base_quantity_indicator_uuid";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_MINIMUM                              = "parameter.indicators.data_source.annual_puntual_rate.quantity.minimum";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_MAXIMUM                              = "parameter.indicators.data_source.annual_puntual_rate.quantity.maximum";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_NUMERATOR_INDICATOR_UUID             = "parameter.indicators.data_source.annual_puntual_rate.quantity.numerator_indicator_uuid";
    public static final String DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_DENOMINATOR_INDICATOR_UUID           = "parameter.indicators.data_source.annual_puntual_rate.quantity.denominator_indicator_uuid";
    // Annual percentage
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE                                            = "parameter.indicators.data_source.annual_percentage_rate";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_METHOD_TYPE                                = "parameter.indicators.data_source.annual_percentage_rate.method_type";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_METHOD                                     = "parameter.indicators.data_source.annual_percentage_rate.method";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_ROUNDING                                   = "parameter.indicators.data_source.annual_percentage_rate.rounding";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY                                   = "parameter.indicators.data_source.annual_percentage_rate.quantity";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_TYPE                              = "parameter.indicators.data_source.annual_percentage_rate.quantity.type";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_UNIT_UUID                         = "parameter.indicators.data_source.annual_percentage_rate.quantity.unit_uuid";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_UNIT_MULTIPLIER                   = "parameter.indicators.data_source.annual_percentage_rate.quantity.unit_multiplier";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_DECIMAL_PLACES                    = "parameter.indicators.data_source.annual_percentage_rate.quantity.decimal_places";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_IS_PERCENTAGE                     = "parameter.indicators.data_source.annual_percentage_rate.quantity.is_percentage";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_PERCENTAGE_OF                     = "parameter.indicators.data_source.annual_percentage_rate.quantity.percentage_of";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_BASE_VALUE                        = "parameter.indicators.data_source.annual_percentage_rate.quantity.base_value";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_BASE_TIME                         = "parameter.indicators.data_source.annual_percentage_rate.quantity.base_time";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_BASE_LOCATION_UUID                = "parameter.indicators.data_source.annual_percentage_rate.quantity.base_location_uuid";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_BASE_QUANTITY_INDICATOR_UUID      = "parameter.indicators.data_source.annual_percentage_rate.quantity.base_quantity_indicator_uuid";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_MINIMUM                           = "parameter.indicators.data_source.annual_percentage_rate.quantity.minimum";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_MAXIMUM                           = "parameter.indicators.data_source.annual_percentage_rate.quantity.maximum";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_NUMERATOR_INDICATOR_UUID          = "parameter.indicators.data_source.annual_percentage_rate.quantity.numerator_indicator_uuid";
    public static final String DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_DENOMINATOR_INDICATOR_UUID        = "parameter.indicators.data_source.annual_percentage_rate.quantity.denominator_indicator_uuid";
    // Interperiod puntual
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE                                          = "parameter.indicators.data_source.interperiod_puntual_rate";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_METHOD_TYPE                              = "parameter.indicators.data_source.interperiod_puntual_rate.method_type";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_METHOD                                   = "parameter.indicators.data_source.interperiod_puntual_rate.method";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_ROUNDING                                 = "parameter.indicators.data_source.interperiod_puntual_rate.rounding";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY                                 = "parameter.indicators.data_source.interperiod_puntual_rate.quantity";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_TYPE                            = "parameter.indicators.data_source.interperiod_puntual_rate.quantity.type";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_UNIT_UUID                       = "parameter.indicators.data_source.interperiod_puntual_rate.quantity.unit_uuid";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_UNIT_MULTIPLIER                 = "parameter.indicators.data_source.interperiod_puntual_rate.quantity.unit_multiplier";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_DECIMAL_PLACES                  = "parameter.indicators.data_source.interperiod_puntual_rate.quantity.decimal_places";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_IS_PERCENTAGE                   = "parameter.indicators.data_source.interperiod_puntual_rate.quantity.is_percentage";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_PERCENTAGE_OF                   = "parameter.indicators.data_source.interperiod_puntual_rate.quantity.percentage_of";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_BASE_VALUE                      = "parameter.indicators.data_source.interperiod_puntual_rate.quantity.base_value";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_BASE_TIME                       = "parameter.indicators.data_source.interperiod_puntual_rate.quantity.base_time";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_BASE_LOCATION_UUID              = "parameter.indicators.data_source.interperiod_puntual_rate.quantity.base_location_uuid";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_BASE_QUANTITY_INDICATOR_UUID    = "parameter.indicators.data_source.interperiod_puntual_rate.quantity.base_quantity_indicator_uuid";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_MINIMUM                         = "parameter.indicators.data_source.interperiod_puntual_rate.quantity.minimum";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_MAXIMUM                         = "parameter.indicators.data_source.interperiod_puntual_rate.quantity.maximum";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_NUMERATOR_INDICATOR_UUID        = "parameter.indicators.data_source.interperiod_puntual_rate.quantity.numerator_indicator_uuid";
    public static final String DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_DENOMINATOR_INDICATOR_UUID      = "parameter.indicators.data_source.interperiod_puntual_rate.quantity.denominator_indicator_uuid";
    // Interperiod percentage
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE                                       = "parameter.indicators.data_source.interperiod_percentage_rate";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_METHOD_TYPE                           = "parameter.indicators.data_source.interperiod_percentage_rate.method_type";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_METHOD                                = "parameter.indicators.data_source.interperiod_percentage_rate.method";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_ROUNDING                              = "parameter.indicators.data_source.interperiod_percentage_rate.rounding";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY                              = "parameter.indicators.data_source.interperiod_percentage_rate.quantity";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_TYPE                         = "parameter.indicators.data_source.interperiod_percentage_rate.quantity.type";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_UNIT_UUID                    = "parameter.indicators.data_source.interperiod_percentage_rate.quantity.unit_uuid";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_UNIT_MULTIPLIER              = "parameter.indicators.data_source.interperiod_percentage_rate.quantity.unit_multiplier";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_DECIMAL_PLACES               = "parameter.indicators.data_source.interperiod_percentage_rate.quantity.decimal_places";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_IS_PERCENTAGE                = "parameter.indicators.data_source.interperiod_percentage_rate.quantity.is_percentage";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_PERCENTAGE_OF                = "parameter.indicators.data_source.interperiod_percentage_rate.quantity.percentage_of";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_BASE_VALUE                   = "parameter.indicators.data_source.interperiod_percentage_rate.quantity.base_value";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_BASE_TIME                    = "parameter.indicators.data_source.interperiod_percentage_rate.quantity.base_time";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_BASE_LOCATION_UUID           = "parameter.indicators.data_source.interperiod_percentage_rate.quantity.base_location_uuid";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_BASE_QUANTITY_INDICATOR_UUID = "parameter.indicators.data_source.interperiod_percentage_rate.quantity.base_quantity_indicator_uuid";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_MINIMUM                      = "parameter.indicators.data_source.interperiod_percentage_rate.quantity.minimum";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_MAXIMUM                      = "parameter.indicators.data_source.interperiod_percentage_rate.quantity.maximum";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_NUMERATOR_INDICATOR_UUID     = "parameter.indicators.data_source.interperiod_percentage_rate.quantity.numerator_indicator_uuid";
    public static final String DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_DENOMINATOR_INDICATOR_UUID   = "parameter.indicators.data_source.interperiod_percentage_rate.quantity.denominator_indicator_uuid";

    // Granularities and values
    public static final String GEOGRAPHICAL_VALUE                                                            = "parameter.indicators.geographical_value";
    public static final String GEOGRAPHICAL_VALUE_UUID                                                       = "parameter.indicators.geographical_value_uuid";
    public static final String GEOGRAPHICAL_VALUE_CODE                                                       = "parameter.indicators.geographical_value_code";
    public static final String GEOGRAPHICAL_VALUE_ORDER                                                      = "parameter.indicators.geographical_value_order";
    public static final String GEOGRAPHICAL_VALUE_GRANULARITY                                                = "parameter.indicators.geographical_value_granularity";

    public static final String GEOGRAPHICAL_GRANULARITY                                                      = "parameter.indicators.geographical_granularity";
    public static final String GEOGRAPHICAL_GRANULARITY_UUID                                                 = "parameter.indicators.geographical_granularity.uuid";
    public static final String GEOGRAPHICAL_GRANULARITY_CODE                                                 = "parameter.indicators.geographical_granularity.code";

    public static final String UNIT_MULTIPLIER                                                               = "parameter.indicators.unitMultiplier";
    public static final String UNIT_MULTIPLIER_VALUE                                                         = "parameter.indicators.unitMultiplier.value";
    public static final String UNIT_MULTIPLIER_UUID                                                          = "parameter.indicators.unitMultiplier.uuid";

    public static final String QUANTITY_UNIT                                                                 = "parameter.indicators.quantityUnit";
    public static final String QUANTITY_UNIT_UUID                                                            = "parameter.indicators.quantityUnit.uuid";
    public static final String QUANTITY_UNIT_TITLE                                                           = "parameter.indicators.quantityUnit.title";

    public static final String TIME_VALUE                                                                    = "parameter.indicators.time_value";
    public static final String TIME_GRANULARITY                                                              = "parameter.indicators.time_granularity";
    public static final String MEASURE_VALUE                                                                 = "parameter.indicators.measure_value";

    // Miscellaneous
    public static final String SUBJECT_CODE                                                                  = "parameter.indicators.subject_code";
    public static final String INDICATOR_CODES                                                               = "parameter.indicators.indicator_codes";
    public static final String INDICATOR_INSTANCES_CODES                                                     = "parameter.indicators.indicator_instances_codes";
    public static final String GEO_CODE                                                                      = "parameter.indicators.geographical_code";
    public static final String MEASURE_VALUES                                                                = "parameter.indicators.measure_values";
    public static final String NUM_RESULTS                                                                   = "parameter.indicators.num_results";
    public static final String DSPL_DATASET_TITLE                                                            = "parameter.indicators.dataset_title";

}
