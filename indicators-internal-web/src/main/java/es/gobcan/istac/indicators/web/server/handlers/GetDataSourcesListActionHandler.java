package es.gobcan.istac.indicators.web.server.handlers;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHolder;
import es.gobcan.istac.indicators.web.shared.GetDataSourcesListAction;
import es.gobcan.istac.indicators.web.shared.GetDataSourcesListResult;

@Component
public class GetDataSourcesListActionHandler extends AbstractActionHandler<GetDataSourcesListAction, GetDataSourcesListResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetDataSourcesListActionHandler() {
        super(GetDataSourcesListAction.class);
    }

    @Override
    public GetDataSourcesListResult execute(GetDataSourcesListAction action, ExecutionContext context) throws ActionException {
        try {
            List<DataSourceDto> dataSourceDtos = indicatorsServiceFacade.retrieveDataSourcesByIndicator(ServiceContextHolder.getCurrentServiceContext(), action.getIndicatorUuid(),
                    action.getIndicatorVersion());
            return new GetDataSourcesListResult(dataSourceDtos);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(GetDataSourcesListAction action, GetDataSourcesListResult result, ExecutionContext context) throws ActionException {

    }

}
