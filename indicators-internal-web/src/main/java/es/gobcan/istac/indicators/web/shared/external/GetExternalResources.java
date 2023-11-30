package es.gobcan.istac.indicators.web.shared.external;

import org.siemac.metamac.web.common.shared.criteria.ExternalResourceWebCriteria;
import org.siemac.metamac.web.common.shared.domain.ExternalItemsResult;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetExternalResources {

    @In(1)
    ExternalResourceWebCriteria externalResourceWebCriteria;

    @In(2)
    int                         firstResult;

    @In(3)
    int                         maxResults;

    @Out(1)
    ExternalItemsResult         externalItemsResult;
}
