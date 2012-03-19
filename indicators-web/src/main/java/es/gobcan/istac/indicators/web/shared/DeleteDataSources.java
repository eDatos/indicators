package es.gobcan.istac.indicators.web.shared;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;


@GenDispatch(isSecure=false)
public class DeleteDataSources {

    @In(1)
    List<String> uuids;
    
}
