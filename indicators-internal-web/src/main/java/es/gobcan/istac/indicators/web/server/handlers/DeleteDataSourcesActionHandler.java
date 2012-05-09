package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHolder;
import es.gobcan.istac.indicators.web.shared.DeleteDataSourcesAction;
import es.gobcan.istac.indicators.web.shared.DeleteDataSourcesResult;

@Component
public class DeleteDataSourcesActionHandler extends AbstractActionHandler<DeleteDataSourcesAction, DeleteDataSourcesResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public DeleteDataSourcesActionHandler() {
        super(DeleteDataSourcesAction.class);
    }

    @Override
    public DeleteDataSourcesResult execute(DeleteDataSourcesAction action, ExecutionContext context) throws ActionException {
        for (String uuid : action.getUuids()) {
            try {
                indicatorsServiceFacade.deleteDataSource(ServiceContextHolder.getCurrentServiceContext(), uuid);
            } catch (MetamacException e) {
                throw WebExceptionUtils.createMetamacWebException(e);
            }
        }
        return new DeleteDataSourcesResult();

    }

    @Override
    public void undo(DeleteDataSourcesAction action, DeleteDataSourcesResult result, ExecutionContext context) throws ActionException {

    }

}
