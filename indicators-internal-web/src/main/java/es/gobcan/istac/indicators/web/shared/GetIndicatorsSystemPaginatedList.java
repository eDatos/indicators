package es.gobcan.istac.indicators.web.shared;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

@GenDispatch(isSecure = false)
public class GetIndicatorsSystemPaginatedList {

    @In(1)
    int                          maxResults;
    @In(2)
    int                          firstResult;

    @Out(1)
    List<IndicatorsSystemDtoWeb> indicatorsSystemList;

    @Out(2)
    Integer                      totalResults;
}
