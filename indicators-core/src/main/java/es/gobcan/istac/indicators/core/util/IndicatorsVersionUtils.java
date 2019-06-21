package es.gobcan.istac.indicators.core.util;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.shared.VersionResult;
import org.siemac.metamac.core.common.util.shared.VersionUtil;

import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

public class IndicatorsVersionUtils {

    public static final String INITIAL_VERSION = VersionUtil.INITIAL_VERSION;

    private IndicatorsVersionUtils() {

    }

    public static Boolean isInitialVersion(String version) {
        return VersionUtil.isInitialVersion(version);
    }

    public static VersionResult createNextVersion(String olderVersion, VersionTypeEnum versionType) throws MetamacException {
        try {
            return VersionUtil.createNextVersion(olderVersion, versionType);
        } catch (UnsupportedOperationException e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.RESOURCE_MAXIMUM_VERSION_REACHED).withMessageParameters(versionType, olderVersion).build();
        }
    }
}
