package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValueAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValueResult;

@Component
public class GetGeographicalValueActionHandler extends SecurityActionHandler<GetGeographicalValueAction, GetGeographicalValueResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetGeographicalValueActionHandler() {
        super(GetGeographicalValueAction.class);
    }

    @Override
    public GetGeographicalValueResult executeSecurityAction(GetGeographicalValueAction action) throws ActionException {
        try {
            GeographicalValueDto geographicalValueDto = indicatorsServiceFacade.retrieveGeographicalValue(ServiceContextHolder.getCurrentServiceContext(), action.getGeographicalValueUuid());
            return new GetGeographicalValueResult(geographicalValueDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(GetGeographicalValueAction action, GetGeographicalValueResult result, ExecutionContext context) throws ActionException {

    }

}
