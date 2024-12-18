package es.gobcan.istac.indicators.rest.facadeimpl;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.io.DeleteOnCloseFileInputStream;
import org.siemac.metamac.core.common.util.rest.RequestUtil;
import org.siemac.metamac.rest.enume.utils.ResourcesFormatOperation;
import org.siemac.metamac.rest.search.criteria.SculptorCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsExtendedVO;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataGeoDimensionFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataMeasureDimensionFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataTimeDimensionFilterVO;
import es.gobcan.istac.indicators.rest.IndicatorsRestConstants;
import es.gobcan.istac.indicators.rest.clients.SrmRestInternalFacade;
import es.gobcan.istac.indicators.rest.facadeapi.GeographicalValuesRestFacade;
import es.gobcan.istac.indicators.rest.facadeapi.IndicatorRestFacade;
import es.gobcan.istac.indicators.rest.mapper.DataTypeRequest;
import es.gobcan.istac.indicators.rest.mapper.Do2TypeMapper;
import es.gobcan.istac.indicators.rest.mapper.IndicatorsRest2DoMapper;
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
    public Response retrieveIndicatorDataXLSX(String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities) throws MetamacException {
        return retrieveIndicatorDataPlainText(indicatorCode, selectedRepresentations, selectedGranularities, "xlsx");
    }
    @Override
    public Response retrieveIndicatorDataCSV(String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities) throws MetamacException {
        return retrieveIndicatorDataPlainText(indicatorCode, selectedRepresentations, selectedGranularities, "csv");
    }
    @Override
    public Response retrieveIndicatorDataTSV(String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities) throws MetamacException {
        return retrieveIndicatorDataPlainText(indicatorCode, selectedRepresentations, selectedGranularities, "tsv");
    }

    private Response retrieveIndicatorDataPlainText(String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities, String format)
            throws MetamacException {
        try {
            return createPlainTextResourceAccess(indicatorCode, selectedRepresentations, selectedGranularities, "tsv");
        } catch (Exception e) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, indicatorCode);
        }
    }

    private Response createPlainTextResourceAccess(String indicatorCode, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities, String format)
            throws Exception {
        IndicatorVersion indicatorVersion = retrieveIndicatorByCode(indicatorCode);
        IndicatorsDataFilterVO dataFilter = getIndicatorsDataFilter(selectedRepresentations, selectedGranularities);
        IndicatorObservationsExtendedVO indicatorObservationsExtended = indicatorsApiService.findObservationsExtendedInIndicator(indicatorVersion.getIndicator().getUuid(), dataFilter);
        DataTypeRequest dataTypeRequest = retrieveIndicatorDataCommon(indicatorCode, selectedRepresentations, selectedGranularities, true);
        DataType indicatoDataType = do2TypeMapper.createDataType(dataTypeRequest, true);

        FileOutputStream outputStreamObservations = null;
        try {

            final File tmpFileObservations = File.createTempFile(indicatorCode, format);
            outputStreamObservations = new FileOutputStream(tmpFileObservations);

            return Response.ok(new DeleteOnCloseFileInputStream(tmpFileObservations), getMimeTypeFromFormat(format.toUpperCase()))
                    .header("Content-Disposition", getContentDisposition(indicatorCode, format)).build();
        } finally {
            IOUtils.closeQuietly(outputStreamObservations);
        }

    }

    private String getMimeTypeFromFormat(String type) {
        if (type == null) {
            return null;
        }
        ResourcesFormatOperation resourcesFormatOperation = new ResourcesFormatOperation();
        return resourcesFormatOperation.getMimeType(type.toUpperCase());

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
