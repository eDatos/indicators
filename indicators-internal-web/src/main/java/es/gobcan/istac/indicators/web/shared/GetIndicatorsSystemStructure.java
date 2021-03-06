package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.IndicatorsSystemStructureDto;

@GenDispatch(isSecure = false)
public class GetIndicatorsSystemStructure {

    @In(1)
    String                       code;

    @Out(1)
    IndicatorsSystemStructureDto structure;

}