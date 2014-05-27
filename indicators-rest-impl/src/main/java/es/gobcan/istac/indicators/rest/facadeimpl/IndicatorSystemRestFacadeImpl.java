package es.gobcan.istac.indicators.rest.facadeimpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.search.criteria.SculptorCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorInstanceProperties;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemHistory;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsExtendedVO;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataGeoDimensionFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataMeasureDimensionFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataTimeDimensionFilterVO;
import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.facadeapi.IndicatorSystemRestFacade;
import es.gobcan.istac.indicators.rest.mapper.DataTypeRequest;
import es.gobcan.istac.indicators.rest.mapper.Do2TypeMapper;
import es.gobcan.istac.indicators.rest.mapper.IndicatorInstancesRest2DoMapper;
import es.gobcan.istac.indicators.rest.serviceapi.IndicatorsApiService;
import es.gobcan.istac.indicators.rest.types.DataType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemHistoryType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;
import es.gobcan.istac.indicators.rest.types.ListResultType;
import es.gobcan.istac.indicators.rest.types.MetadataType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;
import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;
import es.gobcan.istac.indicators.rest.util.ConditionUtil;
import es.gobcan.istac.indicators.rest.util.CriteriaUtil;
import es.gobcan.istac.indicators.rest.util.RequestUtil;

@Service
public class IndicatorSystemRestFacadeImpl implements IndicatorSystemRestFacade {

    private Logger                          logger               = LoggerFactory.getLogger(getClass());

    @Autowired
    protected IndicatorsApiService          indicatorsApiService = null;

    @Autowired
    private Do2TypeMapper                   dto2TypeMapper       = null;

    @Autowired
    private IndicatorInstancesRest2DoMapper indicatorInstancesRest2DoMapper;

    @Override
    public PagedResultType<IndicatorsSystemBaseType> findIndicatorsSystems(final String baseURL, final RestCriteriaPaginator paginator) throws Exception {
        PagingParameter pagingParameter = CriteriaUtil.createPagingParameter(paginator);
        PagedResult<IndicatorsSystemVersion> indicatorsSystemVersions = findIndicatorsSystems(pagingParameter);

        List<IndicatorsSystemBaseType> result = dto2TypeMapper.indicatorsSystemDoToBaseType(indicatorsSystemVersions.getValues(), baseURL);

        PagedResultType<IndicatorsSystemBaseType> resultType = new PagedResultType<IndicatorsSystemBaseType>();
        resultType.setKind(RestConstants.KIND_INDICATOR_SYSTEMS);
        resultType.setLimit(paginator.getLimit());
        resultType.setOffset(paginator.getOffset());
        resultType.setTotal(indicatorsSystemVersions.getTotalRows());
        resultType.setItems(result);

        return resultType;
    }

    protected PagedResult<IndicatorsSystemVersion> findIndicatorsSystems(PagingParameter pagingParameter) throws MetamacException {
        return indicatorsApiService.findIndicatorsSystems(pagingParameter);
    }

    protected IndicatorsSystemVersion retrieveIndicatorsSystemByCode(String idIndicatorSystem) throws MetamacException {
        return indicatorsApiService.retrieveIndicatorsSystemByCode(idIndicatorSystem);
    }

    protected PagedResult<IndicatorInstance> findIndicatorsInstancesInIndicatorsSystems(SculptorCriteria sculptorCriteria) throws MetamacException {
        return indicatorsApiService.findIndicatorsInstancesInIndicatorsSystems(sculptorCriteria);
    }

    protected IndicatorInstance retrieveIndicatorInstanceByCode(String idIndicatorSystem, String idIndicatorInstance) throws MetamacException {
        return indicatorsApiService.retrieveIndicatorInstanceByCode(idIndicatorSystem, idIndicatorInstance);
    }

    @Override
    public IndicatorsSystemType retrieveIndicatorsSystem(final String baseURL, String idIndicatorSystem) throws Exception {
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorsSystemByCode(idIndicatorSystem);
        IndicatorsSystemType result = dto2TypeMapper.indicatorsSystemDoToType(indicatorsSystemVersion, baseURL);
        return result;
    }

    @Override
    public List<IndicatorsSystemHistoryType> findIndicatorsSystemHistoryByCode(final String baseURL, final String code, final int maxResults) throws Exception {
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorsSystemByCode(code);

        List<IndicatorsSystemHistory> systemHistories = indicatorsApiService.findIndicatorsSystemHistory(indicatorsSystemVersion.getIndicatorsSystem().getUuid(), maxResults);

        List<IndicatorsSystemHistoryType> systemHistoriesType = new ArrayList<IndicatorsSystemHistoryType>();
        for (IndicatorsSystemHistory systemHistory : systemHistories) {
            systemHistoriesType.add(dto2TypeMapper.indicatorsSystemHistoryDoToType(systemHistory, baseURL));
        }
        return systemHistoriesType;
    }

    @Override
    public ListResultType<IndicatorInstanceBaseType> retrieveIndicatorsInstances(final String baseUrl, final String idIndicatorSystem, String q, String order, String fields,
            Map<String, List<String>> representation, Map<String, List<String>> selectedGranularities) throws Exception {

        // Parse Query
        SculptorCriteria sculptorCriteria = indicatorInstancesRest2DoMapper.queryParams2SculptorCriteria(q, order, null, null);
        ConditionalCriteria systemCondition = ConditionalCriteria.equal(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().indicatorsSystem().code(), idIndicatorSystem);
        sculptorCriteria.getConditions().add(systemCondition);

        PagedResult<IndicatorInstance> pagedInstances = findIndicatorsInstancesInIndicatorsSystems(sculptorCriteria);

        List<IndicatorInstance> instances = pagedInstances.getValues();

        // Mapping to type
        Map<String, IndicatorInstanceBaseType> instancesBaseType = buildInstancesBaseTypeMap(instances, baseUrl);

        // Fields filter. Only support for +metadata, +data
        Set<String> fieldsToAdd = RequestUtil.parseFields(fields);

        if (selectedGranularities.size() > 0 || representation.size() > 0 || fieldsToAdd.contains("+data")) {
            boolean includeObservationsAttributes = fieldsToAdd.contains("+observationsMetadata");
            Iterator<IndicatorInstance> iterator = instances.iterator();
            while (iterator.hasNext()) {
                IndicatorInstance indicatorInstance = iterator.next();
                String systemCode = indicatorInstance.getElementLevel().getIndicatorsSystemVersion().getIndicatorsSystem().getCode();
                String instanceCode = indicatorInstance.getCode();
                DataType dataType = retrieveIndicatorInstanceDataByCode(baseUrl, systemCode, instanceCode, representation, selectedGranularities, includeObservationsAttributes);
                if (dataType.getObservation().size() > 0) {
                    if (fieldsToAdd.contains("+data")) {
                        instancesBaseType.get(instanceCode).setData(dataType);
                    }
                } else {
                    iterator.remove();
                }
            }
        }

        if (fieldsToAdd.contains("+metadata")) {
            for (IndicatorInstance indicatorInstance : instances) {
                IndicatorInstanceBaseType baseType = instancesBaseType.get(indicatorInstance.getCode());

                MetadataType metadataType = new MetadataType();
                dto2TypeMapper.indicatorsInstanceDoToMetadataType(indicatorInstance, metadataType, baseUrl);
                baseType.setMetadata(metadataType);
            }
        }

        List<IndicatorInstanceBaseType> filteredInstances = new ArrayList<IndicatorInstanceBaseType>();
        for (IndicatorInstance instance : instances) {
            filteredInstances.add(instancesBaseType.get(instance.getCode()));
        }

        ListResultType<IndicatorInstanceBaseType> result = new ListResultType<IndicatorInstanceBaseType>();
        result.setItems(filteredInstances);
        result.setKind(RestConstants.KIND_INDICATOR_INSTANCES);
        return result;
    }

    private Map<String, IndicatorInstanceBaseType> buildInstancesBaseTypeMap(List<IndicatorInstance> instances, String baseUrl) {
        List<IndicatorInstanceBaseType> indicatorInstanceTypes = dto2TypeMapper.indicatorsInstanceDoToBaseType(instances, baseUrl);
        Map<String, IndicatorInstanceBaseType> mapping = new LinkedHashMap<String, IndicatorInstanceBaseType>();
        for (IndicatorInstanceBaseType baseType : indicatorInstanceTypes) {
            mapping.put(baseType.getId(), baseType);
        }
        return mapping;
    }

    @Override
    public PagedResultType<IndicatorInstanceBaseType> retrievePaginatedIndicatorsInstances(final String baseUrl, final String idIndicatorSystem, String q, String order, Integer limit, Integer offset,
            String fields, Map<String, List<String>> representation, Map<String, List<String>> selectedGranularities) throws Exception {

        long timeInit = System.currentTimeMillis();
        // Parse Query
        SculptorCriteria sculptorCriteria = indicatorInstancesRest2DoMapper.queryParams2SculptorCriteria(q, order, limit, offset);
        ConditionalCriteria systemCondition = ConditionalCriteria.equal(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().indicatorsSystem().code(), idIndicatorSystem);
        sculptorCriteria.getConditions().add(systemCondition);

        PagedResult<IndicatorInstance> instances = findIndicatorsInstancesInIndicatorsSystems(sculptorCriteria);

        // Mapping to type
        List<IndicatorInstanceBaseType> indicatorInstanceTypes = dto2TypeMapper.indicatorsInstanceDoToBaseType(instances.getValues(), baseUrl);
        PagedResultType<IndicatorInstanceBaseType> result = new PagedResultType<IndicatorInstanceBaseType>();
        result.setKind(RestConstants.KIND_INDICATOR_INSTANCES);
        result.setLimit(limit);
        result.setTotal(instances.getTotalRows());
        result.setOffset(instances.getStartRow());
        result.setItems(indicatorInstanceTypes);

        // Fields filter. Only support for +metadata, +data
        Set<String> fieldsToAdd = RequestUtil.parseFields(fields);

        long timeBeforeMetadata = System.currentTimeMillis();
        logger.info("Tiempo instancias: " + (timeBeforeMetadata - timeInit));
        if (fieldsToAdd.contains("+metadata")) {
            for (int i = 0; i < result.getItems().size(); i++) {
                IndicatorInstanceBaseType baseType = result.getItems().get(i);
                IndicatorInstance indicatorInstance = instances.getValues().get(i);

                MetadataType metadataType = new MetadataType();
                dto2TypeMapper.indicatorsInstanceDoToMetadataType(indicatorInstance, metadataType, baseUrl);
                baseType.setMetadata(metadataType);
            }
        }
        long timeAfterMetadata = System.currentTimeMillis();
        logger.info("Tiempo metadata: " + (timeAfterMetadata - timeBeforeMetadata));

        if (fieldsToAdd.contains("+data")) {
            boolean includeObservationsAttributes = fieldsToAdd.contains("+observationsMetadata");
            for (IndicatorInstanceBaseType type : result.getItems()) {
                DataType dataType = retrieveIndicatorInstanceDataByCode(baseUrl, type.getSystemCode(), type.getId(), representation, selectedGranularities, includeObservationsAttributes);

                type.setData(dataType);
            }
        }
        long timeAfterData = System.currentTimeMillis();
        logger.info("Tiempo data: " + (timeAfterData - timeAfterMetadata));

        logger.info("Tiempo total: " + (timeAfterData - timeInit));
        return result;
    }

    @Override
    public IndicatorInstanceType retrieveIndicatorInstanceByCode(final String baseURL, final String idIndicatorSystem, final String idIndicatorInstance) throws Exception {
        IndicatorInstance indicatorInstance = retrieveIndicatorInstanceByCode(idIndicatorSystem, idIndicatorInstance);
        IndicatorInstanceType result = dto2TypeMapper.indicatorsInstanceDoToType(indicatorInstance, baseURL);
        return result;
    }

    @Override
    public DataType retrieveIndicatorInstanceDataByCode(String baseUrl, String idIndicatorSystem, String idIndicatorInstance, Map<String, List<String>> selectedRepresentations,
            Map<String, List<String>> selectedGranularities, boolean includeObservationMetadata) throws Exception {
        IndicatorInstance indicatorInstance = retrieveIndicatorInstanceByCode(idIndicatorSystem, idIndicatorInstance);

        IndicatorsDataGeoDimensionFilterVO geoFilter = ConditionUtil.filterGeographicalDimension(selectedRepresentations, selectedGranularities);
        IndicatorsDataTimeDimensionFilterVO timeFilter = ConditionUtil.filterTimeDimension(selectedRepresentations, selectedGranularities);
        IndicatorsDataMeasureDimensionFilterVO measureFilter = ConditionUtil.filterMeasureDimension(selectedRepresentations);

        IndicatorsDataFilterVO dataFilter = new IndicatorsDataFilterVO();
        dataFilter.setGeoFilter(geoFilter);
        dataFilter.setTimeFilter(timeFilter);
        dataFilter.setMeasureFilter(measureFilter);

        DataType dataType;
        long timeInit = System.currentTimeMillis();
        if (includeObservationMetadata) {
            IndicatorObservationsExtendedVO instanceObservations = indicatorsApiService.findObservationsExtendedInIndicatorInstance(indicatorInstance.getUuid(), dataFilter);
            long timeObs = System.currentTimeMillis();
            logger.info("   Time obs " + (timeObs - timeInit));

            DataTypeRequest dataTypeRequest = new DataTypeRequest(indicatorInstance, instanceObservations.getGeographicalCodes(), instanceObservations.getTimeCodes(),
                    instanceObservations.getMeasureCodes(), instanceObservations.getObservations());
            dataType = dto2TypeMapper.createDataType(dataTypeRequest, includeObservationMetadata);

            long timeMapper = System.currentTimeMillis();
            logger.info("   Time mapper " + (timeMapper - timeObs));
        } else {
            IndicatorObservationsVO instanceObservations = indicatorsApiService.findObservationsInIndicatorInstance(indicatorInstance.getUuid(), dataFilter);
            long timeObs = System.currentTimeMillis();
            logger.info("   Time obs " + (timeObs - timeInit));

            DataTypeRequest dataTypeRequest = new DataTypeRequest(indicatorInstance, instanceObservations.getGeographicalCodes(), instanceObservations.getTimeCodes(),
                    instanceObservations.getMeasureCodes(), instanceObservations.getObservations());
            dataType = dto2TypeMapper.createDataType(dataTypeRequest, includeObservationMetadata);

            long timeMapper = System.currentTimeMillis();
            logger.info("   Time mapper " + (timeMapper - timeObs));
        }

        return dataType;
    }

}
