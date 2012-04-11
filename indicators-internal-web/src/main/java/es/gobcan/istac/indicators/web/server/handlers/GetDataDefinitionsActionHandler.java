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

import es.gobcan.istac.indicators.core.dto.DataDefinitionDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.shared.GetDataDefinitionsAction;
import es.gobcan.istac.indicators.web.shared.GetDataDefinitionsResult;

@Component
public class GetDataDefinitionsActionHandler extends AbstractActionHandler<GetDataDefinitionsAction, GetDataDefinitionsResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetDataDefinitionsActionHandler() {
        super(GetDataDefinitionsAction.class);
    }

    @Override
    public GetDataDefinitionsResult execute(GetDataDefinitionsAction action, ExecutionContext context) throws ActionException {
        try {
            List<DataDefinitionDto> dataDefinitionsDtos = indicatorsServiceFacade.retrieveDataDefinitions(ServiceContextHelper.getServiceContext());
            return new GetDataDefinitionsResult(dataDefinitionsDtos);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(GetDataDefinitionsAction action, GetDataDefinitionsResult result, ExecutionContext context) throws ActionException {

    }

}
