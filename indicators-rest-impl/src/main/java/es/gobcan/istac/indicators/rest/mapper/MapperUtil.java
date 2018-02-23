package es.gobcan.istac.indicators.rest.mapper;

import static es.gobcan.istac.indicators.rest.constants.IndicatorsRestApiConstants.DEFAULT;
import static es.gobcan.istac.indicators.rest.constants.IndicatorsRestApiConstants.DEFAULT_LANGUAGE;

import java.util.LinkedHashMap;
import java.util.Map;

import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.statistical_operations.v1_0.domain.Operation;

import es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto;
import es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto;

import es.gobcan.istac.indicators.rest.clients.adapters.OperationIndicators;

public class MapperUtil {

    private MapperUtil() {
    }

    public static Map<String, String> getDefaultLabel(Object defaultLabel) {
        if (defaultLabel == null) {
            return null;
        }

        Map<String, String> labels = new LinkedHashMap<String, String>();
        labels.put(DEFAULT, defaultLabel.toString());
        return labels;
    }

    public static Map<String, String> getLocalisedLabel(InternationalString internationalString) {
        if (internationalString == null || internationalString.getTexts() == null || internationalString.getTexts().size() == 0) {
            return null;
        }

        Map<String, String> labels = new LinkedHashMap<String, String>(internationalString.getTexts().size() + 1);
        String defaultLabel = null;
        String defaultLabelLocale = null;
        for (LocalisedString localisedString : internationalString.getTexts()) {
            labels.put(localisedString.getLang(), localisedString.getValue());
            if ((defaultLabel == null) || (defaultLabel != null && localisedString.getLang().equals(DEFAULT_LANGUAGE))
                    || (defaultLabel != null && defaultLabelLocale.equals(DEFAULT_LANGUAGE) && localisedString.getLang().startsWith(DEFAULT_LANGUAGE))) {
                defaultLabel = localisedString.getValue();
                defaultLabelLocale = localisedString.getLang();
            }
        }
        labels.put(DEFAULT, defaultLabel);
        return labels;
    }

    public static Map<String, String> getLocalisedLabel(org.siemac.metamac.core.common.ent.domain.InternationalString internationalString) {
        if (internationalString == null || internationalString.getTexts() == null || internationalString.getTexts().size() == 0) {
            return null;
        }

        Map<String, String> labels = new LinkedHashMap<String, String>(internationalString.getTexts().size() + 1);
        String defaultLabel = null;
        String defaultLabelLocale = null;
        for (org.siemac.metamac.core.common.ent.domain.LocalisedString localisedString : internationalString.getTexts()) {
            labels.put(localisedString.getLocale(), localisedString.getLabel());
            if ((defaultLabel == null) || (defaultLabel != null && localisedString.getLocale().equals(DEFAULT_LANGUAGE))
                    || (defaultLabel != null && defaultLabelLocale.equals(DEFAULT_LANGUAGE) && localisedString.getLocale().startsWith(DEFAULT_LANGUAGE))) {
                defaultLabel = localisedString.getLabel();
                defaultLabelLocale = localisedString.getLocale();
            }
        }
        labels.put(DEFAULT, defaultLabel);
        return labels;
    }

    public static Map<String, String> getLocalisedLabel(InternationalStringDto internationalString) {
        if (internationalString == null || internationalString.getTexts() == null || internationalString.getTexts().size() == 0) {
            return null;
        }

        Map<String, String> labels = new LinkedHashMap<String, String>(internationalString.getTexts().size() + 1);
        String defaultLabel = null;
        String defaultLabelLocale = null;
        for (LocalisedStringDto localisedString : internationalString.getTexts()) {
            labels.put(localisedString.getLocale(), localisedString.getLabel());
            if ((defaultLabel == null) || (defaultLabel != null && localisedString.getLocale().equals(DEFAULT_LANGUAGE))
                    || (defaultLabel != null && defaultLabelLocale.equals(DEFAULT_LANGUAGE) && localisedString.getLocale().startsWith(DEFAULT_LANGUAGE))) {
                defaultLabel = localisedString.getLabel();
                defaultLabelLocale = localisedString.getLocale();
            }
        }
        labels.put(DEFAULT, defaultLabel);
        return labels;
    }

    public static OperationIndicators getOperationIndicators(Operation operation) {
        OperationIndicators target = new OperationIndicators();
        target.setId(operation.getId());
        target.setTitle(getLocalisedLabel(operation.getName()));
        target.setAcronym(getLocalisedLabel(operation.getAcronym()));
        target.setDescription(getLocalisedLabel(operation.getDescription()));
        target.setObjective(getLocalisedLabel(operation.getObjective()));
        target.setUri(operation.getSelfLink().getHref());
        return target;
    }

    public static OperationIndicators getOperationIndicators(org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation operation) {
        OperationIndicators target = new OperationIndicators();
        target.setId(operation.getId());
        target.setTitle(getLocalisedLabel(operation.getName()));
        target.setAcronym(getLocalisedLabel(operation.getAcronym()));
        target.setDescription(getLocalisedLabel(operation.getDescription()));
        target.setObjective(getLocalisedLabel(operation.getObjective()));
        target.setUri(operation.getSelfLink().getHref());
        return target;
    }

}
