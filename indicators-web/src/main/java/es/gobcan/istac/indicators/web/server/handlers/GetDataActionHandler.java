package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.serviceapi.DataDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsDataServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.shared.GetDataAction;
import es.gobcan.istac.indicators.web.shared.GetDataResult;

@Component
public class GetDataActionHandler extends AbstractActionHandler<GetDataAction, GetDataResult> {

    @Autowired
    private IndicatorsDataServiceFacade indicatorsDataServiceFacade;
    
    public GetDataActionHandler() {
        super(GetDataAction.class);
    }

    @Override
    public GetDataResult execute(GetDataAction action, ExecutionContext context) throws ActionException {
        try {
            DataDto dataDto = indicatorsDataServiceFacade.retrieveData(ServiceContextHelper.getServiceContext(), action.getUuid());
            return new GetDataResult(dataDto);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(GetDataAction action, GetDataResult result, ExecutionContext context) throws ActionException {
        
    }
    
}
