package es.gobcan.istac.indicators.core.mapper;

import java.util.ArrayList;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.criteria.mapper.SculptorCriteria2MetamacCriteria;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public MetamacCriteriaResult<IndicatorSummaryDto> pageResultToMetamacCriteriaResultIndicatorSummary(ServiceContext ctx, PagedResult<IndicatorVersion> source, Integer pageSize)
            throws MetamacException {
        MetamacCriteriaResult<IndicatorSummaryDto> target = new MetamacCriteriaResult<IndicatorSummaryDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<IndicatorSummaryDto>());
            for (IndicatorVersion indicatorVersion : source.getValues()) {
                target.getResults().add(do2DtoMapper.indicatorDoToDtoSummary(ctx, indicatorVersion));
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

    @Override
    public MetamacCriteriaResult<GeographicalGranularityDto> pageResultToMetamacCriteriaResultGeographicalGranularity(PagedResult<GeographicalGranularity> source, Integer pageSize) {
        MetamacCriteriaResult<GeographicalGranularityDto> target = new MetamacCriteriaResult<GeographicalGranularityDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<GeographicalGranularityDto>());
            for (GeographicalGranularity geographicalGranularity : source.getValues()) {
                target.getResults().add(do2DtoMapper.geographicalGranularityDoToDto(geographicalGranularity));
            }
        }
        return target;
    }

    @Override
    public MetamacCriteriaResult<QuantityUnitDto> pageResultToMetamacCriteriaResultQuantiyUnit(PagedResult<QuantityUnit> source, Integer pageSize) {
        MetamacCriteriaResult<QuantityUnitDto> target = new MetamacCriteriaResult<QuantityUnitDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<QuantityUnitDto>());
            for (QuantityUnit quantityUnit : source.getValues()) {
                target.getResults().add(do2DtoMapper.quantityUnitDoToDto(quantityUnit));
            }
        }
        return target;
    }

    @Override
    public MetamacCriteriaResult<UnitMultiplierDto> pageResultToMetamacCriteriaResultUnitMultiplier(PagedResult<UnitMultiplier> source, Integer pageSize) {
        MetamacCriteriaResult<UnitMultiplierDto> target = new MetamacCriteriaResult<UnitMultiplierDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<UnitMultiplierDto>());
            for (UnitMultiplier unitMultiplier : source.getValues()) {
                target.getResults().add(do2DtoMapper.unitMultiplierDoToDto(unitMultiplier));
            }
        }
        return target;
    }
}
