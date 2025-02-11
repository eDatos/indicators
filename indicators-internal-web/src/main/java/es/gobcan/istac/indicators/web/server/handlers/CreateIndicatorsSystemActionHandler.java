package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.utils.DtoUtils;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorsSystemAction;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorsSystemResult;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

@Component
public class CreateIndicatorsSystemActionHandler extends SecurityActionHandler<CreateIndicatorsSystemAction, CreateIndicatorsSystemResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public CreateIndicatorsSystemActionHandler() {
        super(CreateIndicatorsSystemAction.class);
    }

    @Override
    public CreateIndicatorsSystemResult executeSecurityAction(CreateIndicatorsSystemAction action) throws ActionException {
        try {

            IndicatorsSystemDtoWeb indicatorsSystemDtoWeb = DtoUtils.createIndicatorsSystemDtoWeb(action.getIndicatorsSystemDto());
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.createIndicatorsSystem(ServiceContextHolder.getCurrentServiceContext(), indicatorsSystemDtoWeb);

            return new CreateIndicatorsSystemResult(DtoUtils.createIndicatorsSystemDtoWeb(indicatorsSystemDto));
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
