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
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;

@Component
public class SculptorCriteria2MetamacCriteriaMapperImpl implements SculptorCriteria2MetamacCriteriaMapper {

    @Autowired
    private Do2DtoMapper do2DtoMapper;

    @Override
    public MetamacCriteriaResult<IndicatorsSystemDto> pageResultToMetamacCriteriaResultIndicatorsSystem(PagedResult<IndicatorsSystemVersion> source, Integer pageSize) {
        MetamacCriteriaResult<IndicatorsSystemDto> target = new MetamacCriteriaResult<IndicatorsSystemDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<IndicatorsSystemDto>());
            for (IndicatorsSystemVersion indicatorsSystemVersion : source.getValues()) {
                target.getResults().add(do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion));
            }
        }
        return target;
    }
    
    @Override
    public MetamacCriteriaResult<IndicatorDto> pageResultToMetamacCriteriaResultIndicator(PagedResult<IndicatorVersion> source, Integer pageSize) {
        MetamacCriteriaResult<IndicatorDto> target = new MetamacCriteriaResult<IndicatorDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<IndicatorDto>());
            for (IndicatorVersion indicatorVersion : source.getValues()) {
                target.getResults().add(do2DtoMapper.indicatorDoToDto(indicatorVersion));
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
