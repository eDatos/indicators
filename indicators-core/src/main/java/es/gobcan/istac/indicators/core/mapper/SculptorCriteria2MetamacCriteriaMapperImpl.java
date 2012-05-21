package es.gobcan.istac.indicators.core.mapper;

import java.util.ArrayList;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.criteria.mapper.SculptorCriteria2MetamacCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemSummaryDto;

@Component
public class SculptorCriteria2MetamacCriteriaMapperImpl implements SculptorCriteria2MetamacCriteriaMapper {

    @Autowired
    private Do2DtoMapper do2DtoMapper;

    @Override
    public MetamacCriteriaResult<IndicatorsSystemSummaryDto> pageResultToMetamacCriteriaResultIndicatorsSystemSummary(PagedResult<IndicatorsSystemVersion> source, Integer pageSize) {
        MetamacCriteriaResult<IndicatorsSystemSummaryDto> target = new MetamacCriteriaResult<IndicatorsSystemSummaryDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<IndicatorsSystemSummaryDto>());
            for (IndicatorsSystemVersion indicatorsSystemVersion : source.getValues()) {
                target.getResults().add(do2DtoMapper.indicatorsSystemDoToDtoSummary(indicatorsSystemVersion));
            }
        }
        return target;
    }
    
    @Override
    public MetamacCriteriaResult<IndicatorSummaryDto> pageResultToMetamacCriteriaResultIndicatorSummary(PagedResult<IndicatorVersion> source, Integer pageSize) {
        MetamacCriteriaResult<IndicatorSummaryDto> target = new MetamacCriteriaResult<IndicatorSummaryDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<IndicatorSummaryDto>());
            for (IndicatorVersion indicatorVersion : source.getValues()) {
                target.getResults().add(do2DtoMapper.indicatorDoToDtoSummary(indicatorVersion));
            }
        }
        return target;
    }
    
    @Override
    public MetamacCriteriaResult<GeographicalValueDto> pageResultToMetamacCriteriaResultGeographicalValue(PagedResult<GeographicalValue> source, Integer pageSize) {
        MetamacCriteriaResult<GeographicalValueDto> target = new MetamacCriteriaResult<GeographicalValueDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<GeographicalValueDto>());
            for (GeographicalValue geographicalValue : source.getValues()) {
                target.getResults().add(do2DtoMapper.geographicalValueDoToDto(geographicalValue));
            }
        }
        return target;
    }
}
