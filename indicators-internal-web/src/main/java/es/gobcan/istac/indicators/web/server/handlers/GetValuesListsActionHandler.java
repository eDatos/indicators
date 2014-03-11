package es.gobcan.istac.indicators.web.server.handlers;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.GetValuesListsAction;
import es.gobcan.istac.indicators.web.shared.GetValuesListsResult;

@Component
public class GetValuesListsActionHandler extends SecurityActionHandler<GetValuesListsAction, GetValuesListsResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetValuesListsActionHandler() {
        super(GetValuesListsAction.class);
    }

    @Override
    public GetValuesListsResult executeSecurityAction(GetValuesListsAction action) throws ActionException {
        try {
            List<QuantityUnitDto> quantityUnits = indicatorsServiceFacade.retrieveQuantityUnits(ServiceContextHolder.getCurrentServiceContext());

            List<GeographicalGranularityDto> geoGranularities = indicatorsServiceFacade.retrieveGeographicalGranularities(ServiceContextHolder.getCurrentServiceContext());

            List<UnitMultiplierDto> unitMultiplers = indicatorsServiceFacade.retrieveUnitsMultipliers(ServiceContextHolder.getCurrentServiceContext());

            return new GetValuesListsResult(quantityUnits, geoGranularities, unitMultiplers);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
