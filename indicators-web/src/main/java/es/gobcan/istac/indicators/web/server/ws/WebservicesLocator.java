package es.gobcan.istac.indicators.web.server.ws;

import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPBinding;

import org.siemac.metamac.gopestat.internal.ws.v1_0.MetamacGopestatInternalInterfaceV10;
import org.siemac.metamac.gopestat.internal.ws.v1_0.MetamacGopestatInternalV10;


public class WebservicesLocator implements ServletContextListener {

    private static WebservicesLocator   INSTANCE                        = new WebservicesLocator();

    private static String                   GOPESTAT_INTERNAL_ENDPOINT_ADDRESS;

    static {
        GOPESTAT_INTERNAL_ENDPOINT_ADDRESS = "http://localhost:8080/gopestat-web/ws/gopestat/internal/v1.0/gopestatInternalInterface"; // TODO
    }

//    @WebServiceRef(name = "metamacGopestatInternal")
    private MetamacGopestatInternalV10 metamacGopestatInternal;


    public static WebservicesLocator getInstance() {
        return INSTANCE;
    }

    // Implementantion: ServletContextListener
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // do nothing
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        INSTANCE = this;
    }

//    private static ThreadLocal<WSPorts> ports = new ThreadLocal<WSPorts>() {
//        @Override
//        protected WSPorts initialValue() {
//            return new WSPorts();
//        }
//    };

    public MetamacGopestatInternalInterfaceV10 getGopestatInternalInterface() {
//        WSPorts wsPorts = ports.get();
//        if (wsPorts.getMetamacGopestatInternalInterface() == null) {
//            synchronized (this) {
//                if (wsPorts.getMetamacGopestatInternalInterface() == null) {
                    MetamacGopestatInternalInterfaceV10 metamacGopestatInternalInterface = metamacGopestatInternal.getMetamacGopestatInternalSOAPV10();
                    /* La configuracion solo se carga una vez por thread aunque sea dinamico */
                    updateWSPort(metamacGopestatInternalInterface, GOPESTAT_INTERNAL_ENDPOINT_ADDRESS);
//                    wsPorts.setMetamacGopestatInternalInterface(metamacGopestatInternalInterface);
//                }
//            }
//        }
//        return wsPorts.getMetamacGopestatInternalInterface();
          return metamacGopestatInternalInterface;
    }

    
    private void enableMtom(Object port) {
        BindingProvider bp = (BindingProvider) port;
        SOAPBinding binding = (SOAPBinding) bp.getBinding();
        binding.setMTOMEnabled(true);
    }

    private void updateWSPort(Object port, String url/*, String userName, String password*/) {
        BindingProvider bp = (BindingProvider) port;
        Map<String, Object> rctx = bp.getRequestContext();
        rctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        // TODO webservice user
//        rctx.put(BindingProvider.USERNAME_PROPERTY, userName);
//        rctx.put(BindingProvider.PASSWORD_PROPERTY, password);
    }
}
