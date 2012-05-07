package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

public class ServiceUtils {

    private static final NumberFormat formatterMajor = new DecimalFormat("0");
    private static final NumberFormat formatterMinor = new DecimalFormat("000");
    private static final String SEPARATOR_LIST_DTO_TO_STRING_DO = "##";

    public static String generateVersionNumber(String actualVersionNumber, VersionTypeEnum versionType) throws MetamacException {

        if (actualVersionNumber == null) {
            return IndicatorsConstants.VERSION_NUMBER_INITIAL;
        }

        String[] versionNumberSplited = actualVersionNumber.split("\\.");
        Integer versionNumberMajor = Integer.valueOf(versionNumberSplited[0]);
        Integer versionNumberMinor = Integer.valueOf(versionNumberSplited[1]);

        if (VersionTypeEnum.MAJOR.equals(versionType)) {
            versionNumberMajor++;
            versionNumberMinor = 0;
        } else if (VersionTypeEnum.MINOR.equals(versionType)) {
            versionNumberMinor++;
        } else {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "Unsupported value for " + VersionTypeEnum.class.getCanonicalName() + ": " + versionType);
        }
        return (new StringBuilder()).append(formatterMajor.format(versionNumberMajor)).append(".").append(formatterMinor.format(versionNumberMinor)).toString();
    }
    
    public static String dtoList2DtoString(List<String> source) {
        if (source == null || source.size() == 0) {
            return null;
        }
        return StringUtils.join(source, SEPARATOR_LIST_DTO_TO_STRING_DO);
        
    }
    public static List<String> doString2DtoList(String source) {
        if (org.siemac.metamac.core.common.util.shared.StringUtils.isBlank(source)) {
            return null;
        }
        return Arrays.asList(org.apache.commons.lang.StringUtils.split(source, SEPARATOR_LIST_DTO_TO_STRING_DO));
    }
    
    public static InternationalString generateInternationalStringInDefaultLocales(String label) {
        InternationalString target = new InternationalString();
        
        LocalisedString localisedStringEs = new LocalisedString();
        localisedStringEs.setLabel(label);
        localisedStringEs.setLocale(IndicatorsConstants.LOCALE_SPANISH);
        target.addText(localisedStringEs);
        
        LocalisedString localisedStringEn = new LocalisedString();
        localisedStringEn.setLabel(label);
        localisedStringEn.setLocale(IndicatorsConstants.LOCALE_ENGLISH);
        target.addText(localisedStringEn);
        
        return target;
    }
}