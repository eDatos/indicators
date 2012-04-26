package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.DataDefinitionDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHolder;
import es.gobcan.istac.indicators.web.shared.GetDataDefinitionAction;
import es.gobcan.istac.indicators.web.shared.GetDataDefinitionResult;

@Component
public class GetDataDefinitionActionHandler extends AbstractActionHandler<GetDataDefinitionAction, GetDataDefinitionResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetDataDefinitionActionHandler() {
        super(GetDataDefinitionAction.class);
    }

    @Override
    public GetDataDefinitionResult execute(GetDataDefinitionAction action, ExecutionContext context) throws ActionException {
        try {
            DataDefinitionDto dataDefinitionDto = indicatorsServiceFacade.retrieveDataDefinition(ServiceContextHolder.getCurrentServiceContext(), action.getUuid());
            return new GetDataDefinitionResult(dataDefinitionDto);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(GetDataDefinitionAction action, GetDataDefinitionResult result, ExecutionContext context) throws ActionException {

    }

}
