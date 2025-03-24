package es.gobcan.istac.indicators.rest.facadeimpl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.rest.RequestUtil;
import org.siemac.metamac.rest.search.criteria.SculptorCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.conf.MetadataProperties;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsExtendedVO;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataGeoDimensionFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataMeasureDimensionFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataTimeDimensionFilterVO;
import es.gobcan.istac.indicators.rest.ExcelMapper;
import es.gobcan.istac.indicators.rest.ExportResourceAccessToPlainText;
import es.gobcan.istac.indicators.rest.IndicatorsRestConstants;
import es.gobcan.istac.indicators.rest.ResourceAccess;
import es.gobcan.istac.indicators.rest.clients.SrmRestInternalFacade;
import es.gobcan.istac.indicators.rest.facadeapi.GeographicalValuesRestFacade;
import es.gobcan.istac.indicators.rest.facadeapi.IndicatorRestFacade;
import es.gobcan.istac.indicators.rest.mapper.DataTypeRequest;
import es.gobcan.istac.indicators.rest.mapper.Do2TypeMapper;
import es.gobcan.istac.indicators.rest.mapper.IndicatorsRest2DoMapper;
import es.gobcan.istac.indicators.rest.mapper.SrmRestObjectsMapper;
import es.gobcan.istac.indicators.rest.serviceapi.IndicatorsApiService;
import es.gobcan.istac.indicators.rest.types.AttributeType;
import es.gobcan.istac.indicators.rest.types.DataDimensionType;
import es.gobcan.istac.indicators.rest.types.DataType;
import es.gobcan.istac.indicators.rest.types.IndicatorBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorType;
import es.gobcan.istac.indicators.rest.types.JsonStatDataType;
import es.gobcan.istac.indicators.rest.types.MetadataType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;
import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;
import es.gobcan.istac.indicators.rest.util.ConditionUtil;
import es.gobcan.istac.indicators.rest.util.GeographicalValuesOldVersionCompatibilityUtils;

@Service("IndicatorsRestFacade")
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
            srmRestObjectsMapper.setGeographicalCodesByVariableElement(srmRestInternalFacade.retrieveGeographicalElementsIdByCodesOfCodelists(metadataProperties.getDefaultGeographicalCodeListUrn()));
            srmRestObjectsMapper.setGeographicalVariableElementsByCode(srmRestInternalFacade.retrieveVariableElementsIdByCodesOfCodelists(metadataProperties.getDefaultGeographicalCodeListUrn()));
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

    protected PagedResult<IndicatorVersion> findIndicators(SculptorCriteria sculptorCriteria) throws MetamacException {
        return indicatorsApiService.findIndicators(sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());
    }

    protected IndicatorVersion retrieveIndicatorByCode(String indicatorCode) throws MetamacException {
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
        IndicatorVersion indicatorVersion = retrieveIndicatorByCode(indicatorCode);
        IndicatorsDataFilterVO dataFilter = getIndicatorsDataFilter(selectedRepresentations, selectedGranularities, new HashMap<>());
        IndicatorObservationsExtendedVO indicatorObservationsExtended = indicatorsApiService.findObservationsExtendedInIndicator(indicatorVersion.getIndicator().getUuid(), dataFilter);
        return do2TypeMapper.indicatorDoToJsonStatType(indicatorVersion, indicatorObservationsExtended);
    }

    @Override
    public ResponseEntity<byte[]> retrieveIndicatorDataXLSX(String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities)
            throws MetamacException {
        return retrieveIndicatorDataPlainText(indicatorCode, selectedRepresentations, selectedGranularities, "xlsx");
    }
    @Override
    public ResponseEntity<byte[]> retrieveIndicatorDataCSV(String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities)
            throws MetamacException {
        return retrieveIndicatorDataPlainText(indicatorCode, selectedRepresentations, selectedGranularities, "csv");
    }
    @Override
    public ResponseEntity<byte[]> retrieveIndicatorDataTSV(String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities)
            throws MetamacException {
        return retrieveIndicatorDataPlainText(indicatorCode, selectedRepresentations, selectedGranularities, "tsv");
    }

    private ResponseEntity<byte[]> retrieveIndicatorDataPlainText(String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities,
            String format)
            throws MetamacException {
        try {
            byte[] content = null;
            DataType indicatorData = retrieveIndicatorData(indicatorCode, selectedRepresentations, selectedGranularities, true);
            ResourceAccess resourceAccess = new ResourceAccess(indicatorData);
            ExportResourceAccessToPlainText exportResourceAccessToPlainText = new ExportResourceAccessToPlainText();
            exportResourceAccessToPlainText.checkMaxRowsInXlsxFormat(resourceAccess, format, configurationService.retrieveMaxXlsxRows(), indicatorCode);

            FileOutputStream outputStreamObservations = null;
            ByteArrayOutputStream byteArrayOutputStream = null;
            FileInputStream inputStream = null;
            try {
                String fileNamePrefix = IndicatorsRestConstants.API_INDICATORS_INDICATORS_DATA + "-" + indicatorCode;

                final File tmpFileObservations = File.createTempFile(fileNamePrefix, format);
                outputStreamObservations = new FileOutputStream(tmpFileObservations);
                exportResourceAccessToPlainText.exportResourceAccessToPlainText(resourceAccess, format, outputStreamObservations);

                byteArrayOutputStream = new ByteArrayOutputStream();
                // return ResponseEntity<>.ok(new DeleteOnCloseFileInputStream(tmpFileObservations), ResourcesFormat.getMimeType(format.toUpperCase()))
                // .header("Content-Disposition", getContentDisposition(fileNamePrefix, format)).build();

                inputStream = new FileInputStream(tmpFileObservations);

                IOUtils.copy(inputStream, byteArrayOutputStream);

                content = byteArrayOutputStream.toByteArray();

                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Disposition", getContentDisposition(indicatorCode, format));
                return new ResponseEntity<>(content, headers, HttpStatus.OK);

            } finally {
                IOUtils.closeQuietly(outputStreamObservations);
            }

            // switch (format.toLowerCase()) {
            // case "csv":
            // content = convertJsonToText(indicatorData, ",").getBytes(StandardCharsets.UTF_8);
            // break;
            // case "tsv":
            // content = convertJsonToText(indicatorData, "\t").getBytes(StandardCharsets.UTF_8);
            // break;
            // case "xlsx":
            // content = generateXlsxFromJson(indicatorData);
            // break;
            // default:
            // throw new IllegalArgumentException("Unsupported format: " + format);
            // }

        } catch (Exception e) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, indicatorCode);
        }
    }

    private String convertJsonToText(DataType indicatorData, String delimiter) {
        StringBuilder csvBuilder = new StringBuilder();

        List<String> headers = extractHeadersFromIndicatorData(indicatorData);
        csvBuilder.append(String.join(delimiter, headers)).append("\n");

        List<List<String>> rows = extractRowsFromIndicatorData(indicatorData, headers);
        for (List<String> row : rows) {
            csvBuilder.append(String.join(delimiter, row)).append("\n");
        }

        return csvBuilder.toString();
    }
    private List<String> extractHeadersFromIndicatorData(DataType indicatorData) {
        List<String> headers = new ArrayList<>();

        if (indicatorData == null) {
            return headers;
        }

        Map<String, DataDimensionType> dimensions = indicatorData.getDimension();
        if (dimensions != null) {
            for (String key : dimensions.keySet()) {
                headers.add(key);
            }
            DataDimensionType measureDimension = dimensions.get("MEASURE");
            if (measureDimension != null && measureDimension.getRepresentation() != null) {
                Map<String, Integer> measureIndexMap = measureDimension.getRepresentation().getIndex();
                if (measureIndexMap != null) {
                    for (String measureKey : measureIndexMap.keySet()) {
                        headers.add(measureKey);
                    }
                }
            }
        }

        List<Map<String, AttributeType>> attributes = indicatorData.getAttribute();
        if (attributes != null) {
            for (Map<String, AttributeType> attributeMap : attributes) {
                if (attributeMap != null) {
                    for (String attributeKey : attributeMap.keySet()) {
                        if (!headers.contains(attributeKey)) {
                            headers.add(attributeKey);
                        }
                    }
                }
            }
        }

        return headers;
    }

    private List<List<String>> extractRowsFromIndicatorData(DataType indicatorData, List<String> headers) {
        List<List<String>> rows = new ArrayList<>();

        if (indicatorData == null) {
            return rows;
        }

        Map<String, DataDimensionType> dimensions = indicatorData.getDimension();
        List<Map<String, Object>> combinations = new ArrayList<>();

        // Generar todas las combinaciones posibles de dimensiones
        if (dimensions != null) {
            for (String dimensionKey : dimensions.keySet()) {
                DataDimensionType dimension = dimensions.get(dimensionKey);
                if (dimension != null && dimension.getRepresentation() != null) {
                    Map<String, Integer> indexMap = dimension.getRepresentation().getIndex();
                    if (indexMap != null) {
                        if (combinations.isEmpty()) {
                            // Inicializar combinaciones con la primera dimensión
                            for (String key : indexMap.keySet()) {
                                Map<String, Object> combination = new HashMap<>();
                                combination.put(dimensionKey, key);
                                combinations.add(combination);
                            }
                        } else {
                            // Agregar combinaciones adicionales para dimensiones subsiguientes
                            List<Map<String, Object>> newCombinations = new ArrayList<>();
                            for (Map<String, Object> existing : combinations) {
                                for (String key : indexMap.keySet()) {
                                    Map<String, Object> newCombination = new HashMap<>(existing);
                                    newCombination.put(dimensionKey, key);
                                    newCombinations.add(newCombination);
                                }
                            }
                            combinations = newCombinations;
                        }
                    }
                }
            }
        }

        // Generar las filas basadas en las combinaciones
        for (Map<String, Object> combination : combinations) {
            List<String> row = new ArrayList<>(Collections.nCopies(headers.size(), ""));

            // Insertar valores de las combinaciones de dimensiones en las columnas correspondientes
            for (Map.Entry<String, Object> entry : combination.entrySet()) {
                int headerIndex = headers.indexOf(entry.getKey());
                if (headerIndex >= 0) {
                    row.set(headerIndex, entry.getValue().toString());
                }
            }

            // Agregar la observación correspondiente
            List<String> observations = indicatorData.getObservation();
            if (observations != null) {
                int obsIndex = headers.indexOf("OBS_VALUE");
                if (obsIndex >= 0 && observations.size() > 0) {
                    row.set(obsIndex, observations.get(0)); // Aquí podrías ajustar la lógica si hay múltiples observaciones
                }
            }

            // Insertar valores de los atributos en las columnas correspondientes
            List<Map<String, AttributeType>> attributes = indicatorData.getAttribute();

            if (attributes != null) {
                for (Map<String, AttributeType> attributeMap : attributes) {
                    for (Map.Entry<String, AttributeType> entry : attributeMap.entrySet()) {
                        int headerIndex = headers.indexOf(entry.getKey());

                        // Validar si headerIndex y entry tienen valores válidos
                        if (headerIndex >= 0 && entry.getValue() != null) {
                            String attributeValue = (entry.getValue().getValue() != null) ? entry.getValue().getValue().toString() : ""; // Valor por defecto en caso de nulo

                            row.set(headerIndex, attributeValue);
                        }
                    }
                }
            }

            rows.add(row);
        }

        return rows;
    }
    private byte[] generateXlsxFromJson(DataType indicatorData) throws MetamacException {
        excelMapper = new ExcelMapper();

        List<String> headers = extractHeadersFromIndicatorData(indicatorData);
        Map<String, String> headerMap = createHeaderMap(headers);
        excelMapper.createHeaderRow(headerMap);

        List<List<String>> rows = extractRowsFromIndicatorData(indicatorData, headers);
        for (List<String> rowData : rows) {
            Map<String, String> rowMap = createRowMap(rowData);
            excelMapper.addObservationRow(rowMap);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        excelMapper.writeExcelWorkBookToOutputStream(outputStream);
        return outputStream.toByteArray();
    }

    private Map<String, String> createHeaderMap(List<String> headers) {
        Map<String, String> headerMap = new HashMap<>();
        for (String header : headers) {
            headerMap.put(header, header);
        }
        return headerMap;
    }

    private Map<String, String> createRowMap(List<String> rowData) {
        Map<String, String> rowMap = new HashMap<>();
        for (int i = 0; i < rowData.size(); i++) {
            rowMap.put("Column" + i, rowData.get(i));
        }
        return rowMap;
    }

    private static String getContentDisposition(String fileNamePrefix, String format) {
        return "attachment; filename=" + getExportFileName(fileNamePrefix, format);
    }

    private static String getExportFileName(String fileNamePrefix, String format) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return fileNamePrefix + "_" + timestamp + "." + format;
    }
    @Override
    public DataType retrieveIndicatorData(String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities, boolean includeObservationMetadata)
            throws MetamacException {
        SrmRestObjectsMapper srmRestObjectsMapper = new SrmRestObjectsMapper();
        srmRestObjectsMapper.setGeographicalCodesByVariableElement(srmRestInternalFacade.retrieveGeographicalElementsIdByCodesOfCodelists(metadataProperties.getDefaultGeographicalCodeListUrn()));
        srmRestObjectsMapper.setGeographicalVariableElementsByCode(srmRestInternalFacade.retrieveVariableElementsIdByCodesOfCodelists(metadataProperties.getDefaultGeographicalCodeListUrn()));

        DataTypeRequest dataTypeRequest = retrieveIndicatorDataCommon(indicatorCode, selectedRepresentations, selectedGranularities, includeObservationMetadata, srmRestObjectsMapper);

        return do2TypeMapper.createDataTypeWithGeographicalCodes(dataTypeRequest, includeObservationMetadata, srmRestObjectsMapper.getGeographicalCodesByVariableElement());
    }

    public DataTypeRequest retrieveIndicatorDataCommon(String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities,
            boolean includeObservationMetadata, SrmRestObjectsMapper srmRestObjectsMapper) throws MetamacException {
        IndicatorVersion indicatorVersion = retrieveIndicatorByCode(indicatorCode);

        IndicatorsDataFilterVO dataFilter = getIndicatorsDataFilter(selectedRepresentations, selectedGranularities, srmRestObjectsMapper.getGeographicalVariableElementsByCode());
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
        DataTypeRequest dataTypeRequest = retrieveIndicatorDataCommon(indicatorCode, selectedRepresentations, selectedGranularities, includeObservationMetadata, srmRestObjectsMapper);
        dataTypeRequest.setGeoValuesOldVersionCompatibilityUtils(geoValuesOldVersionCompatibilityUtils);
        return do2TypeMapper.createDataTypeWithGeographicalCodes(dataTypeRequest, includeObservationMetadata, srmRestObjectsMapper.getGeographicalCodesByVariableElement());

    }

    private IndicatorsDataFilterVO getIndicatorsDataFilter(Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities,
            Map<String, String> geographicalValuesCodes) throws MetamacException {

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
