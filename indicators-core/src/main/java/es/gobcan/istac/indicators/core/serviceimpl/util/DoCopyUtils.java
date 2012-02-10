package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.HashSet;
import java.util.Set;

import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;

import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;

public class DoCopyUtils {

    /**
     * Create a new IndicatorsSystemVersion copying values from a source
     */
    public static IndicatorsSystemVersion copy(IndicatorsSystemVersion source) {
        IndicatorsSystemVersion target = new IndicatorsSystemVersion();
        target.setUriGopestat(source.getUriGopestat());
        target.setTitle(copy(source.getTitle()));
        target.setAcronym(copy(source.getAcronym()));
        target.setObjetive(copy(source.getObjetive()));
        target.setDescription(copy(source.getDescription()));
        return target;
    }

    private static InternationalString copy(InternationalString source) {
        if (source == null) {
            return null;
        }
        InternationalString target = new InternationalString();
        target.getTexts().addAll(copyLocalisedStrings(source.getTexts()));
        return target;
    }
    
    private static Set<LocalisedString> copyLocalisedStrings(Set<LocalisedString> sources) {
        Set<LocalisedString> targets = new HashSet<LocalisedString>();
        for (LocalisedString source : sources) {
            LocalisedString target = new LocalisedString();
            target.setLabel(source.getLabel());
            target.setLocale(source.getLocale());
            targets.add(target);
        }
        return targets;
    }
}