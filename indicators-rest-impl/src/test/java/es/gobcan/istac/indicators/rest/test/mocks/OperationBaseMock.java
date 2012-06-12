package es.gobcan.istac.indicators.rest.test.mocks;

import java.util.HashMap;
import java.util.Map;

import org.siemac.metamac.statistical.operations.external.ws.v1_0.domain.OperationBase;
import org.siemac.metamac.statistical.operations.external.ws.v1_0.domain.ProcStatusType;

public class OperationBaseMock {

    private static Map<String, OperationBase> INSTANCES = new HashMap<String, OperationBase>();

    public static OperationBase mockOperationBase1() {
        OperationBase operationBase = INSTANCES.get("OperationBase_1");
        if (operationBase != null) {
            return operationBase;
        }
        
        operationBase = new OperationBase();
        operationBase.setCode("CODIGO_0001"); 
        operationBase.setTitle(MockUtil.createInternationalString());
        operationBase.setAcronym(MockUtil.createInternationalString());
        operationBase.setDescription(MockUtil.createInternationalString());
        operationBase.setObjective(MockUtil.createInternationalString());
        operationBase.setProcStatus(ProcStatusType.PUBLISH_EXTERNALLY);
        return operationBase;
    }

    public static OperationBase mockOperationBase2() {
        OperationBase operationBase = INSTANCES.get("OperationBase_2");
        if (operationBase != null) {
            return operationBase;
        }
        
        operationBase = new OperationBase();
        operationBase.setCode("COD_00002");
        operationBase.setTitle(MockUtil.createInternationalString());
        operationBase.setAcronym(MockUtil.createInternationalString());
        operationBase.setDescription(MockUtil.createInternationalString());
        operationBase.setObjective(MockUtil.createInternationalString());
        operationBase.setProcStatus(ProcStatusType.PUBLISH_EXTERNALLY);
        return operationBase;
    }

}
