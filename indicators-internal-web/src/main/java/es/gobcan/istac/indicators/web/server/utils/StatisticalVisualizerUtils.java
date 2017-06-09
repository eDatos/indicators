package es.gobcan.istac.indicators.web.server.utils;

import org.siemac.metamac.rest.utils.RestUtils;

public class StatisticalVisualizerUtils {

    public static String STATISTICAL_VISUALIZER_RESOURCE_TYPE_PARAM                    = "resourceType";
    public static String STATISTICAL_VISUALIZER_RESOURCE_ID_PARAM                      = "resourceId";
    public static String STATISTICAL_VISUALIZER_INDICATOR_SYSTEM_PARAM                 = "indicatorSystem";
    public static String STATISTICAL_VISUALIZER_RESOURCE_TYPE_INDICATOR_INSTANCE_VALUE = "indicatorInstance";
    public static String STATISTICAL_VISUALIZER_RESOURCE_TYPE_INDICATOR_VALUE          = "indicator";
    public static String STATISTICAL_VISUALIZER_SUBPATH                                = "data.html";

    public static String buildIndicatorInstanceUrl(String visualizerEndpoint, String indicatorInstanceCode, String indicatorsSystemCode) {
        String indicatorInstanceUrl = visualizerEndpoint;
        indicatorInstanceUrl = RestUtils.createLink(indicatorInstanceUrl, STATISTICAL_VISUALIZER_SUBPATH);
        indicatorInstanceUrl = RestUtils.createLinkWithQueryParam(indicatorInstanceUrl, STATISTICAL_VISUALIZER_RESOURCE_ID_PARAM, indicatorInstanceCode);
        indicatorInstanceUrl = RestUtils.createLinkWithQueryParam(indicatorInstanceUrl, STATISTICAL_VISUALIZER_INDICATOR_SYSTEM_PARAM, indicatorsSystemCode);
        indicatorInstanceUrl = RestUtils.createLinkWithQueryParam(indicatorInstanceUrl, STATISTICAL_VISUALIZER_RESOURCE_TYPE_PARAM, STATISTICAL_VISUALIZER_RESOURCE_TYPE_INDICATOR_INSTANCE_VALUE);
        return indicatorInstanceUrl;
    }

    public static String buildIndicatorUrl(String visualizerEndpoint, String indicatorCode) {
        String indicatorUrl = visualizerEndpoint;
        indicatorUrl = RestUtils.createLink(indicatorUrl, STATISTICAL_VISUALIZER_SUBPATH);
        indicatorUrl = RestUtils.createLinkWithQueryParam(indicatorUrl, STATISTICAL_VISUALIZER_RESOURCE_ID_PARAM, indicatorCode);
        indicatorUrl = RestUtils.createLinkWithQueryParam(indicatorUrl, STATISTICAL_VISUALIZER_RESOURCE_TYPE_PARAM, STATISTICAL_VISUALIZER_RESOURCE_TYPE_INDICATOR_VALUE);
        return indicatorUrl;
    }

}
