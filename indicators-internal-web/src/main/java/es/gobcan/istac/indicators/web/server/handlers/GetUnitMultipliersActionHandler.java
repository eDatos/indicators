package es.gobcan.istac.indicators.web.server.handlers;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.GetUnitMultipliersAction;
import es.gobcan.istac.indicators.web.shared.GetUnitMultipliersResult;

@Component
public class GetUnitMultipliersActionHandler extends AbstractActionHandler<GetUnitMultipliersAction, GetUnitMultipliersResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetUnitMultipliersActionHandler() {
        super(GetUnitMultipliersAction.class);
    }

    @Override
    public GetUnitMultipliersResult execute(GetUnitMultipliersAction action, ExecutionContext context) throws ActionException {
        try {
            List<UnitMultiplierDto> unitMultiplierDtos = indicatorsServiceFacade.retrieveUnitsMultipliers(ServiceContextHolder.getCurrentServiceContext());
            return new GetUnitMultipliersResult(unitMultiplierDtos);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(GetUnitMultipliersAction action, GetUnitMultipliersResult result, ExecutionContext context) throws ActionException {

    }

}
