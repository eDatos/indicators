package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical_operations.rest.internal.v1_0.domain.Operation;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.rest.StatisticalOperationsRestInternalFacade;
import es.gobcan.istac.indicators.web.server.utils.DtoUtils;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemByCodeAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemByCodeResult;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

@Component
public class GetIndicatorsSystemByCodeActionHandler extends SecurityActionHandler<GetIndicatorsSystemByCodeAction, GetIndicatorsSystemByCodeResult> {

    @Autowired
    private IndicatorsServiceFacade                 indicatorsServiceFacade;

    @Autowired
    private StatisticalOperationsRestInternalFacade statisticalOperationsRestInternalFacade;

    public GetIndicatorsSystemByCodeActionHandler() {
        super(GetIndicatorsSystemByCodeAction.class);
    }

    @Override
    public GetIndicatorsSystemByCodeResult executeSecurityAction(GetIndicatorsSystemByCodeAction action) throws ActionException {
        // Retrieve operation from WS
        Operation operation = statisticalOperationsRestInternalFacade.retrieveOperation(action.getCode());
        // Check if operation (indicators system) exists in the DB
        try {
            // If exists, updates indicators system
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(ServiceContextHolder.getCurrentServiceContext(), action.getCode(),
                    action.getVersionNumber());
            return new GetIndicatorsSystemByCodeResult(DtoUtils.updateIndicatorsSystemDtoWeb(new IndicatorsSystemDtoWeb(), indicatorsSystemDto, operation));
        } catch (MetamacException e) {
            return new GetIndicatorsSystemByCodeResult(DtoUtils.createIndicatorsSystemDtoWeb(operation));
        }
    }

}
