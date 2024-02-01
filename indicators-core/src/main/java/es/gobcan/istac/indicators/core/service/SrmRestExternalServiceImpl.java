package es.gobcan.istac.indicators.core.service;

import static org.siemac.edatos.core.common.util.shared.UrnUtils.splitUrnItemScheme;

import org.siemac.metamac.rest.structural_resources.v1_0.domain.Codes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(SrmRestExternalService.BEAN_ID)
public class SrmRestExternalServiceImpl implements SrmRestExternalService {

    @Autowired
    private RestApiLocator restApiLocator;

    @Override
    public Codes retrieveCodesOfCodelist(String codelistUrn, boolean retrieveLastVersion) {

        String[] params = splitUrnItemScheme(codelistUrn);
        String agencyId = params[0];
        String resourceId = params[1];
        String version = params[2];
        if (retrieveLastVersion) {
            version = "~latest";
        }

        return restApiLocator.getSrmRestExternalFacadeV10().findCodes(agencyId, resourceId, version, null, null, null, null, null, null, null, null, null);

    }

}
