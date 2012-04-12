package es.gobcan.istac.indicators.web.ws;

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
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.ProcStatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatisticalOperationsInternalWebServiceFacadeImpl implements StatisticalOperationsInternalWebServiceFacade {

    @Autowired
    private WebServicesLocator webservicesLocator;

    @Override
    public OperationBase retrieveOperation(String operationCode) throws MetamacExceptionFault {
        return webservicesLocator.getMetamacStatisticalOperationsInternalInterface().retrieveOperation(operationCode);
    }

    // TODO paginación? METAMAC-434
    @Override
    public OperationBaseList findOperationsIndicatorsSystem() throws MetamacExceptionFault {
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        metamacCriteria.setRestriction(new MetamacCriteriaConjunctionRestriction());
        ((MetamacCriteriaConjunctionRestriction) metamacCriteria.getRestriction()).setRestrictions(new MetamacCriteriaRestrictionList());

        // Is indicators system
        MetamacCriteriaPropertyRestriction isIndicatorsSystemPropertyRestriction = new MetamacCriteriaPropertyRestriction();
        isIndicatorsSystemPropertyRestriction.setPropertyName(OperationCriteriaPropertyRestriction.IS_INDICATORS_SYSTEM.value());
        isIndicatorsSystemPropertyRestriction.setOperation(OperationType.EQ);
        isIndicatorsSystemPropertyRestriction.setBooleanValue(Boolean.TRUE);
        ((MetamacCriteriaConjunctionRestriction) metamacCriteria.getRestriction()).getRestrictions().getRestriction().add(isIndicatorsSystemPropertyRestriction);

        // Only published externally
        MetamacCriteriaPropertyRestriction publishedExternallyRestriction = new MetamacCriteriaPropertyRestriction();
        publishedExternallyRestriction.setPropertyName(OperationCriteriaPropertyRestriction.PROC_STATUS.value());
        publishedExternallyRestriction.setOperation(OperationType.EQ);
        publishedExternallyRestriction.setStringValue(ProcStatusType.PUBLISH_EXTERNALLY.value());
        ((MetamacCriteriaConjunctionRestriction) metamacCriteria.getRestriction()).getRestrictions().getRestriction().add(publishedExternallyRestriction);
        
        // Find
        FindOperationsResult findOperationsResult = webservicesLocator.getMetamacStatisticalOperationsInternalInterface().findOperations(metamacCriteria);
        return findOperationsResult.getOperations();
    }
}