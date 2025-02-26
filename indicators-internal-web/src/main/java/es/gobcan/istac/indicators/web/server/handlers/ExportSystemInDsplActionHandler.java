package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.ExportSystemInDsplAction;
import es.gobcan.istac.indicators.web.shared.ExportSystemInDsplResult;

@Component
public class ExportSystemInDsplActionHandler extends SecurityActionHandler<ExportSystemInDsplAction, ExportSystemInDsplResult> {

    protected final Logger          logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public ExportSystemInDsplActionHandler() {
        super(ExportSystemInDsplAction.class);
    }

    @Override
    public ExportSystemInDsplResult executeSecurityAction(ExportSystemInDsplAction action) throws ActionException {
        try {
            indicatorsServiceFacade.planifyExportsDsplJob(ServiceContextHolder.getCurrentServiceContext(), action.getSystemUuid(), action.getCode(), action.isMergeTimeGranularities(),
                    Boolean.TRUE.equals(action.getIsOperational()));
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
        return new ExportSystemInDsplResult(null);
    }
}
