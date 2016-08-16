package es.gobcan.istac.indicators.web.shared;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;

@GenDispatch(isSecure = false)
public class GetIndicatorList {

    @Out(1)
    List<IndicatorSummaryDto> indicatorList;
}
