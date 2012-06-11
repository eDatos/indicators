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
import es.gobcan.istac.indicators.web.shared.SaveDataSourceAction;
import es.gobcan.istac.indicators.web.shared.SaveDataSourceResult;

@Component
public class SaveDataSourceActionHandler extends SecurityActionHandler<SaveDataSourceAction, SaveDataSourceResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public SaveDataSourceActionHandler() {
        super(SaveDataSourceAction.class);
    }

    @Override
    public SaveDataSourceResult executeSecurityAction(SaveDataSourceAction action) throws ActionException {
        try {
            DataSourceDto dataSourceDto = null;
            if (action.getDataSourceDtoToSave().getUuid() == null) {
                dataSourceDto = indicatorsServiceFacade.createDataSource(ServiceContextHolder.getCurrentServiceContext(), action.getIndicatorUuid(), action.getDataSourceDtoToSave());
            } else {
                dataSourceDto = indicatorsServiceFacade.updateDataSource(ServiceContextHolder.getCurrentServiceContext(), action.getDataSourceDtoToSave());
            }
            return new SaveDataSourceResult(dataSourceDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(SaveDataSourceAction action, SaveDataSourceResult result, ExecutionContext context) throws ActionException {

    }

}
