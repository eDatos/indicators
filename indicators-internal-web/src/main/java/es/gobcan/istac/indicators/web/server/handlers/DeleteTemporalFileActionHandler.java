package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.DeleteTemporalFileAction;
import es.gobcan.istac.indicators.web.shared.DeleteTemporalFileResult;

@Component
public class DeleteTemporalFileActionHandler extends SecurityActionHandler<DeleteTemporalFileAction, DeleteTemporalFileResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public DeleteTemporalFileActionHandler() {
        super(DeleteTemporalFileAction.class);
    }

    public DeleteTemporalFileActionHandler(Class<DeleteTemporalFileAction> actionType) {
        super(actionType);
    }

    @Override
    public DeleteTemporalFileResult executeSecurityAction(DeleteTemporalFileAction action) throws ActionException {
        try {
            indicatorsServiceFacade.deleteTemporalFile(ServiceContextHolder.getCurrentServiceContext(), action.getTemporalFile());
            return new DeleteTemporalFileResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
