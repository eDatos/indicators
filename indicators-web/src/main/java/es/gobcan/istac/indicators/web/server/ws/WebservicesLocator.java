package es.gobcan.istac.indicators.web.server.ws;

import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.gopestat.internal.ws.v1_0.MetamacGopestatInternalInterfaceV10;
import org.siemac.metamac.gopestat.internal.ws.v1_0.MetamacGopestatInternalV10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebservicesLocator {

    private final String GOPESTAT_INTERNAL_ENDPOINT_ADDRESS_PROPERTY = "indicators.ws.metamac.gopestat.internal.endpoint";
    
    @Autowired
    private ConfigurationService                configurationService;

    private MetamacGopestatInternalInterfaceV10 gopestatInternalInterface;

    public MetamacGopestatInternalInterfaceV10 getGopestatInternalInterface() {
        if (gopestatInternalInterface == null) {
            // TODO thread local?
            MetamacGopestatInternalV10 metamacGopestatInternal = new MetamacGopestatInternalV10();
            gopestatInternalInterface = metamacGopestatInternal.getMetamacGopestatInternalSOAPV10();
            String port = configurationService.getProperties().getProperty(GOPESTAT_INTERNAL_ENDPOINT_ADDRESS_PROPERTY);
            updateWSPort(gopestatInternalInterface, port);
            
        }
        return gopestatInternalInterface;
    }

    private void updateWSPort(Object port, String url/* , String userName, String password */) {
        BindingProvider bp = (BindingProvider) port;
        Map<String, Object> rctx = bp.getRequestContext();
        rctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        // TODO webservice user
        // rctx.put(BindingProvider.USERNAME_PROPERTY, userName);
        // rctx.put(BindingProvider.PASSWORD_PROPERTY, password);
    }
}