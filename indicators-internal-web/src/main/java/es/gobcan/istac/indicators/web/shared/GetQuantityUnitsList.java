package es.gobcan.istac.indicators.web.shared;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.serviceapi.QuantityUnitDto;

@GenDispatch(isSecure = false)
public class GetQuantityUnitsList {

    @Out(1)
    List<QuantityUnitDto> quantityUnits;
    
}
