package es.gobcan.istac.indicators.core.error.utils;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.utils.TranslateExceptions;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;

public class TranslateExceptionUtils {

    private TranslateExceptionUtils() {
    }

    public static MetamacException translateMetamacException(ServiceContext ctx, MetamacException metamacException) {
        return (MetamacException) getTranslateExceptionsBean().translateException(ctx, metamacException);
    }

    private static TranslateExceptions getTranslateExceptionsBean() {
        return (TranslateExceptions) ApplicationContextProvider.getApplicationContext().getBean("translateExceptions");
    }

}
