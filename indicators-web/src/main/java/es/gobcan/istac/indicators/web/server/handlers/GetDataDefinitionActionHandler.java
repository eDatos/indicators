package es.gobcan.istac.indicators.web.server.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.serviceapi.DataBasicDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsDataServiceFacade;
import es.gobcan.istac.indicators.web.shared.GetDataDefinitionAction;
import es.gobcan.istac.indicators.web.shared.GetDataDefinitionResult;


@Component
public class GetDataDefinitionActionHandler extends AbstractActionHandler<GetDataDefinitionAction, GetDataDefinitionResult> {

    @Autowired
    private IndicatorsDataServiceFacade indicatorsDataServiceFacade;
    
    public GetDataDefinitionActionHandler() {
        super(GetDataDefinitionAction.class);
    }

    @Override
    public GetDataDefinitionResult execute(GetDataDefinitionAction action, ExecutionContext context) throws ActionException {
        return new GetDataDefinitionResult(new DataBasicDto()); // TODO
    }

    @Override
    public void undo(GetDataDefinitionAction action, GetDataDefinitionResult result, ExecutionContext context) throws ActionException {
        
    }

}
