package es.gobcan.istac.indicators.web.shared;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.DataSourceDto;

@GenDispatch(isSecure = false)
public class GetDataSourcesList {

    @In(1)
    String              indicatorUuid;

    @In(2)
    String              indicatorVersion;

    @Out(1)
    List<DataSourceDto> dataSourceDtos;

}
