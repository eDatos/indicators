package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.server.utils.DtoUtils;
import es.gobcan.istac.indicators.web.shared.ArchiveIndicatorsSystemAction;
import es.gobcan.istac.indicators.web.shared.ArchiveIndicatorsSystemResult;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

@Component
public class ArchiveIndicatorsSystemActionHandler extends AbstractActionHandler<ArchiveIndicatorsSystemAction, ArchiveIndicatorsSystemResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public ArchiveIndicatorsSystemActionHandler() {
        super(ArchiveIndicatorsSystemAction.class);
    }

    @Override
    public ArchiveIndicatorsSystemResult execute(ArchiveIndicatorsSystemAction action, ExecutionContext context) throws ActionException {
        try {
            IndicatorsSystemDtoWeb indicatorsSystemDtoWeb = action.getIndicatorsSystemToArchive();
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.archiveIndicatorsSystem(ServiceContextHelper.getServiceContext(), indicatorsSystemDtoWeb.getUuid());
            return new ArchiveIndicatorsSystemResult(DtoUtils.updateIndicatorsSystemDtoWeb(indicatorsSystemDtoWeb, indicatorsSystemDto));
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(ArchiveIndicatorsSystemAction action, ArchiveIndicatorsSystemResult result, ExecutionContext context) throws ActionException {

    }

}
