package es.gobcan.istac.indicators.rest.clients;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categories;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryResourceInternal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.service.RestApiLocator;

@Component(SrmRestInternalFacade.BEAN_ID)
public class SrmRestInternalFacadeImpl implements SrmRestInternalFacade {

    private static Logger  logger = LoggerFactory.getLogger(SrmRestInternalFacadeImpl.class);

    @Autowired
    private RestApiLocator restApiLocator;

    @Override
    public CategoryResourceInternal retrieveCategoryByCategoryElement(String categorySchemeUrn, String categoryElementCode) throws MetamacException {
        String fields = "+categoryElement";

        String[] params = UrnUtils.splitUrnItemScheme(categorySchemeUrn);
        String agencyId = params[0];
        String resourceId = params[1];
        String version = params[2];

        Categories categories = restApiLocator.getSrmRestInternalFacadeV10().findCategories(agencyId, resourceId, version, null, null, null, null, fields);
        if (categories.getCategories() != null && !categories.getCategories().isEmpty()) {
            for (CategoryResourceInternal category : categories.getCategories()) {
                if (category.getCategoryElement() != null && category.getCategoryElement().getId().equals(categoryElementCode)) {
                    return category;
                }
            }
        }

        return null;
    }

}
