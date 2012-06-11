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

import es.gobcan.istac.indicators.core.dto.DataDefinitionDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.GetDataDefinitionsAction;
import es.gobcan.istac.indicators.web.shared.GetDataDefinitionsResult;

@Component
public class GetDataDefinitionsActionHandler extends SecurityActionHandler<GetDataDefinitionsAction, GetDataDefinitionsResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetDataDefinitionsActionHandler() {
        super(GetDataDefinitionsAction.class);
    }

    @Override
    public GetDataDefinitionsResult executeSecurityAction(GetDataDefinitionsAction action) throws ActionException {
        try {
            List<DataDefinitionDto> dataDefinitionsDtos = indicatorsServiceFacade.retrieveDataDefinitions(ServiceContextHolder.getCurrentServiceContext());
            return new GetDataDefinitionsResult(dataDefinitionsDtos);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(GetDataDefinitionsAction action, GetDataDefinitionsResult result, ExecutionContext context) throws ActionException {

    }

}
