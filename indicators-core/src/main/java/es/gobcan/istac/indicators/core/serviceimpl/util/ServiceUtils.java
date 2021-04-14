package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;

import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.LastValue;
import es.gobcan.istac.indicators.core.factory.InternationalStringFactory;
import es.gobcan.istac.indicators.core.vo.GeographicalValueVO;

public class ServiceUtils {

    private static final String SEPARATOR_LIST_DTO_TO_STRING_DO = "##";

    public static final int     ORACLE_IN_MAX                   = 1000;

    private ServiceUtils() {
    }

    public static String dtoList2DtoString(List<String> source) {
        if (source == null || source.size() == 0) {
            return StringUtils.EMPTY;
        }
        return StringUtils.join(source, SEPARATOR_LIST_DTO_TO_STRING_DO);

    }
    public static List<String> doString2DtoList(String source) {
        if (org.siemac.metamac.core.common.util.shared.StringUtils.isBlank(source)) {
            return null;
        }
        return Arrays.asList(org.apache.commons.lang.StringUtils.split(source, SEPARATOR_LIST_DTO_TO_STRING_DO));
    }

    public static InternationalString generateInternationalStringInDefaultLocales(String label) throws MetamacException {
        return getInternationalStringFactoryBean().getInternationalStringInDefaultLocales(label);
    }

    public static void sortGeographicalValuesList(List<GeographicalValue> geographicalValues) {
        Collections.sort(geographicalValues, new Comparator<GeographicalValue>() {

            @Override
            public int compare(GeographicalValue o1, GeographicalValue o2) {
                return o1.getOrder().compareTo(o2.getOrder());
            }
        });
    }

    public static void sortGeographicalValuesVOList(List<GeographicalValueVO> geographicalValues) {
        Collections.sort(geographicalValues, new Comparator<GeographicalValueVO>() {

            @Override
            public int compare(GeographicalValueVO o1, GeographicalValueVO o2) {
                return o1.getOrder().compareTo(o2.getOrder());
            }
        });
    }

    public static void sortLastValuesCache(List<? extends LastValue> latestValues) {
        Collections.sort(latestValues, new Comparator<LastValue>() {

            @Override
            public int compare(LastValue o1, LastValue o2) {
                // desc order
                return o1.getLastDataUpdated().compareTo(o2.getLastDataUpdated()) * (-1);
            }
        });
    }

    private static InternationalStringFactory getInternationalStringFactoryBean() {
        return (InternationalStringFactory) ApplicationContextProvider.getApplicationContext().getBean(InternationalStringFactory.BEAN_ID);
    }
}