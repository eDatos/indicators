package es.gobcan.istac.indicators.rest.facadeimpl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.rest.RequestUtil;
import org.siemac.metamac.rest.search.criteria.SculptorCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.conf.MetadataProperties;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsExtendedVO;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataGeoDimensionFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataMeasureDimensionFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataTimeDimensionFilterVO;
import es.gobcan.istac.indicators.rest.IndicatorsRestConstants;
import es.gobcan.istac.indicators.rest.clients.SrmRestInternalFacade;
import es.gobcan.istac.indicators.rest.dto.IndicatorSelection;
import es.gobcan.istac.indicators.rest.enume.ExportFormatEnum;
import es.gobcan.istac.indicators.rest.exports.ExcelMapper;
import es.gobcan.istac.indicators.rest.exports.ExporterResourceAccess;
import es.gobcan.istac.indicators.rest.exports.ResourceAccess;
import es.gobcan.istac.indicators.rest.facadeapi.GeographicalValuesRestFacade;
import es.gobcan.istac.indicators.rest.facadeapi.IndicatorRestFacade;
import es.gobcan.istac.indicators.rest.mapper.DataTypeRequest;
import es.gobcan.istac.indicators.rest.mapper.Do2TypeMapper;
import es.gobcan.istac.indicators.rest.mapper.IndicatorSelectionMapper;
import es.gobcan.istac.indicators.rest.mapper.IndicatorsRest2DoMapper;
import es.gobcan.istac.indicators.rest.mapper.SrmRestObjectsMapper;
import es.gobcan.istac.indicators.rest.serviceapi.IndicatorsApiService;
import es.gobcan.istac.indicators.rest.types.DataType;
import es.gobcan.istac.indicators.rest.types.IndicatorBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorType;
import es.gobcan.istac.indicators.rest.types.JsonStatDataType;
import es.gobcan.istac.indicators.rest.types.MetadataType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;
import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;
import es.gobcan.istac.indicators.rest.util.ConditionUtil;
import es.gobcan.istac.indicators.rest.util.GeographicalValuesOldVersionCompatibilityUtils;

@Service
public class IndicatorRestFacadeImpl implements IndicatorRestFacade {

    protected Logger                 logger                = LoggerFactory.getLogger(IndicatorRestFacadeImpl.class);

    @Autowired
    private Do2TypeMapper            do2TypeMapper;

    @Autowired
    protected IndicatorsApiService   indicatorsApiService;

    @Autowired
    private IndicatorsRest2DoMapper  indicatorsRest2DoMapper;

    @Autowired
    private SrmRestInternalFacade    srmRestInternalFacade = null;

    @Autowired
    GeographicalValuesRestFacade     geographicalValuesRestFacade;

    @Autowired
    private final MetadataProperties metadataProperties    = null;

    @Autowired
    IndicatorsConfigurationService   configurationService;

    private ExcelMapper              excelMapper;

    @SuppressWarnings("unchecked")
    @Override
    public PagedResultType<IndicatorBaseType> findIndicators(String q, String order, final RestCriteriaPaginator paginator, String fields, Map<String, List<String>> representation)
            throws MetamacException {
        // Parse Query
        SculptorCriteria sculptorCriteria = indicatorsRest2DoMapper.queryParams2SculptorCriteria(q, order, paginator.getLimit(), paginator.getOffset());

        // Find
        PagedResult<IndicatorVersion> indicatorsVersions = findIndicators(sculptorCriteria);

        // Parse fields
        Set<String> fieldsToAdd = RequestUtil.parseFields(fields);

        // Mapping to type
        List<IndicatorBaseType> result = do2TypeMapper.indicatorDoToBaseType(indicatorsVersions.getValues());

        SrmRestObjectsMapper srmRestObjectsMapper = new SrmRestObjectsMapper();
        if (fieldsToAdd.contains("+metadata") || fieldsToAdd.contains("+data")) {
            srmRestInternalFacade.retrieveCodelistVariableElementInformation(metadataProperties.getDefaultGeographicalCodeListUrn(), srmRestObjectsMapper);
        }

        GeographicalValuesOldVersionCompatibilityUtils geoValuesOldVersionCompatibilityUtils = getInformationForOldGeographicalValuesCompatibility(indicatorsVersions.getValues(), representation,
                srmRestObjectsMapper);

        // Fields filter. Only support for +metadata, +data
        if (fieldsToAdd.contains("+metadata")) {

            for (int i = 0; i < result.size(); i++) {
                IndicatorBaseType baseType = result.get(i);
                IndicatorVersion indicatorVersion = indicatorsVersions.getValues().get(i);

                MetadataType metadataType = new MetadataType();
                do2TypeMapper.indicatorDoToMetadataType(indicatorVersion, metadataType, geoValuesOldVersionCompatibilityUtils, srmRestObjectsMapper);

                baseType.setMetadata(metadataType);
            }
        }

        if (fieldsToAdd.contains("+data")) {
            boolean includeObservationsAttributes = fieldsToAdd.contains("+observationsMetadata");
            for (IndicatorBaseType indicatorType : result) {
                Map<String, List<String>> selectedGranularities = MapUtils.EMPTY_MAP;
                DataType dataType = retrieveIndicatorData(indicatorType.getCode(), representation, selectedGranularities, geoValuesOldVersionCompatibilityUtils, includeObservationsAttributes,
                        srmRestObjectsMapper);
                indicatorType.setData(dataType);
            }
        }

        // Pagegd result
        PagedResultType<IndicatorBaseType> resultType = new PagedResultType<IndicatorBaseType>();
        resultType.setKind(IndicatorsRestConstants.KIND_INDICATORS);
        resultType.setLimit(paginator.getLimit());
        resultType.setOffset(paginator.getOffset());
        resultType.setTotal(indicatorsVersions.getTotalRows());
        resultType.setItems(result);

        return resultType;
    }

    private GeographicalValuesOldVersionCompatibilityUtils getInformationForOldGeographicalValuesCompatibility(List<IndicatorVersion> indicatorVersions, Map<String, List<String>> representation,
            SrmRestObjectsMapper srmRestObjectsMapper) {
        GeographicalValuesOldVersionCompatibilityUtils geoValuesOldVersionCompatibilityUtils = new GeographicalValuesOldVersionCompatibilityUtils(representation, srmRestObjectsMapper);
        if (indicatorVersions != null && !indicatorVersions.isEmpty()) {
            geoValuesOldVersionCompatibilityUtils.setGeographicalRepresentationByVariableElements(geographicalValuesRestFacade, srmRestInternalFacade, indicatorVersions.get(0), representation);
        }
        return geoValuesOldVersionCompatibilityUtils;

    }

    protected PagedResult<IndicatorVersion> findIndicators(SculptorCriteria sculptorCriteria) throws org.siemac.metamac.core.common.exception.MetamacException {
        return indicatorsApiService.findIndicators(sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());
    }

    protected IndicatorVersion retrieveIndicatorByCode(String indicatorCode) throws org.siemac.metamac.core.common.exception.MetamacException {
        return indicatorsApiService.retrieveIndicatorByCode(indicatorCode);
    }

    @Override
    public IndicatorType retrieveIndicator(String indicatorCode) throws MetamacException {
        IndicatorVersion indicatorsVersion = retrieveIndicatorByCode(indicatorCode);
        return do2TypeMapper.indicatorDoToType(indicatorsVersion);
    }

    @Override
    public JsonStatDataType retrieveJsonStatIndicator(String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities)
            throws MetamacException {
        SrmRestObjectsMapper srmRestObjectsMapper = new SrmRestObjectsMapper();
        srmRestInternalFacade.retrieveCodelistVariableElementInformation(metadataProperties.getDefaultGeographicalCodeListUrn(), srmRestObjectsMapper);

        IndicatorVersion indicatorVersion = retrieveIndicatorByCode(indicatorCode);

        getInformationForOldGeographicalValuesCompatibility(Arrays.asList(indicatorVersion), selectedRepresentations, srmRestObjectsMapper);

        IndicatorsDataFilterVO dataFilter = getIndicatorsDataFilter(selectedRepresentations, selectedGranularities);
        IndicatorObservationsExtendedVO indicatorObservationsExtended = indicatorsApiService.findObservationsExtendedInIndicator(indicatorVersion.getIndicator().getUuid(), dataFilter);
        return do2TypeMapper.indicatorDoToJsonStatType(indicatorVersion, indicatorObservationsExtended);
    }

    @Override
    public ResponseEntity<byte[]> retrieveIndicatorDataXLSX(String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities)
            throws MetamacException {
        return retrieveIndicatorData(indicatorCode, selectedRepresentations, selectedGranularities, ExportFormatEnum.XLSX);
    }
    @Override
    public ResponseEntity<byte[]> retrieveIndicatorDataCSV(String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities)
            throws MetamacException {
        return retrieveIndicatorData(indicatorCode, selectedRepresentations, selectedGranularities, ExportFormatEnum.CSV_COMMA);
    }
    @Override
    public ResponseEntity<byte[]> retrieveIndicatorDataTSV(String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities)
            throws MetamacException {
        return retrieveIndicatorData(indicatorCode, selectedRepresentations, selectedGranularities, ExportFormatEnum.TSV);
    }

    private ResponseEntity<byte[]> retrieveIndicatorData(String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities,
            ExportFormatEnum format) throws MetamacException {
        DataType indicatorData = retrieveIndicatorData(indicatorCode, selectedRepresentations, selectedGranularities, true);
        IndicatorType indicatorType = retrieveIndicator(indicatorCode);
        IndicatorSelection indicatorSelection = IndicatorSelectionMapper.indicatorToIndicatorSelection(indicatorData.getDimension(), indicatorData.getAttribute(), null, null);
        ResourceAccess resourceAccess = new ResourceAccess(indicatorData, indicatorType, indicatorSelection, configurationService.retrieveLanguageDefault());
        return ExporterResourceAccess.exportIndicatorResource(indicatorCode, format, configurationService, resourceAccess);

    }

    @Override
    public DataType retrieveIndicatorData(String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities, boolean includeObservationMetadata)
            throws MetamacException {
        SrmRestObjectsMapper srmRestObjectsMapper = new SrmRestObjectsMapper();
        srmRestInternalFacade.retrieveCodelistVariableElementInformation(metadataProperties.getDefaultGeographicalCodeListUrn(), srmRestObjectsMapper);

        IndicatorVersion indicatorVersion = retrieveIndicatorByCode(indicatorCode);

        GeographicalValuesOldVersionCompatibilityUtils geoValuesOldVersionCompatibilityUtils = getInformationForOldGeographicalValuesCompatibility(Arrays.asList(indicatorVersion),
                selectedRepresentations, srmRestObjectsMapper);

        DataTypeRequest dataTypeRequest = retrieveIndicatorDataCommon(indicatorVersion, selectedRepresentations, selectedGranularities, includeObservationMetadata, srmRestObjectsMapper);
        dataTypeRequest.setGeoValuesOldVersionCompatibilityUtils(geoValuesOldVersionCompatibilityUtils);
        return do2TypeMapper.createDataTypeWithGeographicalCodes(dataTypeRequest, includeObservationMetadata, srmRestObjectsMapper.getGeographicalCodesByVariableElement());
    }

    public DataTypeRequest retrieveIndicatorDataCommon(IndicatorVersion indicatorVersion, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities,
            boolean includeObservationMetadata, SrmRestObjectsMapper srmRestObjectsMapper) throws MetamacException {

        IndicatorsDataFilterVO dataFilter = getIndicatorsDataFilter(selectedRepresentations, selectedGranularities);
        DataTypeRequest dataTypeRequest = null;
        if (includeObservationMetadata) {
            IndicatorObservationsExtendedVO indicatorObservationsExtended = indicatorsApiService.findObservationsExtendedInIndicator(indicatorVersion.getIndicator().getUuid(), dataFilter);

            dataTypeRequest = new DataTypeRequest(indicatorVersion, indicatorObservationsExtended.getGeographicalCodes(), indicatorObservationsExtended.getTimeCodes(),
                    indicatorObservationsExtended.getMeasureCodes(), indicatorObservationsExtended.getObservations());

        } else {
            IndicatorObservationsVO indicatorObservations = indicatorsApiService.findObservationsInIndicator(indicatorVersion.getIndicator().getUuid(), dataFilter);

            dataTypeRequest = new DataTypeRequest(indicatorVersion, indicatorObservations.getGeographicalCodes(), indicatorObservations.getTimeCodes(), indicatorObservations.getMeasureCodes(),
                    indicatorObservations.getObservations());
        }
        return dataTypeRequest;
    }

    private DataType retrieveIndicatorData(String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities,
            GeographicalValuesOldVersionCompatibilityUtils geoValuesOldVersionCompatibilityUtils, boolean includeObservationMetadata, SrmRestObjectsMapper srmRestObjectsMapper)
            throws MetamacException {
        IndicatorVersion indicatorVersion = retrieveIndicatorByCode(indicatorCode);
        DataTypeRequest dataTypeRequest = retrieveIndicatorDataCommon(indicatorVersion, selectedRepresentations, selectedGranularities, includeObservationMetadata, srmRestObjectsMapper);
        dataTypeRequest.setGeoValuesOldVersionCompatibilityUtils(geoValuesOldVersionCompatibilityUtils);
        return do2TypeMapper.createDataTypeWithGeographicalCodes(dataTypeRequest, includeObservationMetadata, srmRestObjectsMapper.getGeographicalCodesByVariableElement());

    }

    private IndicatorsDataFilterVO getIndicatorsDataFilter(Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities) throws MetamacException {

        IndicatorsDataGeoDimensionFilterVO geoFilter = ConditionUtil.filterGeographicalDimension(selectedRepresentations, selectedGranularities);
        IndicatorsDataTimeDimensionFilterVO timeFilter = ConditionUtil.normalizeAndFilterTimeDimension(selectedRepresentations, selectedGranularities);
        IndicatorsDataMeasureDimensionFilterVO measureFilter = ConditionUtil.filterMeasureDimension(selectedRepresentations);

        IndicatorsDataFilterVO dataFilter = new IndicatorsDataFilterVO();
        dataFilter.setGeoFilter(geoFilter);
        dataFilter.setTimeFilter(timeFilter);
        dataFilter.setMeasureFilter(measureFilter);

        return dataFilter;
    }
}
