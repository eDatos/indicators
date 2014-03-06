package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.DeleteUnitMultipliersAction;
import es.gobcan.istac.indicators.web.shared.DeleteUnitMultipliersResult;

@Component
public class DeleteUnitMultipliersActionHandler extends SecurityActionHandler<DeleteUnitMultipliersAction, DeleteUnitMultipliersResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public DeleteUnitMultipliersActionHandler() {
        super(DeleteUnitMultipliersAction.class);
    }

    @Override
    public DeleteUnitMultipliersResult executeSecurityAction(DeleteUnitMultipliersAction action) throws ActionException {
        try {
            for (String uuid : action.getUuids()) {
                indicatorsServiceFacade.deleteUnitMultiplier(ServiceContextHolder.getCurrentServiceContext(), uuid);
            }
            return new DeleteUnitMultipliersResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
