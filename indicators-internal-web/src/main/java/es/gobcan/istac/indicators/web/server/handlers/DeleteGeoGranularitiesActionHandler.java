package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.DeleteGeoGranularitiesAction;
import es.gobcan.istac.indicators.web.shared.DeleteGeoGranularitiesResult;

@Component
public class DeleteGeoGranularitiesActionHandler extends SecurityActionHandler<DeleteGeoGranularitiesAction, DeleteGeoGranularitiesResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public DeleteGeoGranularitiesActionHandler() {
        super(DeleteGeoGranularitiesAction.class);
    }

    @Override
    public DeleteGeoGranularitiesResult executeSecurityAction(DeleteGeoGranularitiesAction action) throws ActionException {
        try {
            for (String uuid : action.getUuids()) {
                indicatorsServiceFacade.deleteGeographicalGranularity(ServiceContextHolder.getCurrentServiceContext(), uuid);
            }
            return new DeleteGeoGranularitiesResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
