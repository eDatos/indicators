package es.gobcan.istac.indicators.core.conf;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component("metadataProperties")
public class MetadataProperties {

    @Autowired
    private IndicatorsConfigurationService configurationService;

    private String defaultInternationalizationLanguage;

    @PostConstruct
    public void setValues() throws MetamacException {
        defaultInternationalizationLanguage = configurationService.retrieveDefaultInternationalizationLanguage();
    }

    public String getDefaultInternationalizationLanguage() {
        return defaultInternationalizationLanguage;
    }
}
