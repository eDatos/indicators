package es.gobcan.istac.indicators.core.service;

import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryElements;

public interface SrmRestInternalService {

    public static final String BEAN_ID = "srmRestInternalService";

    public CategoryElements findCategoryElements(String query, String orderBy, String limit, String offset);

}
