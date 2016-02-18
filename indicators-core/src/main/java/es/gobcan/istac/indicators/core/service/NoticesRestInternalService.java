package es.gobcan.istac.indicators.core.service;

import java.util.List;

import es.gobcan.istac.indicators.core.domain.IndicatorVersion;

public interface NoticesRestInternalService {

    public static final String BEAN_ID = "noticesRestInternalService";

    // Background Notifications
    public void createBackgroundNotification(String actionCode, String messageCode, List<IndicatorVersion> failedPopulationIndicators, Object... messageParams);

    public void createCreateReplaceDatasetErrorBackgroundNotification(IndicatorVersion indicatorVersion);

    public void createAssignRolePermissionsDatasetErrorBackgroundNotification(String dataViewsRole, String viewCode);

    public void createUpdateIndicatorsDataErrorBackgroundNotification(List<IndicatorVersion> failedPopulationIndicators);

    public void createDeleteDatasetErrorBackgroundNotification(IndicatorVersion failedIndicator, String oldDatasetId);

}
