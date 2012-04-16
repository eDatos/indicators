package es.gobcan.istac.indicators.core.mapper;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;

import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;

public interface SculptorCriteria2MetamacCriteriaMapper {

    MetamacCriteriaResult<IndicatorsSystemDto> pageResultToMetamacCriteriaResultIndicatorsSystem(PagedResult<IndicatorsSystemVersion> source);
    MetamacCriteriaResult<IndicatorDto> pageResultToMetamacCriteriaResultIndicator(PagedResult<IndicatorVersion> source);

}
