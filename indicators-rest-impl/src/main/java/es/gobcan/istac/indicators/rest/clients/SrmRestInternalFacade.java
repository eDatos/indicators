package es.gobcan.istac.indicators.rest.clients;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryResourceInternal;

public interface SrmRestInternalFacade {

    public static final String BEAN_ID = "srmRestInternalFacade";

    CategoryResourceInternal retrieveCategoryByCategoryElement(String categorySchemeUrn, String categoryElementCode) throws MetamacException;

}
