package es.gobcan.istac.indicators.web.server.handlers;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.criteria.GeographicalValueCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.criteria.IndicatorsCriteria;
import es.gobcan.istac.indicators.core.criteria.IndicatorsCriteriaConjunctionRestriction;
import es.gobcan.istac.indicators.core.criteria.IndicatorsCriteriaPropertyRestriction;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalValueDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesResult;

@Component
public class GetGeographicalValuesActionHandler extends AbstractActionHandler<GetGeographicalValuesAction, GetGeographicalValuesResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetGeographicalValuesActionHandler() {
        super(GetGeographicalValuesAction.class);
    }

    @Override
    public GetGeographicalValuesResult execute(GetGeographicalValuesAction action, ExecutionContext context) throws ActionException {
        try {
            IndicatorsCriteria criteria = null;
            if (action.getGeographicalGranularityUuid() != null) {
                criteria = new IndicatorsCriteria();
                criteria.setConjunctionRestriction(new IndicatorsCriteriaConjunctionRestriction());
                criteria.getConjunctionRestriction().getRestrictions()
                        .add(new IndicatorsCriteriaPropertyRestriction(GeographicalValueCriteriaPropertyEnum.GEOGRAPHICAL_GRANULARITY_UUID.name(), action.getGeographicalGranularityUuid()));
            }

            List<GeographicalValueDto> geographicalValuesDtos = indicatorsServiceFacade.findGeographicalValues(ServiceContextHelper.getServiceContext(), criteria);

            return new GetGeographicalValuesResult(geographicalValuesDtos);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(GetGeographicalValuesAction action, GetGeographicalValuesResult result, ExecutionContext context) throws ActionException {

    }

}
