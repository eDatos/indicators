package es.gobcan.istac.indicators.core.util;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.core.common.util.shared.VersionUtil;

import es.gobcan.istac.indicators.core.domain.HasVersionNumber;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.service.NoticesRestInternalService;

public class IndicatorsVersionUtils {

    public static final String INITIAL_VERSION = VersionUtil.INITIAL_VERSION;

    private IndicatorsVersionUtils() {

    }

    public static Boolean isInitialVersion(String version) {
        return VersionUtil.isInitialVersion(version);
    }

    public static String createNextVersion(HasVersionNumber versionNumberEntity, VersionTypeEnum versionType) throws MetamacException {
        try {
            return VersionUtil.createNextVersion(versionNumberEntity.getVersionNumber(), versionType).getValue();
        } catch (UnsupportedOperationException e) {

            sendMaximumVersionReachedBackgroundNotification(versionNumberEntity, versionType);
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.RESOURCE_MAXIMUM_VERSION_REACHED).withMessageParameters(versionType, versionNumberEntity.getVersionNumber())
                    .build();
        }
    }

    public static boolean equalsVersionNumber(String versionNumber, String otherVersionNumber) {
        return VersionUtil.versionStringToLong(versionNumber) == VersionUtil.versionStringToLong(otherVersionNumber);
    }

    private static void sendMaximumVersionReachedBackgroundNotification(HasVersionNumber hasVersionNumberEntity, VersionTypeEnum versionTypeEnum) {
        if (hasVersionNumberEntity instanceof IndicatorVersion) {
            getNoticesRestInternalService().createMaximumVersionReachedBackgroundNotification((IndicatorVersion) hasVersionNumberEntity, versionTypeEnum);
        }
    }

    private static NoticesRestInternalService getNoticesRestInternalService() {
        return (NoticesRestInternalService) ApplicationContextProvider.getApplicationContext().getBean(NoticesRestInternalService.BEAN_ID);
    }
}
