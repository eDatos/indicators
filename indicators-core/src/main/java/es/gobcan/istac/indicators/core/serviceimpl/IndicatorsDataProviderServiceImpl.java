package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsDataProviderService;

@Service("indicatorsDataProviderService")
public class IndicatorsDataProviderServiceImpl implements IndicatorsDataProviderService {

    //TODO: Configuration parameter
    private static final String URL_JAXI = "http://192.168.1.44:9080/jaxi-web/tabla.do";

    public IndicatorsDataProviderServiceImpl() {
    }

    @Override
    public String retrieveDataStructureJson(ServiceContext ctx, String uuid) throws MetamacException {
        // retrieve from jaxi
        Map<String, String> params = new HashMap<String, String>();
        params.put("uuidConsulta", uuid);
        String json = requestForJson(URL_JAXI, uuid);
        return json;
    }
    
    @Override
    public String retrieveDataJson(ServiceContext ctx, String uuid) throws MetamacException {
        // TODO Auto-generated method stub
        return null;
    }
   
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


}