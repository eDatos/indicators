package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.criteria.IndicatorsCriteria;
import es.gobcan.istac.indicators.core.domain.DataDefinition;
import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.DataStructure;
import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.ElementLevel;
import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.QuantityUnit;
import es.gobcan.istac.indicators.core.domain.Subject;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataDefinitionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataStructureDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemStructureDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.SubjectDto;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;
import es.gobcan.istac.indicators.core.mapper.Do2DtoMapper;
import es.gobcan.istac.indicators.core.mapper.Dto2DoMapper;
import es.gobcan.istac.indicators.core.repositoryimpl.finders.SubjectIndicatorResult;

/**
 * Implementation of IndicatorServiceFacade.
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
        IndicatorsSystemVersion indicatorsSystemVersion = dto2DoMapper.indicatorsSystemDtoToDo(ctx, indicatorsSystemDto);

        // Create
        IndicatorsSystemVersion indicatorsSystemVersionCreated = getIndicatorsSystemsService().createIndicatorsSystem(ctx, indicatorsSystemVersion);

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

    @Override
    public IndicatorsSystemDto sendIndicatorsSystemToProductionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().sendIndicatorsSystemToProductionValidation(ctx, uuid);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
        return indicatorsSystemDto;
    }

    @Override
    public IndicatorsSystemDto sendIndicatorsSystemToDiffusionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().sendIndicatorsSystemToDiffusionValidation(ctx, uuid);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
        return indicatorsSystemDto;
    }

    @Override
    public IndicatorsSystemDto rejectIndicatorsSystemValidation(ServiceContext ctx, String uuid) throws MetamacException {

        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().rejectIndicatorsSystemValidation(ctx, uuid);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
        return indicatorsSystemDto;
    }

    @Override
    public IndicatorsSystemDto publishIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException {

        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().publishIndicatorsSystem(ctx, uuid);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
        return indicatorsSystemDto;
    }

    @Override
    public IndicatorsSystemDto archiveIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException {

        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().archiveIndicatorsSystem(ctx, uuid);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
        return indicatorsSystemDto;
    }

    @Override
    public IndicatorsSystemDto versioningIndicatorsSystem(ServiceContext ctx, String uuid, VersionTypeEnum versionType) throws MetamacException {

        // Versioning
        IndicatorsSystemVersion indicatorsSystemVersionCreated = getIndicatorsSystemsService().versioningIndicatorsSystem(ctx, uuid, versionType);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersionCreated);
        return indicatorsSystemDto;
    }

    @Override
    public List<IndicatorsSystemDto> findIndicatorsSystems(ServiceContext ctx, IndicatorsCriteria criteria) throws MetamacException {

        // Find
        List<IndicatorsSystemVersion> indicatorsSystemsVersion = getIndicatorsSystemsService().findIndicatorsSystems(ctx, criteria);

        // Transform
        List<IndicatorsSystemDto> indicatorsSystemsDto = new ArrayList<IndicatorsSystemDto>();
        for (IndicatorsSystemVersion indicatorsSystemVersion : indicatorsSystemsVersion) {
            indicatorsSystemsDto.add(do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion));
        }

        return indicatorsSystemsDto;
    }

    @Override
    public List<IndicatorsSystemDto> findIndicatorsSystemsPublished(ServiceContext ctx, IndicatorsCriteria criteria) throws MetamacException {

        // Retrieve published
        List<IndicatorsSystemVersion> indicatorsSystemsVersion = getIndicatorsSystemsService().findIndicatorsSystemsPublished(ctx, criteria);

        // Transform
        List<IndicatorsSystemDto> indicatorsSystemsDto = new ArrayList<IndicatorsSystemDto>();
        for (IndicatorsSystemVersion indicatorsSystemVersion : indicatorsSystemsVersion) {
            indicatorsSystemsDto.add(do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion));
        }

        return indicatorsSystemsDto;
    }

    @Override
    public List<IndicatorsSystemDto> retrieveIndicatorsSystemPublishedForIndicator(ServiceContext ctx, String indicatorUuid) throws MetamacException {

        // Retrieve
        List<IndicatorsSystemVersion> indicatorsSystemsVersion = getIndicatorsSystemsService().retrieveIndicatorsSystemPublishedForIndicator(ctx, indicatorUuid);

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
    public List<DimensionDto> retrieveDimensionsByIndicatorsSystem(ServiceContext ctx, String indicatorsSystemUuid, String indicatorsSystemVersion) throws MetamacException {

        // Retrieve dimensions
        List<Dimension> dimensions = getIndicatorsSystemsService().retrieveDimensionsByIndicatorsSystem(ctx, indicatorsSystemUuid, indicatorsSystemVersion);

        // Transform
        List<DimensionDto> dimensionsDto = new ArrayList<DimensionDto>();
        for (Dimension dimension : dimensions) {
            dimensionsDto.add(do2DtoMapper.dimensionDoToDto(dimension));
        }

        return dimensionsDto;
    }

    @Override
    public DimensionDto updateDimension(ServiceContext ctx, DimensionDto dimensionDto) throws MetamacException {

        // Transform
        Dimension dimension = dto2DoMapper.dimensionDtoToDo(ctx, dimensionDto);

        // Update
        dimension = getIndicatorsSystemsService().updateDimension(ctx, dimension);

        // Transform to Dto
        dimensionDto = do2DtoMapper.dimensionDoToDto(dimension);
        return dimensionDto;
    }

    @Override
    public DimensionDto updateDimensionLocation(ServiceContext ctx, String uuid, String parentTargetUuid, Long orderInLevel) throws MetamacException {

        // Update
        Dimension dimension = getIndicatorsSystemsService().updateDimensionLocation(ctx, uuid, parentTargetUuid, orderInLevel);

        // Transform to Dto
        DimensionDto dimensionDto = do2DtoMapper.dimensionDoToDto(dimension);
        return dimensionDto;
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
    public List<IndicatorInstanceDto> retrieveIndicatorsInstancesByIndicatorsSystem(ServiceContext ctx, String indicatorsSystemUuid, String indicatorsSystemVersion) throws MetamacException {

        // Retrieve indicators instances
        List<IndicatorInstance> indicatorsInstances = getIndicatorsSystemsService().retrieveIndicatorsInstancesByIndicatorsSystem(ctx, indicatorsSystemUuid, indicatorsSystemVersion);

        // Transform
        List<IndicatorInstanceDto> indicatorsInstancesDto = new ArrayList<IndicatorInstanceDto>();
        for (IndicatorInstance indicatorInstance : indicatorsInstances) {
            indicatorsInstancesDto.add(do2DtoMapper.indicatorInstanceDoToDto(indicatorInstance));
        }

        return indicatorsInstancesDto;
    }

    @Override
    public IndicatorInstanceDto updateIndicatorInstance(ServiceContext ctx, IndicatorInstanceDto indicatorInstanceDto) throws MetamacException {

        // Transform
        IndicatorInstance indicatorInstance = dto2DoMapper.indicatorInstanceDtoToDo(ctx, indicatorInstanceDto);

        // Update
        indicatorInstance = getIndicatorsSystemsService().updateIndicatorInstance(ctx, indicatorInstance);

        // Transform to Dto
        indicatorInstanceDto = do2DtoMapper.indicatorInstanceDoToDto(indicatorInstance);
        return indicatorInstanceDto;
    }

    @Override
    public IndicatorInstanceDto updateIndicatorInstanceLocation(ServiceContext ctx, String uuid, String parentTargetUuid, Long orderInLevel) throws MetamacException {

        IndicatorInstance indicatorInstance = getIndicatorsSystemsService().updateIndicatorInstanceLocation(ctx, uuid, parentTargetUuid, orderInLevel);

        // Transform to Dto
        IndicatorInstanceDto indicatorInstanceDto = do2DtoMapper.indicatorInstanceDoToDto(indicatorInstance);
        return indicatorInstanceDto;
    }

    @Override
    public GeographicalValueDto retrieveGeographicalValue(ServiceContext ctx, String uuid) throws MetamacException {

        // Retrieve
        GeographicalValue geographicalValue = getIndicatorsSystemsService().retrieveGeographicalValue(ctx, uuid);

        // Transform
        GeographicalValueDto geographicalValueDto = do2DtoMapper.geographicalValueDoToDto(geographicalValue);
        return geographicalValueDto;
    }

    @Override
    public List<GeographicalValueDto> findGeographicalValues(ServiceContext ctx, IndicatorsCriteria criteria) throws MetamacException {

        // Retrieve geographicalValues
        List<GeographicalValue> geographicalValues = getIndicatorsSystemsService().findGeographicalValues(ctx, criteria);

        // Transform
        List<GeographicalValueDto> geographicalValuesDto = new ArrayList<GeographicalValueDto>();
        for (GeographicalValue geographicalValue : geographicalValues) {
            geographicalValuesDto.add(do2DtoMapper.geographicalValueDoToDto(geographicalValue));
        }

        return geographicalValuesDto;
    }

    @Override
    public GeographicalGranularityDto retrieveGeographicalGranularity(ServiceContext ctx, String uuid) throws MetamacException {

        // Retrieve
        GeographicalGranularity geographicalGranularity = getIndicatorsSystemsService().retrieveGeographicalGranularity(ctx, uuid);

        // Transform
        GeographicalGranularityDto geographicalGranularityDto = do2DtoMapper.geographicalGranularityDoToDto(geographicalGranularity);
        return geographicalGranularityDto;
    }

    @Override
    public List<GeographicalGranularityDto> retrieveGeographicalGranularities(ServiceContext ctx) throws MetamacException {

        // Retrieve geographicalGranularitys
        List<GeographicalGranularity> geographicalGranularitys = getIndicatorsSystemsService().retrieveGeographicalGranularities(ctx);

        // Transform
        List<GeographicalGranularityDto> geographicalGranularitysDto = new ArrayList<GeographicalGranularityDto>();
        for (GeographicalGranularity geographicalGranularity : geographicalGranularitys) {
            geographicalGranularitysDto.add(do2DtoMapper.geographicalGranularityDoToDto(geographicalGranularity));
        }

        return geographicalGranularitysDto;
    }

    public IndicatorDto createIndicator(ServiceContext ctx, IndicatorDto indicatorDto) throws MetamacException {

        // Transform
        IndicatorVersion indicatorVersion = dto2DoMapper.indicatorDtoToDo(ctx, indicatorDto);

        // Create
        IndicatorVersion indicatorVersionCreated = getIndicatorsService().createIndicator(ctx, indicatorVersion);

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

    public IndicatorDto updateIndicator(ServiceContext ctx, IndicatorDto indicatorDto) throws MetamacException {

        // Transform
        IndicatorVersion indicatorVersion = dto2DoMapper.indicatorDtoToDo(ctx, indicatorDto);

        // Update
        indicatorVersion = getIndicatorsService().updateIndicatorVersion(ctx, indicatorVersion);

        // Transform to Dto
        indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersion);
        return indicatorDto;
    }

    @Override
    public IndicatorDto sendIndicatorToProductionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        IndicatorVersion indicatorVersion = getIndicatorsService().sendIndicatorToProductionValidation(ctx, uuid);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersion);
        return indicatorDto;
    }

    @Override
    public IndicatorDto sendIndicatorToDiffusionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        IndicatorVersion indicatorVersion = getIndicatorsService().sendIndicatorToDiffusionValidation(ctx, uuid);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersion);
        return indicatorDto;
    }

    @Override
    public IndicatorDto rejectIndicatorValidation(ServiceContext ctx, String uuid) throws MetamacException {

        IndicatorVersion indicatorVersion = getIndicatorsService().rejectIndicatorValidation(ctx, uuid);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersion);
        return indicatorDto;
    }

    @Override
    public IndicatorDto publishIndicator(ServiceContext ctx, String uuid) throws MetamacException {

        IndicatorVersion indicatorVersion = getIndicatorsService().publishIndicator(ctx, uuid);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersion);
        return indicatorDto;
    }

    @Override
    public IndicatorDto archiveIndicator(ServiceContext ctx, String uuid) throws MetamacException {

        IndicatorVersion indicatorVersion = getIndicatorsService().archiveIndicator(ctx, uuid);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersion);
        return indicatorDto;
    }

    @Override
    public IndicatorDto versioningIndicator(ServiceContext ctx, String uuid, VersionTypeEnum versionType) throws MetamacException {

        // Versioning
        IndicatorVersion indicatorVersionCreated = getIndicatorsService().versioningIndicator(ctx, uuid, versionType);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersionCreated);
        return indicatorDto;
    }

    @Override
    public List<IndicatorDto> findIndicators(ServiceContext ctx, IndicatorsCriteria criteria) throws MetamacException {

        // Find
        List<IndicatorVersion> indicatorsVersion = getIndicatorsService().findIndicators(ctx, criteria);

        // Transform
        List<IndicatorDto> indicatorsDto = new ArrayList<IndicatorDto>();
        for (IndicatorVersion indicatorVersion : indicatorsVersion) {
            indicatorsDto.add(do2DtoMapper.indicatorDoToDto(indicatorVersion));
        }

        return indicatorsDto;
    }

    @Override
    public List<IndicatorDto> findIndicatorsPublished(ServiceContext ctx, IndicatorsCriteria criteria) throws MetamacException {

        // Retrieve published
        List<IndicatorVersion> indicatorsVersion = getIndicatorsService().findIndicatorsPublished(ctx, criteria);

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
    public List<DataSourceDto> retrieveDataSourcesByIndicator(ServiceContext ctx, String indicatorUuid, String indicatorVersion) throws MetamacException {

        // Retrieve dataSources
        List<DataSource> dataSources = getIndicatorsService().retrieveDataSourcesByIndicator(ctx, indicatorUuid, indicatorVersion);

        // Transform
        List<DataSourceDto> dataSourcesDto = new ArrayList<DataSourceDto>();
        for (DataSource dataSource : dataSources) {
            dataSourcesDto.add(do2DtoMapper.dataSourceDoToDto(dataSource));
        }

        return dataSourcesDto;
    }

    @Override
    public DataSourceDto updateDataSource(ServiceContext ctx, DataSourceDto dataSourceDto) throws MetamacException {

        // Transform and update
        DataSource dataSource = dto2DoMapper.dataSourceDtoToDo(ctx, dataSourceDto);

        // Update
        dataSource = getIndicatorsService().updateDataSource(ctx, dataSource);

        // Transform
        dataSourceDto = do2DtoMapper.dataSourceDoToDto(dataSource);
        return dataSourceDto;
    }

    @Override
    public QuantityUnitDto retrieveQuantityUnit(ServiceContext ctx, String uuid) throws MetamacException {

        // Retrieve
        QuantityUnit quantityUnit = getIndicatorsService().retrieveQuantityUnit(ctx, uuid);

        // Transform
        QuantityUnitDto quantityUnitDto = do2DtoMapper.quantityUnitDoToDto(quantityUnit);
        return quantityUnitDto;
    }

    @Override
    public List<QuantityUnitDto> retrieveQuantityUnits(ServiceContext ctx) throws MetamacException {

        // Retrieve quantityUnits
        List<QuantityUnit> quantityUnits = getIndicatorsService().retrieveQuantityUnits(ctx);

        // Transform
        List<QuantityUnitDto> quantityUnitsDto = new ArrayList<QuantityUnitDto>();
        for (QuantityUnit quantityUnit : quantityUnits) {
            quantityUnitsDto.add(do2DtoMapper.quantityUnitDoToDto(quantityUnit));
        }

        return quantityUnitsDto;
    }

    @Override
    public SubjectDto retrieveSubject(ServiceContext ctx, String code) throws MetamacException {

        // Retrieve
        Subject subject = getIndicatorsService().retrieveSubject(ctx, code);

        // Transform
        SubjectDto subjectDto = do2DtoMapper.subjectDoToDto(subject);
        return subjectDto;
    }

    @Override
    public List<SubjectDto> retrieveSubjects(ServiceContext ctx) throws MetamacException {

        // Retrieve subjects
        List<Subject> subjects = getIndicatorsService().retrieveSubjects(ctx);

        // Transform
        List<SubjectDto> subjectsDto = new ArrayList<SubjectDto>();
        for (Subject subject : subjects) {
            subjectsDto.add(do2DtoMapper.subjectDoToDto(subject));
        }

        return subjectsDto;
    }

    @Override
    public List<SubjectDto> retrieveSubjectsInPublishedIndicators(ServiceContext ctx) throws MetamacException {

        // Retrieve subjects
        List<SubjectIndicatorResult> subjects = getIndicatorsService().retrieveSubjectsInPublishedIndicators(ctx);

        // Transform
        List<SubjectDto> subjectsDto = new ArrayList<SubjectDto>();
        for (SubjectIndicatorResult subject : subjects) {
            subjectsDto.add(do2DtoMapper.subjectDoToDto(subject));
        }

        return subjectsDto;
    }
    
    @Override
    public List<DataDefinitionDto> retrieveDataDefinitions(ServiceContext ctx) throws MetamacException {
        // Service call
        List<DataDefinition> dataDefs = getIndicatorsDataService().retrieveDataDefinitions(ctx);
        
        //Transform
        List<DataDefinitionDto> dtos = new ArrayList<DataDefinitionDto>();
        for (DataDefinition basic : dataDefs) {
            dtos.add(do2DtoMapper.dataDefinitionDoToDto(basic));
        }
        return dtos;
    }
    
    @Override
    public DataDefinitionDto retrieveDataDefinition(ServiceContext ctx,String uuid) throws MetamacException {
        // Service call
        DataDefinition dataDef = getIndicatorsDataService().retrieveDataDefinition(ctx,uuid);
        
        //Transform
        DataDefinitionDto dto = null;
        if (dataDef != null) {
            dto = do2DtoMapper.dataDefinitionDoToDto(dataDef);
        }
        return dto;
    }

    @Override
    public DataStructureDto retrieveDataStructure(ServiceContext ctx, String uuid) throws MetamacException {
        //Service call
        DataStructure dataStruc = getIndicatorsDataService().retrieveDataStructure(ctx, uuid);
        
        //Transform
        return do2DtoMapper.dataStructureDoToDto(dataStruc);
    }

    @Override
    public void populateIndicatorData(ServiceContext ctx, String indicatorUuid, String version) throws MetamacException {
        getIndicatorsDataService().populateIndicatorData(ctx, indicatorUuid, version);
    }
}
