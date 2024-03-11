package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.constants.CommonSharedConstants;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.RelatedResourceDto;
import es.gobcan.istac.indicators.core.enume.domain.TypeRelatedResourceEnum;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.utils.MetamacWebCriteriaUtils;
import es.gobcan.istac.indicators.web.shared.GetRelatedResourcesAction;
import es.gobcan.istac.indicators.web.shared.GetRelatedResourcesResult;
import es.gobcan.istac.indicators.web.shared.criteria.GeoValueCriteria;

@Component
public class GetRelatedResourcesActionHandler extends SecurityActionHandler<GetRelatedResourcesAction, GetRelatedResourcesResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetRelatedResourcesActionHandler() {
        super(GetRelatedResourcesAction.class);
    }

    @Override
    public GetRelatedResourcesResult executeSecurityAction(GetRelatedResourcesAction action) throws ActionException {
        try {
            MetamacCriteriaResult<RelatedResourceDto> result = null;

            MetamacCriteria criteria = new MetamacCriteria();
            criteria.setPaginator(new MetamacCriteriaPaginator());
            criteria.getPaginator().setFirstResult(action.getFirstResult());
            criteria.getPaginator().setMaximumResultSize(action.getMaxResults());
            criteria.getPaginator().setCountTotalResults(true);

            if (action.getTypeRelatedResourceEnum() != null && TypeRelatedResourceEnum.GEOGRAPHICAL_VALUE.equals(action.getTypeRelatedResourceEnum())) {

                fillGeographicalValueCriteriaByGranularity(criteria, action);
                result = indicatorsServiceFacade.findGeographicalValuesForIndicatorByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
            } else {
                throw new MetamacWebException(CommonSharedConstants.EXCEPTION_UNKNOWN, "An unknown exception has ocurred. Please contact system administrator.");
            }

            return new GetRelatedResourcesResult(result.getResults(), result.getPaginatorResult().getFirstResult(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    private void fillGeographicalValueCriteriaByGranularity(MetamacCriteria criteria, GetRelatedResourcesAction action) {
        GeoValueCriteria geoValueWebCriteria = (GeoValueCriteria) action.getCriteria();
        MetamacWebCriteriaUtils.buildMetamacCriteriaFromWebcriteria(geoValueWebCriteria);
        criteria.setRestriction(MetamacWebCriteriaUtils.buildMetamacCriteriaFromWebcriteria(geoValueWebCriteria));

    }
}
