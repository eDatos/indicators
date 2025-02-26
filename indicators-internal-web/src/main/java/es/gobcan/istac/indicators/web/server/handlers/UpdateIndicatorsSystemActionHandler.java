package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.utils.DtoUtils;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorsSystemAction;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorsSystemResult;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

@Component
public class UpdateIndicatorsSystemActionHandler extends SecurityActionHandler<UpdateIndicatorsSystemAction, UpdateIndicatorsSystemResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public UpdateIndicatorsSystemActionHandler() {
        super(UpdateIndicatorsSystemAction.class);
    }

    @Override
    public UpdateIndicatorsSystemResult executeSecurityAction(UpdateIndicatorsSystemAction action) throws ActionException {
        try {

            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.updateIndicatorsSystem(ServiceContextHolder.getCurrentServiceContext(), action.getIndicatorsSystemUpdated());
            return new UpdateIndicatorsSystemResult(DtoUtils.createOrUpdateIndicatorsSystemDtoWeb(new IndicatorsSystemDtoWeb(), indicatorsSystemDto));
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(UpdateIndicatorsSystemAction action, UpdateIndicatorsSystemResult result, ExecutionContext context) throws ActionException {

    }

}
