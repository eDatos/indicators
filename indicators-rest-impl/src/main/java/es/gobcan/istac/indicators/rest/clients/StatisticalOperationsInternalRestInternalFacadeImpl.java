package es.gobcan.istac.indicators.rest.clients;

import es.gobcan.istac.indicators.core.conf.MetadataProperties;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.indicators.rest.clients.adapters.OperationIndicators;
import es.gobcan.istac.indicators.rest.mapper.MapperUtil;

public class StatisticalOperationsInternalRestInternalFacadeImpl implements StatisticalOperationsRestInternalFacade {

    @Autowired
    private RestApiLocatorInternal restApiLocator;

    @Autowired
    private MetadataProperties metadataProperties;

    @Override
    public OperationIndicators retrieveOperationById(String operationCode) throws MetamacException {
        Operation operation = restApiLocator.getStatisticalOperationsRestFacadeV10().retrieveOperationById(operationCode);
        return MapperUtil.getOperationIndicators(operation, metadataProperties.getDefaultLanguage());
    }
}
