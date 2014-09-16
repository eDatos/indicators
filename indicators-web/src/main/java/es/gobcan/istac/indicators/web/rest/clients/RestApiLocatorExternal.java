package es.gobcan.istac.indicators.web.rest.clients;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;

public class RestApiLocatorExternal {

    private final String                   INDICATORS_SYSTEM = "indicatorsSystems/{indicatorSystemCode}";
    private final String                   API_VERSION_TAG   = "v1.0";

    private String                         baseApi;

    @Autowired
    private IndicatorsConfigurationService configurationService;

    @PostConstruct
    public void initService() throws Exception {
        StringBuilder indicatorsApiUrlBldr = new StringBuilder(configurationService.retrieveIndicatorsExternalApiUrlBase());
        // Add version
        if (indicatorsApiUrlBldr.charAt(indicatorsApiUrlBldr.length() - 1) != '/') {
            indicatorsApiUrlBldr.append("/");
        }
        indicatorsApiUrlBldr.append(API_VERSION_TAG).append("/").append(INDICATORS_SYSTEM);
        this.baseApi = indicatorsApiUrlBldr.toString();
    }

    public IndicatorsSystemType getIndicatorsSystemsByCode(String indicatorSystemCode) {
        RestTemplate restTemplate = new RestTemplate();

        // GET: /indicatorsSystems/{indicatorSystemCode}
        IndicatorsSystemType indicatorsSystem = restTemplate.getForObject(baseApi, IndicatorsSystemType.class, indicatorSystemCode);

        return indicatorsSystem;
    }
}