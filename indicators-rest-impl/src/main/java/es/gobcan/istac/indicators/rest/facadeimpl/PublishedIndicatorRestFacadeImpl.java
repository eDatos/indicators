package es.gobcan.istac.indicators.rest.facadeimpl;

import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.rest.RestConstants;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.rest.search.criteria.SculptorCriteria;

public class PublishedIndicatorRestFacadeImpl extends IndicatorRestFacadeImpl {

    protected PagedResult<IndicatorVersion> findIndicators(SculptorCriteria sculptorCriteria) throws org.siemac.metamac.core.common.exception.MetamacException {
        return indicatorsService.findIndicatorsPublished(RestConstants.SERVICE_CONTEXT, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());
    }

    protected IndicatorVersion retrieveIndicatorByCode(String indicatorCode) throws org.siemac.metamac.core.common.exception.MetamacException {
        return indicatorsService.retrieveIndicatorPublishedByCode(RestConstants.SERVICE_CONTEXT, indicatorCode);
    }

}
