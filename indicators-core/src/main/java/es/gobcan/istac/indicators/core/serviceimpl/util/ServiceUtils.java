package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.enume.domain.VersiontTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

public class ServiceUtils {

    private static final NumberFormat formatterMajor = new DecimalFormat("0");
    private static final NumberFormat formatterMinor = new DecimalFormat("000");

    public static String generateVersionNumber(String actualVersionNumber, VersiontTypeEnum versionType) throws MetamacException {

        if (actualVersionNumber == null) {
            return IndicatorsConstants.VERSION_NUMBER_INITIAL;
        }

        String[] versionNumberSplited = actualVersionNumber.split("\\.");
        Integer versionNumberMajor = Integer.valueOf(versionNumberSplited[0]);
        Integer versionNumberMinor = Integer.valueOf(versionNumberSplited[1]);

        if (VersiontTypeEnum.MAJOR.equals(versionType)) {
            versionNumberMajor++;
            versionNumberMinor = 0;
        } else if (VersiontTypeEnum.MINOR.equals(versionType)) {
            versionNumberMinor++;
        } else {
            throw new MetamacException(ServiceExceptionType.PARAMETER_UNEXPECTED, versionType, VersiontTypeEnum.class);
        }
        return (new StringBuilder()).append(formatterMajor.format(versionNumberMajor)).append(".").append(formatterMinor.format(versionNumberMinor)).toString();
    }
}