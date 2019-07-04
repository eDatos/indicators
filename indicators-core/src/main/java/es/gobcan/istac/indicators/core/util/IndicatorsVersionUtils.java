package es.gobcan.istac.indicators.core.util;

import java.util.Arrays;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.core.common.util.shared.VersionResult;
import org.siemac.metamac.core.common.util.shared.VersionUtil;

import es.gobcan.istac.indicators.core.domain.HasVersionNumber;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.service.NoticesRestInternalService;

public class IndicatorsVersionUtils {

    public static final String INITIAL_VERSION = VersionUtil.INITIAL_VERSION;

    private IndicatorsVersionUtils() {

    }

    public static Boolean isInitialVersion(String version) {
        return VersionUtil.isInitialVersion(version);
    }

    public static VersionResult createNextVersion(String olderVersionNumber, VersionTypeEnum versionType, String resourceCode) throws MetamacException {
        try {
            return VersionUtil.createNextVersion(olderVersionNumber, versionType);
        } catch (UnsupportedOperationException e) {
            sendMaximumVersionReachedBackgroundNotification(olderVersionNumber, versionType, resourceCode);

            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.RESOURCE_MAXIMUM_VERSION_REACHED).withMessageParameters(versionType, olderVersionNumber).build();
        }
    }

    public static boolean equalsVersionNumber(String versionNumber, String otherVersionNumber) {
        return VersionUtil.versionStringToLong(versionNumber) == VersionUtil.versionStringToLong(otherVersionNumber);
    }

    public static void setVersionNumber(HasVersionNumber versionNumberEntity, String versionNumber, VersionTypeEnum versionTypeEnum, String resourceCode) throws MetamacException {
        versionNumberEntity.setVersionNumber(createNextVersion(versionNumber, versionTypeEnum, resourceCode).getValue());
    }

    private static void sendMaximumVersionReachedBackgroundNotification(String versionNumber, VersionTypeEnum versionTypeEnum, String resourceCode) {
        getNoticesRestInternalService().createMaximumVersionReachedBackgroundNotification(Arrays.asList(versionTypeEnum.getName(), resourceCode, versionNumber));
    }

    private static NoticesRestInternalService getNoticesRestInternalService() {
        return (NoticesRestInternalService) ApplicationContextProvider.getApplicationContext().getBean(NoticesRestInternalService.BEAN_ID);
    }
}
