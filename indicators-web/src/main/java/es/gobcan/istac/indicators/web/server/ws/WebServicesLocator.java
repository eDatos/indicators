package es.gobcan.istac.indicators.web.server.ws;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.ws.BindingProvider;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.gopestat.internal.ws.v1_0.MetamacGopestatInternalInterfaceV10;
import org.siemac.metamac.gopestat.internal.ws.v1_0.MetamacGopestatInternalV10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import es.gobcan.istac.indicators.web.server.ServerConstants;

@Component
public class WebServicesLocator {

    @Autowired
    private ConfigurationService       configurationService;

    private MetamacGopestatInternalV10 metamacGopestatInternal                     = null;
    private String                     metamacGopestatInternalEndpoint             = null;

    private ThreadLocal<WSPorts>       ports                                       = new ThreadLocal<WSPorts>() {
       @Override
       protected WSPorts initialValue() {
           return new WSPorts();
       }
    };

    @PostConstruct
    public void initService() throws Exception {
        metamacGopestatInternalEndpoint = configurationService.getProperties().getProperty(ServerConstants.GOPESTAT_INTERNAL_ENDPOINT_ADDRESS_PROPERTY);
        metamacGopestatInternal = new MetamacGopestatInternalV10(ResourceUtils.getURL("classpath:META-INF/wsdl/metamac-gopestat-internal-v1.0.wsdl"));
    }

    public MetamacGopestatInternalInterfaceV10 getGopestatInternalInterface() {
        WSPorts wsPorts = ports.get();
        if (wsPorts.getGopestatInternalInterface() == null) {
            synchronized (this) {
                if (wsPorts.getGopestatInternalInterface() == null) {
                    MetamacGopestatInternalInterfaceV10 gopestatInternalInterface = metamacGopestatInternal.getMetamacGopestatInternalSOAPV10();
                    updateWSPort(gopestatInternalInterface, metamacGopestatInternalEndpoint);
                    wsPorts.setGopestatInternalInterface(gopestatInternalInterface);
                }
            }
        }
        return wsPorts.getGopestatInternalInterface();
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