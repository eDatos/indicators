package es.gobcan.istac.indicators.web.server.handlers;

import java.util.ArrayList;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
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
        // try {
        // List<String> files = indicatorsServiceFacade.exportIndicatorsSystemPublishedToDsplFiles(ServiceContextHolder.getCurrentServiceContext(), action.getSystemUuid(), action.getSystemTitle(),
        // action.getSystemDescription(), action.isMergeTimeGranularities());
        // return new ExportSystemInDsplResult(files);
        // } catch (MetamacException e) {
        // try {
        // indicatorsServiceFacade.planifyExportsDsplJob(ServiceContextHolder.getCurrentServiceContext(), action.getSystemUuid(), action.getCode(), action.isMergeTimeGranularities());
        // return new ExportSystemInDsplResult(new ArrayList<>());
        // } catch (MetamacException e1) {
        // throw WebExceptionUtils.createMetamacWebException(e1);
        // }
        // }

        try {
            indicatorsServiceFacade.planifyExportsDsplJob(ServiceContextHolder.getCurrentServiceContext(), action.getSystemUuid(), action.getCode(), action.isMergeTimeGranularities());
        } catch (MetamacException e) {
            e.printStackTrace();
        }
        return new ExportSystemInDsplResult(new ArrayList<>());
    }
}
