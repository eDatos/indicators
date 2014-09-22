package es.gobcan.istac.indicators.web.shared;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.web.shared.criteria.IndicatorCriteria;

@GenDispatch(isSecure = false)
public class GetIndicatorPaginatedList {

    @In(1)
    IndicatorCriteria         criteria;

    @Out(1)
    List<IndicatorSummaryDto> indicatorList;

    @Out(2)
    Integer                   firstResult;

    @Out(3)
    Integer                   totalResults;
}
