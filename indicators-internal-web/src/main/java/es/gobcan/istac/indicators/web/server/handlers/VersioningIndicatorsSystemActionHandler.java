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
import es.gobcan.istac.indicators.web.shared.VersioningIndicatorsSystemAction;
import es.gobcan.istac.indicators.web.shared.VersioningIndicatorsSystemResult;

@Component
public class VersioningIndicatorsSystemActionHandler extends AbstractActionHandler<VersioningIndicatorsSystemAction, VersioningIndicatorsSystemResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public VersioningIndicatorsSystemActionHandler() {
        super(VersioningIndicatorsSystemAction.class);
    }

    @Override
    public VersioningIndicatorsSystemResult execute(VersioningIndicatorsSystemAction action, ExecutionContext context) throws ActionException {
        try {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.versioningIndicatorsSystem(ServiceContextHelper.getServiceContext(), action.getUuid(), action.getVersionType());
            return new VersioningIndicatorsSystemResult(indicatorsSystemDto);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(VersioningIndicatorsSystemAction action, VersioningIndicatorsSystemResult result, ExecutionContext context) throws ActionException {

    }

}
