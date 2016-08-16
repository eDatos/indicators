package es.gobcan.istac.indicators.web.server.handlers;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.siemac.metamac.web.common.shared.exception.MetamacWebExceptionItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.PopulateIndicatorDataAction;
import es.gobcan.istac.indicators.web.shared.PopulateIndicatorDataResult;

@Component
public class PopulateIndicatorDataActionHandler extends SecurityActionHandler<PopulateIndicatorDataAction, PopulateIndicatorDataResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public PopulateIndicatorDataActionHandler() {
        super(PopulateIndicatorDataAction.class);
    }

    @Override
    public PopulateIndicatorDataResult executeSecurityAction(PopulateIndicatorDataAction action) throws ActionException {
        try {
            List<MetamacExceptionItem> serviceExceptionItems = indicatorsServiceFacade.populateIndicatorData(ServiceContextHolder.getCurrentServiceContext(), action.getIndicatorUuid());
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(ServiceContextHolder.getCurrentServiceContext(), action.getIndicatorUuid(), null);

            if (serviceExceptionItems.isEmpty()) {
                return new PopulateIndicatorDataResult(indicatorDto, null);
            } else {
                List<MetamacWebExceptionItem> webExceptionItems = WebExceptionUtils.getMetamacWebExceptionItems(null, serviceExceptionItems);
                MetamacWebException webException = new MetamacWebException(webExceptionItems);
                return new PopulateIndicatorDataResult(indicatorDto, webException);
            }
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(PopulateIndicatorDataAction action, PopulateIndicatorDataResult result, ExecutionContext context) throws ActionException {

    }

}
