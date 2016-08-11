package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.utils.DtoUtils;
import es.gobcan.istac.indicators.web.shared.ArchiveIndicatorsSystemAction;
import es.gobcan.istac.indicators.web.shared.ArchiveIndicatorsSystemResult;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

@Component
public class ArchiveIndicatorsSystemActionHandler extends SecurityActionHandler<ArchiveIndicatorsSystemAction, ArchiveIndicatorsSystemResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public ArchiveIndicatorsSystemActionHandler() {
        super(ArchiveIndicatorsSystemAction.class);
    }

    @Override
    public ArchiveIndicatorsSystemResult executeSecurityAction(ArchiveIndicatorsSystemAction action) throws ActionException {
        try {
            IndicatorsSystemDtoWeb indicatorsSystemDtoWeb = action.getIndicatorsSystemToArchive();
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.archiveIndicatorsSystem(ServiceContextHolder.getCurrentServiceContext(), indicatorsSystemDtoWeb.getUuid());
            return new ArchiveIndicatorsSystemResult(DtoUtils.updateIndicatorsSystemDtoWeb(indicatorsSystemDtoWeb, indicatorsSystemDto));
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(ArchiveIndicatorsSystemAction action, ArchiveIndicatorsSystemResult result, ExecutionContext context) throws ActionException {

    }

}
