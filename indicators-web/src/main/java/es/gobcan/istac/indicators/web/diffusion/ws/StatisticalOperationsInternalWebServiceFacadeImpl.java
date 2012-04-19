package es.gobcan.istac.indicators.web.diffusion.ws;

import java.math.BigInteger;
import java.util.List;

import org.siemac.metamac.schema.common.v1_0.domain.MetamacCriteria;
import org.siemac.metamac.schema.common.v1_0.domain.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.schema.common.v1_0.domain.MetamacCriteriaDisjunctionRestriction;
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

    @Override
    public OperationBaseList findOperationsIndicatorsSystem(List<String> indicatorsSystemsCodes) throws MetamacExceptionFault {
        
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        
        MetamacCriteriaConjunctionRestriction conjunction = new MetamacCriteriaConjunctionRestriction();
        metamacCriteria.setRestriction(conjunction);
        
        conjunction.setRestrictions(new MetamacCriteriaRestrictionList());

        // Is indicators system
        MetamacCriteriaPropertyRestriction isIndicatorsSystemPropertyRestriction = new MetamacCriteriaPropertyRestriction();
        isIndicatorsSystemPropertyRestriction.setPropertyName(OperationCriteriaPropertyRestriction.IS_INDICATORS_SYSTEM.value());
        isIndicatorsSystemPropertyRestriction.setOperation(OperationType.EQ);
        isIndicatorsSystemPropertyRestriction.setBooleanValue(Boolean.TRUE);
        conjunction.getRestrictions().getRestriction().add(isIndicatorsSystemPropertyRestriction);

        // Only published externally
        MetamacCriteriaPropertyRestriction publishedExternallyRestriction = new MetamacCriteriaPropertyRestriction();
        publishedExternallyRestriction.setPropertyName(OperationCriteriaPropertyRestriction.PROC_STATUS.value());
        publishedExternallyRestriction.setOperation(OperationType.EQ);
        publishedExternallyRestriction.setStringValue(ProcStatusType.PUBLISH_EXTERNALLY.value());
        conjunction.getRestrictions().getRestriction().add(publishedExternallyRestriction);
        
        // By codes
        if (indicatorsSystemsCodes != null && indicatorsSystemsCodes.size() != 0) {
            MetamacCriteriaDisjunctionRestriction disjunctionCodeRestriction = new MetamacCriteriaDisjunctionRestriction();
            disjunctionCodeRestriction.setRestrictions(new MetamacCriteriaRestrictionList());
            for (String indicatorsSystemCode : indicatorsSystemsCodes) {
                MetamacCriteriaPropertyRestriction codePropertyRestriction = new MetamacCriteriaPropertyRestriction();
                codePropertyRestriction.setPropertyName(OperationCriteriaPropertyRestriction.CODE.value());
                codePropertyRestriction.setOperation(OperationType.EQ);
                codePropertyRestriction.setStringValue(indicatorsSystemCode);
                disjunctionCodeRestriction.getRestrictions().getRestriction().add(codePropertyRestriction);
            }
            conjunction.getRestrictions().getRestriction().add(disjunctionCodeRestriction);
        }
        
        // All results
        metamacCriteria.setMaximumResultSize(BigInteger.valueOf(Integer.MAX_VALUE));
        
        // Find
        FindOperationsResult findOperationsResult = webservicesLocator.getMetamacStatisticalOperationsInternalInterface().findOperations(metamacCriteria);
        return findOperationsResult.getOperations();
    }
}