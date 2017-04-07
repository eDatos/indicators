package es.gobcan.istac.indicators.web.shared;

import java.util.List;

import org.siemac.metamac.core.common.enume.domain.IstacTimeGranularityEnum;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.TimeValueDto;

@GenDispatch(isSecure = false)
public class GetTimeValuesByGranularityInIndicator {

    @In(1)
    String                   indicatorUuid;

    @In(2)
    String                   indicatorVersion;

    @In(3)
    IstacTimeGranularityEnum timeGranularity;

    @Out(1)
    List<TimeValueDto>       timeValueDtos;
}
