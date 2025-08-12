package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;

public class InternationalStringUtils {

    public static Map<String, String> getLocalisedLabel(InternationalString internationalString, String defaultLanguage, String defaultLabel) {
        if (internationalString == null || internationalString.getTexts() == null || internationalString.getTexts().isEmpty()) {
            return null;
        }

        Map<String, String> labels = new LinkedHashMap<String, String>(internationalString.getTexts().size() + 1);
        String defaultLabelValue = null;
        String defaultLabelLocale = null;
        for (LocalisedString localisedString : internationalString.getTexts()) {
            labels.put(localisedString.getLang(), localisedString.getValue());
            if ((defaultLabelValue == null) || (localisedString.getLang().equals(defaultLanguage))
                    || (defaultLabelLocale.equals(defaultLanguage) && localisedString.getLang().startsWith(defaultLanguage))) {
                defaultLabelValue = localisedString.getValue();
                defaultLabelLocale = localisedString.getLang();
            }
        }

        labels.put(defaultLabel, defaultLabelValue);
        return labels;
    }

    public static es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto buildDatasetRepositoryInternationalStringDtoFromCommonInternationalStringDto(
            org.siemac.metamac.rest.common.v1_0.domain.InternationalString internationalStringDto) {
        if (internationalStringDto.getTexts().isEmpty()) {
            return null;
        }

        es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto datasetRepositoryInternationalStringDto = new es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto();
        for (org.siemac.metamac.rest.common.v1_0.domain.LocalisedString locale : internationalStringDto.getTexts()) {
            es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto localised = new es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto();
            localised.setLabel(locale.getValue());
            localised.setLocale(locale.getLang());
            datasetRepositoryInternationalStringDto.addText(localised);
        }
        return datasetRepositoryInternationalStringDto;
    }
}
