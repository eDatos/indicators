package es.gobcan.istac.indicators.web;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;


public class EdatosLocaleResolver extends SessionLocaleResolver {

    private static IndicatorsConfigurationService configurationService;

    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        try {
            String cookieValue = findInternationalizationCookie(httpServletRequest);
            return (cookieValue != null) ? new Locale(cookieValue) : super.resolveLocale(httpServletRequest);
        } catch (MetamacException e) {
            return super.resolveLocale(httpServletRequest);
        }
    }

    private String findInternationalizationCookie(HttpServletRequest request) throws MetamacException {
        String cookieName = getConfigurationService().retrieveInternationalizationCookieId();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private static IndicatorsConfigurationService getConfigurationService() {
        if (configurationService == null) {
            configurationService = (IndicatorsConfigurationService) ApplicationContextProvider.getApplicationContext().getBean("configurationService");
        }
        return configurationService;
    }
}