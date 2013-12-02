package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.SaveUnitMultiplierAction;
import es.gobcan.istac.indicators.web.shared.SaveUnitMultiplierResult;

@Component
public class SaveUnitMultiplierActionHandler extends SecurityActionHandler<SaveUnitMultiplierAction, SaveUnitMultiplierResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public SaveUnitMultiplierActionHandler() {
        super(SaveUnitMultiplierAction.class);
    }

    @Override
    public SaveUnitMultiplierResult executeSecurityAction(SaveUnitMultiplierAction action) throws ActionException {
        try {
            UnitMultiplierDto output = null;
            if (action.getDto().getUuid() == null) {
                output = indicatorsServiceFacade.createUnitMultiplier(ServiceContextHolder.getCurrentServiceContext(), action.getDto());
            } else {
                output = indicatorsServiceFacade.updateUnitMultiplier(ServiceContextHolder.getCurrentServiceContext(), action.getDto());
            }
            return new SaveUnitMultiplierResult(output);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
