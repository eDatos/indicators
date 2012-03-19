package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.stereotype.Service;

/**
 * Implementation of IndicatorsWebserviceFacade.
 */
@Service("indicatorsWebserviceFacade")
@WebService(endpointInterface = "es.gobcan.istac.indicators.core.serviceapi.IndicatorsWebserviceFacadeEndpoint", serviceName = "IndicatorsWebserviceFacade")
public class IndicatorsWebserviceFacadeImpl extends IndicatorsWebserviceFacadeImplBase {

    public IndicatorsWebserviceFacadeImpl() {
    }

    @WebMethod
    public List<String> findIndicatorsSystems(String tipo) throws MetamacException {
        List<String> list = new ArrayList<String>();
        list.add("element1");
        list.add("element2");
        return list;
    }
}
