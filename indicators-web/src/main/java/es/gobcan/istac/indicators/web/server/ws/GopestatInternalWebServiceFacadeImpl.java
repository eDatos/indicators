package es.gobcan.istac.indicators.web.server.ws;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.gopestat.internal.ws.v1_0.MetamacExceptionFault;
import org.siemac.metamac.gopestat.internal.ws.v1_0.domain.OperationBase;
import org.siemac.metamac.gopestat.internal.ws.v1_0.domain.OperationBaseList;
import org.siemac.metamac.gopestat.internal.ws.v1_0.domain.OperationCriteria;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.web.server.utils.WSExceptionUtils;

@Component
public class GopestatInternalWebServiceFacadeImpl implements GopestatInternalWebServiceFacade {
    
    @Autowired
    private WebServicesLocator webservicesLocator;

    @Override
    public OperationBase retrieveOperation(String operationCode) throws MetamacWebException {
        try {
            return webservicesLocator.getGopestatInternalInterface().retrieveOperation(operationCode);
        } catch (MetamacExceptionFault e) {
            List<MetamacExceptionItem> metamacExceptionItems = WSExceptionUtils.getMetamacExceptionItems(e.getFaultInfo().getExceptionItems());
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(metamacExceptionItems));
        }
    }

    @Override
    public OperationBaseList findOperationsIndicatorsSystem() throws MetamacWebException {
        try {
            OperationCriteria criteria = new OperationCriteria();
            criteria.setIsIndicatorsSystem(Boolean.TRUE);
            return webservicesLocator.getGopestatInternalInterface().findOperations(criteria);
        } catch (MetamacExceptionFault e) {
            List<MetamacExceptionItem> metamacExceptionItems = WSExceptionUtils.getMetamacExceptionItems(e.getFaultInfo().getExceptionItems());
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(metamacExceptionItems));
        }
    }
}