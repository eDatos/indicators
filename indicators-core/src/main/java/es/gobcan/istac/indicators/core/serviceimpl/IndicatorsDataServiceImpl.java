package es.gobcan.istac.indicators.core.serviceimpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.map.ObjectMapper;
import org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.dataset.repository.dto.CodeDimensionDto;
import com.arte.statistic.dataset.repository.dto.DatasetRepositoryDto;
import com.arte.statistic.dataset.repository.dto.ObservationExtendedDto;
import com.arte.statistic.dataset.repository.service.DatasetRepositoriesServiceFacade;

import es.gobcan.istac.indicators.core.domain.Data;
import es.gobcan.istac.indicators.core.domain.DataContent;
import es.gobcan.istac.indicators.core.domain.DataDefinition;
import es.gobcan.istac.indicators.core.domain.DataStructure;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceVariableDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalValueDto;
import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceimpl.util.InvocationValidator;

/**
 * Implementation of IndicatorsDataService.
 */
@Service("indicatorsDataService")
public class IndicatorsDataServiceImpl extends IndicatorsDataServiceImplBase {
    
    private static final String GEO_DIMENSION = "GeographicalDimension";
    private static final String TIME_DIMENSION = "TimeDimension";
    private static final String MEASURE_DIMENSION = "MeasureDimension";
    
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
        InvocationValidator.checkRetrieveDataDefinition(uuid,null);
        
        // Find db
        DataDefinition dataDefinition = getDataGpeRepository().findCurrentDataDefinition(uuid);
        if (dataDefinition == null) {
            throw new MetamacException(ServiceExceptionType.DATA_DEFINITION_RETRIEVE_ERROR,uuid);
        }
        return dataDefinition;
    }

    @Override
    public DataStructure retrieveDataStructure(ServiceContext ctx, String uuid) throws MetamacException {
        //Validation
        InvocationValidator.checkRetrieveDataStructure(uuid,null);
        try {
            //Call jaxi for query structure
            String json = getIndicatorsDataProviderService().retrieveDataStructureJson(ctx, uuid);
            return jsonToDataStructure(json);
        } catch (Exception e) {
            throw new MetamacException(e, ServiceExceptionType.DATA_STRUCTURE_RETRIEVE_ERROR);
        }
    }

    @Override
    public void populateIndicatorData(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber) throws MetamacException {
        //Validation
        InvocationValidator.checkPopulateIndicatorData(indicatorUuid, indicatorVersionNumber, null);
        
        try {
            List<DataSourceDto> dataSources = getIndicatorsServiceFacade().retrieveDataSourcesByIndicator(ctx, indicatorUuid, indicatorVersionNumber);
            DatasetRepositoryDto datasetRepoDto = new DatasetRepositoryDto(); 
            datasetRepoDto.setDatasetId("dataset:" + UUID.randomUUID().toString());
            datasetRepoDto.setMaxAttributesObservation(1); //TODO: change when observation-level attributes are defined
            datasetRepoDto.getDimensions().add(GEO_DIMENSION);
            datasetRepoDto.getDimensions().add(TIME_DIMENSION);
            datasetRepoDto.getDimensions().add(MEASURE_DIMENSION);
            //TODO: CHANGE LANGUAGES
            List<String> languages = new ArrayList<String>();
            languages.add("es");
            datasetRepoDto.setLanguages(languages);
            
            datasetRepositoriesServiceFacade.createDatasetRepository(datasetRepoDto);
            
            List<ObservationExtendedDto> observations = new ArrayList<ObservationExtendedDto>();
            for (DataSourceDto dataSource : dataSources) {
                String json = getIndicatorsDataProviderService().retrieveDataJson(ctx, dataSource.getDataGpeUuid());
                Data data = jsonToData(json);
                
                List<String> geoValues = null;
                //Check whether geographical variable is fixed or not 
                if (dataSource.getGeographicalVariable() != null) {
                    geoValues = data.getValueCodes().get(dataSource.getGeographicalVariable());
                } else {
                    geoValues = new ArrayList<String>();
                }
                List<String> timeValues = null;
                //Check whether time variable is fixed or not 
                if (dataSource.getTimeVariable() != null) {
                    timeValues = data.getValueCodes().get(dataSource.getTimeVariable());
                } else {
                    timeValues = new ArrayList<String>();
                }
                
                if (geoValues.size() > 0 && timeValues.size() > 0) {
                    for (String geoVal : geoValues) {
                        for (String timeVal : timeValues) {
                            Map<String,String> varCodes = new HashMap<String,String>();
                            varCodes.put(dataSource.getGeographicalVariable(), geoVal);
                            varCodes.put(dataSource.getTimeVariable(),timeVal);
                            for (DataSourceVariableDto var : dataSource.getOtherVariables()) {
                                varCodes.put(var.getVariable(),var.getCategory());
                            }
                            DataContent dataContent = data.getDataContent(varCodes);
                            ObservationExtendedDto observation = new ObservationExtendedDto();
                            observation.addCodesDimension(new CodeDimensionDto(GEO_DIMENSION,geoVal));
                            observation.addCodesDimension(new CodeDimensionDto(TIME_DIMENSION,timeVal));
                            observation.addCodesDimension(new CodeDimensionDto(MEASURE_DIMENSION,MeasureDimensionTypeEnum.ABSOLUTE.name()));
                            observation.setPrimaryMeasure(dataContent.getValue());
                            observations.add(observation);
                        }
                    }
                } else if (geoValues.size() > 0) {
                    for (String geoVal : geoValues) {
                        Map<String,String> varCodes = new HashMap<String,String>();
                        varCodes.put(dataSource.getGeographicalVariable(), geoVal);
                        for (DataSourceVariableDto var : dataSource.getOtherVariables()) {
                            varCodes.put(var.getVariable(),var.getCategory());
                        }
                        DataContent dataContent = data.getDataContent(varCodes);  
                        ObservationExtendedDto observation = new ObservationExtendedDto();
                        observation.addCodesDimension(new CodeDimensionDto(GEO_DIMENSION,geoVal));
                        observation.addCodesDimension(new CodeDimensionDto(TIME_DIMENSION,dataSource.getTimeValue()));
                        observation.addCodesDimension(new CodeDimensionDto(MEASURE_DIMENSION,MeasureDimensionTypeEnum.ABSOLUTE.name()));
                        observation.setPrimaryMeasure(dataContent.getValue());
                        observations.add(observation);
                    }
                } else if (timeValues.size() > 0) {
                    for (String timeVal : timeValues) {
                        Map<String,String> varCodes = new HashMap<String,String>();
                        varCodes.put(dataSource.getTimeVariable(),timeVal);
                        for (DataSourceVariableDto var : dataSource.getOtherVariables()) {
                            varCodes.put(var.getVariable(),var.getCategory());
                        }
                        DataContent dataContent = data.getDataContent(varCodes);  
                        ObservationExtendedDto observation = new ObservationExtendedDto();
                        observation.addCodesDimension(new CodeDimensionDto(TIME_DIMENSION,timeVal));
                        GeographicalValueDto geoValDto = getIndicatorsServiceFacade().retrieveGeographicalValue(ctx, dataSource.getGeographicalValueUuid());
                        observation.addCodesDimension(new CodeDimensionDto(GEO_DIMENSION,geoValDto.getCode()));
                        observation.addCodesDimension(new CodeDimensionDto(MEASURE_DIMENSION,MeasureDimensionTypeEnum.ABSOLUTE.name()));
                        observation.setPrimaryMeasure(dataContent.getValue());
                        observations.add(observation);
                    }
                }
                else if (geoValues.size() == 0 && timeValues.size() == 0) {
                    Map<String,String> varCodes = new HashMap<String,String>();
                    for (DataSourceVariableDto var : dataSource.getOtherVariables()) {
                        varCodes.put(var.getVariable(),var.getCategory());
                    }
                    DataContent dataContent = data.getDataContent(varCodes);  
                    ObservationExtendedDto observation = new ObservationExtendedDto();
                    observation.addCodesDimension(new CodeDimensionDto(TIME_DIMENSION,dataSource.getTimeValue()));
                    GeographicalValueDto geoValDto = getIndicatorsServiceFacade().retrieveGeographicalValue(ctx, dataSource.getGeographicalValueUuid());
                    observation.addCodesDimension(new CodeDimensionDto(GEO_DIMENSION,geoValDto.getCode()));
                    observation.addCodesDimension(new CodeDimensionDto(MEASURE_DIMENSION,MeasureDimensionTypeEnum.ABSOLUTE.name()));
                    observation.setPrimaryMeasure(dataContent.getValue());
                    observations.add(observation);
                }
            }
            datasetRepositoriesServiceFacade.insertObservationsExtended(datasetRepoDto.getDatasetId(), observations);
            
        } catch (Exception e) {
            throw new MetamacException(e, ServiceExceptionType.UNKNOWN);
        }
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
