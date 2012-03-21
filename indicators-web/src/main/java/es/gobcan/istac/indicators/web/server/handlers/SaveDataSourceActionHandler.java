package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.shared.SaveDataSourceAction;
import es.gobcan.istac.indicators.web.shared.SaveDataSourceResult;


@Component
public class SaveDataSourceActionHandler extends AbstractActionHandler<SaveDataSourceAction, SaveDataSourceResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;
    
    public SaveDataSourceActionHandler() {
        super(SaveDataSourceAction.class);
    }

    @Override
    public SaveDataSourceResult execute(SaveDataSourceAction action, ExecutionContext context) throws ActionException {
        try {
            DataSourceDto dataSourceDto = null;
            if (action.getDataSourceDtoToSave().getUuid() == null) {
                dataSourceDto = indicatorsServiceFacade.createDataSource(ServiceContextHelper.getServiceContext(), action.getIndicatorUuid(), action.getDataSourceDtoToSave());
            } else {
                dataSourceDto = indicatorsServiceFacade.updateDataSource(ServiceContextHelper.getServiceContext(), action.getDataSourceDtoToSave());
            }
            return new SaveDataSourceResult(dataSourceDto);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(SaveDataSourceAction action, SaveDataSourceResult result, ExecutionContext context) throws ActionException {
        
    }

}
