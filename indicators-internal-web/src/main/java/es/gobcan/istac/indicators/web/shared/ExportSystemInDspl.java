package es.gobcan.istac.indicators.web.shared;

import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class ExportSystemInDspl {

    @In(1)
    String                 systemUuid;

    @In(2)
    InternationalStringDto systemTitle;

    @In(3)
    InternationalStringDto systemDescription;

    @Out(1)
    List<String>           files;
}
