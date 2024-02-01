package es.gobcan.istac.indicators.core.service;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Codes;

public interface SrmRestExternalService {

    public static final String BEAN_ID = "srmRestExternalService";

    public Codes retrieveCodesOfCodelist(String codelistUrn, boolean retrieveLastVersion) throws MetamacException;

}
