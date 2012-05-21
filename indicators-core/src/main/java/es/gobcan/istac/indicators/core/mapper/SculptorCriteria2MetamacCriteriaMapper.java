package es.gobcan.istac.indicators.core.mapper;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;

import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;

public interface SculptorCriteria2MetamacCriteriaMapper {

    MetamacCriteriaResult<IndicatorsSystemDto> pageResultToMetamacCriteriaResultIndicatorsSystem(PagedResult<IndicatorsSystemVersion> source, Integer pageSize);
    MetamacCriteriaResult<IndicatorSummaryDto> pageResultToMetamacCriteriaResultIndicatorSummary(PagedResult<IndicatorVersion> source, Integer pageSize);
    MetamacCriteriaResult<GeographicalValueDto> pageResultToMetamacCriteriaResultGeographicalValue(PagedResult<GeographicalValue> source, Integer pageSize);
}
