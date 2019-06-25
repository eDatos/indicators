package es.gobcan.istac.indicators.core.service;

import java.util.List;

import es.gobcan.istac.indicators.core.domain.IndicatorVersion;

public interface NoticesRestInternalService {

    public static final String BEAN_ID = "noticesRestInternalService";

    // Background Notifications
    void createCreateReplaceDatasetErrorBackgroundNotification(IndicatorVersion indicatorVersion);
    void createAssignRolePermissionsDatasetErrorBackgroundNotification(String dataViewsRole, String viewCode);
    void createUpdateIndicatorsDataErrorBackgroundNotification(List<IndicatorVersion> failedPopulationIndicators);
    void createDeleteDatasetErrorBackgroundNotification(IndicatorVersion failedIndicator, String oldDatasetId);
    public void createConsumerFromKafkaErrorBackgroundNotification(String keyMessage);
    void createMinorVersionExpectedMajorVersionOccurredBackgroundNotification(String resourceCode);
    void createMaximumVersionReachedBackgroundNotification(List<String> messageParams);

}
