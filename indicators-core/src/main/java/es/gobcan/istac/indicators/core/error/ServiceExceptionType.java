package es.gobcan.istac.indicators.core.error;

import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;

public class ServiceExceptionType extends CommonServiceExceptionType {

    // Indicators systems
    public static final CommonServiceExceptionType INDICATORS_SYSTEM_NOT_FOUND                              = create("exception.indicators.indicators_system.not_found");
    public static final CommonServiceExceptionType INDICATORS_SYSTEM_VERSION_NOT_FOUND                      = create("exception.indicators.indicators_system.vesion_not_found");
    public static final CommonServiceExceptionType INDICATORS_SYSTEM_NOT_FOUND_WITH_CODE                    = create("exception.indicators.indicators_system.not_found_code");
    public static final CommonServiceExceptionType INDICATORS_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED          = create("exception.indicators.indicators_system.already_exists.code_duplicated");
    public static final CommonServiceExceptionType INDICATORS_SYSTEM_IN_PRODUCTION_NOT_FOUND                = create("exception.indicators.indicators_system.production_not_found");
    public static final CommonServiceExceptionType INDICATORS_SYSTEM_IN_DIFFUSION_NOT_FOUND                 = create("exception.indicators.indicators_system.diffusion_not_found");
    public static final CommonServiceExceptionType INDICATORS_SYSTEM_WRONG_PROC_STATUS                      = create("exception.indicators.indicators_system.wrong_proc_status");
    public static final CommonServiceExceptionType INDICATORS_SYSTEM_MUST_HAVE_INDICATOR_INSTANCE           = create("exception.indicators.indicators_system.must_have_indicator_instance");
    public static final CommonServiceExceptionType INDICATORS_SYSTEM_MUST_HAVE_ALL_INDICATORS_PUBLISHED     = create("exception.indicators.indicators_system.all_indicators_must_be_published");

    // Dimensions
    public static final CommonServiceExceptionType DIMENSION_NOT_FOUND                                      = create("exception.indicators.dimension.not_found");
    public static final CommonServiceExceptionType DIMENSION_NOT_FOUND_IN_INDICATORS_SYSTEM                 = create("exception.indicators.dimension.not_found_in_indicators_system");

    // Indicator instances
    public static final CommonServiceExceptionType INDICATOR_INSTANCE_NOT_FOUND                             = create("exception.indicators.indicator_instance.not_found");
    public static final CommonServiceExceptionType INDICATOR_NOT_FOUND_WITH_CODE                            = create("exception.indicators.indicator.not_found_code");
    public static final CommonServiceExceptionType INDICATOR_INSTANCE_ALREADY_EXIST_INDICATOR_SAME_LEVEL    = create("exception.indicators.indicator_instance.already_exists.indicator_in_same_level");

    // Geographical values
    public static final CommonServiceExceptionType GEOGRAPHICAL_VALUE_NOT_FOUND                             = create("exception.indicators.geographical_value.not_found");

    // Geographical granularities
    public static final CommonServiceExceptionType GEOGRAPHICAL_GRANULARITY_NOT_FOUND                       = create("exception.indicators.geographical_granularity.not_found");

    // Indicators
    public static final CommonServiceExceptionType INDICATOR_NOT_FOUND                                      = create("exception.indicators.indicator.not_found");
    public static final CommonServiceExceptionType INDICATOR_VERSION_NOT_FOUND                              = create("exception.indicators.indicator.version_not_found");
    public static final CommonServiceExceptionType INDICATOR_ALREADY_EXIST_CODE_DUPLICATED                  = create("exception.indicators.indicator.already_exists.code_duplicated");
    public static final CommonServiceExceptionType INDICATOR_IN_PRODUCTION_NOT_FOUND                        = create("exception.indicators.indicator.production_not_found");
    public static final CommonServiceExceptionType INDICATOR_IN_DIFFUSION_NOT_FOUND                         = create("exception.indicators.indicator.diffusion_not_found");
    public static final CommonServiceExceptionType INDICATOR_WRONG_PROC_STATUS                              = create("exception.indicators.indicator.wrong_proc_status");
    public static final CommonServiceExceptionType INDICATOR_MUST_HAVE_DATA_SOURCES                         = create("exception.indicators.indicator.must_have_data_sources");
    public static final CommonServiceExceptionType INDICATOR_MUST_NOT_BE_IN_INDICATORS_SYSTEMS              = create("exception.indicators.indicator.must_not_be_in_indicators_systems");
    public static final CommonServiceExceptionType INDICATOR_MUST_NOT_BE_LINKED_TO_OTHER_INDICATOR          = create("exception.indicators.indicator.must_not_be_linked_to_other_indicator");
    public static final CommonServiceExceptionType INDICATOR_MUST_HAVE_ALL_LINKED_INDICATORS_PUBLISHED      = create("exception.indicators.indicator.all_indicators_must_be_published");
    public static final CommonServiceExceptionType INDICATOR_NOT_POPULATED                                  = create("exception.indicators.indicator.not_populated");

    // Datasources
    public static final CommonServiceExceptionType DATA_SOURCE_NOT_FOUND                                    = create("exception.indicators.data_source.not_found");

    // Quantity units
    public static final CommonServiceExceptionType QUANTITY_UNIT_NOT_FOUND                                  = create("exception.indicators.quantity_unit.not_found");

    // Subjects
    public static final CommonServiceExceptionType SUBJECT_NOT_FOUND                                        = create("exception.indicators.subject.not_found");

    // Data
    public static final CommonServiceExceptionType DATA_DEFINITION_RETRIEVE_ERROR                           = create("exception.indicators.data.definition.error");
    public static final CommonServiceExceptionType DATA_STRUCTURE_RETRIEVE_ERROR                            = create("exception.indicators.data.structure.error");
    public static final CommonServiceExceptionType DATA_RETRIEVE_ERROR                                      = create("exception.indicators.data.error");

    public static final CommonServiceExceptionType DATA_POPULATE_ERROR                                      = create("exception.indicators.data.populate.error");
    public static final CommonServiceExceptionType DATA_POPULATE_UNKNOWN_METHOD_TYPE                        = create("exception.indicators.data.populate.unknown.methodtype.error");
    public static final CommonServiceExceptionType DATA_POPULATE_DATASETREPO_CREATE_ERROR                   = create("exception.indicators.data.populate.datasetrepo.create.error");
    public static final CommonServiceExceptionType DATA_POPULATE_DATASETREPO_FIND_ERROR                     = create("exception.indicators.data.populate.datasetrepo.find.error");
    public static final CommonServiceExceptionType DATA_POPULATE_INVALID_CONTVARIABLE_LOAD_METHOD           = create("exception.indicators.data.populate.invalid.contvariable.load.method");
    public static final CommonServiceExceptionType DATA_POPULATE_INVALID_NOCONTVARIABLE_LOAD_METHOD         = create("exception.indicators.data.populate.invalid.nocontvariable.load.method");
    public static final CommonServiceExceptionType DATA_POPULATE_OBSERVATION_CALCULATE_ERROR                = create("exception.indicators.data.populate.calculate.observation.parse.error");
    public static final CommonServiceExceptionType DATA_POPULATE_RETRIEVE_DATA_ERROR                        = create("exception.indicators.data.populate.retrieve.data.error");
    public static final CommonServiceExceptionType DATA_POPULATE_RETRIEVE_DATA_EMPTY                        = create("exception.indicators.data.populate.retrieve.data.empty");
    public static final CommonServiceExceptionType DATA_POPULATE_UNKNOWN_GEOGRAPHIC_VALUE                   = create("exception.indicators.data.populate.unknown_geographic_value");
    public static final CommonServiceExceptionType DATA_POPULATE_UNKNOWN_TIME_VALUE                         = create("exception.indicators.data.populate.unknown_time_value");

    public static final CommonServiceExceptionType DATA_UPDATE_INDICATORS_GPE_CHECK_ERROR                   = create("exception.indicators.data.update.indicators.gpe.check.error");

    public static final CommonServiceExceptionType CONFIGURATION_NOT_FOUND                                  = create("exception.indicators.configuration.not_found");

    public static final CommonServiceExceptionType DATA_COMPATIBILITY_GEOGRAPHIC_VARIABLE_NOT_EXISTS        = create("exception.indicators.data.compatibility.geografic_var_not_exist");
    public static final CommonServiceExceptionType DATA_COMPATIBILITY_GEOGRAPHIC_VARIABLE_NOT_GEOGRAPHIC    = create("exception.indicators.data.compatibility.geografic_var_not_geographic");
    public static final CommonServiceExceptionType DATA_COMPATIBILITY_GEOGRAPHIC_VALUE_ILLEGAL              = create("exception.indicators.data.compatibility.geografic_value_illegal");
    public static final CommonServiceExceptionType DATA_COMPATIBILITY_TIME_VARIABLE_NOT_EXISTS              = create("exception.indicators.data.compatibility.time_var_not_exist");
    public static final CommonServiceExceptionType DATA_COMPATIBILITY_TIME_VARIABLE_NOT_TEMPORAL            = create("exception.indicators.data.compatibility.time_var_not_temporal");
    public static final CommonServiceExceptionType DATA_COMPATIBILITY_TIME_VALUE_ILLEGAL                    = create("exception.indicators.data.compatibility.time_value_illegal");
    public static final CommonServiceExceptionType DATA_COMPATIBILITY_ABSMETHOD_NO_CONTVARIABLE_ILLEGAL     = create("exception.indicators.data.compatibility.absmethod_nocontvariable_illegal");
    public static final CommonServiceExceptionType DATA_COMPATIBILITY_ABSMETHOD_CONTVARIABLE_ILLEGAL        = create("exception.indicators.data.compatibility.absmethod_contvariable_illegal");
    public static final CommonServiceExceptionType DATA_COMPATIBILITY_RATE_METHOD_NO_CONTVARIABLE_ILLEGAL   = create("exception.indicators.data.compatibility.rate_method_nocontvariable_illegal");
    public static final CommonServiceExceptionType DATA_COMPATIBILITY_RATE_METHOD_CONTVARIABLE_ILLEGAL      = create("exception.indicators.data.compatibility.rate_method_contvariable_illegal");

    public static final CommonServiceExceptionType DATA_COMPATIBILITY_OTHER_VARIABLES_GEOGRAPHIC_INCLUDED   = create("exception.indicators.data.compatibility.other_variables_geographic_included");
    public static final CommonServiceExceptionType DATA_COMPATIBILITY_OTHER_VARIABLES_TEMPORAL_INCLUDED     = create("exception.indicators.data.compatibility.other_variables_temporal_included");
    public static final CommonServiceExceptionType DATA_COMPATIBILITY_OTHER_VARIABLES_CONTVARIABLE_INCLUDED = create("exception.indicators.data.compatibility.other_variables_contvariable_included");
    public static final CommonServiceExceptionType DATA_COMPATIBILITY_OTHER_VARIABLES_UNSPECIFIED_VARIABLES = create("exception.indicators.data.compatibility.other_variables_unspecified_variables");
    public static final CommonServiceExceptionType DATA_COMPATIBILITY_OTHER_VARIABLES_UNKNOWN_VARIABLES     = create("exception.indicators.data.compatibility.other_variables_unknown_variables");

    public static final CommonServiceExceptionType DATA_FIND_OBSERVATIONS_ERROR                             = create("exception.indicators.data.find_observations_error");
    public static final CommonServiceExceptionType DATA_FIND_OBSERVATIONS_EXTENDED_ERROR                    = create("exception.indicators.data.find_observations_extended_error");
    public static final CommonServiceExceptionType DATA_INSTANCES_FIND_OBSERVATIONS_ERROR                   = create("exception.indicators.data.instances.find_observations_error");
    public static final CommonServiceExceptionType DATA_INSTANCES_FIND_OBSERVATIONS_EXTENDED_ERROR          = create("exception.indicators.data.instances.find_observations_extended_error");

}
