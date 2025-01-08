package es.gobcan.istac.indicators.rest.facadeimpl;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsExtendedVO;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataGeoDimensionFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataMeasureDimensionFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataTimeDimensionFilterVO;
import es.gobcan.istac.indicators.rest.ExcelMapper;
import es.gobcan.istac.indicators.rest.IndicatorsRestConstants;
import es.gobcan.istac.indicators.rest.clients.SrmRestInternalFacade;
import es.gobcan.istac.indicators.rest.facadeapi.GeographicalValuesRestFacade;
import es.gobcan.istac.indicators.rest.facadeapi.IndicatorRestFacade;
import es.gobcan.istac.indicators.rest.mapper.DataTypeRequest;
import es.gobcan.istac.indicators.rest.mapper.Do2TypeMapper;
import es.gobcan.istac.indicators.rest.mapper.IndicatorsRest2DoMapper;
import es.gobcan.istac.indicators.rest.serviceapi.IndicatorsApiService;
import es.gobcan.istac.indicators.rest.types.AttributeType;
import es.gobcan.istac.indicators.rest.types.DataDimensionType;
import es.gobcan.istac.indicators.rest.types.DataRepresentationType;
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

    protected Logger                logger                = LoggerFactory.getLogger(IndicatorRestFacadeImpl.class);

    @Autowired
    private Do2TypeMapper           do2TypeMapper;

    @Autowired
    protected IndicatorsApiService  indicatorsApiService;

    @Autowired
    private IndicatorsRest2DoMapper indicatorsRest2DoMapper;

    @Autowired
    private SrmRestInternalFacade   srmRestInternalFacade = null;

    @Autowired
    GeographicalValuesRestFacade    geographicalValuesRestFacade;

    @Autowired
    IndicatorsConfigurationService  configurationService;

    private ExcelMapper             excelMapper;

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

        GeographicalValuesOldVersionCompatibilityUtils geoValuesOldVersionCompatibilityUtils = getInformationForOldGeographicalValuesCompatibility(indicatorsVersions.getValues(), representation);

        // Fields filter. Only support for +metadata, +data
        if (fieldsToAdd.contains("+metadata")) {

            for (int i = 0; i < result.size(); i++) {
                IndicatorBaseType baseType = result.get(i);
                IndicatorVersion indicatorVersion = indicatorsVersions.getValues().get(i);

                MetadataType metadataType = new MetadataType();
                do2TypeMapper.indicatorDoToMetadataType(indicatorVersion, metadataType, geoValuesOldVersionCompatibilityUtils);

                baseType.setMetadata(metadataType);
            }
        }

        if (fieldsToAdd.contains("+data")) {
            boolean includeObservationsAttributes = fieldsToAdd.contains("+observationsMetadata");
            for (IndicatorBaseType indicatorType : result) {
                Map<String, List<String>> selectedGranularities = MapUtils.EMPTY_MAP;
                DataType dataType = retrieveIndicatorData(indicatorType.getCode(), representation, selectedGranularities, geoValuesOldVersionCompatibilityUtils, includeObservationsAttributes);
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

    private GeographicalValuesOldVersionCompatibilityUtils getInformationForOldGeographicalValuesCompatibility(List<IndicatorVersion> indicatorVersions, Map<String, List<String>> representation) {
        GeographicalValuesOldVersionCompatibilityUtils geoValuesOldVersionCompatibilityUtils = new GeographicalValuesOldVersionCompatibilityUtils(representation);
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
        IndicatorsDataFilterVO dataFilter = getIndicatorsDataFilter(selectedRepresentations, selectedGranularities);
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
            String format) throws MetamacException {
        try {
            byte[] content;
            DataType indicatorData = retrieveIndicatorData(indicatorCode, selectedRepresentations, selectedGranularities, false);

            switch (format.toLowerCase()) {
                case "csv":
                    content = convertJsonToText(indicatorData, ",").getBytes(StandardCharsets.UTF_8);
                    break;
                case "tsv":
                    content = convertJsonToText(indicatorData, "\t").getBytes(StandardCharsets.UTF_8);
                    break;
                case "xlsx":
                    content = generateXlsxFromJson(indicatorData);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported format: " + format);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", getContentDisposition(indicatorCode, format));

            return new ResponseEntity<>(content, headers, HttpStatus.OK);

        } catch (Exception e) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, indicatorCode);
        }
    }

    private String convertJsonToText(DataType indicatorData, String delimiter) {
        StringBuilder csvBuilder = new StringBuilder();

        List<String> headers = extractHeadersFromJson(indicatorData);
        csvBuilder.append(String.join(delimiter, headers)).append("\n");

        List<List<String>> rows = extractRowsFromJson(indicatorData);
        for (List<String> row : rows) {
            csvBuilder.append(String.join(delimiter, row)).append("\n");
        }

        return csvBuilder.toString();
    }
    private List<String> extractHeadersFromJson(DataType indicatorData) {
        List<String> headers = new ArrayList<>();

        if (indicatorData == null) {
            return headers;
        }

        Map<String, DataDimensionType> dimensions = indicatorData.getDimension();
        if (dimensions != null) {
            for (String dimensionKey : dimensions.keySet()) {
                DataDimensionType dimension = dimensions.get(dimensionKey);
                if (dimension != null && dimension.getRepresentation() != null) {
                    headers.add(dimensionKey);
                }
            }
        }

        headers.add("Observation");

        List<Map<String, AttributeType>> attributes = indicatorData.getAttribute();
        if (attributes != null) {
            for (Map<String, AttributeType> attributeMap : attributes) {
                if (attributeMap != null) {
                    for (Map.Entry<String, AttributeType> entry : attributeMap.entrySet()) {
                        headers.add(entry.getKey());
                    }
                }
            }
        }

        return headers;
    }

    private List<List<String>> extractRowsFromJson(DataType indicatorData) {
        List<List<String>> rows = new ArrayList<>();

        if (indicatorData == null) {
            return rows;
        }

        Map<String, DataDimensionType> dimensions = indicatorData.getDimension();
        List<String> dimensionKeys = dimensions != null ? new ArrayList<>(dimensions.keySet()) : new ArrayList<>();

        List<String> observations = indicatorData.getObservation();

        List<Map<String, AttributeType>> attributes = indicatorData.getAttribute();

        int rowCount = observations != null ? observations.size() : 0;

        for (int i = 0; i < rowCount; i++) {
            List<String> row = new ArrayList<>();

            if (dimensions != null) {
                for (String key : dimensionKeys) {
                    DataDimensionType dimension = dimensions.get(key);
                    if (dimension != null) {
                        DataRepresentationType representation = dimension.getRepresentation();
                        if (representation != null) {
                            String dimensionValue = "";
                            Map<String, Integer> indexMap = representation.getIndex();
                            if (indexMap != null) {
                                for (Map.Entry<String, Integer> entry : indexMap.entrySet()) {
                                    if (entry.getValue() == i) {
                                        dimensionValue = entry.getKey();
                                        break;
                                    }
                                }
                            }
                            row.add(dimensionValue);
                        } else {
                            row.add("");
                        }
                    } else {
                        row.add("");
                    }
                }
            }

            row.add(observations != null ? observations.get(i) : "");

            if (attributes != null && i < attributes.size()) {
                Map<String, AttributeType> attributeRow = attributes.get(i);
                if (attributeRow != null) {
                    for (AttributeType attribute : attributeRow.values()) {
                        String attributeValue = attribute != null && attribute.getValue() != null ? attribute.getValue().toString() : "";
                        row.add(attributeValue);
                    }
                } else {
                    row.add("");
                }
            }

            rows.add(row);
        }

        return rows;
    }
    private byte[] generateXlsxFromJson(DataType indicatorData) throws MetamacException {
        excelMapper = new ExcelMapper();

        List<String> headers = extractHeadersFromJson(indicatorData);
        Map<String, String> headerMap = createHeaderMap(headers);
        excelMapper.createHeaderRow(headerMap);

        List<List<String>> rows = extractRowsFromJson(indicatorData);
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
            // Si es necesario, asignar un valor al map, en este caso solo utilizo el mismo valor
            headerMap.put(header, header);
        }
        return headerMap;
    }

    private Map<String, String> createRowMap(List<String> rowData) {
        Map<String, String> rowMap = new HashMap<>();
        for (int i = 0; i < rowData.size(); i++) {
            rowMap.put("Column" + i, rowData.get(i)); // Crear una clave para cada columna, por ejemplo "Column0", "Column1", etc.
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

        DataTypeRequest dataTypeRequest = retrieveIndicatorDataCommon(indicatorCode, selectedRepresentations, selectedGranularities, includeObservationMetadata);
        return do2TypeMapper.createDataType(dataTypeRequest, includeObservationMetadata);
    }

    public DataTypeRequest retrieveIndicatorDataCommon(String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities,
            boolean includeObservationMetadata) throws MetamacException {
        IndicatorVersion indicatorVersion = retrieveIndicatorByCode(indicatorCode);

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
            GeographicalValuesOldVersionCompatibilityUtils geoValuesOldVersionCompatibilityUtils, boolean includeObservationMetadata) throws MetamacException {
        DataTypeRequest dataTypeRequest = retrieveIndicatorDataCommon(indicatorCode, selectedRepresentations, selectedGranularities, includeObservationMetadata);
        dataTypeRequest.setGeoValuesOldVersionCompatibilityUtils(geoValuesOldVersionCompatibilityUtils);
        return do2TypeMapper.createDataType(dataTypeRequest, includeObservationMetadata);

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
