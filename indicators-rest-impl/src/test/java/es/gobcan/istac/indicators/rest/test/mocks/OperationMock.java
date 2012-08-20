package es.gobcan.istac.indicators.rest.test.mocks;

import java.util.HashMap;
import java.util.Map;

import org.siemac.metamac.rest.statistical_operations.v1_0.domain.Operation;

public class OperationMock {

    private static Map<String, Operation> INSTANCES = new HashMap<String, Operation>();

    public static Operation mockOperation1() {
        Operation operation = INSTANCES.get("Operation_1");
        if (operation != null) {
            return operation;
        }
        
        operation = new Operation();
        operation.setId("CODIGO_0001"); 
        operation.setTitle(MockUtil.createInternationalString());
        operation.setAcronym(MockUtil.createInternationalString());
        operation.setDescription(MockUtil.createInternationalString());
        operation.setObjective(MockUtil.createInternationalString());
        return operation;
    }

    public static Operation mockOperation2() {
        Operation operation = INSTANCES.get("Operation_2");
        if (operation != null) {
            return operation;
        }
        
        operation = new Operation();
        operation.setId("COD_00002");
        operation.setTitle(MockUtil.createInternationalString());
        operation.setAcronym(MockUtil.createInternationalString());
        operation.setDescription(MockUtil.createInternationalString());
        operation.setObjective(MockUtil.createInternationalString());
        return operation;
    }
}
