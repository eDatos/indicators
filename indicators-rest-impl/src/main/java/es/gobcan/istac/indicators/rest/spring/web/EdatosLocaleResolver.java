package es.gobcan.istac.indicators.rest.spring.web;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

import es.gobcan.istac.indicators.core.conf.MetadataProperties;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.InternationalizationUtils;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

public class EdatosLocaleResolver extends SessionLocaleResolver {

    public EdatosLocaleResolver(MetadataProperties metadataProperties) {
        setDefaultLocale(Locale.forLanguageTag(metadataProperties.getDefaultInternationalizationLanguage()));
    }

    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        try {
            String cookieValue = InternationalizationUtils.getInstance().getCurrentLocale(httpServletRequest);
            return (cookieValue != null) ? new Locale(cookieValue) : determineDefaultLocale(httpServletRequest);
        } catch (MetamacException e) {
            return super.resolveLocale(httpServletRequest);
        }
    }
}
