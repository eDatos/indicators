package es.gobcan.istac.indicators.rest;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;

public final class RestConstants extends org.siemac.metamac.rest.constants.RestConstants {

    private RestConstants() {
    }

    public static final ServiceContext SERVICE_CONTEXT                          = new ServiceContext("admin", "amdmin", "rest");

    public static final String         PARAMETER_Q                              = "q";
    public static final String         PARAMETER_ORDER                          = "order";
    public static final String         PARAMETER_FIELDS                         = "fields";
    public static final String         PARAMETER_REPRESENTATION                 = "representation";
    public static final String         PARAMETER_GRANULARITY                    = "granularity";
    public static final String         PARAMETER_SUBJECT_CODE                   = "subjectCode";
    public static final String         PARAMETER_SYSTEM_CODE                    = "systemCode";
    public static final String         GEOGRAPHICAL_GRANULARITY_CODE            = "geographicalGranularityCode";

    public static final String         KIND_INDICATOR                           = "indicators#indicator";
    public static final String         KIND_INDICATORS                          = "indicators#indicators";
    public static final String         KIND_INDICATOR_DATA                      = "indicators#indicatorData";
    public static final String         KIND_INDICATOR_SYSTEM                    = "indicators#indicatorsSystem";
    public static final String         KIND_INDICATOR_SYSTEMS                   = "indicators#indicatorsSystems";
    public static final String         KIND_INDICATOR_INSTANCE                  = "indicators#indicatorInstance";
    public static final String         KIND_INDICATOR_INSTANCES                 = "indicators#indicatorInstances";
    public static final String         KIND_INDICATOR_INSTANCE_DATA             = "indicators#indicatorInstanceData";
    public static final String         KIND_INDICATOR_DIMENSION                 = "indicators#dimension";
    public static final String         KIND_GEOGRAPHICAL_VALUES                 = "indicators#geographicalValues";
    public static final String         KIND_TIME_GRANULARITIES                  = "indicators#timeGranularities";
    public static final String         KIND_GEOGRAPHICAL_GRANULARITIES          = "indicators#geographicalGranularities";
    public static final String         KIND_SUBJECTS                            = "indicators#subjects";

    public static final String         KIND_STATISTICAL_OPERATION               = TypeExternalArtefactsEnum.STATISTICAL_OPERATION.getValue();

    public static final String         API_INDICATORS_VERSION                   = "v1.0";
    public static final String         API_INDICATORS_INDICATORS                = "indicators";

    public static final String         API_INDICATORS_TIME_GRANULARITIES        = "timeGranularities";

    public static final String         API_INDICATORS_GEOGRAPHIC_GRANULARITIES  = "geographicGranularities";
    public static final String         API_INDICATORS_GEOGRAPHICAL_VALUES       = "geographicalValues";
    public static final String         API_INDICATORS_SUBJECTS                  = "subjects";
    public static final String         API_INDICATORS_INDICATORS_SYSTEMS        = "indicatorsSystems";

    public static final String         API_INDICATORS_INDICATORS_DATA           = "data";
    public static final String         API_INDICATORS_INDICATORS_INSTANCES      = "indicatorsInstances";
    public static final String         API_INDICATORS_INDICATORS_INSTANCES_DATA = "data";

}
