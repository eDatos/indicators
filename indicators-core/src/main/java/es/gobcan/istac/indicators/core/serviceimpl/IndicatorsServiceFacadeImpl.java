package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.ElementLevel;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemStructureDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemStateEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersiontTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.mapper.Do2DtoMapper;
import es.gobcan.istac.indicators.core.mapper.Dto2DoMapper;
import es.gobcan.istac.indicators.core.serviceimpl.util.InvocationValidator;

/**
 * Implementation of IndicatorServiceFacade.
 * TODO criteria y paginación en operaciones de búsqueda: find*
 */
@Service("indicatorsServiceFacade")
public class IndicatorsServiceFacadeImpl extends IndicatorsServiceFacadeImplBase {

    @Autowired
    private Do2DtoMapper do2DtoMapper;

    @Autowired
    private Dto2DoMapper dto2DoMapper;

    public IndicatorsServiceFacadeImpl() {
    }

    public IndicatorsSystemDto createIndicatorsSystem(ServiceContext ctx, IndicatorsSystemDto indicatorsSystemDto) throws MetamacException {

        // Transform
        IndicatorsSystem indicatorsSystem = dto2DoMapper.indicatorsSystemDtoToDo(ctx, indicatorsSystemDto, new IndicatorsSystem());
        IndicatorsSystemVersion draftVersion = dto2DoMapper.indicatorsSystemDtoToDo(ctx, indicatorsSystemDto);

        // Create
        IndicatorsSystemVersion indicatorsSystemVersionCreated = getIndicatorsSystemsService().createIndicatorsSystem(ctx, indicatorsSystem, draftVersion);

        // Transform to Dto
        indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersionCreated);
        return indicatorsSystemDto;
    }

    public IndicatorsSystemDto retrieveIndicatorsSystem(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {

        // Retrieve
        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().retrieveIndicatorsSystem(ctx, uuid, versionNumber);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
        return indicatorsSystemDto;
    }

    @Override
    public IndicatorsSystemDto retrieveIndicatorsSystemPublished(ServiceContext ctx, String uuid) throws MetamacException {

        // Retrieve
        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().retrieveIndicatorsSystemPublished(ctx, uuid);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
        return indicatorsSystemDto;
    }

    public IndicatorsSystemDto retrieveIndicatorsSystemByCode(ServiceContext ctx, String code, String versionNumber) throws MetamacException {

        // Retrieve
        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().retrieveIndicatorsSystemByCode(ctx, code, versionNumber);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
        return indicatorsSystemDto;
    }

    public IndicatorsSystemDto retrieveIndicatorsSystemPublishedByCode(ServiceContext ctx, String code) throws MetamacException {

        // Retrieve
        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().retrieveIndicatorsSystemPublishedByCode(ctx, code);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
        return indicatorsSystemDto;
    }

    @Override
    public IndicatorsSystemStructureDto retrieveIndicatorsSystemStructure(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {

        // Retrieve
        List<ElementLevel> elementsLevelFirstLevel = getIndicatorsSystemsService().retrieveIndicatorsSystemStructure(ctx, uuid, versionNumber);

        // Builds structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = new IndicatorsSystemStructureDto();
        indicatorsSystemStructureDto.setUuid(uuid);
        if (elementsLevelFirstLevel.size() != 0) {
            indicatorsSystemStructureDto.setVersionNumber(elementsLevelFirstLevel.get(0).getIndicatorsSystemVersion().getVersionNumber());
            indicatorsSystemStructureDto.getElements().addAll(do2DtoMapper.elementsLevelsDoToDto(elementsLevelFirstLevel));
        }
        return indicatorsSystemStructureDto;
    }

    public void deleteIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException {
        getIndicatorsSystemsService().deleteIndicatorsSystem(ctx, uuid);
    }

    public void updateIndicatorsSystem(ServiceContext ctx, IndicatorsSystemDto indicatorsSystemDto) throws MetamacException {

        // Retrieve version in production
        IndicatorsSystemVersion indicatorsSystemInProduction = retrieveIndicatorsSystemStateInProduction(ctx, indicatorsSystemDto.getUuid(), true);
        if (!indicatorsSystemInProduction.getVersionNumber().equals(indicatorsSystemDto.getVersionNumber())) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE, indicatorsSystemDto.getUuid(), new IndicatorsSystemStateEnum[]{IndicatorsSystemStateEnum.DRAFT,
                    IndicatorsSystemStateEnum.VALIDATION_REJECTED, IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, IndicatorsSystemStateEnum.DIFFUSION_VALIDATION});
        }

        // Validation
        InvocationValidator.checkUpdateIndicatorsSystem(indicatorsSystemDto, indicatorsSystemInProduction, null);

        // Transform
        // Note: attributes in indicatorSystem entity are non modifiables
        dto2DoMapper.indicatorsSystemDtoToDo(ctx, indicatorsSystemDto, indicatorsSystemInProduction);

        // Update
        getIndicatorsSystemsService().updateIndicatorsSystemVersion(ctx, indicatorsSystemInProduction);
    }

    @Override
    public void sendIndicatorsSystemToProductionValidation(ServiceContext ctx, String uuid) throws MetamacException {
        getIndicatorsSystemsService().sendIndicatorsSystemToProductionValidation(ctx, uuid);
    }

    @Override
    public void sendIndicatorsSystemToDiffusionValidation(ServiceContext ctx, String uuid) throws MetamacException {
        getIndicatorsSystemsService().sendIndicatorsSystemToDiffusionValidation(ctx, uuid);
    }

    @Override
    public void rejectIndicatorsSystemValidation(ServiceContext ctx, String uuid) throws MetamacException {
        getIndicatorsSystemsService().rejectIndicatorsSystemValidation(ctx, uuid);
    }

    @Override
    public void publishIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException {
        getIndicatorsSystemsService().publishIndicatorsSystem(ctx, uuid);
    }

    @Override
    public void archiveIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException {
        getIndicatorsSystemsService().archiveIndicatorsSystem(ctx, uuid);
    }

    @Override
    public IndicatorsSystemDto versioningIndicatorsSystem(ServiceContext ctx, String uuid, VersiontTypeEnum versionType) throws MetamacException {

        // Versioning
        IndicatorsSystemVersion indicatorsSystemVersionCreated = getIndicatorsSystemsService().versioningIndicatorsSystem(ctx, uuid, versionType);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersionCreated);
        return indicatorsSystemDto;
    }

    @Override
    public List<IndicatorsSystemDto> findIndicatorsSystems(ServiceContext ctx) throws MetamacException {

        // Find
        List<IndicatorsSystemVersion> indicatorsSystemsVersion = getIndicatorsSystemsService().findIndicatorsSystems(ctx);

        // Transform
        List<IndicatorsSystemDto> indicatorsSystemsDto = new ArrayList<IndicatorsSystemDto>();
        for (IndicatorsSystemVersion indicatorsSystemVersion : indicatorsSystemsVersion) {
            indicatorsSystemsDto.add(do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion));
        }

        return indicatorsSystemsDto;
    }

    @Override
    public List<IndicatorsSystemDto> findIndicatorsSystemsPublished(ServiceContext ctx) throws MetamacException {

        // Retrieve published
        List<IndicatorsSystemVersion> indicatorsSystemsVersion = getIndicatorsSystemsService().findIndicatorsSystemsPublished(ctx);

        // Transform
        List<IndicatorsSystemDto> indicatorsSystemsDto = new ArrayList<IndicatorsSystemDto>();
        for (IndicatorsSystemVersion indicatorsSystemVersion : indicatorsSystemsVersion) {
            indicatorsSystemsDto.add(do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion));
        }

        return indicatorsSystemsDto;
    }

    @Override
    public DimensionDto createDimension(ServiceContext ctx, String indicatorsSystemUuid, DimensionDto dimensionDto) throws MetamacException {

        // Transform
        Dimension dimension = dto2DoMapper.dimensionDtoToDo(ctx, dimensionDto);

        // Create dimension
        dimension = getIndicatorsSystemsService().createDimension(ctx, indicatorsSystemUuid, dimension);
        
        // Transform to Dto to return
        dimensionDto = do2DtoMapper.dimensionDoToDto(dimension);
        return dimensionDto;
    }

    @Override
    public DimensionDto retrieveDimension(ServiceContext ctx, String uuid) throws MetamacException {

        // Retrieve
        Dimension dimension = getIndicatorsSystemsService().retrieveDimension(ctx, uuid);

        // Transform
        DimensionDto dimensionDto = do2DtoMapper.dimensionDoToDto(dimension);
        return dimensionDto;
    }

    @Override
    public void deleteDimension(ServiceContext ctx, String uuid) throws MetamacException {
        getIndicatorsSystemsService().deleteDimension(ctx, uuid);
    }

    @Override
    public List<DimensionDto> findDimensions(ServiceContext ctx, String indicatorsSystemUuid, String indicatorsSystemVersion) throws MetamacException {

        // Retrieve dimensions
        List<Dimension> dimensions = getIndicatorsSystemsService().findDimensions(ctx, indicatorsSystemUuid, indicatorsSystemVersion);

        // Transform
        List<DimensionDto> dimensionsDto = new ArrayList<DimensionDto>();
        for (Dimension dimension : dimensions) {
            dimensionsDto.add(do2DtoMapper.dimensionDoToDto(dimension));
        }

        return dimensionsDto;
    }

    @Override
    public void updateDimension(ServiceContext ctx, DimensionDto dimensionDto) throws MetamacException {

        // Retrieve
        // TODO comprobar parámetros antes?
        Dimension dimension = getIndicatorsSystemsService().retrieveDimension(ctx, dimensionDto.getUuid());

        // Validation of parameters
        InvocationValidator.checkUpdateDimension(dimensionDto, dimension, null);

        // Check indicators system state
        IndicatorsSystemVersion indicatorsSystemVersion = dimension.getElementLevel().getIndicatorsSystemVersion();
        checkIndicatorsSystemVersionInProduction(indicatorsSystemVersion);

        // Transform and update
        dto2DoMapper.dimensionDtoToDo(ctx, dimensionDto, dimension);
        getIndicatorsSystemsService().updateDimension(ctx, dimension);
    }

    @Override
    public void updateDimensionLocation(ServiceContext ctx, String uuid, String parentTargetUuid, Long orderInLevel) throws MetamacException {
        getIndicatorsSystemsService().updateDimensionLocation(ctx, uuid, parentTargetUuid, orderInLevel);
    }

    @Override
    public IndicatorInstanceDto createIndicatorInstance(ServiceContext ctx, String indicatorsSystemUuid, IndicatorInstanceDto indicatorInstanceDto) throws MetamacException {

        // Transform
        IndicatorInstance indicatorInstance = dto2DoMapper.indicatorInstanceDtoToDo(ctx, indicatorInstanceDto);

        // Create indicator instance
        indicatorInstance = getIndicatorsSystemsService().createIndicatorInstance(ctx, indicatorsSystemUuid, indicatorInstance);
        
        // Transform to Dto to return
        indicatorInstanceDto = do2DtoMapper.indicatorInstanceDoToDto(indicatorInstance);
        return indicatorInstanceDto;
    }

    @Override
    public IndicatorInstanceDto retrieveIndicatorInstance(ServiceContext ctx, String uuid) throws MetamacException {

        // Retrieve
        IndicatorInstance indicatorInstance = getIndicatorsSystemsService().retrieveIndicatorInstance(ctx, uuid);

        // Transform
        IndicatorInstanceDto indicatorInstanceDto = do2DtoMapper.indicatorInstanceDoToDto(indicatorInstance);
        return indicatorInstanceDto;
    }

    @Override
    public void deleteIndicatorInstance(ServiceContext ctx, String uuid) throws MetamacException {
        getIndicatorsSystemsService().deleteIndicatorInstance(ctx, uuid);
    }

    @Override
    public List<IndicatorInstanceDto> findIndicatorsInstances(ServiceContext ctx, String indicatorsSystemUuid, String indicatorsSystemVersion) throws MetamacException {

        // Retrieve indicators instances
        List<IndicatorInstance> indicatorsInstances = getIndicatorsSystemsService().findIndicatorsInstances(ctx, indicatorsSystemUuid, indicatorsSystemVersion);

        // Transform
        List<IndicatorInstanceDto> indicatorsInstancesDto = new ArrayList<IndicatorInstanceDto>();
        for (IndicatorInstance indicatorInstance : indicatorsInstances) {
            indicatorsInstancesDto.add(do2DtoMapper.indicatorInstanceDoToDto(indicatorInstance));
        }

        return indicatorsInstancesDto;
    }

    @Override
    public void updateIndicatorInstance(ServiceContext ctx, IndicatorInstanceDto indicatorInstanceDto) throws MetamacException {

        // Retrieve
        // TODO comprobar parámetros antes?
        IndicatorInstance indicatorInstance = getIndicatorsSystemsService().retrieveIndicatorInstance(ctx, indicatorInstanceDto.getUuid());

        // Validation of parameters
        InvocationValidator.checkUpdateIndicatorInstance(indicatorInstanceDto, indicatorInstance, null);

        // Check indicators system state
        IndicatorsSystemVersion indicatorsSystemVersion = indicatorInstance.getElementLevel().getIndicatorsSystemVersion();
        checkIndicatorsSystemVersionInProduction(indicatorsSystemVersion);

        // Transform and update
        dto2DoMapper.indicatorInstanceDtoToDo(ctx, indicatorInstanceDto, indicatorInstance);
        getIndicatorsSystemsService().updateIndicatorInstance(ctx, indicatorInstance);
    }

    @Override
    public void updateIndicatorInstanceLocation(ServiceContext ctx, String uuid, String parentTargetUuid, Long orderInLevel) throws MetamacException {
        getIndicatorsSystemsService().updateIndicatorInstanceLocation(ctx, uuid, parentTargetUuid, orderInLevel);
    }

    public IndicatorDto createIndicator(ServiceContext ctx, IndicatorDto indicatorDto) throws MetamacException {

        // Transform
        Indicator indicator = dto2DoMapper.indicatorDtoToDo(ctx, indicatorDto, new Indicator());
        IndicatorVersion draftVersion = dto2DoMapper.indicatorDtoToDo(ctx, indicatorDto);

        // Create
        IndicatorVersion indicatorVersionCreated = getIndicatorsService().createIndicator(ctx, indicator, draftVersion);

        // Transform to Dto
        indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersionCreated);
        return indicatorDto;
    }

    public IndicatorDto retrieveIndicator(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {

        // Retrieve
        IndicatorVersion indicatorVersion = getIndicatorsService().retrieveIndicator(ctx, uuid, versionNumber);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersion);
        return indicatorDto;
    }

    @Override
    public IndicatorDto retrieveIndicatorPublished(ServiceContext ctx, String uuid) throws MetamacException {

        // Retrieve
        IndicatorVersion indicatorVersion = getIndicatorsService().retrieveIndicatorPublished(ctx, uuid);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersion);
        return indicatorDto;
    }

    public IndicatorDto retrieveIndicatorByCode(ServiceContext ctx, String code, String versionNumber) throws MetamacException {

        // Retrieve
        IndicatorVersion indicatorVersion = getIndicatorsService().retrieveIndicatorByCode(ctx, code, versionNumber);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersion);
        return indicatorDto;
    }

    public IndicatorDto retrieveIndicatorPublishedByCode(ServiceContext ctx, String code) throws MetamacException {

        // Retrieve
        IndicatorVersion indicatorVersion = getIndicatorsService().retrieveIndicatorPublishedByCode(ctx, code);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersion);
        return indicatorDto;
    }

    public void deleteIndicator(ServiceContext ctx, String uuid) throws MetamacException {
        getIndicatorsService().deleteIndicator(ctx, uuid);
    }

    public void updateIndicator(ServiceContext ctx, IndicatorDto indicatorDto) throws MetamacException {

        // Retrieve version in production
        IndicatorVersion indicatorInProduction = retrieveIndicatorStateInProduction(ctx, indicatorDto.getUuid(), true);
        if (!indicatorInProduction.getVersionNumber().equals(indicatorDto.getVersionNumber())) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_VERSION_WRONG_STATE, indicatorDto.getUuid(), indicatorDto.getVersionNumber());
        }

        // Validation
        InvocationValidator.checkUpdateIndicator(indicatorDto, indicatorInProduction, null);

        // Transform
        // Note: attributes in indicatorSystem entity are non modifiables
        dto2DoMapper.indicatorDtoToDo(ctx, indicatorDto, indicatorInProduction);

        // Update
        getIndicatorsService().updateIndicatorVersion(ctx, indicatorInProduction);
    }

    @Override
    public void sendIndicatorToProductionValidation(ServiceContext ctx, String uuid) throws MetamacException {
        getIndicatorsService().sendIndicatorToProductionValidation(ctx, uuid);
    }

    @Override
    public void sendIndicatorToDiffusionValidation(ServiceContext ctx, String uuid) throws MetamacException {
        getIndicatorsService().sendIndicatorToDiffusionValidation(ctx, uuid);
    }

    @Override
    public void rejectIndicatorValidation(ServiceContext ctx, String uuid) throws MetamacException {
        getIndicatorsService().rejectIndicatorValidation(ctx, uuid);
    }

    @Override
    public void publishIndicator(ServiceContext ctx, String uuid) throws MetamacException {
        getIndicatorsService().publishIndicator(ctx, uuid);
    }

    @Override
    public void archiveIndicator(ServiceContext ctx, String uuid) throws MetamacException {
        getIndicatorsService().archiveIndicator(ctx, uuid);
    }

    @Override
    public IndicatorDto versioningIndicator(ServiceContext ctx, String uuid, VersiontTypeEnum versionType) throws MetamacException {

        // Versioning
        IndicatorVersion indicatorVersionCreated = getIndicatorsService().versioningIndicator(ctx, uuid, versionType);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersionCreated);
        return indicatorDto;
    }

    @Override
    public List<IndicatorDto> findIndicators(ServiceContext ctx) throws MetamacException {

        // Find
        List<IndicatorVersion> indicatorsVersion = getIndicatorsService().findIndicators(ctx);

        // Transform
        List<IndicatorDto> indicatorsDto = new ArrayList<IndicatorDto>();
        for (IndicatorVersion indicatorVersion : indicatorsVersion) {
            indicatorsDto.add(do2DtoMapper.indicatorDoToDto(indicatorVersion));
        }

        return indicatorsDto;
    }

    @Override
    public List<IndicatorDto> findIndicatorsPublished(ServiceContext ctx) throws MetamacException {

        // Retrieve published
        List<IndicatorVersion> indicatorsVersion = getIndicatorsService().findIndicatorsPublished(ctx);

        // Transform
        List<IndicatorDto> indicatorsDto = new ArrayList<IndicatorDto>();
        for (IndicatorVersion indicatorVersion : indicatorsVersion) {
            indicatorsDto.add(do2DtoMapper.indicatorDoToDto(indicatorVersion));
        }

        return indicatorsDto;
    }

    @Override
    public DataSourceDto createDataSource(ServiceContext ctx, String indicatorUuid, DataSourceDto dataSourceDto) throws MetamacException {

        // Transform
        DataSource dataSource = dto2DoMapper.dataSourceDtoToDo(ctx, dataSourceDto);
        
        // Create
        dataSource = getIndicatorsService().createDataSource(ctx, indicatorUuid, dataSource);

        // Transform to Dto to return
        dataSourceDto = do2DtoMapper.dataSourceDoToDto(dataSource);
        return dataSourceDto;
    }

    @Override
    public DataSourceDto retrieveDataSource(ServiceContext ctx, String uuid) throws MetamacException {

        // Retrieve
        DataSource dataSource = getIndicatorsService().retrieveDataSource(ctx, uuid);

        // Transform
        DataSourceDto dataSourceDto = do2DtoMapper.dataSourceDoToDto(dataSource);
        return dataSourceDto;
    }

    @Override
    public void deleteDataSource(ServiceContext ctx, String uuid) throws MetamacException {
        getIndicatorsService().deleteDataSource(ctx, uuid);
    }

    @Override
    public List<DataSourceDto> findDataSources(ServiceContext ctx, String indicatorUuid, String indicatorVersion) throws MetamacException {

        // Retrieve dataSources
        List<DataSource> dataSources = getIndicatorsService().findDataSources(ctx, indicatorUuid, indicatorVersion);

        // Transform
        List<DataSourceDto> dataSourcesDto = new ArrayList<DataSourceDto>();
        for (DataSource dataSource : dataSources) {
            dataSourcesDto.add(do2DtoMapper.dataSourceDoToDto(dataSource));
        }

        return dataSourcesDto;
    }

    @Override
    public void updateDataSource(ServiceContext ctx, DataSourceDto dataSourceDto) throws MetamacException {

        // Transform and update
        DataSource dataSource = dto2DoMapper.dataSourceDtoToDo(ctx, dataSourceDto);
        
        // Update
        getIndicatorsService().updateDataSource(ctx, dataSource);
    }

    /**
     * Retrieves version of an indicators system in production
     */
    private IndicatorsSystemVersion retrieveIndicatorsSystemStateInProduction(ServiceContext ctx, String uuid, boolean throwsExceptionIfNotExistsInProduction) throws MetamacException {
        IndicatorsSystem indicatorsSystem = getIndicatorsSystemsService().retrieveIndicatorsSystemBorrar(ctx, uuid);
        if (indicatorsSystem.getProductionVersion() == null && !throwsExceptionIfNotExistsInProduction) {
            return null; // to throws an specific exception
        }
        if (indicatorsSystem.getProductionVersion() == null) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_IN_PRODUCTION_NOT_FOUND, uuid);
        }
        IndicatorsSystemVersion indicatorsSystemVersionProduction = getIndicatorsSystemsService().retrieveIndicatorsSystem(ctx, uuid, indicatorsSystem.getProductionVersion().getVersionNumber());
        return indicatorsSystemVersionProduction;
    }

    /**
     * Checks that the indicators system version is in any state in production
     */
    private void checkIndicatorsSystemVersionInProduction(IndicatorsSystemVersion indicatorsSystemVersion) throws MetamacException {
        IndicatorsSystemStateEnum state = indicatorsSystemVersion.getState();
        boolean inProduction = IndicatorsSystemStateEnum.DRAFT.equals(state) || IndicatorsSystemStateEnum.VALIDATION_REJECTED.equals(state)
                || IndicatorsSystemStateEnum.PRODUCTION_VALIDATION.equals(state) || IndicatorsSystemStateEnum.DIFFUSION_VALIDATION.equals(state);
        if (!inProduction) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE, indicatorsSystemVersion.getIndicatorsSystem().getUuid(), new IndicatorsSystemStateEnum[]{
                    IndicatorsSystemStateEnum.DRAFT, IndicatorsSystemStateEnum.VALIDATION_REJECTED, IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, IndicatorsSystemStateEnum.DIFFUSION_VALIDATION});

        }
    }
    
    /**
     * Retrieves version of an indicator in production
     */
    private IndicatorVersion retrieveIndicatorStateInProduction(ServiceContext ctx, String uuid, boolean throwsExceptionIfNotExistsInProduction) throws MetamacException {
        Indicator indicator = getIndicatorsService().retrieveIndicatorBorrar(ctx, uuid);
        if (indicator == null) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_FOUND, uuid);
        }
        if (indicator.getProductionVersion() == null && !throwsExceptionIfNotExistsInProduction) {
            return null; // to throws an specific exception
        }
        if (indicator.getProductionVersion() == null) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_IN_PRODUCTION_NOT_FOUND, uuid);
        }
        IndicatorVersion indicatorVersionProduction = getIndicatorsService().retrieveIndicator(ctx, uuid, indicator.getProductionVersion().getVersionNumber());
        return indicatorVersionProduction;
    }
}
