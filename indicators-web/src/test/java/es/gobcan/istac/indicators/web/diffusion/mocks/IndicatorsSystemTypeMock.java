package es.gobcan.istac.indicators.web.diffusion.mocks;

import java.util.HashMap;
import java.util.Map;

import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;


public class IndicatorsSystemTypeMock {

    public static IndicatorsSystemType mockPublishedIndicatorsSystemWithCode(String code) {
        IndicatorsSystemType system = new IndicatorsSystemType();
        
        system.setCode(code);
        system.setId(code);
        Map<String,String> title = new HashMap<String, String>();
        title.put("es","Sistema de indicadores "+code);
        title.put("en","Indicators System "+code);
        system.setTitle(title);
        return system;
    }
}
