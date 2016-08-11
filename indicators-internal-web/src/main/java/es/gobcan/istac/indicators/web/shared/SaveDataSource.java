package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.DataSourceDto;

@GenDispatch(isSecure = false)
public class SaveDataSource {

    @In(1)
    String        indicatorUuid;

    @In(2)
    DataSourceDto dataSourceDtoToSave;

    @Out(1)
    DataSourceDto dataSourceDto;

}
