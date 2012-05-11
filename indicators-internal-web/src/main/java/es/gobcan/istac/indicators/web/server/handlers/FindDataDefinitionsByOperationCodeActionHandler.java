package es.gobcan.istac.indicators.web.server.handlers;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.DataDefinitionDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHolder;
import es.gobcan.istac.indicators.web.shared.FindDataDefinitionsByOperationCodeAction;
import es.gobcan.istac.indicators.web.shared.FindDataDefinitionsByOperationCodeResult;

@Component
public class FindDataDefinitionsByOperationCodeActionHandler extends AbstractActionHandler<FindDataDefinitionsByOperationCodeAction, FindDataDefinitionsByOperationCodeResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public FindDataDefinitionsByOperationCodeActionHandler() {
        super(FindDataDefinitionsByOperationCodeAction.class);
    }

    @Override
    public FindDataDefinitionsByOperationCodeResult execute(FindDataDefinitionsByOperationCodeAction action, ExecutionContext context) throws ActionException {
        try {
            List<DataDefinitionDto> dataDefinitionDtos = indicatorsServiceFacade.findDataDefinitionsByOperationCode(ServiceContextHolder.getCurrentServiceContext(), action.getOperationCode());
            return new FindDataDefinitionsByOperationCodeResult(dataDefinitionDtos);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(FindDataDefinitionsByOperationCodeAction action, FindDataDefinitionsByOperationCodeResult result, ExecutionContext context) throws ActionException {

    }

}
