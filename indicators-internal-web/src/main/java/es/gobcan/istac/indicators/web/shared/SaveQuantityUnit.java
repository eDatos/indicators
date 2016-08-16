package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;

@GenDispatch(isSecure = false)
public class SaveQuantityUnit {

    @In(1)
    QuantityUnitDto quantityUnitDto;

    @Out(1)
    QuantityUnitDto outputQuantityUnitDto;
}
