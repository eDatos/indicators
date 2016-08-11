package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.SaveGeoValueAction;
import es.gobcan.istac.indicators.web.shared.SaveGeoValueResult;

@Component
public class SaveGeoValueActionHandler extends SecurityActionHandler<SaveGeoValueAction, SaveGeoValueResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public SaveGeoValueActionHandler() {
        super(SaveGeoValueAction.class);
    }

    @Override
    public SaveGeoValueResult executeSecurityAction(SaveGeoValueAction action) throws ActionException {
        try {
            GeographicalValueDto output = null;
            if (action.getDto().getUuid() == null) {
                output = indicatorsServiceFacade.createGeographicalValue(ServiceContextHolder.getCurrentServiceContext(), action.getDto());
            } else {
                output = indicatorsServiceFacade.updateGeographicalValue(ServiceContextHolder.getCurrentServiceContext(), action.getDto());
            }
            return new SaveGeoValueResult(output);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
