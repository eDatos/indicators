package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemStructureDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemStructureAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemStructureResult;

@Component
public class GetIndicatorsSystemStructureActionHandler extends SecurityActionHandler<GetIndicatorsSystemStructureAction, GetIndicatorsSystemStructureResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetIndicatorsSystemStructureActionHandler() {
        super(GetIndicatorsSystemStructureAction.class);
    }

    @Override
    public GetIndicatorsSystemStructureResult executeSecurityAction(GetIndicatorsSystemStructureAction action) throws ActionException {
        // Check if operation (indicators system) exists in the DB
        try {
            // If exists, retrieve indicators system structure
            IndicatorsSystemDto system = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(ServiceContextHolder.getCurrentServiceContext(), action.getCode(), null);
            IndicatorsSystemStructureDto structure = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(ServiceContextHolder.getCurrentServiceContext(), system.getUuid(), null);
            return new GetIndicatorsSystemStructureResult(structure);
        } catch (MetamacException e) {
            // If does not exist, return an empty structure
            return new GetIndicatorsSystemStructureResult(new IndicatorsSystemStructureDto());
        }
    }

    @Override
    public void undo(GetIndicatorsSystemStructureAction action, GetIndicatorsSystemStructureResult result, ExecutionContext context) throws ActionException {

    }

}
