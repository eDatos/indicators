package es.gobcan.istac.indicators.rest.facadeimpl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.rest.RequestUtil;
import org.siemac.metamac.rest.search.criteria.SculptorCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.conf.MetadataProperties;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorInstanceProperties;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemHistory;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsExtendedVO;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataGeoDimensionFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataMeasureDimensionFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataTimeDimensionFilterVO;
import es.gobcan.istac.indicators.rest.IndicatorsRestConstants;
import es.gobcan.istac.indicators.rest.clients.SrmRestInternalFacade;
import es.gobcan.istac.indicators.rest.dto.IndicatorSelection;
import es.gobcan.istac.indicators.rest.enume.PlainTextTypeEnum;
import es.gobcan.istac.indicators.rest.exports.ExportResourceAccessToPlainText;
import es.gobcan.istac.indicators.rest.exports.ResourceAccess;
import es.gobcan.istac.indicators.rest.facadeapi.GeographicalValuesRestFacade;
import es.gobcan.istac.indicators.rest.facadeapi.IndicatorSystemRestFacade;
import es.gobcan.istac.indicators.rest.mapper.DataTypeRequest;
import es.gobcan.istac.indicators.rest.mapper.Do2TypeMapper;
import es.gobcan.istac.indicators.rest.mapper.IndicatorInstancesRest2DoMapper;
import es.gobcan.istac.indicators.rest.mapper.IndicatorSelectionMapper;
import es.gobcan.istac.indicators.rest.mapper.IndicatorsSystemRest2DoMapper;
import es.gobcan.istac.indicators.rest.mapper.SrmRestObjectsMapper;
import es.gobcan.istac.indicators.rest.serviceapi.IndicatorsApiService;
import es.gobcan.istac.indicators.rest.types.DataType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemHistoryType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;
import es.gobcan.istac.indicators.rest.types.JsonStatDataType;
import es.gobcan.istac.indicators.rest.types.MetadataType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;
import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;
import es.gobcan.istac.indicators.rest.util.ConditionUtil;
import es.gobcan.istac.indicators.rest.util.ExportUtils;
import es.gobcan.istac.indicators.rest.util.GeographicalValuesOldVersionCompatibilityUtils;

@Service
public class IndicatorSystemRestFacadeImpl implements IndicatorSystemRestFacade {

    @Autowired
    protected IndicatorsApiService          indicatorsApiService  = null;

    @Autowired
    private Do2TypeMapper                   dto2TypeMapper        = null;

    @Autowired
    private IndicatorInstancesRest2DoMapper indicatorInstancesRest2DoMapper;

    @Autowired
    GeographicalValuesRestFacade            geographicalValuesRestFacade;

    @Autowired
    private SrmRestInternalFacade           srmRestInternalFacade = null;

    @Autowired
    private final MetadataProperties        metadataProperties    = null;

    @Autowired
    private IndicatorsSystemRest2DoMapper   indicatorsSystemRest2DoMapper;

    @Autowired
    IndicatorsConfigurationService          configurationService;

    @Override
    public PagedResultType<IndicatorsSystemBaseType> findIndicatorsSystems(String q, String order, final RestCriteriaPaginator paginator) throws MetamacException {

        // Parse Query
        SculptorCriteria sculptorCriteria = indicatorsSystemRest2DoMapper.queryParams2SculptorCriteria(q, order, paginator.getLimit(), paginator.getOffset());

        PagedResult<IndicatorsSystemVersion> indicatorsSystemVersions = findIndicatorsSystems(sculptorCriteria);

        List<IndicatorsSystemBaseType> result = dto2TypeMapper.indicatorsSystemDoToBaseType(indicatorsSystemVersions.getValues());

        PagedResultType<IndicatorsSystemBaseType> resultType = new PagedResultType<IndicatorsSystemBaseType>();
        resultType.setKind(IndicatorsRestConstants.KIND_INDICATOR_SYSTEMS);
        resultType.setLimit(paginator.getLimit());
        resultType.setOffset(paginator.getOffset());
        resultType.setTotal(indicatorsSystemVersions.getTotalRows());
        resultType.setItems(result);

        return resultType;
    }

    @Override
    public IndicatorInstanceType retrieveIndicatorInstanceByCode(final String idIndicatorSystem, final String idIndicatorInstance) throws MetamacException {
        IndicatorInstance indicatorInstance = getIndicatorInstanceByCode(idIndicatorSystem, idIndicatorInstance);
        return dto2TypeMapper.indicatorsInstanceDoToType(indicatorInstance);
    }

    @Override
    public JsonStatDataType retrieveIndicatorInstanceJsonStatByCode(final String idIndicatorSystem, final String idIndicatorInstance, Map<String, List<String>> selectedRepresentations,
            Map<String, List<String>> selectedGranularities) throws MetamacException {

        SrmRestObjectsMapper srmRestObjectsMapper = new SrmRestObjectsMapper();
        srmRestInternalFacade.retrieveCodelistVariableElementInformation(metadataProperties.getDefaultGeographicalCodeListUrn(), srmRestObjectsMapper);

        IndicatorInstance indicatorInstance = indicatorsApiService.retrieveIndicatorInstanceByCode(idIndicatorSystem, idIndicatorInstance);

        getInformationForOldGeographicalValuesCompatibilityForInstanceData(indicatorInstance.getIndicator().getCode(), selectedRepresentations, srmRestObjectsMapper);

        IndicatorVersion indicatorVersion = indicatorsApiService.retrieveIndicatorByCode(indicatorInstance.getIndicator().getCode());
        IndicatorsDataFilterVO dataFilter = getIndicatorDataFilter(selectedRepresentations, selectedGranularities);
        IndicatorObservationsExtendedVO instanceObservations = indicatorsApiService.findObservationsExtendedInIndicatorInstance(indicatorInstance.getUuid(), dataFilter);
        return dto2TypeMapper.indicatorsInstanceDoToJsonStatType(indicatorInstance, indicatorVersion, instanceObservations);
    }

    protected PagedResult<IndicatorsSystemVersion> findIndicatorsSystems(SculptorCriteria sculptorCriteria) throws MetamacException {
        return indicatorsApiService.findIndicatorsSystems(sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());
    }

    protected IndicatorsSystemVersion retrieveIndicatorsSystemByCode(String idIndicatorSystem) throws MetamacException {
        return indicatorsApiService.retrieveIndicatorsSystemByCode(idIndicatorSystem);
    }

    protected PagedResult<IndicatorInstance> findIndicatorsInstancesInIndicatorsSystems(SculptorCriteria sculptorCriteria) throws MetamacException {
        return indicatorsApiService.findIndicatorsInstancesInIndicatorsSystems(sculptorCriteria);
    }

    protected IndicatorInstance getIndicatorInstanceByCode(String idIndicatorSystem, String idIndicatorInstance) throws MetamacException {
        return indicatorsApiService.retrieveIndicatorInstanceByCode(idIndicatorSystem, idIndicatorInstance);
    }

    @Override
    public IndicatorsSystemType retrieveIndicatorsSystem(String idIndicatorSystem) throws MetamacException {
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorsSystemByCode(idIndicatorSystem);
        IndicatorsSystemType result = dto2TypeMapper.indicatorsSystemDoToType(indicatorsSystemVersion);
        return result;
    }

    @Override
    public List<IndicatorsSystemHistoryType> findIndicatorsSystemHistoryByCode(final String code, final int maxResults) throws MetamacException {
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorsSystemByCode(code);

        List<IndicatorsSystemHistory> systemHistories = indicatorsApiService.findIndicatorsSystemHistory(indicatorsSystemVersion.getIndicatorsSystem().getUuid(), maxResults);

        List<IndicatorsSystemHistoryType> systemHistoriesType = new ArrayList<IndicatorsSystemHistoryType>();
        for (IndicatorsSystemHistory systemHistory : systemHistories) {
            systemHistoriesType.add(dto2TypeMapper.indicatorsSystemHistoryDoToType(systemHistory));
        }
        return systemHistoriesType;
    }

    private GeographicalValuesOldVersionCompatibilityUtils getInformationForOldGeographicalValuesCompatibility(PagedResult<IndicatorInstance> instances,
            PagedResultType<IndicatorInstanceBaseType> result, Map<String, List<String>> representation, SrmRestObjectsMapper srmRestObjectsMapper) throws MetamacException {

        GeographicalValuesOldVersionCompatibilityUtils geoValuesOldVersionCompatibilityUtils = new GeographicalValuesOldVersionCompatibilityUtils(representation, true, srmRestObjectsMapper);

        if (geoValuesOldVersionCompatibilityUtils.checkNeedCompatibility(geographicalValuesRestFacade, representation)) {
            for (int i = 0; i < result.getItems().size(); i++) {
                IndicatorInstance indicatorInstance = instances.getValues().get(i);
                IndicatorVersion indicatorVersion = indicatorsApiService.retrieveIndicatorByCode(indicatorInstance.getIndicator().getCode());

                geoValuesOldVersionCompatibilityUtils.convertOldCodesForIndicatorsSystem(srmRestInternalFacade, indicatorVersion);

                if (geoValuesOldVersionCompatibilityUtils.allCodesConverted()) {
                    geoValuesOldVersionCompatibilityUtils.setNewCodesForSelectedRepresentation(representation);
                    break;
                }
            }
        }

        return geoValuesOldVersionCompatibilityUtils;
    }

    @Override
    public PagedResultType<IndicatorInstanceBaseType> retrievePaginatedIndicatorsInstances(final String idIndicatorSystem, String q, String order, Integer limit, Integer offset, String fields,
            Map<String, List<String>> representation, Map<String, List<String>> selectedGranularities) throws MetamacException {

        // Parse Query
        SculptorCriteria sculptorCriteria = indicatorInstancesRest2DoMapper.queryParams2SculptorCriteria(q, order, limit, offset);
        ConditionalCriteria systemCondition = ConditionalCriteria.equal(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().indicatorsSystem().code(), idIndicatorSystem);
        sculptorCriteria.getConditions().add(systemCondition);

        PagedResult<IndicatorInstance> instances = findIndicatorsInstancesInIndicatorsSystems(sculptorCriteria);

        // Mapping to type
        List<IndicatorInstanceBaseType> indicatorInstanceTypes = dto2TypeMapper.indicatorsInstanceDoToBaseType(instances.getValues());
        PagedResultType<IndicatorInstanceBaseType> result = new PagedResultType<IndicatorInstanceBaseType>();
        result.setKind(IndicatorsRestConstants.KIND_INDICATOR_INSTANCES);
        result.setLimit(limit);
        result.setTotal(instances.getTotalRows());
        result.setOffset(instances.getStartRow());
        result.setItems(indicatorInstanceTypes);

        // Fields filter. Only support for +metadata, +data
        Set<String> fieldsToAdd = RequestUtil.parseFields(fields);

        SrmRestObjectsMapper srmRestObjectsMapper = new SrmRestObjectsMapper();
        if (fieldsToAdd.contains("+metadata") || fieldsToAdd.contains("+data")) {
            srmRestInternalFacade.retrieveCodelistVariableElementInformation(metadataProperties.getDefaultGeographicalCodeListUrn(), srmRestObjectsMapper);
        }

        GeographicalValuesOldVersionCompatibilityUtils geoValuesOldVersionCompatibilityUtils = getInformationForOldGeographicalValuesCompatibility(instances, result, representation,
                srmRestObjectsMapper);

        if (fieldsToAdd.contains("+metadata")) {

            for (int i = 0; i < result.getItems().size(); i++) {
                IndicatorInstanceBaseType baseType = result.getItems().get(i);
                IndicatorInstance indicatorInstance = instances.getValues().get(i);

                MetadataType metadataType = new MetadataType();
                dto2TypeMapper.indicatorsInstanceDoToMetadataType(indicatorInstance, metadataType, geoValuesOldVersionCompatibilityUtils, srmRestObjectsMapper.getGeographicalCodesByVariableElement());
                baseType.setMetadata(metadataType);
            }
        }

        if (fieldsToAdd.contains("+data")) {
            boolean includeObservationsAttributes = fieldsToAdd.contains("+observationsMetadata");

            for (IndicatorInstanceBaseType type : result.getItems()) {
                IndicatorInstance indicatorInstance = getIndicatorInstanceByCode(idIndicatorSystem, type.getId());

                DataType dataType = retrieveIndicatorInstanceDataByCodeCommon(indicatorInstance, representation, selectedGranularities, includeObservationsAttributes,
                        geoValuesOldVersionCompatibilityUtils, srmRestObjectsMapper.getGeographicalCodesByVariableElement());

                type.setData(dataType);
            }
        }
        return result;
    }

    private DataType retrieveIndicatorInstanceDataByCodeCommon(IndicatorInstance indicatorInstance, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities,
            boolean includeObservationMetadata, GeographicalValuesOldVersionCompatibilityUtils geoValuesOldVersionCompatibilityUtils, Map<String, String> geographicalValuesCodes)
            throws MetamacException {

        IndicatorsDataFilterVO dataFilter = getIndicatorDataFilter(selectedRepresentations, selectedGranularities);

        DataType dataType;
        if (includeObservationMetadata) {
            IndicatorObservationsExtendedVO instanceObservations = indicatorsApiService.findObservationsExtendedInIndicatorInstance(indicatorInstance.getUuid(), dataFilter);

            DataTypeRequest dataTypeRequest = new DataTypeRequest(indicatorInstance, instanceObservations.getGeographicalCodes(), instanceObservations.getTimeCodes(),
                    instanceObservations.getMeasureCodes(), instanceObservations.getObservations());
            dataTypeRequest.setGeoValuesOldVersionCompatibilityUtils(geoValuesOldVersionCompatibilityUtils);

            if (geographicalValuesCodes.isEmpty()) {
                geographicalValuesCodes = srmRestInternalFacade.retrieveGeographicalElementsIdByCodesOfCodelists(metadataProperties.getDefaultGeographicalCodeListUrn());
            }

            dataType = dto2TypeMapper.createDataTypeWithGeographicalCodes(dataTypeRequest, includeObservationMetadata, geographicalValuesCodes);
        } else {
            IndicatorObservationsVO instanceObservations = indicatorsApiService.findObservationsInIndicatorInstance(indicatorInstance.getUuid(), dataFilter);

            DataTypeRequest dataTypeRequest = new DataTypeRequest(indicatorInstance, instanceObservations.getGeographicalCodes(), instanceObservations.getTimeCodes(),
                    instanceObservations.getMeasureCodes(), instanceObservations.getObservations());
            dataTypeRequest.setGeoValuesOldVersionCompatibilityUtils(geoValuesOldVersionCompatibilityUtils);
            dataType = dto2TypeMapper.createDataTypeWithGeographicalCodes(dataTypeRequest, includeObservationMetadata, geographicalValuesCodes);
        }

        return dataType;
    }

    private GeographicalValuesOldVersionCompatibilityUtils getInformationForOldGeographicalValuesCompatibilityForInstanceData(String indicadorInstanceCode, Map<String, List<String>> representation,
            SrmRestObjectsMapper srmRestObjectsMapper) throws MetamacException {

        GeographicalValuesOldVersionCompatibilityUtils geoValuesOldVersionCompatibilityUtils = new GeographicalValuesOldVersionCompatibilityUtils(representation, true, srmRestObjectsMapper);

        if (geoValuesOldVersionCompatibilityUtils.checkNeedCompatibility(geographicalValuesRestFacade, representation)) {

            IndicatorVersion indicatorVersion = indicatorsApiService.retrieveIndicatorByCode(indicadorInstanceCode);

            geoValuesOldVersionCompatibilityUtils.convertOldCodesForIndicatorsSystem(srmRestInternalFacade, indicatorVersion);

            if (geoValuesOldVersionCompatibilityUtils.allCodesConverted()) {
                geoValuesOldVersionCompatibilityUtils.setNewCodesForSelectedRepresentation(representation);
            }
        }

        return geoValuesOldVersionCompatibilityUtils;

    }

    @Override
    public DataType retrieveIndicatorInstanceDataByCode(String idIndicatorSystem, String idIndicatorInstance, Map<String, List<String>> selectedRepresentations,
            Map<String, List<String>> selectedGranularities, boolean includeObservationMetadata) throws MetamacException {
        SrmRestObjectsMapper srmRestObjectsMapper = new SrmRestObjectsMapper();
        srmRestInternalFacade.retrieveCodelistVariableElementInformation(metadataProperties.getDefaultGeographicalCodeListUrn(), srmRestObjectsMapper);

        IndicatorInstance indicatorInstance = getIndicatorInstanceByCode(idIndicatorSystem, idIndicatorInstance);

        GeographicalValuesOldVersionCompatibilityUtils geoValuesOldVersionCompatibilityUtils = getInformationForOldGeographicalValuesCompatibilityForInstanceData(
                indicatorInstance.getIndicator().getCode(), selectedRepresentations, srmRestObjectsMapper);

        return retrieveIndicatorInstanceDataByCodeCommon(indicatorInstance, selectedRepresentations, selectedGranularities, includeObservationMetadata, geoValuesOldVersionCompatibilityUtils,
                srmRestObjectsMapper.getGeographicalCodesByVariableElement());
    }

    private static IndicatorsDataFilterVO getIndicatorDataFilter(Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities) throws MetamacException {
        IndicatorsDataGeoDimensionFilterVO geoFilter = ConditionUtil.filterGeographicalDimension(selectedRepresentations, selectedGranularities);
        IndicatorsDataTimeDimensionFilterVO timeFilter = ConditionUtil.normalizeAndFilterTimeDimension(selectedRepresentations, selectedGranularities);
        IndicatorsDataMeasureDimensionFilterVO measureFilter = ConditionUtil.filterMeasureDimension(selectedRepresentations);

        IndicatorsDataFilterVO dataFilter = new IndicatorsDataFilterVO();
        dataFilter.setGeoFilter(geoFilter);
        dataFilter.setTimeFilter(timeFilter);
        dataFilter.setMeasureFilter(measureFilter);
        return dataFilter;
    }

    @Override
    public ResponseEntity<byte[]> retrieveIndicatorInstanceDataCSV(String idIndicatorSystem, String idIndicatorInstance, Map<String, List<String>> selectedRepresentations,
            Map<String, List<String>> selectedGranularities) throws MetamacException {
        return retrieveIndicatorInstanceDataPlainText(idIndicatorSystem, idIndicatorInstance, selectedRepresentations, selectedGranularities, PlainTextTypeEnum.CSV_COMMA);
    }

    private ResponseEntity<byte[]> retrieveIndicatorInstanceDataPlainText(String idIndicatorSystem, String idIndicatorInstance, Map<String, List<String>> selectedRepresentations,
            Map<String, List<String>> selectedGranularities, PlainTextTypeEnum format) throws MetamacException {
        try {
            String lang = configurationService.retrieveLanguageDefault();
            byte[] content = null;
            DataType indicatorData = retrieveIndicatorInstanceDataByCode(idIndicatorSystem, idIndicatorInstance, selectedRepresentations, selectedGranularities, true);
            IndicatorInstanceType indicatorInstanceType = retrieveIndicatorInstanceByCode(idIndicatorSystem, idIndicatorInstance);

            IndicatorSelection indicatorSelection = IndicatorSelectionMapper.indicatorToIndicatorSelection(indicatorData.getDimension(), indicatorData.getAttribute(), null, null);
            ResourceAccess resourceAccess = new ResourceAccess(indicatorData, indicatorInstanceType, indicatorSelection, lang);

            ExportResourceAccessToPlainText exportResourceAccessToPlainText = new ExportResourceAccessToPlainText();
            exportResourceAccessToPlainText.checkMaxRowsInXlsxFormat(resourceAccess, format, configurationService.retrieveMaxXlsxRows(), idIndicatorInstance);

            FileOutputStream outputStreamObservations = null;
            ByteArrayOutputStream byteArrayOutputStream = null;
            FileInputStream inputStream = null;
            try {
                String fileNamePrefix = IndicatorsRestConstants.API_INDICATORS_INDICATORS_DATA + "-" + idIndicatorInstance;

                final File tmpFileObservations = File.createTempFile(fileNamePrefix, format.getExtension());
                outputStreamObservations = new FileOutputStream(tmpFileObservations);
                exportResourceAccessToPlainText.exportResourceAccessToPlainText(resourceAccess, format, outputStreamObservations);

                byteArrayOutputStream = new ByteArrayOutputStream();

                inputStream = new FileInputStream(tmpFileObservations);

                IOUtils.copy(inputStream, byteArrayOutputStream);

                content = byteArrayOutputStream.toByteArray();

                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Disposition", ExportUtils.getContentDisposition(idIndicatorInstance, format.getExtension()));
                return new ResponseEntity<>(content, headers, HttpStatus.OK);

            } finally {
                IOUtils.closeQuietly(outputStreamObservations);
            }

        } catch (Exception e) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, idIndicatorInstance);
        }
    }
}
