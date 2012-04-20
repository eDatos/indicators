package es.gobcan.istac.indicators.rest;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;

public class RestConstants {

    public static ServiceContext SERVICE_CONTEXT                          = new ServiceContext("admin", "amdmin", "rest");

    public static String         KIND_INDICATOR                           = "indicators#indicator";
    public static String         KIND_INDICATORS                          = "indicators#indicators";
    public static String         KIND_INDICATOR_DATA                      = "indicators#indicatorData";
    public static String         KIND_INDICATOR_SYSTEM                    = "indicators#indicatorsSystem";
    public static String         KIND_INDICATOR_SYSTEMS                   = "indicators#indicatorsSystems";
    public static String         KIND_INDICATOR_INSTANCE                  = "indicators#indicatorInstance";
    public static String         KIND_INDICATOR_INSTANCES                 = "indicators#indicatorInstances";
    public static String         KIND_INDICATOR_INSTANCE_DATA             = "indicators#indicatorInstanceData";
    public static String         KIND_INDICATOR_DIMENSION                 = "indicators#dimension";

    public static String         KIND_STATISTICAL_OPERATION               = "statisticaOperation#operation";              // TODO

    public static String         API_SLASH                                = "/";
    public static String         API_INDICATORS_BASE                      = "/api/indicators/v1.0";
    public static String         API_INDICATORS_RESOURCES                 = "resources";
    public static String         API_INDICATORS_INDICATORS                = "indicators";
    public static String         API_INDICATORS_INDICATORS_DATA           = "data";
    public static String         API_INDICATORS_INDICATORS_SYSTEMS        = "indicatorsSystems";
    public static String         API_INDICATORS_INDICATORS_INSTANCES      = "indicatorsInstances";
    public static String         API_INDICATORS_INDICATORS_INSTANCES_DATA = "data";

    private RestConstants() {
    }

}
