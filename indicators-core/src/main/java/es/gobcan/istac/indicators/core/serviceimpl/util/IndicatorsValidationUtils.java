package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.Collection;
import java.util.List;

import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;

public class IndicatorsValidationUtils extends ValidationUtils {

    /**
     * Check for a required metadata and add an exception for a failed validation.
     *
     * @param parameter
     * @param parameterName
     * @param exceptions
     */
    public static void checkMetadataRequired(InternationalString parameter, String parameterName, List<MetamacExceptionItem> exceptions) {
        if (isEmpty(parameter)) {
            exceptions.add(new MetamacExceptionItem(CommonServiceExceptionType.METADATA_REQUIRED, parameterName));
        }
    }

    /**
     * Check InternationalString is valid
     */
    public static void checkMetadataOptionalIsValid(InternationalString parameter, String parameterName, List<MetamacExceptionItem> exceptions) {
        if (parameter == null) {
            return;
        }

        // if it is not null, it must be complete
        if (isEmpty(parameter)) {
            exceptions.add(new MetamacExceptionItem(CommonServiceExceptionType.METADATA_INCORRECT, parameterName));
        }
    }

    /**
     * Check if a collection metadata is valid.
     *
     * @param parameter
     * @param parameterName
     * @param exceptions
     */

    @SuppressWarnings("rawtypes")
    public static void checkListMetadataOptionalIsValid(Collection parameter, String parameterName, List<MetamacExceptionItem> exceptions) {

        if (parameter == null) {
            return;
        }

        int exceptionSize = exceptions.size();

        for (Object item : parameter) {
            if (InternationalString.class.isInstance(item)) {
                checkMetadataOptionalIsValid((InternationalString) item, parameterName, exceptions);
            } else {
                checkMetadataOptionalIsValid(item, parameterName, exceptions);
            }

            // With one incorrect item is enough
            if (exceptions.size() > exceptionSize) {
                return;
            }
        }
    }

    /**
     * Check if an InternationalString is empty.
     */
    private static Boolean isEmpty(InternationalString parameter) {
        if (parameter == null) {
            return Boolean.TRUE;
        }
        if (parameter.getTexts().size() == 0) {
            return Boolean.TRUE;
        }
        for (LocalisedString localisedString : parameter.getTexts()) {
            if (isEmpty(localisedString.getLabel()) || isEmpty(localisedString.getLocale())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

}
