package es.gobcan.istac.indicators.rest.mapper;

import org.siemac.metamac.rest.search.criteria.SculptorCriteria;

public interface IndicatorInstancesRest2DoMapper {

    SculptorCriteria queryParams2SculptorCriteria(String q, String order, Integer limit, Integer offset);

}
