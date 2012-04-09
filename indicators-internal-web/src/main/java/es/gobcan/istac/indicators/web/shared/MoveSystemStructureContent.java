package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

import es.gobcan.istac.indicators.core.dto.serviceapi.ElementLevelDto;

@GenDispatch(isSecure = false)
public class MoveSystemStructureContent {

    @In(1)
    String          systemUuid;

    @In(2)
    String          targetDimensionUuid;

    @In(3)
    Long            targetOrderInLevel;

    @In(4)
    ElementLevelDto level;

}
