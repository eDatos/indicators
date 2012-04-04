package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalValueDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValueAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValueResult;


@Component
public class GetGeographicalValueActionHandler extends AbstractActionHandler<GetGeographicalValueAction, GetGeographicalValueResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;
 
    
    public GetGeographicalValueActionHandler() {
        super(GetGeographicalValueAction.class);
    }

    @Override
    public GetGeographicalValueResult execute(GetGeographicalValueAction action, ExecutionContext context) throws ActionException {
        try {
            GeographicalValueDto geographicalValueDto = indicatorsServiceFacade.retrieveGeographicalValue(ServiceContextHelper.getServiceContext(), action.getGeographicalValueUuid());
            return new GetGeographicalValueResult(geographicalValueDto);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(GetGeographicalValueAction action, GetGeographicalValueResult result, ExecutionContext context) throws ActionException {
        
    }
    
}
