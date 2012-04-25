package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.criteria.SculptorCriteria;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.dataset.repository.dto.ConditionDimensionDto;
import com.arte.statistic.dataset.repository.dto.ObservationDto;
import com.arte.statistic.dataset.repository.dto.ObservationExtendedDto;

import es.gobcan.istac.indicators.core.domain.DataDefinition;
import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.DataStructure;
import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.ElementLevel;
import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.QuantityUnit;
import es.gobcan.istac.indicators.core.domain.Subject;
import es.gobcan.istac.indicators.core.domain.TimeGranularity;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.dto.DataDefinitionDto;
import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.DataStructureDto;
import es.gobcan.istac.indicators.core.dto.DimensionDto;
import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemStructureDto;
import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.SubjectDto;
import es.gobcan.istac.indicators.core.dto.TimeGranularityDto;
import es.gobcan.istac.indicators.core.dto.TimeValueDto;
import es.gobcan.istac.indicators.core.enume.domain.RoleEnum;
import es.gobcan.istac.indicators.core.enume.domain.TimeGranularityEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;
import es.gobcan.istac.indicators.core.mapper.Do2DtoMapper;
import es.gobcan.istac.indicators.core.mapper.Dto2DoMapper;
import es.gobcan.istac.indicators.core.mapper.MetamacCriteria2SculptorCriteriaMapper;
import es.gobcan.istac.indicators.core.mapper.SculptorCriteria2MetamacCriteriaMapper;
import es.gobcan.istac.indicators.core.repositoryimpl.finders.SubjectIndicatorResult;
import es.gobcan.istac.indicators.core.security.SecurityUtils;

/**
 * Implementation of IndicatorServiceFacade.
 */
@Service("indicatorsServiceFacade")
public class IndicatorsServiceFacadeImpl extends IndicatorsServiceFacadeImplBase {

    @Autowired
    private Do2DtoMapper                           do2DtoMapper;

    @Autowired
    private Dto2DoMapper                           dto2DoMapper;

    @Autowired
    private MetamacCriteria2SculptorCriteriaMapper metamacCriteria2SculptorCriteriaMapper;

    @Autowired
    private SculptorCriteria2MetamacCriteriaMapper sculptorCriteria2MetamacCriteriaMapper;

    public IndicatorsServiceFacadeImpl() {
    }

    public IndicatorsSystemDto createIndicatorsSystem(ServiceContext ctx, IndicatorsSystemDto indicatorsSystemDto) throws MetamacException {

        // Security (role and access to this indicators system)
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_SISTEMA_INDICADORES);
        checkAccessIndicatorsSystemByCode(ctx, indicatorsSystemDto.getCode(), RoleEnum.TECNICO_SISTEMA_INDICADORES);

        // Transform
        IndicatorsSystemVersion indicatorsSystemVersion = dto2DoMapper.indicatorsSystemDtoToDo(ctx, indicatorsSystemDto);

        // Create
        IndicatorsSystemVersion indicatorsSystemVersionCreated = getIndicatorsSystemsService().createIndicatorsSystem(ctx, indicatorsSystemVersion);

        // Transform to Dto
        indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersionCreated);
        return indicatorsSystemDto;
    }

    public IndicatorsSystemDto retrieveIndicatorsSystem(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().retrieveIndicatorsSystem(ctx, uuid, versionNumber);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
        return indicatorsSystemDto;
    }

    @Override
    public IndicatorsSystemDto retrieveIndicatorsSystemPublished(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().retrieveIndicatorsSystemPublished(ctx, uuid);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
        return indicatorsSystemDto;
    }

    public IndicatorsSystemDto retrieveIndicatorsSystemByCode(ServiceContext ctx, String code, String versionNumber) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().retrieveIndicatorsSystemByCode(ctx, code, versionNumber);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
        return indicatorsSystemDto;
    }

    public IndicatorsSystemDto retrieveIndicatorsSystemPublishedByCode(ServiceContext ctx, String code) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().retrieveIndicatorsSystemPublishedByCode(ctx, code);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
        return indicatorsSystemDto;
    }

    @Override
    public IndicatorsSystemStructureDto retrieveIndicatorsSystemStructure(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

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

        // Security (role and access to this indicators system)
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_SISTEMA_INDICADORES);
        checkAccessIndicatorsSystemByUuid(ctx, uuid, RoleEnum.TECNICO_SISTEMA_INDICADORES);

        // Delete
        getIndicatorsSystemsService().deleteIndicatorsSystem(ctx, uuid);
    }

    @Override
    public IndicatorsSystemDto sendIndicatorsSystemToProductionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Security (role and access to this indicators system)
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_SISTEMA_INDICADORES);
        checkAccessIndicatorsSystemByUuid(ctx, uuid, RoleEnum.TECNICO_SISTEMA_INDICADORES);

        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().sendIndicatorsSystemToProductionValidation(ctx, uuid);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
        return indicatorsSystemDto;
    }

    @Override
    public IndicatorsSystemDto rejectIndicatorsSystemProductionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Security (role and access to this indicators system)
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_PRODUCCION);
        checkAccessIndicatorsSystemByUuid(ctx, uuid, RoleEnum.TECNICO_PRODUCCION);

        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().rejectIndicatorsSystemProductionValidation(ctx, uuid);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
        return indicatorsSystemDto;
    }

    @Override
    public IndicatorsSystemDto sendIndicatorsSystemToDiffusionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Security (role and access to this indicators system)
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_SISTEMA_INDICADORES);
        checkAccessIndicatorsSystemByUuid(ctx, uuid, RoleEnum.TECNICO_SISTEMA_INDICADORES);

        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().sendIndicatorsSystemToDiffusionValidation(ctx, uuid);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
        return indicatorsSystemDto;
    }

    @Override
    public IndicatorsSystemDto rejectIndicatorsSystemDiffusionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Security (role and access to this indicators system)
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_DIFUSION);
        checkAccessIndicatorsSystemByUuid(ctx, uuid, RoleEnum.TECNICO_DIFUSION);

        // Reject
        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().rejectIndicatorsSystemDiffusionValidation(ctx, uuid);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
        return indicatorsSystemDto;
    }

    @Override
    public IndicatorsSystemDto publishIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException {

        // Security (role and access to this indicators system)
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_DIFUSION, RoleEnum.TECNICO_APOYO_DIFUSION);
        checkAccessIndicatorsSystemByUuid(ctx, uuid, RoleEnum.TECNICO_DIFUSION, RoleEnum.TECNICO_APOYO_DIFUSION);

        // Publish
        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().publishIndicatorsSystem(ctx, uuid);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
        return indicatorsSystemDto;
    }

    @Override
    public IndicatorsSystemDto archiveIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException {

        // Security (role and access to this indicators system)
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_DIFUSION);
        checkAccessIndicatorsSystemByUuid(ctx, uuid, RoleEnum.TECNICO_DIFUSION);

        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().archiveIndicatorsSystem(ctx, uuid);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
        return indicatorsSystemDto;
    }

    @Override
    public IndicatorsSystemDto versioningIndicatorsSystem(ServiceContext ctx, String uuid, VersionTypeEnum versionType) throws MetamacException {

        // Security (role and access to this indicators system)
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_SISTEMA_INDICADORES);
        checkAccessIndicatorsSystemByUuid(ctx, uuid, RoleEnum.TECNICO_SISTEMA_INDICADORES);

        // Versioning
        IndicatorsSystemVersion indicatorsSystemVersionCreated = getIndicatorsSystemsService().versioningIndicatorsSystem(ctx, uuid, versionType);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersionCreated);
        return indicatorsSystemDto;
    }

    @Override
    public MetamacCriteriaResult<IndicatorsSystemDto> findIndicatorsSystems(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getIndicatorsSystemVersionCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<IndicatorsSystemVersion> result = getIndicatorsSystemsService().findIndicatorsSystems(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<IndicatorsSystemDto> dtoResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultIndicatorsSystem(result, sculptorCriteria.getPageSize());
        return dtoResult;
    }

    @Override
    public MetamacCriteriaResult<IndicatorsSystemDto> findIndicatorsSystemsPublished(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getIndicatorsSystemVersionCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<IndicatorsSystemVersion> result = getIndicatorsSystemsService().findIndicatorsSystemsPublished(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<IndicatorsSystemDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultIndicatorsSystem(result,
                sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public List<IndicatorsSystemDto> retrieveIndicatorsSystemPublishedForIndicator(ServiceContext ctx, String indicatorUuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

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

        // Security (role and access to this indicators system)
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_SISTEMA_INDICADORES);
        checkAccessIndicatorsSystemByUuid(ctx, indicatorsSystemUuid, RoleEnum.TECNICO_SISTEMA_INDICADORES);

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

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        Dimension dimension = getIndicatorsSystemsService().retrieveDimension(ctx, uuid);

        // Transform
        DimensionDto dimensionDto = do2DtoMapper.dimensionDoToDto(dimension);
        return dimensionDto;
    }

    @Override
    public void deleteDimension(ServiceContext ctx, String uuid) throws MetamacException {

        // Security (role and access to this indicators system)
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_SISTEMA_INDICADORES);
        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().retrieveIndicatorsSystemByDimension(ctx, uuid);
        checkAccessIndicatorsSystem(ctx, indicatorsSystemVersion.getIndicatorsSystem(), RoleEnum.TECNICO_SISTEMA_INDICADORES);

        // Delete
        getIndicatorsSystemsService().deleteDimension(ctx, uuid);
    }

    @Override
    public List<DimensionDto> retrieveDimensionsByIndicatorsSystem(ServiceContext ctx, String indicatorsSystemUuid, String indicatorsSystemVersion) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

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

        // Security (role and access to this indicators system)
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_SISTEMA_INDICADORES);
        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().retrieveIndicatorsSystemByDimension(ctx, dimensionDto.getUuid());
        checkAccessIndicatorsSystem(ctx, indicatorsSystemVersion.getIndicatorsSystem(), RoleEnum.TECNICO_SISTEMA_INDICADORES);

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

        // Security (role and access to this indicators system)
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_SISTEMA_INDICADORES);
        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().retrieveIndicatorsSystemByDimension(ctx, uuid);
        checkAccessIndicatorsSystem(ctx, indicatorsSystemVersion.getIndicatorsSystem(), RoleEnum.TECNICO_SISTEMA_INDICADORES);

        // Update
        Dimension dimension = getIndicatorsSystemsService().updateDimensionLocation(ctx, uuid, parentTargetUuid, orderInLevel);

        // Transform to Dto
        DimensionDto dimensionDto = do2DtoMapper.dimensionDoToDto(dimension);
        return dimensionDto;
    }

    @Override
    public IndicatorInstanceDto createIndicatorInstance(ServiceContext ctx, String indicatorsSystemUuid, IndicatorInstanceDto indicatorInstanceDto) throws MetamacException {

        // Security (role and access to this indicators system)
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_SISTEMA_INDICADORES);
        checkAccessIndicatorsSystemByUuid(ctx, indicatorsSystemUuid, RoleEnum.TECNICO_SISTEMA_INDICADORES);

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

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        IndicatorInstance indicatorInstance = getIndicatorsSystemsService().retrieveIndicatorInstance(ctx, uuid);

        // Transform
        IndicatorInstanceDto indicatorInstanceDto = do2DtoMapper.indicatorInstanceDoToDto(indicatorInstance);
        return indicatorInstanceDto;
    }

    @Override
    public void deleteIndicatorInstance(ServiceContext ctx, String uuid) throws MetamacException {

        // Security (role and access to this indicators system)
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_SISTEMA_INDICADORES);
        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().retrieveIndicatorsSystemByIndicatorInstance(ctx, uuid);
        checkAccessIndicatorsSystem(ctx, indicatorsSystemVersion.getIndicatorsSystem(), RoleEnum.TECNICO_SISTEMA_INDICADORES);

        // Delete
        getIndicatorsSystemsService().deleteIndicatorInstance(ctx, uuid);
    }

    @Override
    public List<IndicatorInstanceDto> retrieveIndicatorsInstancesByIndicatorsSystem(ServiceContext ctx, String indicatorsSystemUuid, String indicatorsSystemVersion) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

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

        // Security (role and access to this indicators system)
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_SISTEMA_INDICADORES);
        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().retrieveIndicatorsSystemByIndicatorInstance(ctx, indicatorInstanceDto.getUuid());
        checkAccessIndicatorsSystem(ctx, indicatorsSystemVersion.getIndicatorsSystem(), RoleEnum.TECNICO_SISTEMA_INDICADORES);

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

        // Security (role and access to this indicators system)
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_SISTEMA_INDICADORES);
        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().retrieveIndicatorsSystemByIndicatorInstance(ctx, uuid);
        checkAccessIndicatorsSystem(ctx, indicatorsSystemVersion.getIndicatorsSystem(), RoleEnum.TECNICO_SISTEMA_INDICADORES);

        // Update
        IndicatorInstance indicatorInstance = getIndicatorsSystemsService().updateIndicatorInstanceLocation(ctx, uuid, parentTargetUuid, orderInLevel);

        // Transform to Dto
        IndicatorInstanceDto indicatorInstanceDto = do2DtoMapper.indicatorInstanceDoToDto(indicatorInstance);
        return indicatorInstanceDto;
    }

    @Override
    public GeographicalValueDto retrieveGeographicalValue(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        GeographicalValue geographicalValue = getIndicatorsSystemsService().retrieveGeographicalValue(ctx, uuid);

        // Transform
        GeographicalValueDto geographicalValueDto = do2DtoMapper.geographicalValueDoToDto(geographicalValue);
        return geographicalValueDto;
    }

    @Override
    public GeographicalValueDto retrieveGeographicalValueByCode(ServiceContext ctx, String code) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        GeographicalValue geographicalValue = getIndicatorsSystemsService().retrieveGeographicalValueByCode(ctx, code);

        // Transform
        GeographicalValueDto geographicalValueDto = do2DtoMapper.geographicalValueDoToDto(geographicalValue);
        return geographicalValueDto;
    }

    @Override
    public MetamacCriteriaResult<GeographicalValueDto> findGeographicalValues(ServiceContext ctx, MetamacCriteria metamacCriteria) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getGeographicalValueCriteriaMapper().metamacCriteria2SculptorCriteria(metamacCriteria);

        // Find
        PagedResult<GeographicalValue> result = getIndicatorsSystemsService().findGeographicalValues(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<GeographicalValueDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultGeographicalValue(result,
                sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public List<GeographicalValueDto> retrieveGeographicalValuesByGranularityInIndicator(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber, String granularityUuid)
            throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        List<GeographicalValue> geographicalValues = getIndicatorsDataService().retrieveGeographicalValuesByGranularityInIndicator(ctx, indicatorUuid, indicatorVersionNumber, granularityUuid);

        // Transform
        List<GeographicalValueDto> geographicalValueDtos = new ArrayList<GeographicalValueDto>();
        for (GeographicalValue geoValue : geographicalValues) {
            geographicalValueDtos.add(do2DtoMapper.geographicalValueDoToDto(geoValue));
        }
        return geographicalValueDtos;
    }

    @Override
    public List<GeographicalValueDto> retrieveGeographicalValuesByGranularityInIndicatorPublished(ServiceContext ctx, String indicatorUuid, String granularityUuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        List<GeographicalValue> geographicalValues = getIndicatorsDataService().retrieveGeographicalValuesByGranularityInIndicatorPublished(ctx, indicatorUuid, granularityUuid);

        // Transform
        List<GeographicalValueDto> geographicalValueDtos = new ArrayList<GeographicalValueDto>();
        for (GeographicalValue geoValue : geographicalValues) {
            geographicalValueDtos.add(do2DtoMapper.geographicalValueDoToDto(geoValue));
        }
        return geographicalValueDtos;
    }
    
    @Override
    public List<GeographicalValueDto> retrieveGeographicalValuesInIndicator(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);
        
        // Retrieve
        List<GeographicalValue> geographicalValues = getIndicatorsDataService().retrieveGeographicalValuesInIndicator(ctx, indicatorUuid, indicatorVersionNumber);

        // Transform
        List<GeographicalValueDto> geographicalValueDtos = new ArrayList<GeographicalValueDto>();
        for (GeographicalValue geoValue : geographicalValues) {
            geographicalValueDtos.add(do2DtoMapper.geographicalValueDoToDto(geoValue));
        }
        return geographicalValueDtos;
    }
    
    @Override
    public List<GeographicalValueDto> retrieveGeographicalValuesInIndicatorPublished(ServiceContext ctx, String indicatorUuid) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);
        
        // Retrieve
        List<GeographicalValue> geographicalValues = getIndicatorsDataService().retrieveGeographicalValuesInIndicatorPublished(ctx, indicatorUuid);

        // Transform
        List<GeographicalValueDto> geographicalValueDtos = new ArrayList<GeographicalValueDto>();
        for (GeographicalValue geoValue : geographicalValues) {
            geographicalValueDtos.add(do2DtoMapper.geographicalValueDoToDto(geoValue));
        }
        return geographicalValueDtos;
    }

    @Override
    public List<GeographicalValueDto> retrieveGeographicalValuesInIndicatorInstance(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        List<GeographicalValue> geographicalValues = getIndicatorsDataService().retrieveGeographicalValuesInIndicatorInstance(ctx, indicatorInstanceUuid);

        // Transform
        List<GeographicalValueDto> geographicalValueDtos = new ArrayList<GeographicalValueDto>();
        for (GeographicalValue geoValue : geographicalValues) {
            geographicalValueDtos.add(do2DtoMapper.geographicalValueDoToDto(geoValue));
        }
        return geographicalValueDtos;
    }

    @Override
    public GeographicalGranularityDto retrieveGeographicalGranularity(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        GeographicalGranularity geographicalGranularity = getIndicatorsSystemsService().retrieveGeographicalGranularity(ctx, uuid);

        // Transform
        GeographicalGranularityDto geographicalGranularityDto = do2DtoMapper.geographicalGranularityDoToDto(geographicalGranularity);
        return geographicalGranularityDto;
    }

    @Override
    public GeographicalGranularityDto retrieveGeographicalGranularityByCode(ServiceContext ctx, String code) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        GeographicalGranularity geographicalGranularity = getIndicatorsSystemsService().retrieveGeographicalGranularityByCode(ctx, code);

        // Transform
        GeographicalGranularityDto geographicalGranularityDto = do2DtoMapper.geographicalGranularityDoToDto(geographicalGranularity);
        return geographicalGranularityDto;
    }

    @Override
    public List<GeographicalGranularityDto> retrieveGeographicalGranularities(ServiceContext ctx) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        List<GeographicalGranularity> geographicalGranularitys = getIndicatorsSystemsService().retrieveGeographicalGranularities(ctx);

        // Transform
        List<GeographicalGranularityDto> geographicalGranularitysDto = new ArrayList<GeographicalGranularityDto>();
        for (GeographicalGranularity geographicalGranularity : geographicalGranularitys) {
            geographicalGranularitysDto.add(do2DtoMapper.geographicalGranularityDoToDto(geographicalGranularity));
        }

        return geographicalGranularitysDto;
    }

    @Override
    public List<GeographicalGranularityDto> retrieveGeographicalGranularitiesInIndicator(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        List<GeographicalGranularity> geographicalGranularities = getIndicatorsDataService().retrieveGeographicalGranularitiesInIndicator(ctx, indicatorUuid, indicatorVersionNumber);

        // Transform
        List<GeographicalGranularityDto> geographicalGranularityDtos = new ArrayList<GeographicalGranularityDto>();
        for (GeographicalGranularity geographicalGranularity : geographicalGranularities) {
            geographicalGranularityDtos.add(do2DtoMapper.geographicalGranularityDoToDto(geographicalGranularity));
        }

        return geographicalGranularityDtos;
    }

    @Override
    public List<GeographicalGranularityDto> retrieveGeographicalGranularitiesInIndicatorPublished(ServiceContext ctx, String indicatorUuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        List<GeographicalGranularity> geographicalGranularities = getIndicatorsDataService().retrieveGeographicalGranularitiesInIndicatorPublished(ctx, indicatorUuid);

        // Transform
        List<GeographicalGranularityDto> geographicalGranularityDtos = new ArrayList<GeographicalGranularityDto>();
        for (GeographicalGranularity geographicalGranularity : geographicalGranularities) {
            geographicalGranularityDtos.add(do2DtoMapper.geographicalGranularityDoToDto(geographicalGranularity));
        }

        return geographicalGranularityDtos;
    }
    
    @Override
    public List<GeographicalGranularityDto> retrieveGeographicalGranularitiesInIndicatorInstance(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        List<GeographicalGranularity> geographicalGranularities = getIndicatorsDataService().retrieveGeographicalGranularitiesInIndicatorInstance(ctx, indicatorInstanceUuid);

        // Transform
        List<GeographicalGranularityDto> geographicalGranularityDtos = new ArrayList<GeographicalGranularityDto>();
        for (GeographicalGranularity geographicalGranularity : geographicalGranularities) {
            geographicalGranularityDtos.add(do2DtoMapper.geographicalGranularityDoToDto(geographicalGranularity));
        }

        return geographicalGranularityDtos;
    }

    @Override
    public TimeGranularityDto retrieveTimeGranularity(ServiceContext ctx, TimeGranularityEnum timeGranularity) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        TimeGranularity timeGranularityDo = getIndicatorsSystemsService().retrieveTimeGranularity(ctx, timeGranularity);

        // Transform
        TimeGranularityDto timeGranularityDto = do2DtoMapper.timeGranularityDoToTimeGranularityDto(timeGranularityDo);
        return timeGranularityDto;
    }

    @Override
    public TimeValueDto retrieveTimeValue(ServiceContext ctx, String timeValue) throws MetamacException {
        
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        TimeValue timeValueDo = getIndicatorsSystemsService().retrieveTimeValue(ctx, timeValue);

        // Transform
        TimeValueDto timeValueDto = do2DtoMapper.timeValueDoToTimeValueDto(timeValueDo);
        return timeValueDto;
    }

    @Override
    public List<TimeGranularityDto> retrieveTimeGranularitiesInIndicator(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        List<TimeGranularity> timeGranularities = getIndicatorsDataService().retrieveTimeGranularitiesInIndicator(ctx, indicatorUuid, indicatorVersionNumber);
        
        //Transform
        List<TimeGranularityDto> timeGranularitiesDtos = new ArrayList<TimeGranularityDto>();
        for (TimeGranularity granularity : timeGranularities) {
            TimeGranularityDto timeGranularityDto = do2DtoMapper.timeGranularityDoToTimeGranularityDto(granularity);
            timeGranularitiesDtos.add(timeGranularityDto);
        }
        return timeGranularitiesDtos;
    }

    @Override
    public List<TimeGranularityDto> retrieveTimeGranularitiesInIndicatorPublished(ServiceContext ctx, String indicatorUuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        List<TimeGranularity> timeGranularities = getIndicatorsDataService().retrieveTimeGranularitiesInIndicatorPublished(ctx, indicatorUuid);
        
        //Transform
        List<TimeGranularityDto> timeGranularitiesDtos = new ArrayList<TimeGranularityDto>();
        for (TimeGranularity granularity : timeGranularities) {
            TimeGranularityDto timeGranularityDto = do2DtoMapper.timeGranularityDoToTimeGranularityDto(granularity);
            timeGranularitiesDtos.add(timeGranularityDto);
        }
        return timeGranularitiesDtos;
    }
    
    @Override
    public List<TimeGranularityDto> retrieveTimeGranularitiesInIndicatorInstance(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);
        
        // Retrieve
        List<TimeGranularity> timeGranularities = getIndicatorsDataService().retrieveTimeGranularitiesInIndicatorPublished(ctx, indicatorInstanceUuid);
        
        //Transform
        List<TimeGranularityDto> timeGranularitiesDtos = new ArrayList<TimeGranularityDto>();
        for (TimeGranularity granularity : timeGranularities) {
            TimeGranularityDto timeGranularityDto = do2DtoMapper.timeGranularityDoToTimeGranularityDto(granularity);
            timeGranularitiesDtos.add(timeGranularityDto);
        }
        return timeGranularitiesDtos;
    }

    @Override
    public List<TimeValueDto> retrieveTimeValuesByGranularityInIndicator(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber, TimeGranularityEnum granularity) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        List<TimeValue> timeValues = getIndicatorsDataService().retrieveTimeValuesByGranularityInIndicator(ctx, indicatorUuid, indicatorVersionNumber, granularity);
        
        //Transform
        List<TimeValueDto> timeValuesDtos = new ArrayList<TimeValueDto>();
        for (TimeValue timeValue: timeValues) {
            TimeValueDto timeValueDto = do2DtoMapper.timeValueDoToTimeValueDto(timeValue);
            timeValuesDtos.add(timeValueDto);
        }
        return timeValuesDtos;
    }

    @Override
    public List<TimeValueDto> retrieveTimeValuesByGranularityInIndicatorPublished(ServiceContext ctx, String indicatorUuid, TimeGranularityEnum granularity) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        List<TimeValue> timeValues = getIndicatorsDataService().retrieveTimeValuesByGranularityInIndicatorPublished(ctx, indicatorUuid, granularity);

        //Transform
        List<TimeValueDto> timeValuesDtos = new ArrayList<TimeValueDto>();
        for (TimeValue timeValue: timeValues) {
            TimeValueDto timeValueDto = do2DtoMapper.timeValueDoToTimeValueDto(timeValue);
            timeValuesDtos.add(timeValueDto);
        }
        return timeValuesDtos;
    }

    @Override
    public List<TimeValueDto> retrieveTimeValuesInIndicator(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        List<TimeValue> timeValues = getIndicatorsDataService().retrieveTimeValuesInIndicator(ctx, indicatorUuid, indicatorVersionNumber);
        
        //Transform
        List<TimeValueDto> timeValuesDtos = new ArrayList<TimeValueDto>();
        for (TimeValue timeValue: timeValues) {
            TimeValueDto timeValueDto = do2DtoMapper.timeValueDoToTimeValueDto(timeValue);
            timeValuesDtos.add(timeValueDto);
        }
        return timeValuesDtos;
    }

    @Override
    public List<TimeValueDto> retrieveTimeValuesInIndicatorPublished(ServiceContext ctx, String indicatorUuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        List<TimeValue> timeValues = getIndicatorsDataService().retrieveTimeValuesInIndicatorPublished(ctx, indicatorUuid);
        
        //Transform
        List<TimeValueDto> timeValuesDtos = new ArrayList<TimeValueDto>();
        for (TimeValue timeValue: timeValues) {
            TimeValueDto timeValueDto = do2DtoMapper.timeValueDoToTimeValueDto(timeValue);
            timeValuesDtos.add(timeValueDto);
        }
        return timeValuesDtos;
    }

    @Override
    public List<TimeValueDto> retrieveTimeValuesInIndicatorInstance(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        List<TimeValue> timeValues = getIndicatorsDataService().retrieveTimeValuesInIndicatorInstance(ctx, indicatorInstanceUuid);
        
        //Transform
        List<TimeValueDto> timeValuesDtos = new ArrayList<TimeValueDto>();
        for (TimeValue timeValue: timeValues) {
            TimeValueDto timeValueDto = do2DtoMapper.timeValueDoToTimeValueDto(timeValue);
            timeValuesDtos.add(timeValueDto);
        }
        return timeValuesDtos;
    }

    public IndicatorDto createIndicator(ServiceContext ctx, IndicatorDto indicatorDto) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_PRODUCCION, RoleEnum.TECNICO_APOYO_PRODUCCION);

        // Transform
        IndicatorVersion indicatorVersion = dto2DoMapper.indicatorDtoToDo(ctx, indicatorDto);

        // Create
        IndicatorVersion indicatorVersionCreated = getIndicatorsService().createIndicator(ctx, indicatorVersion);

        // Transform to Dto
        indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersionCreated);
        return indicatorDto;
    }

    public IndicatorDto retrieveIndicator(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        IndicatorVersion indicatorVersion = getIndicatorsService().retrieveIndicator(ctx, uuid, versionNumber);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersion);
        return indicatorDto;
    }

    @Override
    public IndicatorDto retrieveIndicatorPublished(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        IndicatorVersion indicatorVersion = getIndicatorsService().retrieveIndicatorPublished(ctx, uuid);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersion);
        return indicatorDto;
    }

    public IndicatorDto retrieveIndicatorByCode(ServiceContext ctx, String code, String versionNumber) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        IndicatorVersion indicatorVersion = getIndicatorsService().retrieveIndicatorByCode(ctx, code, versionNumber);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersion);
        return indicatorDto;
    }

    public IndicatorDto retrieveIndicatorPublishedByCode(ServiceContext ctx, String code) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        IndicatorVersion indicatorVersion = getIndicatorsService().retrieveIndicatorPublishedByCode(ctx, code);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersion);
        return indicatorDto;
    }

    public void deleteIndicator(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_PRODUCCION, RoleEnum.TECNICO_APOYO_PRODUCCION);

        // Delete
        getIndicatorsService().deleteIndicator(ctx, uuid);
    }

    public IndicatorDto updateIndicator(ServiceContext ctx, IndicatorDto indicatorDto) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_PRODUCCION, RoleEnum.TECNICO_APOYO_PRODUCCION);

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

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_PRODUCCION, RoleEnum.TECNICO_APOYO_PRODUCCION);

        IndicatorVersion indicatorVersion = getIndicatorsService().sendIndicatorToProductionValidation(ctx, uuid);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersion);
        return indicatorDto;
    }

    @Override
    public IndicatorDto rejectIndicatorProductionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_PRODUCCION);

        IndicatorVersion indicatorVersion = getIndicatorsService().rejectIndicatorProductionValidation(ctx, uuid);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersion);
        return indicatorDto;
    }

    @Override
    public IndicatorDto sendIndicatorToDiffusionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_PRODUCCION);

        IndicatorVersion indicatorVersion = getIndicatorsService().sendIndicatorToDiffusionValidation(ctx, uuid);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersion);
        return indicatorDto;
    }

    @Override
    public IndicatorDto rejectIndicatorDiffusionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_DIFUSION);

        IndicatorVersion indicatorVersion = getIndicatorsService().rejectIndicatorDiffusionValidation(ctx, uuid);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersion);
        return indicatorDto;
    }

    @Override
    public IndicatorDto publishIndicator(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_DIFUSION, RoleEnum.TECNICO_APOYO_DIFUSION);

        IndicatorVersion indicatorVersion = getIndicatorsService().publishIndicator(ctx, uuid);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersion);
        return indicatorDto;
    }

    @Override
    public IndicatorDto archiveIndicator(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_DIFUSION);

        IndicatorVersion indicatorVersion = getIndicatorsService().archiveIndicator(ctx, uuid);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersion);
        return indicatorDto;
    }

    @Override
    public IndicatorDto versioningIndicator(ServiceContext ctx, String uuid, VersionTypeEnum versionType) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_PRODUCCION, RoleEnum.TECNICO_APOYO_PRODUCCION);

        // Versioning
        IndicatorVersion indicatorVersionCreated = getIndicatorsService().versioningIndicator(ctx, uuid, versionType);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersionCreated);
        return indicatorDto;
    }

    @Override
    public MetamacCriteriaResult<IndicatorDto> findIndicators(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getIndicatorVersionCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<IndicatorVersion> result = getIndicatorsService().findIndicators(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<IndicatorDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultIndicator(result, sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public MetamacCriteriaResult<IndicatorDto> findIndicatorsPublished(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getIndicatorVersionCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<IndicatorVersion> result = getIndicatorsService().findIndicatorsPublished(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<IndicatorDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultIndicator(result, sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public DataSourceDto createDataSource(ServiceContext ctx, String indicatorUuid, DataSourceDto dataSourceDto) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_PRODUCCION, RoleEnum.TECNICO_APOYO_PRODUCCION);

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

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        DataSource dataSource = getIndicatorsService().retrieveDataSource(ctx, uuid);

        // Transform
        DataSourceDto dataSourceDto = do2DtoMapper.dataSourceDoToDto(dataSource);
        return dataSourceDto;
    }

    @Override
    public void deleteDataSource(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_PRODUCCION, RoleEnum.TECNICO_APOYO_PRODUCCION);

        // Delete
        getIndicatorsService().deleteDataSource(ctx, uuid);
    }

    @Override
    public List<DataSourceDto> retrieveDataSourcesByIndicator(ServiceContext ctx, String indicatorUuid, String indicatorVersion) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

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

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_PRODUCCION, RoleEnum.TECNICO_APOYO_PRODUCCION);

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

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        QuantityUnit quantityUnit = getIndicatorsService().retrieveQuantityUnit(ctx, uuid);

        // Transform
        QuantityUnitDto quantityUnitDto = do2DtoMapper.quantityUnitDoToDto(quantityUnit);
        return quantityUnitDto;
    }

    @Override
    public List<QuantityUnitDto> retrieveQuantityUnits(ServiceContext ctx) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

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

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        Subject subject = getIndicatorsService().retrieveSubject(ctx, code);

        // Transform
        SubjectDto subjectDto = do2DtoMapper.subjectDoToDto(subject);
        return subjectDto;
    }

    @Override
    public List<SubjectDto> retrieveSubjects(ServiceContext ctx) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

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

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

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

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Service call
        List<DataDefinition> dataDefs = getIndicatorsDataService().retrieveDataDefinitions(ctx);

        // Transform
        List<DataDefinitionDto> dtos = new ArrayList<DataDefinitionDto>();
        for (DataDefinition basic : dataDefs) {
            dtos.add(do2DtoMapper.dataDefinitionDoToDto(basic));
        }
        return dtos;
    }

    @Override
    public DataDefinitionDto retrieveDataDefinition(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Service call
        DataDefinition dataDef = getIndicatorsDataService().retrieveDataDefinition(ctx, uuid);

        // Transform
        DataDefinitionDto dto = null;
        if (dataDef != null) {
            dto = do2DtoMapper.dataDefinitionDoToDto(dataDef);
        }
        return dto;
    }

    @Override
    public DataStructureDto retrieveDataStructure(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Service call
        DataStructure dataStruc = getIndicatorsDataService().retrieveDataStructure(ctx, uuid);

        // Transform
        return do2DtoMapper.dataStructureDoToDto(dataStruc);
    }

    @Override
    public void populateIndicatorData(ServiceContext ctx, String indicatorUuid, String version) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        getIndicatorsDataService().populateIndicatorData(ctx, indicatorUuid, version);
    }

    @Override
    public Map<String, ObservationDto> findObservationsByDimensionsInIndicatorPublished(ServiceContext ctx, String indicatorUuid, List<ConditionDimensionDto> conditions) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        Map<String, ObservationDto> observations = getIndicatorsDataService().findObservationsByDimensionsInIndicatorPublished(ctx, indicatorUuid, conditions);

        return observations;
    }

    @Override
    public Map<String, ObservationExtendedDto> findObservationsExtendedByDimensionsInIndicatorPublished(ServiceContext ctx, String indicatorUuid, List<ConditionDimensionDto> conditions)
            throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        Map<String, ObservationExtendedDto> observations = getIndicatorsDataService().findObservationsExtendedByDimensionsInIndicatorPublished(ctx, indicatorUuid, conditions);

        return observations;
    }

    @Override
    public Map<String, ObservationDto> findObservationsByDimensionsInIndicatorInstance(ServiceContext ctx, String indicatorInstanceUuid, List<ConditionDimensionDto> conditions)
            throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        Map<String, ObservationDto> observations = getIndicatorsDataService().findObservationsByDimensionsInIndicatorInstance(ctx, indicatorInstanceUuid, conditions);

        return observations;
    }

    @Override
    public Map<String, ObservationExtendedDto> findObservationsExtendedByDimensionsInIndicatorInstance(ServiceContext ctx, String indicatorInstanceUuid, List<ConditionDimensionDto> conditions)
            throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        Map<String, ObservationExtendedDto> observations = getIndicatorsDataService().findObservationsExtendedByDimensionsInIndicatorInstance(ctx, indicatorInstanceUuid, conditions);

        return observations;
    }

    /**
     * Checks user has access to indicators system by code
     */
    private void checkAccessIndicatorsSystemByCode(ServiceContext ctx, String code, RoleEnum... roles) throws MetamacException {
        SecurityUtils.checkResourceIndicatorsSystemAllowed(ctx, code, roles);
    }

    /**
     * Retrieves indicators system by uuid and checks has access
     */
    private void checkAccessIndicatorsSystemByUuid(ServiceContext ctx, String uuid, RoleEnum... roles) throws MetamacException {
        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().retrieveIndicatorsSystem(ctx, uuid, null);
        checkAccessIndicatorsSystem(ctx, indicatorsSystemVersion.getIndicatorsSystem(), roles);
    }

    /**
     * Checks user has access to indicators system
     */
    private void checkAccessIndicatorsSystem(ServiceContext ctx, IndicatorsSystem indicatorsSystem, RoleEnum... roles) throws MetamacException {
        checkAccessIndicatorsSystemByCode(ctx, indicatorsSystem.getCode(), roles);
    }
}
