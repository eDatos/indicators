package es.gobcan.istac.indicators.core.service;

import java.util.List;

import es.gobcan.istac.indicators.core.domain.IndicatorVersion;

public interface NoticesRestInternalService {

    public static final String BEAN_ID = "noticesRestInternalService";

    // Background Notifications
    public void createErrorBackgroundNotification(String user, String actionCode, String message, List<IndicatorVersion> failedPopulationIndicators);    

}
