package es.gobcan.istac.indicators.core.service;

import java.util.List;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;

public interface NoticesRestInternalService {

    public static final String BEAN_ID = "noticesRestInternalService";

    // Background Notifications
    void createCreateReplaceDatasetErrorBackgroundNotification(IndicatorVersion indicatorVersion);
    void createAssignRolePermissionsDatasetErrorBackgroundNotification(String dataViewsRole, String viewCode);
    void createUpdateIndicatorsDataErrorBackgroundNotification(List<IndicatorVersion> failedPopulationIndicators);
    void createDeleteDatasetErrorBackgroundNotification(IndicatorVersion failedIndicator, String oldDatasetId);
    public void createConsumerFromKafkaErrorBackgroundNotification(String keyMessage);
    void createMaximumVersionReachedBackgroundNotification(IndicatorVersion indicatorVersion, VersionTypeEnum versionTypeEnum);
    void createPopulateIndicatorDataSuccessBackgroundNotification(String user, Indicator indicator);
    void createPopulateIndicatorDataErrorBackgroundNotification(String user, Indicator indicator, MetamacException metamacException);
}
