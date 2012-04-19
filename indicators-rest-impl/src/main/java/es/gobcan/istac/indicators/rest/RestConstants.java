package es.gobcan.istac.indicators.rest;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;

public class RestConstants {

    public static ServiceContext SERVICE_CONTEXT                     = new ServiceContext("admin", "amdmin", "rest");

    public static String         KIND_INDICATOR_SYSTEM               = "indicators#indicatorsSystem";
    public static String         KIND_INDICATOR_SYSTEMS              = "indicators#indicatorsSystems";
    public static String         KIND_INDICATOR_DIMENSION            = "indicators#indicatorDimension";
    public static String         KIND_INDICATOR_INSTANCE             = "indicators#indicatorInstance";

    public static String         KIND_STATISTICAL_OPERATION          = "statisticaOperation#operation";           // TODO

    public static String         API_SLASH                           = "/";
    public static String         API_INDICATORS_BASE                 = "/api/indicators/v1.0";
    public static String         API_INDICATORS_INDICATORS_SYSTEMS   = "/indicatorsSystems";
    public static String         API_INDICATORS_INDICATORS_INSTANCES = "/indicatorsInstances";

    private RestConstants() {
    }

}
