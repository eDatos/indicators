package es.gobcan.istac.indicators.web.server.handlers;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularitiesInIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularitiesInIndicatorResult;

@Component
public class GetGeographicalGranularitiesInIndicatorActionHandler extends SecurityActionHandler<GetGeographicalGranularitiesInIndicatorAction, GetGeographicalGranularitiesInIndicatorResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetGeographicalGranularitiesInIndicatorActionHandler() {
        super(GetGeographicalGranularitiesInIndicatorAction.class);
    }

    @Override
    public GetGeographicalGranularitiesInIndicatorResult executeSecurityAction(GetGeographicalGranularitiesInIndicatorAction action) throws ActionException {
        try {
            List<GeographicalGranularityDto> geographicalGranularityDtos = indicatorsServiceFacade.retrieveGeographicalGranularitiesInIndicator(ServiceContextHolder.getCurrentServiceContext(),
                    action.getIndicatorUuid(), action.getIndicatorVersion());
            return new GetGeographicalGranularitiesInIndicatorResult(geographicalGranularityDtos);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }

    }

    @Override
    public void undo(GetGeographicalGranularitiesInIndicatorAction action, GetGeographicalGranularitiesInIndicatorResult result, ExecutionContext context) throws ActionException {

    }

}
