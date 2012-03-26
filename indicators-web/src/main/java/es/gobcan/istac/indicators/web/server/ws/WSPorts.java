package es.gobcan.istac.indicators.web.server.ws;

import org.siemac.metamac.gopestat.internal.ws.v1_0.MetamacGopestatInternalInterfaceV10;

public class WSPorts {

    private MetamacGopestatInternalInterfaceV10 gopestatInternalInterface;
    
    public MetamacGopestatInternalInterfaceV10 getGopestatInternalInterface() {
        return gopestatInternalInterface;
    }
    
    public void setGopestatInternalInterface(MetamacGopestatInternalInterfaceV10 gopestatInternalInterface) {
        this.gopestatInternalInterface = gopestatInternalInterface;
    }
}
