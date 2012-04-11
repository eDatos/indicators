package es.gobcan.istac.indicators.core.serviceimpl;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.map.ObjectMapper;
import org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.dataset.repository.dto.AttributeBasicDto;
import com.arte.statistic.dataset.repository.dto.CodeDimensionDto;
import com.arte.statistic.dataset.repository.dto.ConditionDimensionDto;
import com.arte.statistic.dataset.repository.dto.DatasetRepositoryDto;
import com.arte.statistic.dataset.repository.dto.InternationalStringDto;
import com.arte.statistic.dataset.repository.dto.LocalisedStringDto;
import com.arte.statistic.dataset.repository.dto.ObservationExtendedDto;
import com.arte.statistic.dataset.repository.service.DatasetRepositoriesServiceFacade;
import com.arte.statistic.dataset.repository.util.DtoUtils;

import es.gobcan.istac.indicators.core.domain.Data;
import es.gobcan.istac.indicators.core.domain.DataContent;
import es.gobcan.istac.indicators.core.domain.DataDefinition;
import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.DataSourceVariable;
import es.gobcan.istac.indicators.core.domain.DataStructure;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.Quantity;
import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationMethodTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationRoundingEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceimpl.util.DataOperation;
import es.gobcan.istac.indicators.core.serviceimpl.util.InvocationValidator;
import es.gobcan.istac.indicators.core.serviceimpl.util.TimeVariableUtils;

/**
 * Implementation of IndicatorsDataService.
 */
@Service("indicatorsDataService")
public class IndicatorsDataServiceImpl extends IndicatorsDataServiceImplBase {

    public static final String               GEO_DIMENSION     = "GEOGRAPHIC";
    public static final String               TIME_DIMENSION    = "TIME";
    public static final String               MEASURE_DIMENSION = "MEASURE";
    public static final String               CODE_ATTR         = "CODE";
    public static final String               CODE_ATTR_LOC     = "es";
    public static final String               OBS_CONF_ATTR     = "OBS_CONF";
    public static final String               OBS_CONF_LOC      = "es";

    public static final String               DOT_UNAVAILABLE   = "..";
    public static final Double               ZERO_RANGE        = 1E-6;

    private static final Map<String, String> DOT_NOTATION_MAPPING;

    static {
        DOT_NOTATION_MAPPING = new HashMap<String, String>();
        DOT_NOTATION_MAPPING.put(".", "No procede");
        DOT_NOTATION_MAPPING.put("..", "Dato no disponible");
        DOT_NOTATION_MAPPING.put("...", "Dato oculto por impreciso o baja calidad");
        DOT_NOTATION_MAPPING.put("....", "Dato oculto por secreto estadístico");
        DOT_NOTATION_MAPPING.put(".....", "Dato incluido en otra categoría");
        DOT_NOTATION_MAPPING.put("......", "Dato no disponible por vacaciones o festivos");
    }

    @Autowired
    private DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade;

    public IndicatorsDataServiceImpl() {
    }

    @Override
    public List<DataDefinition> retrieveDataDefinitions(ServiceContext ctx) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveDataDefinitions(null);

        // Find db
        List<DataDefinition> dataDefinitions = getDataGpeRepository().findCurrentDataDefinitions();
        return dataDefinitions;
    }

    @Override
    public DataDefinition retrieveDataDefinition(ServiceContext ctx, String uuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveDataDefinition(uuid, null);

        // Find db
        DataDefinition dataDefinition = getDataGpeRepository().findCurrentDataDefinition(uuid);
        if (dataDefinition == null) {
            throw new MetamacException(ServiceExceptionType.DATA_DEFINITION_RETRIEVE_ERROR, uuid);
        }
        return dataDefinition;
    }

    @Override
    public DataStructure retrieveDataStructure(ServiceContext ctx, String uuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveDataStructure(uuid, null);
        try {
            // Call jaxi for query structure
            String json = getIndicatorsDataProviderService().retrieveDataStructureJson(ctx, uuid);
            return jsonToDataStructure(json);
        } catch (Exception e) {
            throw new MetamacException(e,ServiceExceptionType.DATA_STRUCTURE_RETRIEVE_ERROR, uuid);
        }
    }

    @Override
    public void populateIndicatorData(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber) throws MetamacException {
        // Validation
        InvocationValidator.checkPopulateIndicatorData(indicatorUuid, indicatorVersionNumber, null);

        DatasetRepositoryDto datasetRepoDto = null;
        try {
            IndicatorVersion indicatorVersion = getIndicatorsService().retrieveIndicator(ctx, indicatorUuid, indicatorVersionNumber);
            List<DataSource> dataSources = getIndicatorsService().retrieveDataSourcesByIndicator(ctx, indicatorUuid, indicatorVersionNumber);
            
            // Transform list to process first load operations
            List<DataOperation> dataOps = transformDataSourcesForProcessing(dataSources);
            
            datasetRepoDto = createDatasetRepositoryDefinition(indicatorUuid, indicatorVersionNumber);

            // Data will be stored in a map (cache), because the same json can be requested many times
            Map<String, Data> dataCache = new HashMap<String, Data>();
            
            //Process observations for each dataOperation
            for (DataOperation dataOperation : dataOps) {
                Data data = retrieveData(ctx, dataOperation, dataCache);
                List<ObservationExtendedDto> observations = createObservationsFromDataOperationData(dataOperation, data, datasetRepoDto.getDatasetId());
                datasetRepositoriesServiceFacade.insertObservationsExtended(datasetRepoDto.getDatasetId(), observations);
            }
            // Replace the whole dataset
            replaceDatasetRepository(ctx, indicatorVersion, datasetRepoDto);
        } catch (MetamacException e) {
            deleteDatasetRepositoryIfExists(datasetRepoDto);
            throw e;
        } catch (Exception e) {
            deleteDatasetRepositoryIfExists(datasetRepoDto);
            throw new MetamacException(e,ServiceExceptionType.DATA_POPULATE_ERROR, indicatorUuid, indicatorVersionNumber);
        }
    }
    
    private void replaceDatasetRepository(ServiceContext ctx, IndicatorVersion indicatorVersion, DatasetRepositoryDto datasetRepoDto) throws MetamacException {
        String oldDatasetId = indicatorVersion.getDataRepositoryId();
        indicatorVersion.setDataRepositoryId(datasetRepoDto.getDatasetId());
        indicatorVersion.setDataRepositoryTableName(datasetRepoDto.getTableName());
        getIndicatorsService().updateIndicatorVersion(ctx, indicatorVersion);
        if (oldDatasetId != null) {
            try {
                datasetRepositoriesServiceFacade.deleteDatasetRepository(oldDatasetId);
            } catch (ApplicationException e) {
                //TODO: Log warning level showing that old data source could not be deleted
            }
        }
    }
    
    private List<ObservationExtendedDto> createObservationsFromDataOperationData(DataOperation dataOperation, Data data, String datasetId ) throws MetamacException {
     // Get geographic and time values
        List<String> geoValues = getGeographicValue(dataOperation, data);
        List<String> timeValues = getTimeValue(dataOperation, data);
        
        List<ObservationExtendedDto> observations = new ArrayList<ObservationExtendedDto>();
        for (String geoVal : geoValues) {
            for (String timeVal : timeValues) {
                // Map for querying the data from the json
                Map<String, String> varCodesForQuery = new HashMap<String, String>();

                // Only include geographical and time values if a variable has been selected
                if (dataOperation.hasGeographicalVariable()) {
                    varCodesForQuery.put(dataOperation.getGeographicalVariable(), geoVal);
                }
                if (dataOperation.hasTimeVariable()) {
                    varCodesForQuery.put(dataOperation.getTimeVariable(), timeVal);
                }
                for (DataSourceVariable var : dataOperation.getOtherVariables()) {
                    varCodesForQuery.put(var.getVariable(), var.getCategory());
                }

                ObservationExtendedDto observation = null;
                if (RateDerivationMethodTypeEnum.LOAD.equals(dataOperation.getMethodType())) {
                    observation = getObservationValue(dataOperation, data, varCodesForQuery, geoVal, timeVal);
                } else if (RateDerivationMethodTypeEnum.CALCULATE.equals(dataOperation.getMethodType())) {
                    observation = getCalculatedValue(dataOperation, datasetId, geoVal, timeVal);
                } else {
                    throw new MetamacException(ServiceExceptionType.DATA_POPULATE_UNKNOWN_METHOD_TYPE, dataOperation.getMethodType());
                }
                observations.add(observation);
            }
        }
        return observations;
    }
    
    private void deleteDatasetRepositoryIfExists(DatasetRepositoryDto datasetRepositoryDto) {
        try {
            if (datasetRepositoryDto != null && datasetRepositoryDto.getDatasetId() != null) {
                datasetRepositoriesServiceFacade.deleteDatasetRepository(datasetRepositoryDto.getDatasetId());
            }
        } catch (ApplicationException e) {
            //TODO: Log warning level showing that new failing data source could not be deleted
        }
    }
    
    /*
     * Retrieve data from IndicatorDataProvider
     * The data is not always asked to the service, sometimes the requested data is in the data cache.
     */
    private Data retrieveData(ServiceContext ctx, DataOperation dataOperation, Map<String,Data> dataCache) throws MetamacException {
        try {
            Data data = dataCache.get(dataOperation.getDataGpeUuid());
            if (data == null) {
                String json = getIndicatorsDataProviderService().retrieveDataJson(ctx, dataOperation.getDataGpeUuid());
                if (json == null) {
                    throw new MetamacException(ServiceExceptionType.DATA_POPULATE_RETRIEVE_DATA_EMPTY, dataOperation.getDataGpeUuid(),dataOperation.getDataSourceUuid());
                }
                data = jsonToData(json);
                dataCache.put(dataOperation.getDataGpeUuid(), data);
            }
            return data;
        } catch (MetamacException e) {
            throw e;
        } catch (Exception e) {
            throw new MetamacException(e,ServiceExceptionType.DATA_POPULATE_RETRIEVE_DATA_ERROR,dataOperation.getDataGpeUuid(),dataOperation.getDataSourceUuid());
        }
    }

    /*
     * Returns the geographic value whether a geographical variable has been selected or a value
     * has been fixed
     */
    private List<String> getGeographicValue(DataOperation dataOperation, Data data) {
        List<String> geoValues = null;
        // Check whether geographical variable is fixed or not
        if (dataOperation.hasGeographicalVariable()) {
            geoValues = data.getValueCodes().get(dataOperation.getGeographicalVariable());
        } else {
            geoValues = new ArrayList<String>();
            GeographicalValue geoVal = dataOperation.getGeographicalValue();
            geoValues.add(geoVal.getCode());
        }
        return geoValues;
    }

    /*
     * Returns the time value whether a geographical variable has been selected or a value
     * has been fixed
     */
    private List<String> getTimeValue(DataOperation dataOperation, Data data) {
        List<String> timeValues = null;
        // Check whether time variable is fixed or not
        if (dataOperation.getTimeVariable() != null) {
            timeValues = data.getValueCodes().get(dataOperation.getTimeVariable());
        } else {
            timeValues = new ArrayList<String>();
            timeValues.add(dataOperation.getTimeValue());
        }
        return timeValues;
    }

    /*
     * Creates Indicator's basic structure in dataset repository
     */
    private DatasetRepositoryDto createDatasetRepositoryDefinition(String indicatorUuid, String indicatorVersion) throws MetamacException {
        DatasetRepositoryDto datasetRepoDto = new DatasetRepositoryDto();
        datasetRepoDto.setDatasetId("dataset:" + UUID.randomUUID().toString());
        datasetRepoDto.setMaxAttributesObservation(2);
        datasetRepoDto.getDimensions().add(GEO_DIMENSION);
        datasetRepoDto.getDimensions().add(TIME_DIMENSION);
        datasetRepoDto.getDimensions().add(MEASURE_DIMENSION);

        List<String> languages = new ArrayList<String>();
        languages.add("es");
        datasetRepoDto.setLanguages(languages);

        try {
            datasetRepoDto = datasetRepositoriesServiceFacade.createDatasetRepository(datasetRepoDto);
        } catch (ApplicationException e) {
            throw new MetamacException(ServiceExceptionType.DATA_POPULATE_DATASETREPO_CREATE_ERROR, indicatorUuid, indicatorVersion);
        }
        return datasetRepoDto;
    }

    private ObservationExtendedDto getObservationValue(DataOperation dataOperation, Data data, Map<String, String> varCodes, String geoValue, String timeValue) throws MetamacException {
        DataContent content = getValue(dataOperation, data, varCodes);
        String value = content.getValue();

        ObservationExtendedDto observation = new ObservationExtendedDto();
        observation.addCodesDimension(new CodeDimensionDto(GEO_DIMENSION, geoValue));
        observation.addCodesDimension(new CodeDimensionDto(TIME_DIMENSION, timeValue));
        observation.addCodesDimension(new CodeDimensionDto(MEASURE_DIMENSION, dataOperation.getMeasureDimension().name()));
        observation.addAttribute(createAttribute(CODE_ATTR, CODE_ATTR_LOC, dataOperation.getDataSourceUuid()));

        // Check for dotted notation
        if (isDottedString(value)) {
            observation.addAttribute(createAttribute(OBS_CONF_ATTR, OBS_CONF_LOC, getDotNotationMeaning(value)));
            observation.setPrimaryMeasure(null);
        } else {
            observation.setPrimaryMeasure(value);
        }
        return observation;
    }

    /*
     * Get value has to take a look to method type and methd
     * For LOAD method type method can be OBS_VALUE or A category name for ContVariable
     */
    private DataContent getValue(DataOperation dataOperation, Data data, Map<String, String> varCodes) throws MetamacException {
        Map<String, String> codes = new HashMap<String, String>(varCodes);
        if (data.hasContVariable()) {
            if (DataSourceDto.OBS_VALUE.equals(dataOperation.getMethod())) { // INCOMPATIBLE DataSource, got obs_value but the query has contvariable
                throw new MetamacException(ServiceExceptionType.DATA_POPULATE_INVALID_CONTVARIABLE_LOAD_METHOD, dataOperation.getMethod());
            } else {
                List<String> contVarCodes = data.getValueCodes().get(data.getContVariable());
                if (contVarCodes.contains(dataOperation.getMethod())) {
                    codes.put(data.getContVariable(), dataOperation.getMethod());
                    return data.getDataContent(codes);
                } else {
                    throw new MetamacException(ServiceExceptionType.DATA_POPULATE_INVALID_CONTVARIABLE_LOAD_METHOD, dataOperation.getMethod());
                }
            }
        } else {
            if (DataSourceDto.OBS_VALUE.equals(dataOperation.getMethod())) {
                return data.getDataContent(codes);
            } else {
                throw new MetamacException(ServiceExceptionType.DATA_POPULATE_INVALID_NOCONTVARIABLE_LOAD_METHOD, dataOperation.getMethod());
            }
        }
    }

    /*
     * Get value has to take a look to method type and methd
     * For LOAD method type method can be OBS_VALUE or A category name for ContVariable
     */
    private ObservationExtendedDto getCalculatedValue(DataOperation dataOperation, String datasetId, String geoValue, String timeValue) throws MetamacException {
        String previousTimeValue = null;
        if (dataOperation.isAnnualMethod()) {
            previousTimeValue = TimeVariableUtils.calculatePreviousYearTimeValue(timeValue);
        } else if (dataOperation.isInterPeriodMethod()) {
            previousTimeValue = TimeVariableUtils.calculatePreviousTimeValue(timeValue);
        } else {
            throw new MetamacException(ServiceExceptionType.DATA_POPULATE_UNKNOWN_METHOD_TYPE, dataOperation.getMethodType());
        }

        ConditionDimensionDto geoConditionDto = createCondition(GEO_DIMENSION,geoValue);
        ConditionDimensionDto timeConditionDto = createCondition(TIME_DIMENSION, previousTimeValue, timeValue);
        ConditionDimensionDto measureConditionDto = createCondition(MEASURE_DIMENSION, MeasureDimensionTypeEnum.ABSOLUTE.name());
        List<ConditionDimensionDto> conditions = Arrays.asList(geoConditionDto, timeConditionDto, measureConditionDto);
        Map<String, ObservationExtendedDto> observationsMap = null;
        try {
            observationsMap = datasetRepositoriesServiceFacade.findObservationsExtendedByDimensions(datasetId, conditions);
        } catch (ApplicationException e) {
            throw new MetamacException(e,ServiceExceptionType.DATA_POPULATE_DATASETREPO_FIND_ERROR);
        }
        
        String keyCurrentTimeObs = generateObservationUniqueKey(geoValue, timeValue, MeasureDimensionTypeEnum.ABSOLUTE.name());
        String keyPreviousTimeObs = generateObservationUniqueKey(geoValue, previousTimeValue, MeasureDimensionTypeEnum.ABSOLUTE.name());

        ObservationExtendedDto currentObs = observationsMap.get(keyCurrentTimeObs);
        ObservationExtendedDto previousObs = observationsMap.get(keyPreviousTimeObs);

        
        //Create base for observation
        ObservationExtendedDto observation = new ObservationExtendedDto();
        observation.addCodesDimension(new CodeDimensionDto(GEO_DIMENSION, geoValue));
        observation.addCodesDimension(new CodeDimensionDto(TIME_DIMENSION, timeValue));
        observation.addCodesDimension(new CodeDimensionDto(MEASURE_DIMENSION, dataOperation.getMeasureDimension().name()));
        observation.addAttribute(createAttribute(CODE_ATTR, CODE_ATTR_LOC, dataOperation.getDataSourceUuid()));
        
        /* Some observations were not found*/
        if (currentObs == null || previousObs == null) {
            observation.setPrimaryMeasure(null);
            observation.addAttribute(createAttribute(OBS_CONF_ATTR, OBS_CONF_LOC, getDotNotationMeaning(DOT_UNAVAILABLE)));
            return observation;
        }
        
        /* Some observations have dot notation values */
        if (currentObs.getPrimaryMeasure() == null || previousObs.getPrimaryMeasure() == null) {
            observation.setPrimaryMeasure(null);
            observation.addAttribute(mergeObsConfAttribute(currentObs, previousObs));
            return observation;
        }  else { //Calculate
            Double currentValue = null;
            Double previousValue = null;
            try {
                //Data format is: 5.300,05 it should be => 5300.05
                String currentValStr = currentObs.getPrimaryMeasure().replace(".", "").replace(",", ".");
                String previousValStr = previousObs.getPrimaryMeasure().replace(".", "").replace(",", ".");
                currentValue = Double.parseDouble(currentValStr);
                previousValue = Double.parseDouble(previousValStr);
            } catch (NumberFormatException e) {
                throw new MetamacException(ServiceExceptionType.DATA_POPULATE_OBSERVATION_CALCULATE_ERROR,currentObs.getPrimaryMeasure(),previousObs.getPrimaryMeasure());
            }
            Double calculatedValue = null;
            if (dataOperation.isPercentageMethod()) {
                if (Math.abs(previousValue) < ZERO_RANGE) {
                    observation.setPrimaryMeasure(null);
                    observation.addAttribute(createAttribute(OBS_CONF_ATTR, OBS_CONF_LOC, getDotNotationMeaning(DOT_UNAVAILABLE)));
                    return observation;
                }
                Quantity quantity = dataOperation.getQuantity();
                calculatedValue = ((currentValue - previousValue)/previousValue) * quantity.getUnitMultiplier();
            } else if (dataOperation.isPuntualMethod()) {
                calculatedValue = currentValue - previousValue;
            } else {
                throw new MetamacException(ServiceExceptionType.DATA_POPULATE_UNKNOWN_METHOD_TYPE, dataOperation.getMethodType());
            }
            String formattedValue = formatCalculatedValue(calculatedValue, dataOperation);
            observation.setPrimaryMeasure(formattedValue);
        }
        return observation;
    }
    private String formatCalculatedValue(Double value, DataOperation dataOperation) {
        DecimalFormat formatter = new DecimalFormat();
        if (RateDerivationRoundingEnum.UPWARD.equals(dataOperation.getRateRounding())) {
            formatter.setRoundingMode(RoundingMode.HALF_UP);
        } else if (RateDerivationRoundingEnum.DOWN.equals(dataOperation.getRateRounding())) {
            formatter.setRoundingMode(RoundingMode.HALF_DOWN);
        }
        formatter.setMaximumFractionDigits(dataOperation.getQuantity().getDecimalPlaces());
        formatter.setMinimumFractionDigits(dataOperation.getQuantity().getDecimalPlaces());
        return formatter.format(value);
    }

    /*
     * Transform list of datasources to a list of simpler operations, sorted by precedence
     * Load operations must be executed prior calculated operation.
     */
    private List<DataOperation> transformDataSourcesForProcessing(List<DataSource> dataSources) {
        LinkedList<DataOperation> dataOperations = new LinkedList<DataOperation>();
        // Load operations are posisionated first
        for (DataSource dataSource : dataSources) {
            if (dataSource.getAbsoluteMethod() != null) {
                DataOperation dataOp = new DataOperation(dataSource, MeasureDimensionTypeEnum.ABSOLUTE);
                dataOperations.addFirst(dataOp);
            }
            if (dataSource.getAnnualPercentageRate() != null) {
                DataOperation dataOp = new DataOperation(dataSource, MeasureDimensionTypeEnum.ANNUAL_PERCENTAGE_RATE);
                if (RateDerivationMethodTypeEnum.LOAD.equals(dataOp.getMethodType())) {
                    dataOperations.addFirst(dataOp);
                } else {
                    dataOperations.addLast(dataOp);
                }
            }
            if (dataSource.getAnnualPuntualRate() != null) {
                DataOperation dataOp = new DataOperation(dataSource, MeasureDimensionTypeEnum.ANNUAL_PUNTUAL_RATE);
                if (RateDerivationMethodTypeEnum.LOAD.equals(dataOp.getMethodType())) {
                    dataOperations.addFirst(dataOp);
                } else {
                    dataOperations.addLast(dataOp);
                }
            }
            if (dataSource.getInterperiodPercentageRate() != null) {
                DataOperation dataOp = new DataOperation(dataSource, MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE);
                if (RateDerivationMethodTypeEnum.LOAD.equals(dataOp.getMethodType())) {
                    dataOperations.addFirst(dataOp);
                } else {
                    dataOperations.addLast(dataOp);
                }
            }
            if (dataSource.getInterperiodPuntualRate() != null) {
                DataOperation dataOp = new DataOperation(dataSource, MeasureDimensionTypeEnum.INTERPERIOD_PUNTUAL_RATE);
                if (RateDerivationMethodTypeEnum.LOAD.equals(dataOp.getMethodType())) {
                    dataOperations.addFirst(dataOp);
                } else {
                    dataOperations.addLast(dataOp);
                }
            }
        }
        return dataOperations;
    }

    /*
     * Gets dot convention descriptions
     */
    public static String getDotNotationMeaning(String dottedStr) {
        return DOT_NOTATION_MAPPING.get(dottedStr);
    }

    private boolean isDottedString(String str) {
        return getDotNotationMeaning(str) != null;
    }

    /*
     * Helper for attributes
     */
    private AttributeBasicDto createAttribute(String id, String locale, String value) {
        AttributeBasicDto attributeBasicDto = new AttributeBasicDto();
        attributeBasicDto.setAttributeId(id);
        InternationalStringDto intStr = new InternationalStringDto();
        LocalisedStringDto locStr = new LocalisedStringDto();
        locStr.setLocale(locale);
        locStr.setLabel(value);
        intStr.addText(locStr);
        attributeBasicDto.setValue(intStr);
        return attributeBasicDto;
    }

    /*
     * Helper for merge Two OBS_CONF attributes into one
     */
    private AttributeBasicDto mergeObsConfAttribute(ObservationExtendedDto obs1, ObservationExtendedDto obs2) {
        if (obs1.getPrimaryMeasure() == null && obs2.getPrimaryMeasure() == null) {
            String desc1 = getAttribute(OBS_CONF_ATTR, obs1).getValue().getLocalisedLabel(OBS_CONF_LOC);
            String desc2 = getAttribute(OBS_CONF_ATTR, obs2).getValue().getLocalisedLabel(OBS_CONF_LOC);
            return createAttribute(OBS_CONF_ATTR, OBS_CONF_LOC, desc1 + ", " + desc2);
        } else if (obs1.getPrimaryMeasure() == null) {
            String desc1 = getAttribute(OBS_CONF_ATTR, obs1).getValue().getLocalisedLabel(OBS_CONF_LOC);
            return createAttribute(OBS_CONF_ATTR, OBS_CONF_LOC, desc1);
        } else if (obs2.getPrimaryMeasure() == null) {
            String desc2 = getAttribute(OBS_CONF_ATTR, obs2).getValue().getLocalisedLabel(OBS_CONF_LOC);
            return createAttribute(OBS_CONF_ATTR, OBS_CONF_LOC, desc2);
        }
        return null;
    }

    /*
     * Helper for ConditionDimension
     */
    private ConditionDimensionDto createCondition(String dimensionId, String... codeDimensions) {
        ConditionDimensionDto condition = new ConditionDimensionDto();
        condition.setDimensionId(dimensionId);
        condition.setCodesDimension(Arrays.asList(codeDimensions));
        return condition;
    }

    /*
     * Helper for generate id for observation maps
     */
    private String generateObservationUniqueKey(String geoValue, String timeValue, String measureValue) {
        CodeDimensionDto geoDimCode = new CodeDimensionDto(GEO_DIMENSION, geoValue);
        CodeDimensionDto timeDimCode = new CodeDimensionDto(TIME_DIMENSION, timeValue);
        CodeDimensionDto measureDimCode = new CodeDimensionDto(MEASURE_DIMENSION, measureValue);
        return DtoUtils.generateUniqueKey(Arrays.asList(geoDimCode, timeDimCode, measureDimCode));
    }

    /*
     * Helper to get certain attribute from an observation
     */
    private AttributeBasicDto getAttribute(String attributeId, ObservationExtendedDto observation) {
        for (AttributeBasicDto attr : observation.getAttributes()) {
            if (OBS_CONF_ATTR.equals(attr.getAttributeId())) {
                return attr;
            }
        }
        return null;
    }

    /*
     * Private methods that get info from jaxi
     */
    private DataStructure jsonToDataStructure(String json) throws Exception {
        DataStructure target = new DataStructure();
        ObjectMapper mapper = new ObjectMapper();
        target = mapper.readValue(json, DataStructure.class);
        return target;
    }

    /*
     * Private methods that get data from jaxi
     */
    private Data jsonToData(String json) throws Exception {
        Data target = new Data();
        ObjectMapper mapper = new ObjectMapper();
        target = mapper.readValue(json, Data.class);
        return target;
    }
}
