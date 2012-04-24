package es.gobcan.istac.indicators.rest.facadeapi;

import java.util.List;

import es.gobcan.istac.indicators.rest.types.ThemeType;

public interface ThemesRestFacade {

    public List<ThemeType> retrieveThemes(final String baseUrl) throws Exception;

}
