package es.gobcan.istac.indicators.core.navigation;

import java.text.MessageFormat;

import es.gobcan.istac.indicators.core.navigation.shared.NameTokens;
import es.gobcan.istac.indicators.core.navigation.shared.PlaceRequestParams;


public class InternalWebApplicationNavigation {

    private static final String ANCHOR    = "#";
    private static final String SEPARATOR = "/";
    private static final String SEMICOLON = ";";
    private static final String EQUALS    = "=";

    private static final String indicatorPattern;
    private String webApplicationPath;

    static {
        indicatorPattern = "{0}" + SEPARATOR + ANCHOR + NameTokens.indicatorPage + SEMICOLON + PlaceRequestParams.indicatorParam + EQUALS + "{1}";
    }
    
    public InternalWebApplicationNavigation(String webApplicationPath) {
        this.webApplicationPath = webApplicationPath;
    }

    public String buildIndicatorUrl(String indicatorUuid) {
        return MessageFormat.format(indicatorPattern, this.webApplicationPath, indicatorUuid);
    }
}