package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
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
import org.siemac.metamac.core.common.ent.domain.InternationalStringProperties.InternationalStringProperty;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import es.gobcan.istac.indicators.core.domain.GeographicalValueProperties;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionProperties;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.QuantityUnit;
import es.gobcan.istac.indicators.core.domain.QuantityUnitProperties;
import es.gobcan.istac.indicators.core.domain.Subject;
import es.gobcan.istac.indicators.core.domain.TimeGranularity;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.domain.UnitMultiplier;
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
import es.gobcan.istac.indicators.core.enume.domain.TimeGranularityEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;
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

    private static Logger                          LOG = LoggerFactory.getLogger(IndicatorsServiceFacadeImpl.class);

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
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
        return indicatorsSystemDto;
    }

    @Override
    public IndicatorsSystemDto sendIndicatorsSystemToDiffusionValidation(ServiceContext ctx, String uuid) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ADMINISTRADOR);

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
    public List<TimeValueDto> retrieveTimeValuesByGranularityInIndicator(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber, TimeGranularityEnum granularity)
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
        return do2DtoMapper.indicatorDoToDto(indicatorVersionCreated);
    }

    @Override
    public IndicatorDto retrieveIndicator(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        IndicatorVersion indicatorVersion = getIndicatorsService().retrieveIndicator(ctx, uuid, versionNumber);

        // Transform to Dto
        return do2DtoMapper.indicatorDoToDto(indicatorVersion);
    }

    @Override
    public IndicatorDto retrieveIndicatorByCode(ServiceContext ctx, String code, String versionNumber) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Retrieve
        IndicatorVersion indicatorVersion = getIndicatorsService().retrieveIndicatorByCode(ctx, code, versionNumber);

        // Transform to Dto
        return do2DtoMapper.indicatorDoToDto(indicatorVersion);
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
        return do2DtoMapper.indicatorDoToDto(indicatorVersion);
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
    public PublishIndicatorResultDto publishIndicator(ServiceContext ctx, String uuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.TECNICO_DIFUSION, RoleEnum.TECNICO_APOYO_DIFUSION);

        PublishIndicatorResult publishIndicatorResult = getIndicatorsService().publishIndicator(ctx, uuid);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(publishIndicatorResult.getIndicatorVersion());
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
        return do2DtoMapper.indicatorDoToDto(indicatorVersionCreated);
    }

    @Override
    public MetamacCriteriaResult<IndicatorSummaryDto> findIndicators(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getIndicatorVersionCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);
        addEditionLanguageCondition(sculptorCriteria, IndicatorVersionProperties.title());

        // Find
        PagedResult<IndicatorVersion> result = getIndicatorsService().findIndicators(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        return sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultIndicatorSummary(result, sculptorCriteria.getPageSize());
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
    public void updateIndicatorsData(ServiceContext ctx) throws MetamacException {
        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        getIndicatorsDataService().updateIndicatorsData(ctx);
    }

    @Override
    public void populateIndicatorData(ServiceContext ctx, String indicatorUuid) throws MetamacException {

        // Security
        SecurityUtils.checkServiceOperationAllowed(ctx, RoleEnum.ANY_ROLE_ALLOWED);

        getIndicatorsDataService().populateIndicatorData(ctx, indicatorUuid);
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
        addEditionLanguageCondition(sculptorCriteria, GeographicalValueProperties.title(), GeographicalValueProperties.granularity().title());

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
        addEditionLanguageCondition(sculptorCriteria, QuantityUnitProperties.title());

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
            throw new MetamacException(ServiceExceptionType.QUANTITY_UNIT_CAN_NOT_BE_REMOVED, uuid);
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

    /**
     * Adds a condition to the {@link SculptorCriteria} to filter the results by the edition language. The filtering allows the query to be faster and allows the resources to be sorted by a specific
     * locale of an {@link InternationalString}.
     *
     * @param sculptorCriteria
     * @param properties
     * @throws MetamacException
     */
    @SuppressWarnings("rawtypes")
    private void addEditionLanguageCondition(SculptorCriteria sculptorCriteria, InternationalStringProperty... properties) throws MetamacException {
        String defaultLanguage = configurationService.retrieveLanguageDefault();
        for (InternationalStringProperty property : properties) {
            sculptorCriteria.getConditions().add(ConditionalCriteria.equal(property.texts().locale(), defaultLanguage));
        }
    }
}
