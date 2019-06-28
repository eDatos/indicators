package es.gobcan.istac.indicators.core.util;

import java.util.Arrays;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
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

    public static VersionResult createNextVersion(String olderVersionNumber, VersionTypeEnum versionType) throws MetamacException {
        try {
            return VersionUtil.createNextVersion(olderVersionNumber, versionType);
        } catch (UnsupportedOperationException e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.RESOURCE_MAXIMUM_VERSION_REACHED).withMessageParameters(versionType, olderVersionNumber).build();
        }
    }

    public static boolean equalsVersionNumber(String versionNumber, String otherVersionNumber) {
        return VersionUtil.versionStringToLong(versionNumber) == VersionUtil.versionStringToLong(otherVersionNumber);
    }

    public static void setVersionNumber(HasVersionNumber versionNumberEntity, String versionNumber, VersionTypeEnum versionTypeEnum, NoticesRestInternalService noticesRestInternalService,
            String resourceCode) throws MetamacException {
        // TODO INDISTAC-1054 Temporary, notifications are sent to the administrator, it's necessary to check it and define others roles if necessary
        try {
            VersionResult versionResult = createNextVersion(versionNumber, versionTypeEnum);

            versionNumberEntity.setVersionNumber(versionResult.getValue());

            sendMinorVersionExpectedMajorVersionOccurredBackgroudNotificationIfOccurred(versionTypeEnum, noticesRestInternalService, resourceCode, versionResult);
        } catch (MetamacException e) {
            sendMaximumVersionReachedBackgroundNotificationIfOccurred(versionNumber, versionTypeEnum, noticesRestInternalService, resourceCode, e);
            throw e;
        }
    }

    private static void sendMaximumVersionReachedBackgroundNotificationIfOccurred(String versionNumber, VersionTypeEnum versionTypeEnum, NoticesRestInternalService noticesRestInternalService,
            String resourceCode, MetamacException e) {
        if (checkResourceMaximumVersionReachedException(e)) {
            noticesRestInternalService.createMaximumVersionReachedBackgroundNotification(Arrays.asList(versionTypeEnum.getName(), resourceCode, versionNumber));
        }
    }

    private static void sendMinorVersionExpectedMajorVersionOccurredBackgroudNotificationIfOccurred(VersionTypeEnum versionTypeEnum, NoticesRestInternalService noticesRestInternalService,
            String resourceCode, VersionResult versionResult) {
        if (checkMinorVersionExpectedMajorVersionOccurred(versionTypeEnum, versionResult)) {
            noticesRestInternalService.createMinorVersionExpectedMajorVersionOccurredBackgroundNotification(resourceCode);
        }
    }

    private static boolean checkMinorVersionExpectedMajorVersionOccurred(VersionTypeEnum versionTypeEnum, VersionResult versionResult) {
        return VersionTypeEnum.MINOR.equals(versionTypeEnum) && VersionTypeEnum.MAJOR.equals(versionResult.getType());
    }

    private static boolean checkResourceMaximumVersionReachedException(MetamacException metamacException) {
        return CollectionUtils.exists(metamacException.getExceptionItems(), new Predicate() {

            @Override
            public boolean evaluate(Object object) {
                return ServiceExceptionType.RESOURCE_MAXIMUM_VERSION_REACHED.getCode().equals(((MetamacExceptionItem) object).getCode());
            }
        });
    }
}
