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

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.GetDataDefinitionsOperationsCodesAction;
import es.gobcan.istac.indicators.web.shared.GetDataDefinitionsOperationsCodesResult;

@Component
public class GetDataDefinitionsOperationsCodesActionHandler extends SecurityActionHandler<GetDataDefinitionsOperationsCodesAction, GetDataDefinitionsOperationsCodesResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetDataDefinitionsOperationsCodesActionHandler() {
        super(GetDataDefinitionsOperationsCodesAction.class);
    }

    @Override
    public GetDataDefinitionsOperationsCodesResult executeSecurityAction(GetDataDefinitionsOperationsCodesAction action) throws ActionException {
        try {
            List<String> operationCodes = indicatorsServiceFacade.retrieveDataDefinitionsOperationsCodes(ServiceContextHolder.getCurrentServiceContext());
            return new GetDataDefinitionsOperationsCodesResult(operationCodes);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(GetDataDefinitionsOperationsCodesAction action, GetDataDefinitionsOperationsCodesResult result, ExecutionContext context) throws ActionException {

    }

}
