package es.gobcan.istac.indicators.rest.spring.web;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.InternationalizationUtils;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

public class EdatosLocaleResolver extends SessionLocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        try {
            String cookieValue = InternationalizationUtils.getInstance().getCurrentLocale(httpServletRequest);
            return (cookieValue != null) ? new Locale(cookieValue) : super.resolveLocale(httpServletRequest);
        } catch (MetamacException e) {
            return super.resolveLocale(httpServletRequest);
        }
    }
}
