package es.gobcan.istac.indicators.web.server.ws;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.ws.BindingProvider;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.MetamacStatisticalOperationsInternalInterfaceV10;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.MetamacStatisticalOperationsInternalV10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import es.gobcan.istac.indicators.web.server.ServerConstants;

@Component
public class WebServicesLocator {

    @Autowired
    private ConfigurationService                    configurationService;

    private MetamacStatisticalOperationsInternalV10 metamacStatisticalOperationsInternal        = null;
    private String                                  metamacStatisticalOperationInternalEndpoint = null;

    private ThreadLocal<WebServicePorts>            ports                                       = new ThreadLocal<WebServicePorts>() {
            @Override
            protected WebServicePorts initialValue() {
                return new WebServicePorts();
            }
         };

    @PostConstruct
    public void initService() throws Exception {
        metamacStatisticalOperationInternalEndpoint = configurationService.getProperties().getProperty(ServerConstants.METAMAC_STATISTICAL_OPERATIONS_INTERNAL_ENDPOINT_ADDRESS_PROPERTY);
        metamacStatisticalOperationsInternal = new MetamacStatisticalOperationsInternalV10(ResourceUtils.getURL("classpath:META-INF/wsdl/metamac-statistical-operations-internal-v1.0.wsdl"));
    }

    public MetamacStatisticalOperationsInternalInterfaceV10 getMetamacStatisticalOperationsInternalInterface() {
        WebServicePorts wsPorts = ports.get();
        if (wsPorts.getMetamacStatisticalOperationsInternalInterface() == null) {
            synchronized (this) {
                if (wsPorts.getMetamacStatisticalOperationsInternalInterface() == null) {
                    MetamacStatisticalOperationsInternalInterfaceV10 metamacStatisticalOperationsInternalInterface = metamacStatisticalOperationsInternal
                            .getMetamacStatisticalOperationsInternalSOAPV10();
                    updateWSPort(metamacStatisticalOperationsInternalInterface, metamacStatisticalOperationInternalEndpoint);
                    wsPorts.setMetamacStatisticalOperationsInternalInterface(metamacStatisticalOperationsInternalInterface);
                }
            }
        }
        return wsPorts.getMetamacStatisticalOperationsInternalInterface();
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