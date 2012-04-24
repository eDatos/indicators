package es.gobcan.istac.indicators.rest.facadeimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.domain.Subject;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsService;
import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.facadeapi.ThemesRestFacade;
import es.gobcan.istac.indicators.rest.mapper.Do2TypeMapper;
import es.gobcan.istac.indicators.rest.types.ThemeType;

@Service("yhemesRestFacade")
public class ThemesRestFacadeImpl implements ThemesRestFacade {

    protected Logger          logger            = LoggerFactory.getLogger(getClass());

    @Autowired
    private IndicatorsService indicatorsService = null;

    @Autowired
    private Do2TypeMapper     dto2TypeMapper    = null;

    @Override
    public List<ThemeType> retrieveThemes(final String baseUrl) throws Exception {
        List<Subject> subjects = indicatorsService.retrieveSubjects(RestConstants.SERVICE_CONTEXT);
        List<ThemeType> themeTypes = dto2TypeMapper.subjectDoToType(subjects);
        return themeTypes;
    }

}
