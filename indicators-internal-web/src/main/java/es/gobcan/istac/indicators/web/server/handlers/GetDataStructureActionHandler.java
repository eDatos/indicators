package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.DataStructureDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import es.gobcan.istac.indicators.web.shared.GetDataStructureAction;
import es.gobcan.istac.indicators.web.shared.GetDataStructureResult;

@Component
public class GetDataStructureActionHandler extends AbstractActionHandler<GetDataStructureAction, GetDataStructureResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetDataStructureActionHandler() {
        super(GetDataStructureAction.class);
    }

    @Override
    public GetDataStructureResult execute(GetDataStructureAction action, ExecutionContext context) throws ActionException {
        try {
            DataStructureDto dataStructureDto = indicatorsServiceFacade.retrieveDataStructure(ServiceContextHolder.getCurrentServiceContext(), action.getUuid());
            return new GetDataStructureResult(dataStructureDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(GetDataStructureAction action, GetDataStructureResult result, ExecutionContext context) throws ActionException {

    }

}
