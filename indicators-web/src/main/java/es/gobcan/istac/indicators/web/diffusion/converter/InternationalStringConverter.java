package es.gobcan.istac.indicators.web.converter;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.springframework.core.convert.converter.Converter;

import es.gobcan.istac.indicators.web.WebConstants;

// TODO mensajes en diferentes idiomas. Por ahora sólo devuelve español
// TODO coger el locale del navegador web
final class InternationalStringConverter implements Converter<org.siemac.metamac.core.common.dto.InternationalStringDto, java.lang.String> {

    public String convert(InternationalStringDto source) {
        for (LocalisedStringDto localisedStringDto : source.getTexts()) {
            if (WebConstants.LOCALE_ES.equals(localisedStringDto.getLocale())) {
                return localisedStringDto.getLabel();
            }
        }
        // If the locale doesn't exist, then show it in english
        for (LocalisedStringDto localisedStringDto : source.getTexts()) {
            if (WebConstants.LOCALE_EN.equals(localisedStringDto.getLocale())) {
                return localisedStringDto.getLabel();
            }
        }
        return null;
    }
}