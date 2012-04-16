package es.gobcan.istac.indicators.core.mapper;

import org.siemac.metamac.core.common.criteria.MetamacCriteriaTransform;

import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;

public interface MetamacCriteria2SculptorCriteriaMapper {

    public MetamacCriteriaTransform<IndicatorsSystemVersion> getIndicatorsSystemVersionCriteriaTransform();
    public MetamacCriteriaTransform<IndicatorVersion> getIndicatorVersionCriteriaTransform();
    public MetamacCriteriaTransform<GeographicalValue> getGeographicalValueCriteriaTransform();
}
