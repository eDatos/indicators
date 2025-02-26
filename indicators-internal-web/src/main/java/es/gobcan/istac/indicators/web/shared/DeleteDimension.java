package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

@GenDispatch(isSecure = false)
public class DeleteDimension {

    @In(1)
    String                 dimensionUuid;

    @In(2)
    String                 indicatorsSystemCode;

    @Out(1)
    IndicatorsSystemDtoWeb indicatorsSystemUpdated;
}
