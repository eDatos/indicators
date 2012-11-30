package es.gobcan.istac.indicators.rest.test.mocks;

import java.util.HashMap;
import java.util.Map;

import org.siemac.metamac.rest.statistical_operations.v1_0.domain.Operation;

import es.gobcan.istac.indicators.rest.clients.adapters.OperationIndicators;

public class OperationMock {

    private static Map<String, OperationIndicators> INSTANCES = new HashMap<String, OperationIndicators>();

    public static OperationIndicators mockOperation1() {
        OperationIndicators operation = INSTANCES.get("Operation_1");
        if (operation != null) {
            return operation;
        }
        
        operation = new OperationIndicators();
        operation.setId("CODIGO_0001"); 
        operation.setTitle(MockUtil.createInternationalStringMap());
        operation.setAcronym(MockUtil.createInternationalStringMap());
        operation.setDescription(MockUtil.createInternationalStringMap());
        operation.setObjective(MockUtil.createInternationalStringMap());
        return operation;
    }

    public static OperationIndicators mockOperation2() {
        OperationIndicators operation = INSTANCES.get("Operation_2");
        if (operation != null) {
            return operation;
        }
        
        operation = new OperationIndicators();
        operation.setId("COD_00002");
        operation.setTitle(MockUtil.createInternationalStringMap());
        operation.setAcronym(MockUtil.createInternationalStringMap());
        operation.setDescription(MockUtil.createInternationalStringMap());
        operation.setObjective(MockUtil.createInternationalStringMap());
        return operation;
    }
}
