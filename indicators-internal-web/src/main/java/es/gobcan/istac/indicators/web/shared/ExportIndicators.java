package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.web.shared.criteria.IndicatorCriteria;

@GenDispatch(isSecure = false)
public class ExportIndicators {
      
    @In(1)
    IndicatorCriteria criteria;
    
    @Out(1)
    String fileName;
}
