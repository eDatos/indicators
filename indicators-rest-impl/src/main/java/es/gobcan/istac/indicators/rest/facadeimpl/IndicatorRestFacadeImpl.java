package es.gobcan.istac.indicators.rest.facadeimpl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.rest.search.criteria.SculptorCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.dataset.repository.dto.ConditionDimensionDto;
import com.arte.statistic.dataset.repository.dto.ObservationExtendedDto;

import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.MeasureValue;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsDataService;
import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.facadeapi.IndicatorRestFacade;
import es.gobcan.istac.indicators.rest.mapper.DataTypeRequest;
import es.gobcan.istac.indicators.rest.mapper.Do2TypeMapper;
import es.gobcan.istac.indicators.rest.mapper.IndicatorsRest2DoMapper;
import es.gobcan.istac.indicators.rest.serviceapi.IndicatorsApiService;
import es.gobcan.istac.indicators.rest.types.DataType;
import es.gobcan.istac.indicators.rest.types.IndicatorBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorType;
import es.gobcan.istac.indicators.rest.types.MetadataType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;
import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;
import es.gobcan.istac.indicators.rest.util.ConditionUtil;
import es.gobcan.istac.indicators.rest.util.RequestUtil;

@Service
public class IndicatorRestFacadeImpl implements IndicatorRestFacade {

    protected Logger logger = LoggerFactory.getLogger(IndicatorRestFacadeImpl.class);

    @Autowired
    private Do2TypeMapper dto2TypeMapper;

    @Autowired
    protected IndicatorsApiService indicatorsApiService;

    @Autowired
    private IndicatorsDataService indicatorsDataService;

    @Autowired
    private IndicatorsRest2DoMapper indicatorsRest2DoMapper;

    @Override
    public PagedResultType<IndicatorBaseType> findIndicators(String baseUrl, String q, String order, final RestCriteriaPaginator paginator, String fields, Map<String, List<String>> representation) throws Exception {

        // Parse Query
        SculptorCriteria sculptorCriteria = indicatorsRest2DoMapper.queryParams2SculptorCriteria(q, order, paginator.getLimit(), paginator.getOffset());

        // Find
        PagedResult<IndicatorVersion> indicatorsVersions = findIndicators(sculptorCriteria);

        // Parse fields
        Set<String> fieldsToAdd = RequestUtil.parseFields(fields);

        // Mapping to type
        List<IndicatorBaseType> result = dto2TypeMapper.indicatorDoToBaseType(indicatorsVersions.getValues(), baseUrl);

        // Fields filter. Only support for +metadata, +data
        if(fieldsToAdd.contains("+metadata")) {
            for(int i = 0; i < result.size(); i++) {
                IndicatorBaseType baseType = result.get(i);
                IndicatorVersion indicatorVersion = indicatorsVersions.getValues().get(i);

                MetadataType metadataType = new MetadataType();
                dto2TypeMapper.indicatorDoToMetadataType(indicatorVersion, metadataType, baseUrl);
                baseType.setMetadata(metadataType);
            }
        }

        if(fieldsToAdd.contains("+data")) {
            for(IndicatorBaseType indicatorType : result) {
                Map<String, List<String>> selectedGranularities = MapUtils.EMPTY_MAP;
                DataType dataType = retrieveIndicatorData(baseUrl, indicatorType.getCode(), representation, selectedGranularities);
                indicatorType.setData(dataType);
            }
        }

        // Pagegd result
        PagedResultType<IndicatorBaseType> resultType = new PagedResultType<IndicatorBaseType>();
        resultType.setKind(RestConstants.KIND_INDICATORS);
        resultType.setLimit(paginator.getLimit());
        resultType.setOffset(paginator.getOffset());
        resultType.setTotal(indicatorsVersions.getTotalRows());
        resultType.setItems(result);

        return resultType;
    }

    protected PagedResult<IndicatorVersion> findIndicators(SculptorCriteria sculptorCriteria) throws org.siemac.metamac.core.common.exception.MetamacException {
        return indicatorsApiService.findIndicators(sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());
    }

    protected IndicatorVersion retrieveIndicatorByCode(String indicatorCode) throws org.siemac.metamac.core.common.exception.MetamacException {
        return indicatorsApiService.retrieveIndicatorByCode(indicatorCode);
    }

    @Override
    public IndicatorType retrieveIndicator(String baseUrl, String indicatorCode) throws Exception {
        IndicatorVersion indicatorsVersion = retrieveIndicatorByCode(indicatorCode);
        IndicatorType result = dto2TypeMapper.indicatorDoToType(indicatorsVersion, baseUrl);
        return result;
    }

    @Override
    public DataType retrieveIndicatorData(String baseUrl, String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities) throws Exception {
        IndicatorVersion indicatorVersion = retrieveIndicatorByCode(indicatorCode);

        List<GeographicalValue> geographicalValues = indicatorsDataService.retrieveGeographicalValuesInIndicator(RestConstants.SERVICE_CONTEXT, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
        List<TimeValue> timeValues = indicatorsDataService.retrieveTimeValuesInIndicator(RestConstants.SERVICE_CONTEXT, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
        List<MeasureValue> measureValues = indicatorsDataService.retrieveMeasureValuesInIndicator(RestConstants.SERVICE_CONTEXT, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());

        List<ConditionDimensionDto> conditionDimensionDtos = new ArrayList<ConditionDimensionDto>();
        ConditionUtil.filterGeographicalValues(selectedRepresentations, selectedGranularities, geographicalValues, conditionDimensionDtos);
        ConditionUtil.filterTimeValues(selectedRepresentations, selectedGranularities, timeValues, conditionDimensionDtos);
        ConditionUtil.filterMeasureValues(selectedRepresentations, selectedGranularities, measureValues, conditionDimensionDtos);

        Map<String, ObservationExtendedDto> observationMap = indicatorsDataService.findObservationsExtendedByDimensionsInIndicatorLastVersion(RestConstants.SERVICE_CONTEXT, indicatorVersion.getIndicator().getUuid(), conditionDimensionDtos);

        DataTypeRequest dataTypeRequest = new DataTypeRequest(indicatorVersion, geographicalValues, timeValues, measureValues, observationMap);
        return dto2TypeMapper.createDataType(dataTypeRequest);
    }
}
