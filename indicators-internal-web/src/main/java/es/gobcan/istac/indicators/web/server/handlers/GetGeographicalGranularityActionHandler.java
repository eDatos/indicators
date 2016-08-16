package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularityAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularityResult;

@Component
public class GetGeographicalGranularityActionHandler extends SecurityActionHandler<GetGeographicalGranularityAction, GetGeographicalGranularityResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetGeographicalGranularityActionHandler() {
        super(GetGeographicalGranularityAction.class);
    }

    @Override
    public GetGeographicalGranularityResult executeSecurityAction(GetGeographicalGranularityAction action) throws ActionException {
        try {
            return new GetGeographicalGranularityResult(indicatorsServiceFacade.retrieveGeographicalGranularity(ServiceContextHolder.getCurrentServiceContext(),
                    action.getGeographicalGranularityUuid()));
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
