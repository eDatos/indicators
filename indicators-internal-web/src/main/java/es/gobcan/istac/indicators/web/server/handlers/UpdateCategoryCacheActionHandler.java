package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.UpdateCategoryCacheAction;
import es.gobcan.istac.indicators.web.shared.UpdateCategoryCacheResult;

@Component
public class UpdateCategoryCacheActionHandler extends SecurityActionHandler<UpdateCategoryCacheAction, UpdateCategoryCacheResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public UpdateCategoryCacheActionHandler() {
        super(UpdateCategoryCacheAction.class);
    }

    @Override
    public UpdateCategoryCacheResult executeSecurityAction(UpdateCategoryCacheAction action) throws ActionException {

        try {
            indicatorsServiceFacade.updateCategoryCacheAll(ServiceContextHolder.getCurrentServiceContext());
            return new UpdateCategoryCacheResult.Builder().build();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
