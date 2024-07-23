package es.gobcan.istac.indicators.web.shared;

import java.util.List;

import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.RelatedResourceDto;
import es.gobcan.istac.indicators.core.enume.domain.TypeRelatedResourceEnum;

@GenDispatch(isSecure = false)
public class GetRelatedResources {

    @In(1)
    TypeRelatedResourceEnum  typeRelatedResourceEnum;

    @In(2)
    int                      firstResult;

    @In(3)
    int                      maxResults;

    @In(4)
    MetamacWebCriteria       criteria;

    @Out(1)
    List<RelatedResourceDto> relatedResourceDtos;

    @Out(2)
    Integer                  firstResultOut;

    @Out(3)
    Integer                  totalResults;
}
