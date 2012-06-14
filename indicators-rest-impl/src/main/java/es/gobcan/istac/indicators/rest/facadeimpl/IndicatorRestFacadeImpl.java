package es.gobcan.istac.indicators.rest.facadeimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.dataset.repository.dto.ConditionDimensionDto;
import com.arte.statistic.dataset.repository.dto.ObservationExtendedDto;

import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionProperties;
import es.gobcan.istac.indicators.core.domain.MeasureValue;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsDataService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsService;
import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.facadeapi.IndicatorRestFacade;
import es.gobcan.istac.indicators.rest.mapper.DataTypeRequest;
import es.gobcan.istac.indicators.rest.mapper.Do2TypeMapper;
import es.gobcan.istac.indicators.rest.types.DataType;
import es.gobcan.istac.indicators.rest.types.IndicatorBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;
import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;
import es.gobcan.istac.indicators.rest.util.ConditionUtil;
import es.gobcan.istac.indicators.rest.util.CriteriaUtil;

@Service("indicatorRestFacade")
public class IndicatorRestFacadeImpl implements IndicatorRestFacade {

    protected Logger              logger                = LoggerFactory.getLogger(getClass());

    @Autowired
    private Do2TypeMapper         dto2TypeMapper        = null;

    @Autowired
    private IndicatorsService     indicatorsService     = null;

    @Autowired
    private IndicatorsDataService indicatorsDataService = null;

    @Override
    public PagedResultType<IndicatorBaseType> findIndicators(final String baseUrl, final String subjectCode, final RestCriteriaPaginator paginator) throws Exception {
        PagingParameter pagingParameter = CriteriaUtil.createPagingParameter(paginator);
        List<ConditionalCriteria> criterias = new ArrayList<ConditionalCriteria>();
        if (StringUtils.isNotBlank(subjectCode)) {
            criterias.add( ConditionalCriteria.ignoreCaseLike(IndicatorVersionProperties.subjectCode(), "%"+subjectCode+"%"));
        }
        PagedResult<IndicatorVersion> indicatorsVersions = indicatorsService.findIndicatorsPublished(RestConstants.SERVICE_CONTEXT, criterias, pagingParameter);

        List<IndicatorBaseType> result = dto2TypeMapper.indicatorDoToBaseType(indicatorsVersions.getValues(), baseUrl);

        PagedResultType<IndicatorBaseType> resultType = new PagedResultType<IndicatorBaseType>();
        resultType.setKind(RestConstants.KIND_INDICATORS);
        resultType.setLimit(paginator.getLimit());
        resultType.setOffset(paginator.getOffset());
        resultType.setTotal(indicatorsVersions.getTotalRows());
        resultType.setItems(result);
        return resultType;
    }
    
    @Override
    public IndicatorType retrieveIndicator(String baseUrl, String indicatorCode) throws Exception {
        IndicatorVersion indicatorsVersion = indicatorsService.retrieveIndicatorPublishedByCode(RestConstants.SERVICE_CONTEXT, indicatorCode);
        IndicatorType result = dto2TypeMapper.indicatorDoToType(indicatorsVersion, baseUrl);
        return result;
    }
    
    @Override
    public DataType retrieveIndicatorData(String baseUrl, String indicatorCode, Map<String, List<String>> selectedRepresentations,  Map<String, List<String>> selectedGranularities) throws Exception {
        IndicatorVersion indicatorVersion = indicatorsService.retrieveIndicatorPublishedByCode(RestConstants.SERVICE_CONTEXT, indicatorCode);
        
        List<GeographicalValue> geographicalValues = indicatorsDataService.retrieveGeographicalValuesInIndicator(RestConstants.SERVICE_CONTEXT, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
        List<TimeValue> timeValues = indicatorsDataService.retrieveTimeValuesInIndicator(RestConstants.SERVICE_CONTEXT, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
        List<MeasureValue> measureValues = indicatorsDataService.retrieveMeasureValuesInIndicator(RestConstants.SERVICE_CONTEXT, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
                   
        List<ConditionDimensionDto> conditionDimensionDtos = new ArrayList<ConditionDimensionDto>();
        ConditionUtil.filterGeographicalValues(selectedRepresentations, selectedGranularities, geographicalValues, conditionDimensionDtos);
        ConditionUtil.filterTimeValues(selectedRepresentations, selectedGranularities, timeValues, conditionDimensionDtos);
        ConditionUtil.filterMeasureValues(selectedRepresentations, selectedGranularities, measureValues, conditionDimensionDtos);
        
        Map<String, ObservationExtendedDto> observationMap = indicatorsDataService.findObservationsExtendedByDimensionsInIndicatorPublished(RestConstants.SERVICE_CONTEXT, indicatorVersion.getIndicator().getUuid(), conditionDimensionDtos);
        
        DataTypeRequest dataTypeRequest = new DataTypeRequest(indicatorVersion, geographicalValues, timeValues, measureValues, observationMap);
        return dto2TypeMapper.createDataType(dataTypeRequest);
    }
}
