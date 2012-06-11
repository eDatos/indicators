package es.gobcan.istac.indicators.web.server.handlers;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.GetQuantityUnitsListAction;
import es.gobcan.istac.indicators.web.shared.GetQuantityUnitsListResult;

@Component
public class GetQuantityUnitsListActionHandler extends SecurityActionHandler<GetQuantityUnitsListAction, GetQuantityUnitsListResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetQuantityUnitsListActionHandler() {
        super(GetQuantityUnitsListAction.class);
    }

    @Override
    public GetQuantityUnitsListResult executeSecurityAction(GetQuantityUnitsListAction action) throws ActionException {
        try {
            List<QuantityUnitDto> quantityUnitDtos = indicatorsServiceFacade.retrieveQuantityUnits(ServiceContextHolder.getCurrentServiceContext());
            return new GetQuantityUnitsListResult(quantityUnitDtos);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(GetQuantityUnitsListAction action, GetQuantityUnitsListResult result, ExecutionContext context) throws ActionException {

    }

}
