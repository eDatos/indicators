package es.gobcan.istac.indicators.core.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component("metadataProperties")
public class MetadataProperties {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IndicatorsConfigurationService configurationService;

    private List<String> internationalizationLanguages;

    @PostConstruct
    public void setValues() {
        try {
            internationalizationLanguages = configurationService.retrieveInternationalizationLanguages();
        } catch (Exception e) {
            log.error("Error getting the value of a metadata {}", e);
            internationalizationLanguages = new ArrayList<>();
        }
    }

    public String getDefaultLanguage() {
        return internationalizationLanguages.size() == 0 ? null : internationalizationLanguages.get(0);
    }
}
