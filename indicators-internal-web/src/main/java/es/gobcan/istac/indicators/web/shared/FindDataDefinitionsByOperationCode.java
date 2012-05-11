package es.gobcan.istac.indicators.web.shared;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.DataDefinitionDto;

@GenDispatch(isSecure = false)
public class FindDataDefinitionsByOperationCode {

    @In(1)
    String operationCode;
    
    @Out(1)
    List<DataDefinitionDto> dataDefinitionDtos;
    
}
