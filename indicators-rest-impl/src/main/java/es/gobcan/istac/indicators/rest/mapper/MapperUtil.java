package es.gobcan.istac.indicators.rest.mapper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.statistical_operations.v1_0.domain.Operation;

import es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto;
import es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto;
import es.gobcan.istac.indicators.rest.clients.adapters.OperationIndicators;

import static es.gobcan.istac.indicators.rest.constants.IndicatorsRestApiConstants.DEFAULT;
import static es.gobcan.istac.indicators.rest.constants.IndicatorsRestApiConstants.DEFAULT_LANGUAGE;

public class MapperUtil {

    private MapperUtil() {
    }

    public static String getDefaultValue(org.siemac.metamac.core.common.ent.domain.InternationalString internationalString) {
        if (internationalString == null || CollectionUtils.isEmpty(internationalString.getTexts())) {
            return null;
        }

        for (org.siemac.metamac.core.common.ent.domain.LocalisedString text : internationalString.getTexts()) {
            if (Objects.equals(DEFAULT_LANGUAGE, text.getLocale())) {
                return text.getLabel();
            }
        }

        return internationalString.getTexts().stream().findFirst().get().getLabel();
    }

    public static Map<String, String> getLocalisedLabel(InternationalString internationalString, String defaultLanguage) {
        if (internationalString == null || internationalString.getTexts() == null || internationalString.getTexts().size() == 0) {
            return null;
        }

        Map<String, String> labels = new LinkedHashMap<String, String>(internationalString.getTexts().size() + 1);
        String defaultLabel = null;
        String defaultLabelLocale = null;
        for (LocalisedString localisedString : internationalString.getTexts()) {
            labels.put(localisedString.getLang(), localisedString.getValue());
            if ((defaultLabel == null) || (defaultLabel != null && localisedString.getLang().equals(defaultLanguage))
                    || (defaultLabel != null && defaultLabelLocale.equals(defaultLanguage) && localisedString.getLang().startsWith(defaultLanguage))) {
                defaultLabel = localisedString.getValue();
                defaultLabelLocale = localisedString.getLang();
            }
        }
        labels.put(DEFAULT, defaultLabel);
        return labels;
    }

    public static Map<String, String> getLocalisedLabel(org.siemac.metamac.core.common.ent.domain.InternationalString internationalString, String defaultLanguage) {
        if (internationalString == null || internationalString.getTexts() == null || internationalString.getTexts().size() == 0) {
            return null;
        }

        Map<String, String> labels = new LinkedHashMap<String, String>(internationalString.getTexts().size() + 1);
        String defaultLabel = null;
        String defaultLabelLocale = null;
        for (org.siemac.metamac.core.common.ent.domain.LocalisedString localisedString : internationalString.getTexts()) {
            labels.put(localisedString.getLocale(), localisedString.getLabel());
            if ((defaultLabel == null) || (defaultLabel != null && localisedString.getLocale().equals(defaultLanguage))
                    || (defaultLabel != null && defaultLabelLocale.equals(defaultLanguage) && localisedString.getLocale().startsWith(defaultLanguage))) {
                defaultLabel = localisedString.getLabel();
                defaultLabelLocale = localisedString.getLocale();
            }
        }
        labels.put(DEFAULT, defaultLabel);
        return labels;
    }

    public static Map<String, String> getLocalisedLabel(InternationalStringDto internationalString, String defaultLanguage) {
        if (internationalString == null || internationalString.getTexts() == null || internationalString.getTexts().size() == 0) {
            return null;
        }

        Map<String, String> labels = new LinkedHashMap<String, String>(internationalString.getTexts().size() + 1);
        String defaultLabel = null;
        String defaultLabelLocale = null;
        for (LocalisedStringDto localisedString : internationalString.getTexts()) {
            labels.put(localisedString.getLocale(), localisedString.getLabel());
            if ((defaultLabel == null) || (defaultLabel != null && localisedString.getLocale().equals(defaultLanguage))
                    || (defaultLabel != null && defaultLabelLocale.equals(defaultLanguage) && localisedString.getLocale().startsWith(defaultLanguage))) {
                defaultLabel = localisedString.getLabel();
                defaultLabelLocale = localisedString.getLocale();
            }
        }
        labels.put(DEFAULT, defaultLabel);
        return labels;
    }

    public static OperationIndicators getOperationIndicators(Operation operation, String defaultLanguage) {
        OperationIndicators target = new OperationIndicators();
        target.setId(operation.getId());
        target.setTitle(getLocalisedLabel(operation.getName(), defaultLanguage));
        target.setAcronym(getLocalisedLabel(operation.getAcronym(), defaultLanguage));
        target.setDescription(getLocalisedLabel(operation.getDescription(), defaultLanguage));
        target.setObjective(getLocalisedLabel(operation.getObjective(), defaultLanguage));
        target.setUri(operation.getSelfLink().getHref());
        return target;
    }

    public static OperationIndicators getOperationIndicators(org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation operation, String defaultLanguage) {
        OperationIndicators target = new OperationIndicators();
        target.setId(operation.getId());
        target.setTitle(getLocalisedLabel(operation.getName(), defaultLanguage));
        target.setAcronym(getLocalisedLabel(operation.getAcronym(), defaultLanguage));
        target.setDescription(getLocalisedLabel(operation.getDescription(), defaultLanguage));
        target.setObjective(getLocalisedLabel(operation.getObjective(), defaultLanguage));
        target.setUri(operation.getSelfLink().getHref());
        return target;
    }

}
