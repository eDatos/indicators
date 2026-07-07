package es.gobcan.istac.indicators.core.service;

import java.util.List;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;

public interface NoticesRestInternalService {

    public static final String BEAN_ID = "noticesRestInternalService";

    // Background Notifications
    void createCreateReplaceDatasetErrorBackgroundNotification(IndicatorVersion indicatorVersion);
    void createAssignRolePermissionsDatasetErrorBackgroundNotification(String dataViewsRole, String viewCode);
    void createUpdateIndicatorsDataErrorBackgroundNotification(List<IndicatorVersion> failedPopulationIndicators);
    void createUpdateIndicatorsDataErrorFromKafkaMessageNotification(List<IndicatorVersion> failedPopulationIndicators, String urn);
    void createDeleteDatasetErrorBackgroundNotification(IndicatorVersion failedIndicator, String oldDatasetId);
    public void createConsumerFromKafkaErrorBackgroundNotification(String keyMessage);
    void createMaximumVersionReachedBackgroundNotification(IndicatorVersion indicatorVersion, VersionTypeEnum versionTypeEnum);
    void createPopulateIndicatorDataSuccessBackgroundNotification(String user, Indicator indicator);
    void createPopulateIndicatorDataErrorBackgroundNotification(String user, Indicator indicator, MetamacException metamacException);
    void createIndicatorStreamMessageErrorBackgroundNotification(IndicatorVersion indicatorVersion);
    void createIndicatorsSystemStreamMessageErrorBackgroundNotification(IndicatorsSystemVersion indicatorsSystemVersion);
    void createExportDSPLNotification(String user, String code, String url, List<String> files);
    void createExportDSPLErrorNotification(String user, String code,  MetamacException exception);
    void createUpdateCategoryCacheErrorNotification(String user, String actionCode, MetamacException exception);
    void createUpdateCategoryCacheDuplicateCategoryElementErrorNotification(String user, String actionCode, MetamacException exception);
    void updateGeopgraphicalValuesFromSrmVariableElementsErrorNotification(String actionCode, String messageParams, MetamacException exception);
    void updateGeographicalGranularitiesVisualisationOrderNotFoundErrorNotification(String codelistUrn, String visualisationOrder);
}
