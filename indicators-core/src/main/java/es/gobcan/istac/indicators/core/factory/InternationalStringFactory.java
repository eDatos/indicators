package es.gobcan.istac.indicators.core.factory;

import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.exception.MetamacException;

public interface InternationalStringFactory {

    public static final String BEAN_ID = "internationalStringFactory";

    InternationalString getInternationalStringInDefaultLocales(String label) throws MetamacException;
}
