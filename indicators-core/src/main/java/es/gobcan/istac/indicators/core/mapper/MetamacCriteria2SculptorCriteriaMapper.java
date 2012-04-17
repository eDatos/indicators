package es.gobcan.istac.indicators.core.mapper;

import org.siemac.metamac.core.common.criteria.mapper.MetamacCriteria2SculptorCriteria;

import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;

public interface MetamacCriteria2SculptorCriteriaMapper {

    public MetamacCriteria2SculptorCriteria<IndicatorsSystemVersion> getIndicatorsSystemVersionCriteriaMapper();
    public MetamacCriteria2SculptorCriteria<IndicatorVersion> getIndicatorVersionCriteriaMapper();
    public MetamacCriteria2SculptorCriteria<GeographicalValue> getGeographicalValueCriteriaMapper();
}
