package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.GetDataSourceAction;
import es.gobcan.istac.indicators.web.shared.GetDataSourceResult;

@Component
public class GetDataSourceActionHandler extends SecurityActionHandler<GetDataSourceAction, GetDataSourceResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetDataSourceActionHandler() {
        super(GetDataSourceAction.class);
    }

    @Override
    public GetDataSourceResult executeSecurityAction(GetDataSourceAction action) throws ActionException {
        try {
            DataSourceDto dataSourceDto = indicatorsServiceFacade.retrieveDataSource(ServiceContextHolder.getCurrentServiceContext(), action.getUuid());
            return new GetDataSourceResult(dataSourceDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(GetDataSourceAction action, GetDataSourceResult result, ExecutionContext context) throws ActionException {

    }

}
