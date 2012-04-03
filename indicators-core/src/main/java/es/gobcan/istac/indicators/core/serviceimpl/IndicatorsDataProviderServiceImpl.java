package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsDataProviderService;

@Service("indicatorsDataProviderService")
public class IndicatorsDataProviderServiceImpl implements IndicatorsDataProviderService {

    private final String JAXI_URL = "indicators.jaxi.url";

    @Autowired
    private ConfigurationService configurationService;
    
    public IndicatorsDataProviderServiceImpl() {
    }

    @Override
    public String retrieveDataStructureJson(ServiceContext ctx, String uuid) throws MetamacException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uuidConsulta", uuid);
        params.put("type", "structure");
        return requestForJson(getJaxiUrl()+"/tabla.do", uuid);
    }
    
    @Override
    public String retrieveDataJson(ServiceContext ctx, String uuid) throws MetamacException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uuidConsulta", uuid);
        params.put("type", "data");
        return requestForJson(getJaxiUrl()+"/tabla.do", uuid);
    }
   
    // retrieve from jaxi
    private String requestForJson(String url, String param) {
        Client client = new Client();
        client.setFollowRedirects(true);
        WebResource wresource = client.resource(url);
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
        if (param != null) {
            params.add("uuidConsulta",param);
        }
        // Overrides accion
        params.put("accion", Arrays.asList("jsonMtd"));
        String result = wresource.queryParams(params).accept(MediaType.APPLICATION_JSON).get(String.class);
        return result;
    }


    private String getJaxiUrl() {
        return configurationService.getConfig().getString(JAXI_URL);
    }
}