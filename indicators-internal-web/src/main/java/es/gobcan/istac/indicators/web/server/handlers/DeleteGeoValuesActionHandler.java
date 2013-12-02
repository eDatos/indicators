package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.DeleteGeoValuesAction;
import es.gobcan.istac.indicators.web.shared.DeleteGeoValuesResult;

@Component
public class DeleteGeoValuesActionHandler extends SecurityActionHandler<DeleteGeoValuesAction, DeleteGeoValuesResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public DeleteGeoValuesActionHandler() {
        super(DeleteGeoValuesAction.class);
    }

    @Override
    public DeleteGeoValuesResult executeSecurityAction(DeleteGeoValuesAction action) throws ActionException {
        try {
            for (String uuid : action.getUuids()) {
                indicatorsServiceFacade.deleteGeographicalValue(ServiceContextHolder.getCurrentServiceContext(), uuid);
            }
            return new DeleteGeoValuesResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
