package es.gobcan.istac.indicators.web.shared;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.TimeValueDto;
import es.gobcan.istac.indicators.core.enume.domain.TimeGranularityEnum;

@GenDispatch(isSecure = false)
public class GetTimeValuesByGranularityInIndicator {

    @In(1)
    String              indicatorUuid;

    @In(2)
    String              indicatorVersion;

    @In(3)
    TimeGranularityEnum timeGranularity;

    @Out(1)
    List<TimeValueDto>  timeValueDtos;
}
