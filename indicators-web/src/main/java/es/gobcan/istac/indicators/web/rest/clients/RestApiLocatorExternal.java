package es.gobcan.istac.indicators.web.rest.clients;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.siemac.edatos.core.common.constants.CoreCommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;

public class RestApiLocatorExternal {

    private final String                   INDICATORS_SYSTEM = "indicatorsSystems/{indicatorSystemCode}";
    private final String                   API_VERSION_TAG   = "v1.0";

    private String                         baseApi;
    public String                          apiKey;

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
        try {
            this.apiKey = configurationService.retrieveIndicatorsWebRestApiKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public IndicatorsSystemType getIndicatorsSystemsByCode(String indicatorSystemCode) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        if (StringUtils.isNotBlank(apiKey)) {
            headers.set(CoreCommonConstants.API_KEY_PARAMETER, apiKey);
        }

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        // GET: /indicatorsSystems/{indicatorSystemCode}
        ResponseEntity<IndicatorsSystemType> response = restTemplate.exchange(baseApi, HttpMethod.GET, requestEntity, IndicatorsSystemType.class, indicatorSystemCode);
        return response.getBody();
    }
}