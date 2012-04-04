package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceDto;

@GenDispatch(isSecure=false)
public class GetDataSource {

    @In(1)
    String uuid;
    
    @Out(1)
    DataSourceDto dataSourceDto;
    
}
