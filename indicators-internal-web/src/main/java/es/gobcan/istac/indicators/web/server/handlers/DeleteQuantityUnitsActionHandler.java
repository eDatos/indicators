package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.DeleteQuantityUnitsAction;
import es.gobcan.istac.indicators.web.shared.DeleteQuantityUnitsResult;

@Component
public class DeleteQuantityUnitsActionHandler extends SecurityActionHandler<DeleteQuantityUnitsAction, DeleteQuantityUnitsResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public DeleteQuantityUnitsActionHandler() {
        super(DeleteQuantityUnitsAction.class);
    }

    @Override
    public DeleteQuantityUnitsResult executeSecurityAction(DeleteQuantityUnitsAction action) throws ActionException {
        try {
            for (String uuid : action.getUuids()) {
                indicatorsServiceFacade.deleteQuantityUnit(ServiceContextHolder.getCurrentServiceContext(), uuid);
            }
            return new DeleteQuantityUnitsResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
