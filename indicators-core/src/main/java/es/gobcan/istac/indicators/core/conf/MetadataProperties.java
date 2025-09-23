package es.gobcan.istac.indicators.core.conf;

import javax.annotation.PostConstruct;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("metadataProperties")
public class MetadataProperties {

    @Autowired
    private IndicatorsConfigurationService configurationService;

    private String                         defaultInternationalizationLanguage;
    private String                         codelistAnnotationTypePositionUnit;
    private String                         defaultCategoryScheme;
    private String                         defaultGeographicalCodeListUrn;
    private String                         apiKey;

    @PostConstruct
    public void setValues() throws MetamacException {
        defaultInternationalizationLanguage = configurationService.retrieveDefaultInternationalizationLanguage();
        codelistAnnotationTypePositionUnit = configurationService.retrieveCodelistAnnotationTypePositionUnit();
        defaultCategoryScheme = configurationService.retrieveDefaultCategoryScheme();
        defaultGeographicalCodeListUrn = configurationService.retrieveDefaultGeographicalCodeListUrn();
        apiKey = configurationService.retrieveIndicatorsWebRestApiKey();
    }

    public String getDefaultInternationalizationLanguage() {
        return defaultInternationalizationLanguage;
    }

    public String getCodelistAnnotationTypePositionUnit() {
        return codelistAnnotationTypePositionUnit;
    }

    public String getDefaultCategoryScheme() {
        return defaultCategoryScheme;
    }

    public String getDefaultGeographicalCodeListUrn() {
        return defaultGeographicalCodeListUrn;
    }

    public String getApiKey() {
        return apiKey;
    }
}
