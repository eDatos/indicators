package es.gobcan.istac.indicators.web.shared;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.web.shared.criteria.QueryWebCriteria;

@GenDispatch(isSecure = false)
public class GetQueriesPaginatedList {

    @In(1)
    int                   firstResult;

    @In(2)
    int                   maxResults;

    @In(3)
    QueryWebCriteria      criteria;

    @Out(1)
    List<ExternalItemDto> queriesList;

    @Out(2)
    Integer               firstResultOut;

    @Out(3)
    Integer               totalResults;
}
