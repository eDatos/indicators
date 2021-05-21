package es.gobcan.istac.indicators.core.factory;

import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;

@Component(InternationalStringFactory.BEAN_ID)
public class InternationalStringFactoryImpl implements InternationalStringFactory {

    @Autowired
    IndicatorsConfigurationService configurationService;

    @Override
    public InternationalString getInternationalStringInDefaultLocales(String label) throws MetamacException {
        InternationalString target = new InternationalString();

        for (String language : configurationService.retrieveLanguages()) {
            LocalisedString localisedString = new LocalisedString();
            localisedString.setLabel(label);
            localisedString.setLocale(language);
            target.addText(localisedString);
        }

        return target;
    }

}
