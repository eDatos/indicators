package es.gobcan.istac.indicators.web.server.handlers;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.serviceapi.QuantityUnitDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.shared.GetQuantityUnitsListAction;
import es.gobcan.istac.indicators.web.shared.GetQuantityUnitsListResult;

@Component
public class GetQuantityUnitsListActionHandler extends AbstractActionHandler<GetQuantityUnitsListAction, GetQuantityUnitsListResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetQuantityUnitsListActionHandler() {
        super(GetQuantityUnitsListAction.class);
    }

    @Override
    public GetQuantityUnitsListResult execute(GetQuantityUnitsListAction action, ExecutionContext context) throws ActionException {
        try {
            List<QuantityUnitDto> quantityUnitDtos = indicatorsServiceFacade.findQuantityUnits(ServiceContextHelper.getServiceContext());
            return new GetQuantityUnitsListResult(quantityUnitDtos);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(GetQuantityUnitsListAction action, GetQuantityUnitsListResult result, ExecutionContext context) throws ActionException {

    }

}
