package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.SaveGeoGranularityAction;
import es.gobcan.istac.indicators.web.shared.SaveGeoGranularityResult;

@Component
public class SaveGeoGranularityActionHandler extends SecurityActionHandler<SaveGeoGranularityAction, SaveGeoGranularityResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public SaveGeoGranularityActionHandler() {
        super(SaveGeoGranularityAction.class);
    }

    @Override
    public SaveGeoGranularityResult executeSecurityAction(SaveGeoGranularityAction action) throws ActionException {
        try {
            GeographicalGranularityDto output = null;
            if (action.getDto().getUuid() == null) {
                output = indicatorsServiceFacade.createGeographicalGranularity(ServiceContextHolder.getCurrentServiceContext(), action.getDto());
            } else {
                output = indicatorsServiceFacade.updateGeographicalGranularity(ServiceContextHolder.getCurrentServiceContext(), action.getDto());
            }
            return new SaveGeoGranularityResult(output);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
