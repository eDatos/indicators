package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.criteria.SculptorCriteria;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.enume.domain.IstacTimeGranularityEnum;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.criteria.GeographicalValueCriteriaOrderEnum;
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
import es.gobcan.istac.indicators.core.domain.UnitMultiplier;
import es.gobcan.istac.indicators.core.domain.jsonstat.JsonStatData;
import es.gobcan.istac.indicators.core.dto.DataDefinitionDto;
import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.DataStructureDto;
import es.gobcan.istac.indicators.core.dto.DimensionDto;
import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemStructureDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemSummaryDto;
import es.gobcan.istac.indicators.core.dto.PublishIndicatorResultDto;
import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.SubjectDto;
import es.gobcan.istac.indicators.core.dto.TimeGranularityDto;
import es.gobcan.istac.indicators.core.dto.TimeValueDto;
import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;
import es.gobcan.istac.indicators.core.enume.domain.RoleEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.mapper.Do2DtoMapper;
import es.gobcan.istac.indicators.core.mapper.Dto2DoMapper;
import es.gobcan.istac.indicators.core.mapper.MetamacCriteria2SculptorCriteriaMapper;
import es.gobcan.istac.indicators.core.mapper.SculptorCriteria2MetamacCriteriaMapper;
import es.gobcan.istac.indicators.core.security.SecurityUtils;
import es.gobcan.istac.indicators.core.serviceimpl.util.PublishIndicatorResult;

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

    @Autowired
    private ConfigurationService                   configurationService;

    public IndicatorsServiceFacadeImpl() {
    }

    @Override
    public IndicatorsSystemDto createIndicatorsSystem(ServiceContext ctx, IndicatorsSystemDto indicatorsSystemDto) throws MetamacException {

        // Security (role and access to this indicators system)
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_SISTEMA_INDICADORES);
        checkAccessIndicatorsSystemByCode(ctx, indicatorsSystemDto.getCode(), RoleEnum.TECNICO_SISTEMA_INDICADORES);

        // Transform
        IndicatorsSystemVersion indicatorsSystemVersion = dto2DoMapper.indicatorsSystemDtoToDo(ctx, indicatorsSystemDto);

        // Create
        IndicatorsSystemVersion indicatorsSystemVersionCreated = getIndicatorsSystemsService().createIndicatorsSystem(ctx, indicatorsSystemVersion);

        // Transform to Dto
        return do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersionCreated);
    }

    @Override
    public IndicatorsSystemDto retrieveIndicatorsSystemByCode(ServiceContext ctx, String code, String versionNumber) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().retrieveIndicatorsSystemByCode(ctx, code, versionNumber);

        // Transform to Dto
        return do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
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

    @Override
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
        return do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
    }

    @Override
    public IndicatorsSystemDto rejectIndicatorsSystemProductionValidation(ServiceContext ctx, String uuid) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ADMINISTRADOR);

        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().rejectIndicatorsSystemProductionValidation(ctx, uuid);

        // Transform to Dto
        return do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
    }

    @Override
    public IndicatorsSystemDto sendIndicatorsSystemToDiffusionValidation(ServiceContext ctx, String uuid) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ADMINISTRADOR);

        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().sendIndicatorsSystemToDiffusionValidation(ctx, uuid);

        // Transform to Dto
        return do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
    }

    @Override
    public IndicatorsSystemDto rejectIndicatorsSystemDiffusionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Security (role and access to this indicators system)
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_DIFUSION);
        checkAccessIndicatorsSystemByUuid(ctx, uuid, RoleEnum.TECNICO_DIFUSION);

        // Reject
        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().rejectIndicatorsSystemDiffusionValidation(ctx, uuid);

        // Transform to Dto
        return do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
    }

    @Override
    public IndicatorsSystemDto publishIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException {

        // Security (role and access to this indicators system)
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_DIFUSION, RoleEnum.TECNICO_APOYO_DIFUSION);
        checkAccessIndicatorsSystemByUuid(ctx, uuid, RoleEnum.TECNICO_DIFUSION, RoleEnum.TECNICO_APOYO_DIFUSION);

        // Publish
        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().publishIndicatorsSystem(ctx, uuid);

        // Transform to Dto
        return do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
    }

    @Override
    public IndicatorsSystemDto archiveIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException {

        // Security (role and access to this indicators system)
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_DIFUSION);
        checkAccessIndicatorsSystemByUuid(ctx, uuid, RoleEnum.TECNICO_DIFUSION);

        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().archiveIndicatorsSystem(ctx, uuid);

        // Transform to Dto
        return do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
    }

    @Override
    public IndicatorsSystemDto versioningIndicatorsSystem(ServiceContext ctx, String uuid, VersionTypeEnum versionType) throws MetamacException {

        // Security (role and access to this indicators system)
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_SISTEMA_INDICADORES);
        checkAccessIndicatorsSystemByUuid(ctx, uuid, RoleEnum.TECNICO_SISTEMA_INDICADORES);

        // Versioning
        IndicatorsSystemVersion indicatorsSystemVersionCreated = getIndicatorsSystemsService().versioningIndicatorsSystem(ctx, uuid, versionType);

        // Transform to Dto
        return do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersionCreated);
    }

    @Override
    public MetamacCriteriaResult<IndicatorsSystemSummaryDto> findIndicatorsSystems(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getIndicatorsSystemVersionCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<IndicatorsSystemVersion> result = getIndicatorsSystemsService().findIndicatorsSystems(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        return sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultIndicatorsSystemSummary(result, sculptorCriteria.getPageSize());
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
        return do2DtoMapper.dimensionDoToDto(dimension);
    }

    @Override
    public DimensionDto retrieveDimension(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        Dimension dimension = getIndicatorsSystemsService().retrieveDimension(ctx, uuid);

        // Transform
        return do2DtoMapper.dimensionDoToDto(dimension);
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
        return do2DtoMapper.dimensionDoToDto(dimension);
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
        return do2DtoMapper.dimensionDoToDto(dimension);
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
        return do2DtoMapper.indicatorInstanceDoToDto(indicatorInstance);
    }

    @Override
    public IndicatorInstanceDto retrieveIndicatorInstance(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        IndicatorInstance indicatorInstance = getIndicatorsSystemsService().retrieveIndicatorInstance(ctx, uuid);

        // Transform
        return do2DtoMapper.indicatorInstanceDoToDto(indicatorInstance);
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
        return do2DtoMapper.indicatorInstanceDoToDto(indicatorInstance);
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
        return do2DtoMapper.indicatorInstanceDoToDto(indicatorInstance);
    }

    @Override
    public List<String> exportIndicatorsSystemPublishedToDsplFiles(ServiceContext ctx, String indicatorsSystemUuid, InternationalStringDto title, InternationalStringDto description,
            boolean mergeTimeGranularities) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Transform
        InternationalString intTitle = dto2DoMapper.internationalStringDtoToDo(ctx, title, null);
        InternationalString intDescription = dto2DoMapper.internationalStringDtoToDo(ctx, description, null);

        // Service call
        return getDsplExporterService().exportIndicatorsSystemPublishedToDsplFiles(ctx, indicatorsSystemUuid, intTitle, intDescription, mergeTimeGranularities);
    }

    @Override
    public List<GeographicalValueDto> retrieveGeographicalValuesByGranularityInIndicator(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber, String granularityUuid)
            throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        List<GeographicalValue> geographicalValues = getIndicatorsCoverageService().retrieveGeographicalValuesByGranularityInIndicator(ctx, indicatorUuid, indicatorVersionNumber, granularityUuid);

        // Transform
        List<GeographicalValueDto> geographicalValueDtos = new ArrayList<GeographicalValueDto>();
        for (GeographicalValue geoValue : geographicalValues) {
            geographicalValueDtos.add(do2DtoMapper.geographicalValueDoToDto(geoValue));
        }
        return geographicalValueDtos;
    }

    @Override
    public List<GeographicalGranularityDto> retrieveGeographicalGranularitiesInIndicator(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        IndicatorVersion indicatorVersion = getIndicatorsService().retrieveIndicator(ctx, indicatorUuid, indicatorVersionNumber);

        List<GeographicalGranularity> geographicalGranularities = getIndicatorsCoverageService().retrieveGeographicalGranularitiesInIndicatorVersion(ctx, indicatorVersion);

        // Transform
        List<GeographicalGranularityDto> geographicalGranularityDtos = new ArrayList<GeographicalGranularityDto>();
        for (GeographicalGranularity geographicalGranularity : geographicalGranularities) {
            geographicalGranularityDtos.add(do2DtoMapper.geographicalGranularityDoToDto(geographicalGranularity));
        }

        return geographicalGranularityDtos;
    }

    @Override
    public List<TimeGranularityDto> retrieveTimeGranularitiesInIndicator(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        List<TimeGranularity> timeGranularities = getIndicatorsCoverageService().retrieveTimeGranularitiesInIndicator(ctx, indicatorUuid, indicatorVersionNumber);

        // Transform
        List<TimeGranularityDto> timeGranularitiesDtos = new ArrayList<TimeGranularityDto>();
        for (TimeGranularity granularity : timeGranularities) {
            TimeGranularityDto timeGranularityDto = do2DtoMapper.timeGranularityDoToTimeGranularityDto(granularity);
            timeGranularitiesDtos.add(timeGranularityDto);
        }
        return timeGranularitiesDtos;
    }

    @Override
    public List<TimeValueDto> retrieveTimeValuesByGranularityInIndicator(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber, IstacTimeGranularityEnum granularity)
            throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        List<TimeValue> timeValues = getIndicatorsCoverageService().retrieveTimeValuesByGranularityInIndicator(ctx, indicatorUuid, indicatorVersionNumber, granularity);

        // Transform
        List<TimeValueDto> timeValuesDtos = new ArrayList<TimeValueDto>();
        for (TimeValue timeValue : timeValues) {
            TimeValueDto timeValueDto = do2DtoMapper.timeValueDoToTimeValueDto(timeValue);
            timeValuesDtos.add(timeValueDto);
        }
        return timeValuesDtos;
    }

    @Override
    public IndicatorDto createIndicator(ServiceContext ctx, IndicatorDto indicatorDto) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_PRODUCCION, RoleEnum.TECNICO_APOYO_PRODUCCION);

        // Transform
        IndicatorVersion indicatorVersion = dto2DoMapper.indicatorDtoToDo(ctx, indicatorDto);

        // Create
        IndicatorVersion indicatorVersionCreated = getIndicatorsService().createIndicator(ctx, indicatorVersion);

        // Transform to Dto
        return do2DtoMapper.indicatorDoToDto(ctx, indicatorVersionCreated);
    }

    @Override
    public IndicatorDto retrieveIndicator(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        IndicatorVersion indicatorVersion = getIndicatorsService().retrieveIndicator(ctx, uuid, versionNumber);

        // Transform to Dto
        return do2DtoMapper.indicatorDoToDto(ctx, indicatorVersion);
    }

    @Override
    public IndicatorDto retrieveIndicatorByCode(ServiceContext ctx, String code, String versionNumber) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        IndicatorVersion indicatorVersion = getIndicatorsService().retrieveIndicatorByCode(ctx, code, versionNumber);

        // Transform to Dto
        return do2DtoMapper.indicatorDoToDto(ctx, indicatorVersion);
    }

    @Override
    public void deleteIndicator(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_PRODUCCION, RoleEnum.TECNICO_APOYO_PRODUCCION);

        // Delete
        getIndicatorsService().deleteIndicator(ctx, uuid);
    }

    @Override
    public IndicatorDto updateIndicator(ServiceContext ctx, IndicatorDto indicatorDto) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_PRODUCCION, RoleEnum.TECNICO_APOYO_PRODUCCION);

        // Transform
        IndicatorVersion indicatorVersion = dto2DoMapper.indicatorDtoToDo(ctx, indicatorDto);

        // Update
        indicatorVersion = getIndicatorsService().updateIndicatorVersion(ctx, indicatorVersion);

        // Transform to Dto
        return do2DtoMapper.indicatorDoToDto(ctx, indicatorVersion);
    }

    @Override
    public IndicatorDto sendIndicatorToProductionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_PRODUCCION, RoleEnum.TECNICO_APOYO_PRODUCCION);

        IndicatorVersion indicatorVersion = getIndicatorsService().sendIndicatorToProductionValidation(ctx, uuid);

        // Transform to Dto
        return do2DtoMapper.indicatorDoToDto(ctx, indicatorVersion);
    }

    @Override
    public IndicatorDto rejectIndicatorProductionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_PRODUCCION);

        IndicatorVersion indicatorVersion = getIndicatorsService().rejectIndicatorProductionValidation(ctx, uuid);

        // Transform to Dto
        return do2DtoMapper.indicatorDoToDto(ctx, indicatorVersion);
    }

    @Override
    public IndicatorDto sendIndicatorToDiffusionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_PRODUCCION);

        IndicatorVersion indicatorVersion = getIndicatorsService().sendIndicatorToDiffusionValidation(ctx, uuid);

        // Transform to Dto
        return do2DtoMapper.indicatorDoToDto(ctx, indicatorVersion);
    }

    @Override
    public IndicatorDto rejectIndicatorDiffusionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_DIFUSION);

        IndicatorVersion indicatorVersion = getIndicatorsService().rejectIndicatorDiffusionValidation(ctx, uuid);

        // Transform to Dto
        return do2DtoMapper.indicatorDoToDto(ctx, indicatorVersion);
    }

    @Override
    public PublishIndicatorResultDto publishIndicator(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_DIFUSION, RoleEnum.TECNICO_APOYO_DIFUSION);

        PublishIndicatorResult publishIndicatorResult = getIndicatorsService().publishIndicator(ctx, uuid);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(ctx, publishIndicatorResult.getIndicatorVersion());
        PublishIndicatorResultDto publishIndicatorResultDto = new PublishIndicatorResultDto();
        publishIndicatorResultDto.setIndicator(indicatorDto);
        publishIndicatorResultDto.setPublicationFailedReason(publishIndicatorResult.getPublicationFailedReason());
        return publishIndicatorResultDto;
    }

    @Override
    public IndicatorDto archiveIndicator(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_DIFUSION);

        IndicatorVersion indicatorVersion = getIndicatorsService().archiveIndicator(ctx, uuid);

        // Transform to Dto
        return do2DtoMapper.indicatorDoToDto(ctx, indicatorVersion);
    }

    @Override
    public IndicatorDto versioningIndicator(ServiceContext ctx, String uuid, VersionTypeEnum versionType) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_PRODUCCION, RoleEnum.TECNICO_APOYO_PRODUCCION);

        // Versioning
        IndicatorVersion indicatorVersionCreated = getIndicatorsService().versioningIndicator(ctx, uuid, versionType);

        // Transform to Dto
        return do2DtoMapper.indicatorDoToDto(ctx, indicatorVersionCreated);
    }

    @Override
    public MetamacCriteriaResult<IndicatorSummaryDto> findIndicators(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getIndicatorVersionCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<IndicatorVersion> result = getIndicatorsService().findIndicators(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        return sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultIndicatorSummary(ctx, result, sculptorCriteria.getPageSize());
    }

    @Override
    public String exportIndicatorsTsv(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getIndicatorVersionCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Export
        return getIndicatorsService().exportIndicatorsTsv(ctx, sculptorCriteria.getConditions());
    }

    @Override
    public void disableNotifyPopulationErrors(ServiceContext ctx, String indicatorVersionUuid) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ADMINISTRADOR);

        getIndicatorsService().disableNotifyPopulationErrors(ctx, indicatorVersionUuid);
    }

    @Override
    public void enableNotifyPopulationErrors(ServiceContext ctx, String indicatorVersionUuid) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ADMINISTRADOR);

        getIndicatorsService().enableNotifyPopulationErrors(ctx, indicatorVersionUuid);
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
        return do2DtoMapper.dataSourceDoToDto(dataSource);
    }

    @Override
    public DataSourceDto retrieveDataSource(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        DataSource dataSource = getIndicatorsService().retrieveDataSource(ctx, uuid);

        // Transform
        return do2DtoMapper.dataSourceDoToDto(dataSource);
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
        return do2DtoMapper.dataSourceDoToDto(dataSource);
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
    public List<String> retrieveDataDefinitionsOperationsCodes(ServiceContext ctx) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Service call
        return getIndicatorsDataService().retrieveDataDefinitionsOperationsCodes(ctx);
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
    public List<DataDefinitionDto> findDataDefinitionsByOperationCode(ServiceContext ctx, String operationCode) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Service call
        List<DataDefinition> dataDefs = getIndicatorsDataService().findDataDefinitionsByOperationCode(ctx, operationCode);

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
    public DataStructureDto retrieveJsonStatData(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Service call

        // TODO EDATOS-3388 Clean this!
        List<String> jsonFiles = new ArrayList<>();

        /* https://ibestat.caib.es/ibestat/page?p=px_tablas&nodeId=f58f0937-c64f-469d-bad5-99f29bbb59ce&path=economia%2FTURISMO%2F02.%20Gasto%20y%20perfil%20de%20los%20turistas%20(EGATUR) */
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/68904221-f893-456c-8046-261150ba9a2e/I208004_n100.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/3ace5e2c-4e99-4b9b-bf2d-24aa10db34bb/I208004_n101.json");
        jsonFiles.add(" https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/97351f7e-c887-4b66-8d2f-1439db56f371/I208004_n103.json");

        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/db2e560e-ef71-4241-a8a2-a24a9b8f51e7/I208004_n001.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/755a8af8-2b59-41ee-9c2d-9aa0f4f8509f/I208004_n002.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/8dcba641-0159-4d51-8ced-2d0d38e5ea33/I208004_n003.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/50c1c57b-cdbc-45b5-96d8-3df669e28fb0/I208004_n004.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/c7a0ef55-54f4-4590-86f4-6ab48f0efa11/I208004_n005.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/7472d606-7f0d-4265-b04d-c13f1161e534/I208004_n300.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/6fff41ac-1d36-4432-83de-35036a5b8d84/I208004_n301.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/c44fbf3f-7d75-4f1d-9bdc-b489ab7f3f09/I208004_n302.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/2a9b9a08-5b9b-4015-8d01-1a7e614d5832/I208004_n303.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/8632ca55-048b-4b2f-84c3-45a9ac9ea47a/I208004_n304.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/f49f81da-457f-4b0a-8d4e-eb666d97029f/I208004_n305.json");

        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/d77cc37c-4f34-494e-a044-66005a4aa664/I208004_0001.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/cc2971fd-3360-4119-b80e-9474f286d0e5/I208004_0002.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/27df0278-f444-4dd9-8dcf-edba3818748e/I208004_0003.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/68cbee2f-1e78-459c-b184-53cf961f4363/I208004_0004.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/01fe67ee-ec5e-46ac-bf60-f88fae23f353/I208004_0101.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/f9fa867b-5388-4d4a-b64b-44f3152afc46/I208004_0102.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/28a88a88-3f27-4fab-8a2e-1e41ab1269c6/I208004_0103.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/3dc30799-722a-472f-9484-879662158df8/I208004_0104.json");

        /* https://ibestat.caib.es/ibestat/page?p=px_tablas&nodeId=9c775c09-f186-4f34-9234-73946cff3ef1&path=economia%2FPRECIOS%20Y%20SALARIOS%2FIPC */
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/0bfe0604-fbde-4038-adef-868fbd5dd43a/E30138_16000.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/5296ceb0-8b61-439f-857d-e00e189a7954/E30138_16001.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/f6852013-3a4f-4878-adda-ae3900aa9344/E30138_16002.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/c2618af7-7b42-4510-b759-0f4ae59f41e6/E30138_16003.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/1ff5c1db-9132-4843-bd5d-c2acd45425f4/E30138_16004.json");

        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/3f05fe7c-1220-4eba-b114-516bb75ee4c3/E30138_11001.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/d2464d33-723e-4f3b-aba4-81134e9985e6/E30138_11002.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/567dc27d-b70b-405f-903c-3d7b2ce222da/E30138_11003.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/bd237eb4-11ca-4d44-bb42-459e4eff6e8a/E30138_11004.json");

        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/33e39ca5-aaab-4edf-b649-6a6288284749/E30138_06001.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/e3244b9b-e128-4bd3-8882-dfacabc3a54f/E30138_06002.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/002554f5-6dd9-4408-a115-cd3a0a5991b5/E30138_06003.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/eae3cd98-7af8-457a-b8fa-faef80d16fa5/E30138_06004.json");

        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/3899783d-f82d-4fc4-b96c-743d16045c87/E30138_01001.json");

        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/e22cba86-59d3-45ad-b7b1-2b6256186088/E30138_92001.json");

        /* https://ibestat.caib.es/ibestat/estadistiques/societat/seguretat-justicia/menors-condemnats/55322655-b6eb-439e-b5ce-d4a8cc8a1a8c */
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/96f8516e-b9ee-4120-ad33-a76e6b71d856/E30467_00001.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/c0058c2d-6a0e-4261-9629-db2595d32434/E30467_00018.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/51a3e5ab-f2f6-423c-90d9-5c66c7c68e80/E30467_00006.json");

        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/60f09987-e313-47c9-8f81-62d4e6618923/E30467_00010.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/bafb7e95-7e01-4330-a4fc-f41c4613eb44/E30467_00011.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/1fbc0ed9-2fcc-4b73-9307-4c10eabfb5db/E30467_00016.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/2656d0a5-9077-4988-a10e-1289326abf1b/E30467_00003.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/1db979d8-8a85-41fa-a881-38d45dfe3574/E30467_00002.json");

        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/f8ab3aea-7a45-468f-8ff9-fc0ec29a9e8c/E30467_00012.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/e0bb874b-ff75-4b4e-8791-31013dcb1a8b/E30467_00013.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/432ee507-5ee4-4422-bd74-b4b40dc35287/E30467_00004.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/e39eacff-067f-49da-a7fe-03aeef05fd4a/E30467_00014.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/8b04fb3b-9f6b-4c18-9246-007a63c08c0f/E30467_00015.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/18134344-6a29-4cc9-be3e-57b5fc641c0f/E30467_00017.json");

        /* https://ibestat.caib.es/ibestat/estadistiques/economia/empreses/demografia-empresarial/a11e3c92-37e6-4973-98c1-c8abb7006e84 */
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/bca7800e-ce9c-4ae7-8e90-2479d5b98832/I216019_0009.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/9f1a1ddd-ab22-47bf-a3e5-faaf6e0ef9f4/I216019_0010.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/654e8406-3029-4e22-8b41-06c876f047a0/I216019_0011.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/898714f0-bfa5-4dc8-b711-e6155b78bcee/I216019_0012.json");

        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/b562cd3e-2115-4437-876a-1bac5b04d3d0/I216019_0001.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/0b937a53-543e-4f35-b6f9-dc714e2f45a4/I216019_0002.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/3dc5b6ef-81dd-457d-9232-cdd1a6c423c7/I216019_0007.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/604787bd-5e0e-45c6-bd56-efb659c456fe/I216019_0008.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/857af307-f07d-4080-8be9-5ca75b8b0e5b/I216019_0013.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/78bdb0d5-01db-470c-9bef-8ad3f58da93f/I216019_0003.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/5d02c0ac-65ec-4fdb-9f49-e2a719946c64/I216019_0004.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/d993e474-c30d-43f9-80ad-5edacba283ad/I216019_0014.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/7ee21236-c497-480e-a823-311993b16319/I216019_0005.json");
        jsonFiles.add("https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/60c2b323-a1d5-4b40-a5ed-42fd117bb0aa/I216019_0006.json");

        JsonStatData jsonStatDataStructure = new JsonStatData();
        for (String jsonFile : jsonFiles) {
            jsonStatDataStructure = getIndicatorsDataService().retrieveJsonStatData(ctx, jsonFile);
            do2DtoMapper.dataStructureDoToDto(jsonStatDataStructure);
        }

        // Transform
        return do2DtoMapper.dataStructureDoToDto(jsonStatDataStructure);
    }

    @Override
    public List<IndicatorVersion> updateIndicatorsDataFromGpe(ServiceContext ctx) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        return getIndicatorsDataService().updateIndicatorsDataFromGpe(ctx);
    }

    @Override
    public List<IndicatorVersion> updateIndicatorsDataFromMetamac(ServiceContext ctx, SpecificRecordBase message) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        return getIndicatorsDataService().updateIndicatorsDataFromMetamac(ctx, message);
    }

    @Override
    public void planifyPopulateIndicatorData(ServiceContext ctx, String indicatorUuid) throws MetamacException {
        // Security
        SecurityUtils.canPopulateIndicatorData(ctx);

        getIndicatorsDataService().planifyPopulateIndicatorData(ctx, indicatorUuid);
    }

    // -------------------------------------------------------------------------------------------
    // GEOGRAPHICAL VALUES
    // -------------------------------------------------------------------------------------------

    @Override
    public GeographicalValueDto retrieveGeographicalValue(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        GeographicalValue geographicalValue = getIndicatorsSystemsService().retrieveGeographicalValue(ctx, uuid);

        // Transform
        return do2DtoMapper.geographicalValueDoToDto(geographicalValue);
    }

    @Override
    public MetamacCriteriaResult<GeographicalValueDto> findGeographicalValues(ServiceContext ctx, MetamacCriteria metamacCriteria) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Transform
        if (metamacCriteria == null) {
            metamacCriteria = new MetamacCriteria();
        }
        if (CollectionUtils.isEmpty(metamacCriteria.getOrdersBy())) {
            // Default order
            // Granularity
            MetamacCriteriaOrder orderGranularity = new MetamacCriteriaOrder();
            orderGranularity.setPropertyName(GeographicalValueCriteriaOrderEnum.GEOGRAPHICAL_GRANULARITY_UUID.name());
            orderGranularity.setType(OrderTypeEnum.ASC);
            metamacCriteria.getOrdersBy().add(orderGranularity);
            // Order in granularity
            MetamacCriteriaOrder orderOrderInGranularity = new MetamacCriteriaOrder();
            orderOrderInGranularity.setPropertyName(GeographicalValueCriteriaOrderEnum.ORDER.name());
            orderOrderInGranularity.setType(OrderTypeEnum.ASC);
            metamacCriteria.getOrdersBy().add(orderOrderInGranularity);
        }
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getGeographicalValueCriteriaMapper().metamacCriteria2SculptorCriteria(metamacCriteria);

        PagedResult<GeographicalValue> result = getIndicatorsSystemsService().findGeographicalValues(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        return sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultGeographicalValue(result, sculptorCriteria.getPageSize());
    }

    @Override
    public GeographicalValueDto createGeographicalValue(ServiceContext ctx, GeographicalValueDto geographicalValueDto) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ADMINISTRADOR);

        // Transform to entity
        GeographicalValue geographicalValue = dto2DoMapper.geographicalValueDtoToDo(ctx, geographicalValueDto);

        // Service call
        geographicalValue = getIndicatorsSystemsService().createGeographicalValue(ctx, geographicalValue);

        // Transform to Dto
        return do2DtoMapper.geographicalValueDoToDto(geographicalValue);
    }

    @Override
    public GeographicalValueDto updateGeographicalValue(ServiceContext ctx, GeographicalValueDto geographicalValueDto) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ADMINISTRADOR);

        // Transform to entity
        GeographicalValue geographicalValue = dto2DoMapper.geographicalValueDtoToDo(ctx, geographicalValueDto);

        // Service call
        geographicalValue = getIndicatorsSystemsService().updateGeographicalValue(ctx, geographicalValue);

        // Transform to Dto
        return do2DtoMapper.geographicalValueDoToDto(geographicalValue);
    }

    @Override
    public void deleteGeographicalValue(ServiceContext ctx, String uuid) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ADMINISTRADOR);

        // Service call
        try {
            getIndicatorsSystemsService().deleteGeographicalValue(ctx, uuid);
        } catch (PersistenceException e) {
            throw new MetamacException(e, ServiceExceptionType.GEOGRAPHICAL_VALUE_CAN_NOT_BE_REMOVED, uuid);
        }

    }

    // -------------------------------------------------------------------------------------------
    // GEOGRAPHICAL GRANULARITIES
    // -------------------------------------------------------------------------------------------

    @Override
    public MetamacCriteriaResult<GeographicalGranularityDto> findGeographicalGranularities(ServiceContext ctx, MetamacCriteria metamacCriteria) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getGeographicalGranularityCriteriaMapper().metamacCriteria2SculptorCriteria(metamacCriteria);

        // Find
        PagedResult<GeographicalGranularity> result = getIndicatorsSystemsService().findGeographicalGranularities(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        return sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultGeographicalGranularity(result, sculptorCriteria.getPageSize());
    }

    @Override
    public GeographicalGranularityDto retrieveGeographicalGranularity(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        GeographicalGranularity geographicalGranularity = getIndicatorsSystemsService().retrieveGeographicalGranularity(ctx, uuid);

        // Transform
        return do2DtoMapper.geographicalGranularityDoToDto(geographicalGranularity);
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
    public GeographicalGranularityDto createGeographicalGranularity(ServiceContext ctx, GeographicalGranularityDto geographicalGranularityDto) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ADMINISTRADOR);

        // Transform to entity
        GeographicalGranularity geographicalGranularity = dto2DoMapper.geographicalGranularityDtoToDo(ctx, geographicalGranularityDto);

        // Service call
        geographicalGranularity = getIndicatorsSystemsService().createGeographicalGranularity(ctx, geographicalGranularity);

        // Transform to Dto
        return do2DtoMapper.geographicalGranularityDoToDto(geographicalGranularity);
    }

    @Override
    public GeographicalGranularityDto updateGeographicalGranularity(ServiceContext ctx, GeographicalGranularityDto geographicalGranularityDto) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ADMINISTRADOR);

        // Transform to entity
        GeographicalGranularity geographicalGranularity = dto2DoMapper.geographicalGranularityDtoToDo(ctx, geographicalGranularityDto);

        // Service call
        geographicalGranularity = getIndicatorsSystemsService().updateGeographicalGranularity(ctx, geographicalGranularity);

        // Transform to Dto
        return do2DtoMapper.geographicalGranularityDoToDto(geographicalGranularity);
    }

    @Override
    public void deleteGeographicalGranularity(ServiceContext ctx, String uuid) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ADMINISTRADOR);

        // Service call
        try {
            getIndicatorsSystemsService().deleteGeographicalGranularity(ctx, uuid);
        } catch (PersistenceException e) {
            throw new MetamacException(e, ServiceExceptionType.GEOGRAPHICAL_GRANULARITY_CAN_NOT_BE_REMOVED, uuid);
        }
    }

    // -------------------------------------------------------------------------------------------
    // QUANTITY UNITS
    // -------------------------------------------------------------------------------------------

    @Override
    public MetamacCriteriaResult<QuantityUnitDto> findQuantityUnits(ServiceContext ctx, MetamacCriteria metamacCriteria) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getQuantityUnitCriteriaMapper().metamacCriteria2SculptorCriteria(metamacCriteria);

        // Find
        PagedResult<QuantityUnit> result = getIndicatorsService().findQuantityUnits(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        return sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultQuantiyUnit(result, sculptorCriteria.getPageSize());
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
    public QuantityUnitDto createQuantityUnit(ServiceContext ctx, QuantityUnitDto quantityUnitDto) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ADMINISTRADOR);

        // Transform to entity
        QuantityUnit quantityUnit = dto2DoMapper.quantityUnitDtoToDo(ctx, quantityUnitDto);

        // Service call
        quantityUnit = getIndicatorsService().createQuantityUnit(ctx, quantityUnit);

        // Transform to Dto
        return do2DtoMapper.quantityUnitDoToDto(quantityUnit);
    }

    @Override
    public QuantityUnitDto updateQuantityUnit(ServiceContext ctx, QuantityUnitDto quantityUnitDto) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ADMINISTRADOR);

        // Transform to entity
        QuantityUnit quantityUnit = dto2DoMapper.quantityUnitDtoToDo(ctx, quantityUnitDto);

        // Service call
        quantityUnit = getIndicatorsService().updateQuantityUnit(ctx, quantityUnit);

        // Transform to Dto
        return do2DtoMapper.quantityUnitDoToDto(quantityUnit);
    }

    @Override
    public void deleteQuantityUnit(ServiceContext ctx, String uuid) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ADMINISTRADOR);

        // Service call
        try {
            getIndicatorsService().deleteQuantityUnit(ctx, uuid);
        } catch (PersistenceException e) {
            throw new MetamacException(e, ServiceExceptionType.QUANTITY_UNIT_CAN_NOT_BE_REMOVED, uuid);
        }
    }

    // -------------------------------------------------------------------------------------------
    // UNIT MULTIPLIERS
    // -------------------------------------------------------------------------------------------

    @Override
    public MetamacCriteriaResult<UnitMultiplierDto> findUnitMultipliers(ServiceContext ctx, MetamacCriteria metamacCriteria) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getUnitMultiplierCriteriaMapper().metamacCriteria2SculptorCriteria(metamacCriteria);

        // Find
        PagedResult<UnitMultiplier> result = getIndicatorsService().findUnitMultipliers(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        return sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultUnitMultiplier(result, sculptorCriteria.getPageSize());
    }

    @Override
    public List<UnitMultiplierDto> retrieveUnitsMultipliers(ServiceContext ctx) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        List<UnitMultiplier> unitsMultipliers = getIndicatorsService().retrieveUnitsMultipliers(ctx);

        // Transform
        List<UnitMultiplierDto> unitsMultipliersDto = new ArrayList<UnitMultiplierDto>();
        for (UnitMultiplier unitMultiplier : unitsMultipliers) {
            unitsMultipliersDto.add(do2DtoMapper.unitMultiplierDoToDto(unitMultiplier));
        }

        return unitsMultipliersDto;
    }

    @Override
    public UnitMultiplierDto createUnitMultiplier(ServiceContext ctx, UnitMultiplierDto unitMultiplierDto) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ADMINISTRADOR);

        // Transform to entity
        UnitMultiplier unitMultiplier = dto2DoMapper.unitMultiplierDtoToDo(ctx, unitMultiplierDto);

        // Service call
        unitMultiplier = getIndicatorsService().createUnitMultiplier(ctx, unitMultiplier);

        // Transform to Dto
        return do2DtoMapper.unitMultiplierDoToDto(unitMultiplier);
    }

    @Override
    public UnitMultiplierDto updateUnitMultiplier(ServiceContext ctx, UnitMultiplierDto unitMultiplierDto) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ADMINISTRADOR);

        // Transform to entity
        UnitMultiplier unitMultiplier = dto2DoMapper.unitMultiplierDtoToDo(ctx, unitMultiplierDto);

        // Service call
        unitMultiplier = getIndicatorsService().updateUnitMultiplier(ctx, unitMultiplier);

        // Transform to Dto
        return do2DtoMapper.unitMultiplierDoToDto(unitMultiplier);
    }

    @Override
    public void deleteUnitMultiplier(ServiceContext ctx, String unitMultiplierUuid) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ADMINISTRADOR);

        // Service call
        getIndicatorsService().deleteUnitMultiplier(ctx, unitMultiplierUuid);
    }

    // -------------------------------------------------------------------------------------------
    // OTHER PRIVATE METHODS
    // -------------------------------------------------------------------------------------------

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
