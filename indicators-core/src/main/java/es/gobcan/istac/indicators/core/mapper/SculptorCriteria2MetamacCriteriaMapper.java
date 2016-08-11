package es.gobcan.istac.indicators.core.mapper;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;

import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.QuantityUnit;
import es.gobcan.istac.indicators.core.domain.UnitMultiplier;
import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemSummaryDto;
import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;

public interface SculptorCriteria2MetamacCriteriaMapper {

    MetamacCriteriaResult<IndicatorsSystemSummaryDto> pageResultToMetamacCriteriaResultIndicatorsSystemSummary(PagedResult<IndicatorsSystemVersion> source, Integer pageSize);

    MetamacCriteriaResult<IndicatorSummaryDto> pageResultToMetamacCriteriaResultIndicatorSummary(PagedResult<IndicatorVersion> source, Integer pageSize);

    MetamacCriteriaResult<GeographicalValueDto> pageResultToMetamacCriteriaResultGeographicalValue(PagedResult<GeographicalValue> source, Integer pageSize);

    MetamacCriteriaResult<GeographicalGranularityDto> pageResultToMetamacCriteriaResultGeographicalGranularity(PagedResult<GeographicalGranularity> source, Integer pageSize);

    MetamacCriteriaResult<QuantityUnitDto> pageResultToMetamacCriteriaResultQuantiyUnit(PagedResult<QuantityUnit> source, Integer pageSize);

    MetamacCriteriaResult<UnitMultiplierDto> pageResultToMetamacCriteriaResultUnitMultiplier(PagedResult<UnitMultiplier> source, Integer pageSize);
}
