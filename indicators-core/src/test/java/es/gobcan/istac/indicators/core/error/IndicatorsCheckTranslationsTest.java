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
        return new Class[]{ServiceExceptionParameters.class};
    }

    @Override
    public Locale[] getLocalesToTranslate() {
        // We do not use the one defined in CoreCommon because in future it is going to have Portuguese
        // and this application does not need translation for this language
        return new Locale[]{Locale.ENGLISH, new Locale("es")};
    }

}