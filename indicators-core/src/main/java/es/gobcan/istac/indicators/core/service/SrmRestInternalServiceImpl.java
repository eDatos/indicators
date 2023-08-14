package es.gobcan.istac.indicators.core.service;

import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryElements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(SrmRestInternalService.BEAN_ID)
public class SrmRestInternalServiceImpl implements SrmRestInternalService {

    private static Logger  logger = LoggerFactory.getLogger(SrmRestInternalServiceImpl.class);

    @Autowired
    private RestApiLocator restApiLocator;

    @Override
    public CategoryElements findCategoryElements(String query, String orderBy, String limit, String offset) {
        try {
            return restApiLocator.getSrmRestInternalFacadeV10().findCategoryElements(query, orderBy, limit, offset);
        } catch (Exception e) {
            logger.error("Unable to find Category elements", e);
            throw toRestException(e);
        }
    }

    private RestException toRestException(Exception e) {
        throw toRestException(e);
    }
}
