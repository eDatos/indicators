package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.shared.GetDataSourceAction;
import es.gobcan.istac.indicators.web.shared.GetDataSourceResult;

@Component
public class GetDataSourceActionHandler extends AbstractActionHandler<GetDataSourceAction, GetDataSourceResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetDataSourceActionHandler() {
        super(GetDataSourceAction.class);
    }

    @Override
    public GetDataSourceResult execute(GetDataSourceAction action, ExecutionContext context) throws ActionException {
        try {
            DataSourceDto dataSourceDto = indicatorsServiceFacade.retrieveDataSource(ServiceContextHelper.getServiceContext(), action.getUuid());
            return new GetDataSourceResult(dataSourceDto);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(GetDataSourceAction action, GetDataSourceResult result, ExecutionContext context) throws ActionException {

    }

}
