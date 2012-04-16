package es.gobcan.istac.indicators.core.mapper;

import java.util.ArrayList;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;

@Component
public class SculptorCriteria2MetamacCriteriaMapperImpl implements SculptorCriteria2MetamacCriteriaMapper {

    @Autowired
    private Do2DtoMapper do2DtoMapper;

    @SuppressWarnings("unchecked")
    @Override
    public MetamacCriteriaResult<IndicatorsSystemDto> pageResultToMetamacCriteriaResultIndicatorsSystem(PagedResult<IndicatorsSystemVersion> source) {
        MetamacCriteriaResult<IndicatorsSystemDto> target = pageResultToMetamacCriteriaResult(source);
        if (source.getValues() != null) {
            target.setResults(new ArrayList<IndicatorsSystemDto>());
            for (IndicatorsSystemVersion indicatorsSystemVersion : source.getValues()) {
                target.getResults().add(do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion));
            }
        }
        return target;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public MetamacCriteriaResult<IndicatorDto> pageResultToMetamacCriteriaResultIndicator(PagedResult<IndicatorVersion> source) {
        MetamacCriteriaResult<IndicatorDto> target = pageResultToMetamacCriteriaResult(source);
        if (source.getValues() != null) {
            target.setResults(new ArrayList<IndicatorDto>());
            for (IndicatorVersion indicatorVersion : source.getValues()) {
                target.getResults().add(do2DtoMapper.indicatorDoToDto(indicatorVersion));
            }
        }
        return target;
    }

    @SuppressWarnings("rawtypes")
    private MetamacCriteriaResult pageResultToMetamacCriteriaResult(PagedResult source) {
        MetamacCriteriaResult target = new MetamacCriteriaResult();
        target.setFirstResult(source.getStartRow());
        target.setMaximumResultSize(source.getPageSize());
        target.setTotalResults(source.getTotalRows());
        return target;
    }
}
