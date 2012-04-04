package es.gobcan.istac.indicators.web.server.ws;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.schema.common.v1_0.domain.MetamacCriteria;
import org.siemac.metamac.schema.common.v1_0.domain.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.schema.common.v1_0.domain.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.schema.common.v1_0.domain.MetamacCriteriaRestrictionList;
import org.siemac.metamac.schema.common.v1_0.domain.OperationType;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.MetamacExceptionFault;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.FindOperationsResult;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.OperationBase;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.OperationBaseList;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.OperationCriteriaPropertyRestriction;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.web.server.utils.WSExceptionUtils;

@Component
public class StatisticalOperationsInternalWebServiceFacadeImpl implements StatisticalOperationsInternalWebServiceFacade {

    @Autowired
    private WebServicesLocator webservicesLocator;

    @Override
    public OperationBase retrieveOperation(String operationCode) throws MetamacWebException {
        try {
            return webservicesLocator.getMetamacStatisticalOperationsInternalInterface().retrieveOperation(operationCode);
        } catch (MetamacExceptionFault e) {
            List<MetamacExceptionItem> metamacExceptionItems = WSExceptionUtils.getMetamacExceptionItems(e.getFaultInfo().getExceptionItems());
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(metamacExceptionItems));
        }
    }

    // TODO pendiente de paginación METAMAC-434
    @Override
    public OperationBaseList findOperationsIndicatorsSystem() throws MetamacWebException {
        try {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            metamacCriteria.setRestriction(new MetamacCriteriaConjunctionRestriction());
            ((MetamacCriteriaConjunctionRestriction) metamacCriteria.getRestriction()).setRestrictions(new MetamacCriteriaRestrictionList());

            // Is indicators system
            MetamacCriteriaPropertyRestriction isIndicatorsSystemPropertyRestriction = new MetamacCriteriaPropertyRestriction();
            isIndicatorsSystemPropertyRestriction.setPropertyName(OperationCriteriaPropertyRestriction.IS_INDICATORS_SYSTEM.value());
            isIndicatorsSystemPropertyRestriction.setOperation(OperationType.EQ);
            isIndicatorsSystemPropertyRestriction.setBooleanValue(Boolean.TRUE);
            ((MetamacCriteriaConjunctionRestriction) metamacCriteria.getRestriction()).getRestrictions().getRestriction().add(isIndicatorsSystemPropertyRestriction);

            FindOperationsResult findOperationsResult = webservicesLocator.getMetamacStatisticalOperationsInternalInterface().findOperations(metamacCriteria);
            return findOperationsResult.getOperations();
        } catch (MetamacExceptionFault e) {
            List<MetamacExceptionItem> metamacExceptionItems = WSExceptionUtils.getMetamacExceptionItems(e.getFaultInfo().getExceptionItems());
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(metamacExceptionItems));
        }
    }
}