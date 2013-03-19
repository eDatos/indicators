package es.gobcan.istac.indicators.web.client.utils;

import java.util.List;

import org.siemac.metamac.web.common.client.utils.CommonErrorUtils;

import es.gobcan.istac.indicators.web.client.IndicatorsWeb;

public class ErrorUtils extends CommonErrorUtils {

    public static List<String> getErrorMessages(Throwable caught, String alternativeMessage) {
        return getErrorMessages(IndicatorsWeb.getCoreMessages(), caught, alternativeMessage);
    }
}
