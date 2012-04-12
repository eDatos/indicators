package es.gobcan.istac.indicators.web.ws;

import org.siemac.metamac.statistical.operations.internal.ws.v1_0.MetamacStatisticalOperationsInternalInterfaceV10;

public class WebServicePorts {

    private MetamacStatisticalOperationsInternalInterfaceV10 metamacStatisticalOperationsInternalInterface;
    
    public MetamacStatisticalOperationsInternalInterfaceV10 getMetamacStatisticalOperationsInternalInterface() {
        return metamacStatisticalOperationsInternalInterface;
    }
    
    public void setMetamacStatisticalOperationsInternalInterface(MetamacStatisticalOperationsInternalInterfaceV10 metamacStatisticalOperationsInternalInterface) {
        this.metamacStatisticalOperationsInternalInterface = metamacStatisticalOperationsInternalInterface;
    }
}
