package es.gobcan.istac.indicators.web.server.handlers;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHolder;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularitiesAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularitiesResult;

@Component
public class GetGeographicalGranularitiesActionHandler extends AbstractActionHandler<GetGeographicalGranularitiesAction, GetGeographicalGranularitiesResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetGeographicalGranularitiesActionHandler() {
        super(GetGeographicalGranularitiesAction.class);
    }

    @Override
    public GetGeographicalGranularitiesResult execute(GetGeographicalGranularitiesAction action, ExecutionContext context) throws ActionException {
        try {
            List<GeographicalGranularityDto> geographicalGranularityDtos = indicatorsServiceFacade.retrieveGeographicalGranularities(ServiceContextHolder.getCurrentServiceContext());
            return new GetGeographicalGranularitiesResult(geographicalGranularityDtos);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(GetGeographicalGranularitiesAction action, GetGeographicalGranularitiesResult result, ExecutionContext context) throws ActionException {

    }

}
