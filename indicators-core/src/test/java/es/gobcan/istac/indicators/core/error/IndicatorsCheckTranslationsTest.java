package es.gobcan.istac.indicators.core.error;

import java.util.Locale;

import org.siemac.metamac.common.test.translations.CheckTranslationsTestBase;

public class IndicatorsCheckTranslationsTest extends CheckTranslationsTestBase {

    @Override
    @SuppressWarnings("rawtypes")
    public Class[] getServiceExceptionTypeClasses() {
        return new Class[]{ServiceExceptionType.class};
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Class[] getServiceExceptionParameterClasses() {
        // We don't need to check nothing because the parameters translation is in the internal-web project
        return new Class[]{};
    }

    @Override
    public Locale[] getLocalesToTranslate() {
        return LOCALES_TO_TRANSLATE;
    }

}