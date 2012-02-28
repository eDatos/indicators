package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemStructureDto;
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.server.services.IndicatorsServiceWrapper;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemStructureAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemStructureResult;

@Component
public class GetIndicatorsSystemStructureHandler extends AbstractActionHandler<GetIndicatorsSystemStructureAction, GetIndicatorsSystemStructureResult> {

    @Autowired
    private IndicatorsServiceWrapper service;
    
    public GetIndicatorsSystemStructureHandler() {
        super(GetIndicatorsSystemStructureAction.class);
    }

    @Override
    public GetIndicatorsSystemStructureResult execute(GetIndicatorsSystemStructureAction action, ExecutionContext context) throws ActionException {
       
        try {
            IndicatorsSystemStructureDto structure = null;
            structure = service.retrieveIndicatorsSystemStructureByCode(ServiceContextHelper.getServiceContext(), action.getCode());
            return new GetIndicatorsSystemStructureResult(structure);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(GetIndicatorsSystemStructureAction action, GetIndicatorsSystemStructureResult result, ExecutionContext context) throws ActionException {

    }

}
