package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsDataProviderService;

@Service("indicatorsDataProviderService")
public class IndicatorsDataProviderServiceImpl implements IndicatorsDataProviderService {

    private static final String            JAXI_URL_SUFFIX_TABLA_DO         = "/tabla.do";

    private static final String            JAXI_PARAM_TYPE                  = "type";
    private static final String            JAXI_PARAM_TYPE_VALUE_STRUCTURE  = "structure";
    private static final String            JAXI_PARAM_TYPE_VALUE_DATA       = "data";

    private static final String            JAXI_PARAM_ACCION                = "accion";
    private static final String            JAXI_PARAM_ACCION_VALUE_JSON_MTD = "jsonMtd";

    private static final String            JAXI_PARAM_UUID_CONSULTA         = "uuidConsulta";

    private static final Logger            LOG                              = LoggerFactory.getLogger(IndicatorsDataProviderServiceImpl.class);

    @Autowired
    private IndicatorsConfigurationService configurationService;

    public IndicatorsDataProviderServiceImpl() {
    }

    @Override
    public String retrieveDataStructureJson(ServiceContext ctx, String uuid) throws MetamacException {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put(JAXI_PARAM_UUID_CONSULTA, uuid);
            params.put(JAXI_PARAM_TYPE, JAXI_PARAM_TYPE_VALUE_STRUCTURE);
            return requestForJson(getJaxiUrl(), uuid);
        } catch (Exception e) {
            throw new MetamacException(e, ServiceExceptionType.DATA_STRUCTURE_RETRIEVE_ERROR, uuid);
        }
    }

    @Override
    public String retrieveDataJson(ServiceContext ctx, String uuid) throws MetamacException {
        try {
            LOG.info("Retriving data form URL: " + getJaxiUrl() + "?accion=jsonMtd&uuidConsulta=" + uuid);
            Map<String, String> params = new HashMap<String, String>();
            params.put(JAXI_PARAM_UUID_CONSULTA, uuid);
            params.put(JAXI_PARAM_TYPE, JAXI_PARAM_TYPE_VALUE_DATA);
            return requestForJson(getJaxiUrl(), uuid);
        } catch (Exception e) {
            throw new MetamacException(e, ServiceExceptionType.DATA_RETRIEVE_ERROR, uuid);
        }
    }
    // retrieve from jaxi
    private String requestForJson(String url, String param) {
        Client client = new Client();
        client.setFollowRedirects(true);
        WebResource wresource = client.resource(url);
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
        if (param != null) {
            params.add(JAXI_PARAM_UUID_CONSULTA, param);
        }
        // Overrides accion
        params.put(JAXI_PARAM_ACCION, Arrays.asList(JAXI_PARAM_ACCION_VALUE_JSON_MTD));
        String result = wresource.queryParams(params).accept(MediaType.APPLICATION_JSON).get(String.class);
        return result;
    }

    private String getJaxiUrl() throws MetamacException {
        String jaxiUrlBase = StringUtils.removeEnd(configurationService.retrieveJaxiLocalUrl(), "/");
        return jaxiUrlBase + JAXI_URL_SUFFIX_TABLA_DO;
    }
}