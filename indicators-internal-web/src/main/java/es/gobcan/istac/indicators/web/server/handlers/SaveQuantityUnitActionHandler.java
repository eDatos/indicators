package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.SaveQuantityUnitAction;
import es.gobcan.istac.indicators.web.shared.SaveQuantityUnitResult;

@Component
public class SaveQuantityUnitActionHandler extends SecurityActionHandler<SaveQuantityUnitAction, SaveQuantityUnitResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public SaveQuantityUnitActionHandler() {
        super(SaveQuantityUnitAction.class);
    }

    @Override
    public SaveQuantityUnitResult executeSecurityAction(SaveQuantityUnitAction action) throws ActionException {
        try {
            QuantityUnitDto output = null;
            if (action.getQuantityUnitDto().getUuid() == null) {
                output = indicatorsServiceFacade.createQuantityUnit(ServiceContextHolder.getCurrentServiceContext(), action.getQuantityUnitDto());
            } else {
                output = indicatorsServiceFacade.updateQuantityUnit(ServiceContextHolder.getCurrentServiceContext(), action.getQuantityUnitDto());
            }
            return new SaveQuantityUnitResult(output);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
