package es.gobcan.istac.indicators.core.mapper;

import org.siemac.metamac.core.common.criteria.mapper.MetamacCriteria2SculptorCriteria;

import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.QuantityUnit;
import es.gobcan.istac.indicators.core.domain.UnitMultiplier;

public interface MetamacCriteria2SculptorCriteriaMapper {

    public MetamacCriteria2SculptorCriteria<IndicatorsSystemVersion> getIndicatorsSystemVersionCriteriaMapper();

    public MetamacCriteria2SculptorCriteria<IndicatorVersion> getIndicatorVersionCriteriaMapper();

    public MetamacCriteria2SculptorCriteria<GeographicalValue> getGeographicalValueCriteriaMapper();

    public MetamacCriteria2SculptorCriteria<GeographicalGranularity> getGeographicalGranularityCriteriaMapper();

    public MetamacCriteria2SculptorCriteria<QuantityUnit> getQuantityUnitCriteriaMapper();

    public MetamacCriteria2SculptorCriteria<UnitMultiplier> getUnitMultiplierCriteriaMapper();
}
