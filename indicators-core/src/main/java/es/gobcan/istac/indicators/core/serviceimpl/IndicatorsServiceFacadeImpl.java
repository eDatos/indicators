package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.IndicatorsConstants;
import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.ElementLevel;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionInformation;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersionInformation;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemStructureDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorStateEnum;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemStateEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersiontTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.mapper.Do2DtoMapper;
import es.gobcan.istac.indicators.core.mapper.Dto2DoMapper;
import es.gobcan.istac.indicators.core.serviceimpl.util.DoCopyUtils;
import es.gobcan.istac.indicators.core.serviceimpl.util.InvocationValidator;
import es.gobcan.istac.indicators.core.serviceimpl.util.ServiceUtils;

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

    /**
     * TODO qué datos se almacenarán? Los sistemas de indicadores se obtienen desde Gopestat, y aquí se almacenan como "referencias" a ellas
     */
    public IndicatorsSystemDto createIndicatorsSystem(ServiceContext ctx, IndicatorsSystemDto indicatorsSystemDto) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkCreateIndicatorsSystem(indicatorsSystemDto, null);
        checkIndicatorsSystemCodeUnique(ctx, indicatorsSystemDto.getCode(), null);
        checkIndicatorsSystemUriGopestatUnique(ctx, indicatorsSystemDto.getUriGopestat(), null);

        // Transform
        IndicatorsSystem indicatorsSystem = new IndicatorsSystem();
        dto2DoMapper.indicatorsSystemDtoToDo(indicatorsSystemDto, indicatorsSystem);
        indicatorsSystem.setDiffusionVersion(null);
        // Version in draft
        IndicatorsSystemVersion draftVersion = dto2DoMapper.indicatorsSystemDtoToDo(indicatorsSystemDto);
        draftVersion.setState(IndicatorsSystemStateEnum.DRAFT);
        draftVersion.setVersionNumber(ServiceUtils.generateVersionNumber(null, VersiontTypeEnum.MAJOR));

        // Create
        IndicatorsSystemVersion indicatorsSystemVersionCreated = getIndicatorsSystemsService().createIndicatorsSystemVersion(ctx, indicatorsSystem, draftVersion);

        // Transform to Dto
        indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersionCreated);
        return indicatorsSystemDto;
    }

    public IndicatorsSystemDto retrieveIndicatorsSystem(ServiceContext ctx, String uuid, String version) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorsSystem(uuid, version, null);

        // Retrieve version requested or last version
        IndicatorsSystemVersion indicatorsSystemVersion = null;
        if (version == null) {
            // Retrieve last version
            IndicatorsSystem indicatorsSystem = getIndicatorsSystemsService().retrieveIndicatorsSystem(ctx, uuid);
            version = indicatorsSystem.getProductionVersion() != null ? indicatorsSystem.getProductionVersion().getVersionNumber() : indicatorsSystem.getDiffusionVersion().getVersionNumber();
        }
        indicatorsSystemVersion = getIndicatorsSystemsService().retrieveIndicatorsSystemVersion(ctx, uuid, version);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
        return indicatorsSystemDto;
    }

    @Override
    public IndicatorsSystemDto retrieveIndicatorsSystemPublished(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorsSystemPublished(uuid, null);

        // Retrieve published version
        IndicatorsSystemVersion publishedIndicatorsSystemVersion = retrieveIndicatorsSystemStateInDiffusion(ctx, uuid, false);
        if (publishedIndicatorsSystemVersion == null || !IndicatorsSystemStateEnum.PUBLISHED.equals(publishedIndicatorsSystemVersion.getState())) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE, uuid, new IndicatorsSystemStateEnum[]{IndicatorsSystemStateEnum.PUBLISHED});
        }

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(publishedIndicatorsSystemVersion);
        return indicatorsSystemDto;
    }

    public IndicatorsSystemDto retrieveIndicatorsSystemByCode(ServiceContext ctx, String code) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorsSystemByCode(code, null);

        // Retrieve indicators system by code
        List<IndicatorsSystem> indicatorsSystems = getIndicatorsSystemsService().findIndicatorsSystems(ctx, code);
        if (indicatorsSystems.size() == 0) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND_WITH_CODE, code);
        } else if (indicatorsSystems.size() > 1) {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "Found more than one indicators system with code " + code);
        }

        IndicatorsSystem indicatorsSystem = indicatorsSystems.get(0);
        String lastVersion = indicatorsSystem.getProductionVersion() != null ? indicatorsSystem.getProductionVersion().getVersionNumber() : indicatorsSystem.getDiffusionVersion().getVersionNumber();
        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().retrieveIndicatorsSystemVersion(ctx, indicatorsSystem.getUuid(), lastVersion);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
        return indicatorsSystemDto;
    }

    public IndicatorsSystemDto retrieveIndicatorsSystemPublishedByCode(ServiceContext ctx, String code) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorsSystemPublishedByCode(code, null);

        // Retrieve indicators system by code
        List<IndicatorsSystem> indicatorsSystems = getIndicatorsSystemsService().findIndicatorsSystems(ctx, code);
        if (indicatorsSystems.size() == 0) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND_WITH_CODE, code);
        } else if (indicatorsSystems.size() > 1) {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "Found more than one indicators system with code " + code);
        }

        // Retrieve only published
        IndicatorsSystem indicatorsSystem = indicatorsSystems.get(0);
        if (indicatorsSystem.getDiffusionVersion() == null) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE, indicatorsSystem.getUuid(), new IndicatorsSystemStateEnum[]{IndicatorsSystemStateEnum.PUBLISHED});
        }
        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemsService().retrieveIndicatorsSystemVersion(ctx, indicatorsSystem.getUuid(),
                indicatorsSystem.getDiffusionVersion().getVersionNumber());
        if (!IndicatorsSystemStateEnum.PUBLISHED.equals(indicatorsSystemVersion.getState())) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE, indicatorsSystem.getUuid(), new IndicatorsSystemStateEnum[]{IndicatorsSystemStateEnum.PUBLISHED});
        }

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion);
        return indicatorsSystemDto;
    }

    @Override
    public IndicatorsSystemStructureDto retrieveIndicatorsSystemStructure(ServiceContext ctx, String uuid, String version) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorsSystemStructure(uuid, version, null);

        // Retrieve version requested or last version
        IndicatorsSystemVersion indicatorsSystemVersion = null;
        if (version == null) {
            // Retrieve last version
            IndicatorsSystem indicatorsSystem = getIndicatorsSystemsService().retrieveIndicatorsSystem(ctx, uuid);
            version = indicatorsSystem.getProductionVersion() != null ? indicatorsSystem.getProductionVersion().getVersionNumber() : indicatorsSystem.getDiffusionVersion().getVersionNumber();
        }
        indicatorsSystemVersion = getIndicatorsSystemsService().retrieveIndicatorsSystemVersion(ctx, uuid, version);

        // Builds structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = new IndicatorsSystemStructureDto();
        indicatorsSystemStructureDto.setUuid(uuid);
        indicatorsSystemStructureDto.setVersionNumber(indicatorsSystemVersion.getVersionNumber());
        indicatorsSystemStructureDto.getElements().addAll(do2DtoMapper.elementsLevelsDoToDto(indicatorsSystemVersion.getChildrenFirstLevel()));
        return indicatorsSystemStructureDto;
    }

    public void deleteIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkDeleteIndicatorsSystem(uuid, null);

        // Retrieve version in production
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorsSystemStateInProduction(ctx, uuid, true);

        // Delete whole indicators system or only last version
        if (IndicatorsConstants.VERSION_NUMBER_INITIAL.equals(indicatorsSystemVersion.getVersionNumber())) {
            // If indicators system is not published or archived, delete whole indicators system
            getIndicatorsSystemsService().deleteIndicatorsSystem(ctx, uuid);
        } else {
            getIndicatorsSystemsService().deleteIndicatorsSystemVersion(ctx, uuid, indicatorsSystemVersion.getVersionNumber());
            indicatorsSystemVersion.getIndicatorsSystem().setProductionVersion(null);
            getIndicatorsSystemsService().updateIndicatorsSystem(ctx, indicatorsSystemVersion.getIndicatorsSystem());
        }
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
        dto2DoMapper.indicatorsSystemDtoToDo(indicatorsSystemDto, indicatorsSystemInProduction);

        // Update
        getIndicatorsSystemsService().updateIndicatorsSystemVersion(ctx, indicatorsSystemInProduction);
    }

    @Override
    public void sendIndicatorsSystemToProductionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkSendIndicatorsSystemToProductionValidation(uuid, null);

        // Retrieve version in draft
        IndicatorsSystemVersion indicatorsSystemInProduction = retrieveIndicatorsSystemStateInProduction(ctx, uuid, false);
        if (indicatorsSystemInProduction == null
                || (!IndicatorsSystemStateEnum.DRAFT.equals(indicatorsSystemInProduction.getState()) && !IndicatorsSystemStateEnum.VALIDATION_REJECTED.equals(indicatorsSystemInProduction.getState()))) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE, uuid, new IndicatorsSystemStateEnum[]{IndicatorsSystemStateEnum.DRAFT,
                    IndicatorsSystemStateEnum.VALIDATION_REJECTED});
        }

        // Validate to send to production
        checkIndicatorsSystemToSendToProductionValidation(ctx, uuid, indicatorsSystemInProduction.getVersionNumber());

        // Update state
        indicatorsSystemInProduction.setState(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION);
        indicatorsSystemInProduction.setProductionValidationDate(new DateTime());
        indicatorsSystemInProduction.setProductionValidationUser(ctx.getUserId());
        getIndicatorsSystemsService().updateIndicatorsSystemVersion(ctx, indicatorsSystemInProduction);
    }

    @Override
    public void sendIndicatorsSystemToDiffusionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkSendIndicatorsSystemToDiffusionValidation(uuid, null);

        // Retrieve version in production validation
        IndicatorsSystemVersion indicatorsSystemInProduction = retrieveIndicatorsSystemStateInProduction(ctx, uuid, false);
        if (indicatorsSystemInProduction == null || !IndicatorsSystemStateEnum.PRODUCTION_VALIDATION.equals(indicatorsSystemInProduction.getState())) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE, uuid, new IndicatorsSystemStateEnum[]{IndicatorsSystemStateEnum.PRODUCTION_VALIDATION});
        }

        // Validate to send to diffusion
        checkIndicatorsSystemToSendToDiffusionValidation(ctx, uuid, indicatorsSystemInProduction.getVersionNumber());

        // Update state
        indicatorsSystemInProduction.setState(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION);
        indicatorsSystemInProduction.setDiffusionValidationDate(new DateTime());
        indicatorsSystemInProduction.setDiffusionValidationUser(ctx.getUserId());
        getIndicatorsSystemsService().updateIndicatorsSystemVersion(ctx, indicatorsSystemInProduction);
    }

    @Override
    public void rejectIndicatorsSystemValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRejectIndicatorsSystemValidation(uuid, null);

        // Retrieve version in production (state can be production or diffusion validation)
        IndicatorsSystemVersion indicatorsSystemInProduction = retrieveIndicatorsSystemStateInProduction(ctx, uuid, false);
        if (indicatorsSystemInProduction == null
                || (!IndicatorsSystemStateEnum.PRODUCTION_VALIDATION.equals(indicatorsSystemInProduction.getState()) && !IndicatorsSystemStateEnum.DIFFUSION_VALIDATION
                        .equals(indicatorsSystemInProduction.getState()))) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE, uuid, new IndicatorsSystemStateEnum[]{IndicatorsSystemStateEnum.PRODUCTION_VALIDATION,
                    IndicatorsSystemStateEnum.DIFFUSION_VALIDATION});
        }

        // Validate to reject
        checkIndicatorsSystemToReject(ctx, uuid, indicatorsSystemInProduction.getVersionNumber());

        // Update state
        indicatorsSystemInProduction.setState(IndicatorsSystemStateEnum.VALIDATION_REJECTED);
        indicatorsSystemInProduction.setProductionValidationDate(null);
        indicatorsSystemInProduction.setProductionValidationUser(null);
        indicatorsSystemInProduction.setDiffusionValidationDate(null);
        indicatorsSystemInProduction.setDiffusionValidationUser(null);
        getIndicatorsSystemsService().updateIndicatorsSystemVersion(ctx, indicatorsSystemInProduction);
    }

    // TODO comprobar que todos los indicadores tienen alguna versión PUBLISHED
    @Override
    public void publishIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkPublishIndicatorsSystem(uuid, null);

        // Retrieve version in diffusion validation
        IndicatorsSystemVersion indicatorsSystemInProduction = retrieveIndicatorsSystemStateInProduction(ctx, uuid, false);
        if (indicatorsSystemInProduction == null || !IndicatorsSystemStateEnum.DIFFUSION_VALIDATION.equals(indicatorsSystemInProduction.getState())) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE, uuid, new IndicatorsSystemStateEnum[]{IndicatorsSystemStateEnum.DIFFUSION_VALIDATION});
        }

        // Validate to publish
        checkIndicatorsSystemToPublish(ctx, uuid, indicatorsSystemInProduction.getVersionNumber());

        // Update state
        indicatorsSystemInProduction.setState(IndicatorsSystemStateEnum.PUBLISHED);
        indicatorsSystemInProduction.setPublicationDate(new DateTime());
        indicatorsSystemInProduction.setPublicationUser(ctx.getUserId());
        getIndicatorsSystemsService().updateIndicatorsSystemVersion(ctx, indicatorsSystemInProduction);

        IndicatorsSystem indicatorsSystem = indicatorsSystemInProduction.getIndicatorsSystem();
        // Remove possible last version in diffusion
        if (indicatorsSystem.getDiffusionVersion() != null) {
            getIndicatorsSystemsService().deleteIndicatorsSystemVersion(ctx, uuid, indicatorsSystem.getDiffusionVersion().getVersionNumber());
        }
        indicatorsSystem.setDiffusionVersion(new IndicatorsSystemVersionInformation(indicatorsSystemInProduction.getId(), indicatorsSystemInProduction.getVersionNumber()));
        indicatorsSystem.setProductionVersion(null);

        getIndicatorsSystemsService().updateIndicatorsSystem(ctx, indicatorsSystem);
    }

    @Override
    public void archiveIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkArchiveIndicatorsSystem(uuid, null);

        // Retrieve version published
        IndicatorsSystemVersion indicatorsSystemInDiffusion = retrieveIndicatorsSystemStateInDiffusion(ctx, uuid, false);
        if (indicatorsSystemInDiffusion == null || !IndicatorsSystemStateEnum.PUBLISHED.equals(indicatorsSystemInDiffusion.getState())) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE, uuid, new IndicatorsSystemStateEnum[]{IndicatorsSystemStateEnum.PUBLISHED});
        }

        // Validate to archive
        checkIndicatorsSystemToArchive(ctx, uuid, indicatorsSystemInDiffusion.getVersionNumber());

        // Update state
        indicatorsSystemInDiffusion.setState(IndicatorsSystemStateEnum.ARCHIVED);
        indicatorsSystemInDiffusion.setArchiveDate(new DateTime());
        indicatorsSystemInDiffusion.setArchiveUser(ctx.getUserId());
        getIndicatorsSystemsService().updateIndicatorsSystemVersion(ctx, indicatorsSystemInDiffusion);
    }

    @Override
    public IndicatorsSystemDto versioningIndicatorsSystem(ServiceContext ctx, String uuid, VersiontTypeEnum versionType) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkVersioningIndicatorsSystem(uuid, versionType, null);

        IndicatorsSystem indicatorsSystem = getIndicatorsSystemsService().retrieveIndicatorsSystem(ctx, uuid);
        if (indicatorsSystem.getProductionVersion() != null) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE, uuid, new IndicatorsSystemStateEnum[]{IndicatorsSystemStateEnum.PUBLISHED,
                    IndicatorsSystemStateEnum.ARCHIVED});
        }

        // Initialize new version, copying values of version in diffusion
        IndicatorsSystemVersion indicatorsSystemVersionDiffusion = retrieveIndicatorsSystemStateInDiffusion(ctx, uuid, true);
        IndicatorsSystemVersion indicatorsSystemNewVersion = DoCopyUtils.copy(indicatorsSystemVersionDiffusion);
        indicatorsSystemNewVersion.setState(IndicatorsSystemStateEnum.DRAFT);
        indicatorsSystemNewVersion.setVersionNumber(ServiceUtils.generateVersionNumber(indicatorsSystemVersionDiffusion.getVersionNumber(), versionType));

        // Create
        IndicatorsSystemVersion indicatorsSystemVersionCreated = getIndicatorsSystemsService().createIndicatorsSystemVersion(ctx, indicatorsSystem, indicatorsSystemNewVersion);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersionCreated);
        return indicatorsSystemDto;
    }

    // TODO obtener directamente las últimas versiones con consulta? añadir columna lastVersion?
    @Override
    public List<IndicatorsSystemDto> findIndicatorsSystems(ServiceContext ctx) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindIndicatorsSystems(null);

        // Find
        List<IndicatorsSystem> indicatorsSystems = getIndicatorsSystemsService().findIndicatorsSystems(ctx, null);

        // Transform
        List<IndicatorsSystemDto> indicatorsSystemsDto = new ArrayList<IndicatorsSystemDto>();
        for (IndicatorsSystem indicatorsSystem : indicatorsSystems) {
            // Last version
            IndicatorsSystemVersionInformation lastVersion = indicatorsSystem.getProductionVersion() != null ? indicatorsSystem.getProductionVersion() : indicatorsSystem.getDiffusionVersion();
            IndicatorsSystemVersion indicatorsSystemLastVersion = getIndicatorsSystemsService().retrieveIndicatorsSystemVersion(ctx, indicatorsSystem.getUuid(), lastVersion.getVersionNumber());
            indicatorsSystemsDto.add(do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemLastVersion));
        }

        return indicatorsSystemsDto;
    }

    @Override
    public List<IndicatorsSystemDto> findIndicatorsSystemsPublished(ServiceContext ctx) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindIndicatorsSystemsPublished(null);

        // Retrieve published
        List<IndicatorsSystemVersion> indicatorsSystemsVersion = getIndicatorsSystemsService().findIndicatorsSystemVersions(ctx, null, IndicatorsSystemStateEnum.PUBLISHED);

        // Transform
        List<IndicatorsSystemDto> indicatorsSystemsDto = new ArrayList<IndicatorsSystemDto>();
        for (IndicatorsSystemVersion indicatorsSystemVersion : indicatorsSystemsVersion) {
            indicatorsSystemsDto.add(do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion));
        }

        return indicatorsSystemsDto;
    }

    @Override
    public DimensionDto createDimension(ServiceContext ctx, String indicatorsSystemUuid, DimensionDto dimensionDto) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkCreateDimension(indicatorsSystemUuid, dimensionDto, null);

        // Retrieve indicators system version and check it is in production
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorsSystemStateInProduction(ctx, indicatorsSystemUuid, true);

        // Transform
        ElementLevel elementLevel = dto2DoMapper.dimensionDtoToDo(dimensionDto);

        // Create dimension
        if (dimensionDto.getParentUuid() == null) {
            // Add to first level in indicators system

            // Create element level with dimension
            elementLevel.setIndicatorsSystemVersion(indicatorsSystemVersion);
            elementLevel.setIndicatorsSystemVersionFirstLevel(indicatorsSystemVersion);
            elementLevel.setParent(null);
            elementLevel = getIndicatorsSystemsService().createDimension(ctx, elementLevel);

            // Update indicators system adding element
            indicatorsSystemVersion.addChildrenFirstLevel(elementLevel);
            indicatorsSystemVersion.addChildrenAllLevel(elementLevel);
            indicatorsSystemVersion = getIndicatorsSystemsService().updateIndicatorsSystemVersion(ctx, indicatorsSystemVersion);

            // Check order and update order of other elements in this level
            updateIndicatorsSystemElementsOrdersInLevelAddingElement(ctx, indicatorsSystemVersion.getChildrenFirstLevel(), elementLevel);
        } else {
            // Add to parent element

            ElementLevel elementLevelParent = getIndicatorsSystemsService().retrieveDimension(ctx, dimensionDto.getParentUuid()).getElementLevel();

            // Check dimension parent belogs to indicators system provided
            IndicatorsSystemVersion indicatorsSystemVersionOfDimensionParent = elementLevelParent.getIndicatorsSystemVersion();
            if (!indicatorsSystemVersionOfDimensionParent.getIndicatorsSystem().getUuid().equals(indicatorsSystemUuid)) {
                throw new MetamacException(ServiceExceptionType.DIMENSION_NOT_FOUND_IN_INDICATORS_SYSTEM, dimensionDto.getParentUuid(), indicatorsSystemUuid);
            }
            // Create element level with dimension
            elementLevel.setIndicatorsSystemVersionFirstLevel(null);
            elementLevel.setIndicatorsSystemVersion(indicatorsSystemVersion);
            elementLevel.setParent(elementLevelParent);
            elementLevel = getIndicatorsSystemsService().createDimension(ctx, elementLevel);

            // Update parent level adding element
            elementLevelParent.addChildren(elementLevel);
            elementLevelParent = getIndicatorsSystemsService().updateElementLevel(ctx, elementLevelParent);

            // Update order of other elements in this level
            updateIndicatorsSystemElementsOrdersInLevelAddingElement(ctx, elementLevelParent.getChildren(), elementLevel);

            // Update indicators system adding element to all children
            indicatorsSystemVersion.addChildrenAllLevel(elementLevel);
            getIndicatorsSystemsService().updateIndicatorsSystemVersion(ctx, indicatorsSystemVersion);
        }

        // Transform to Dto to return
        dimensionDto = do2DtoMapper.dimensionDoToDto(elementLevel.getDimension());
        return dimensionDto;
    }

    @Override
    public DimensionDto retrieveDimension(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveDimension(uuid, null);

        // Retrieve
        Dimension dimension = getIndicatorsSystemsService().retrieveDimension(ctx, uuid);
        DimensionDto dimensionDto = do2DtoMapper.dimensionDoToDto(dimension);
        return dimensionDto;
    }

    @Override
    public void deleteDimension(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkDeleteDimension(uuid, null);

        // Check indicators system state
        Dimension dimension = getIndicatorsSystemsService().retrieveDimension(ctx, uuid);
        IndicatorsSystemVersion indicatorsSystemVersion = dimension.getElementLevel().getIndicatorsSystemVersion();
        checkIndicatorsSystemVersionInProduction(indicatorsSystemVersion);

        // Delete
        getIndicatorsSystemsService().deleteDimension(ctx, dimension.getElementLevel());

        // Update orders of other elements in level
        List<ElementLevel> elementsAtLevel = null;
        if (dimension.getElementLevel().getParent() == null) {
            elementsAtLevel = indicatorsSystemVersion.getChildrenFirstLevel();
        } else {
            elementsAtLevel = dimension.getElementLevel().getParent().getChildren();
        }
        updateIndicatorsSystemElementsOrdersInLevelRemovingElement(ctx, elementsAtLevel, dimension.getElementLevel().getOrderInLevel());
    }

    @Override
    public List<DimensionDto> findDimensions(ServiceContext ctx, String indicatorsSystemUuid, String indicatorsSystemVersion) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindDimensions(indicatorsSystemUuid, indicatorsSystemVersion, null);

        // Retrieve dimensions and transform
        List<Dimension> dimensions = getIndicatorsSystemsService().findDimensions(ctx, indicatorsSystemUuid, indicatorsSystemVersion);
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
        dto2DoMapper.dimensionDtoToDo(dimensionDto, dimension);
        getIndicatorsSystemsService().updateDimension(ctx, dimension);
    }

    @Override
    public void updateDimensionLocation(ServiceContext ctx, String uuid, String parentTargetUuid, Long orderInLevel) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkUpdateDimensionLocation(uuid, parentTargetUuid, orderInLevel, null);

        // Update location
        ElementLevel elementLevel = getIndicatorsSystemsService().retrieveDimension(ctx, uuid).getElementLevel();
        updateElementLevelLocation(ctx, elementLevel, parentTargetUuid, orderInLevel);
    }

    // TODO atributos de granularidadesId como foreign keys a las tablas de granularidades?
    @Override
    public IndicatorInstanceDto createIndicatorInstance(ServiceContext ctx, String indicatorsSystemUuid, IndicatorInstanceDto indicatorInstanceDto) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkCreateIndicatorInstance(indicatorsSystemUuid, indicatorInstanceDto, null);

        // Retrieve indicators system version and check it is in production
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorsSystemStateInProduction(ctx, indicatorsSystemUuid, true);

        // Retrieve indicator
        Indicator indicator = getIndicatorsService().retrieveIndicator(ctx, indicatorInstanceDto.getIndicatorUuid());
        
        // Transform
        ElementLevel elementLevel = dto2DoMapper.indicatorInstanceDtoToDo(indicatorInstanceDto);
        elementLevel.getIndicatorInstance().setIndicator(indicator);
        // Note: update indicator adding indicator instance is not necesary

        // Create indicator instance
        if (indicatorInstanceDto.getParentUuid() == null) {
            // Add to first level in indicators system

            // Checks same indicator uuid is not already in level
            checkIndicatorInstanceUniqueIndicator(indicatorInstanceDto.getIndicatorUuid(), indicatorsSystemVersion.getChildrenFirstLevel());

            // Create element level with indicator instance
            elementLevel.setIndicatorsSystemVersion(indicatorsSystemVersion);
            elementLevel.setIndicatorsSystemVersionFirstLevel(indicatorsSystemVersion);
            elementLevel.setParent(null);
            elementLevel = getIndicatorsSystemsService().createIndicatorInstance(ctx, elementLevel);

            // Update indicators system adding element
            indicatorsSystemVersion.addChildrenFirstLevel(elementLevel);
            indicatorsSystemVersion.addChildrenAllLevel(elementLevel);
            getIndicatorsSystemsService().updateIndicatorsSystemVersion(ctx, indicatorsSystemVersion);

            // Check order and update order of other elements in this level
            updateIndicatorsSystemElementsOrdersInLevelAddingElement(ctx, indicatorsSystemVersion.getChildrenFirstLevel(), elementLevel);
        } else {
            // Add to parent element

            ElementLevel elementLevelParent = getIndicatorsSystemsService().retrieveDimension(ctx, indicatorInstanceDto.getParentUuid()).getElementLevel();

            // Check dimension parent belogs to indicators system provided
            IndicatorsSystemVersion indicatorsSystemVersionOfDimensionParent = elementLevelParent.getIndicatorsSystemVersion();
            if (!indicatorsSystemVersionOfDimensionParent.getIndicatorsSystem().getUuid().equals(indicatorsSystemUuid)) {
                throw new MetamacException(ServiceExceptionType.DIMENSION_NOT_FOUND_IN_INDICATORS_SYSTEM, indicatorInstanceDto.getParentUuid(), indicatorsSystemUuid);
            }

            // Checks same indicator uuid is not already in level
            checkIndicatorInstanceUniqueIndicator(indicatorInstanceDto.getIndicatorUuid(), elementLevelParent.getChildren());

            // Create element level with indicator instance
            elementLevel.setIndicatorsSystemVersionFirstLevel(null);
            elementLevel.setIndicatorsSystemVersion(indicatorsSystemVersion);
            elementLevel.setParent(elementLevelParent);
            elementLevel = getIndicatorsSystemsService().createIndicatorInstance(ctx, elementLevel);

            // Update parent level adding element
            elementLevelParent.addChildren(elementLevel);
            getIndicatorsSystemsService().updateElementLevel(ctx, elementLevelParent);

            // Update order of other elements in this level
            updateIndicatorsSystemElementsOrdersInLevelAddingElement(ctx, elementLevelParent.getChildren(), elementLevel);

            // Update indicators system adding element to all children
            indicatorsSystemVersion.addChildrenAllLevel(elementLevel);
            getIndicatorsSystemsService().updateIndicatorsSystemVersion(ctx, indicatorsSystemVersion);
        }
        
        // Transform to Dto to return
        indicatorInstanceDto = do2DtoMapper.indicatorInstanceDoToDto(elementLevel.getIndicatorInstance());
        return indicatorInstanceDto;
    }

    @Override
    public IndicatorInstanceDto retrieveIndicatorInstance(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorInstance(uuid, null);

        // Retrieve
        IndicatorInstance indicatorInstance = getIndicatorsSystemsService().retrieveIndicatorInstance(ctx, uuid);
        IndicatorInstanceDto indicatorInstanceDto = do2DtoMapper.indicatorInstanceDoToDto(indicatorInstance);
        return indicatorInstanceDto;
    }

    @Override
    public void deleteIndicatorInstance(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkDeleteIndicatorInstance(uuid, null);

        // Check indicators system state
        IndicatorInstance indicatorInstance = getIndicatorsSystemsService().retrieveIndicatorInstance(ctx, uuid);
        IndicatorsSystemVersion indicatorsSystemVersion = indicatorInstance.getElementLevel().getIndicatorsSystemVersion();
        checkIndicatorsSystemVersionInProduction(indicatorsSystemVersion);

        // Delete
        getIndicatorsSystemsService().deleteIndicatorInstance(ctx, indicatorInstance.getElementLevel());

        // Update orders of other elements in level
        List<ElementLevel> elementsAtLevel = null;
        if (indicatorInstance.getElementLevel().getParent() == null) {
            elementsAtLevel = indicatorsSystemVersion.getChildrenFirstLevel();
        } else {
            elementsAtLevel = indicatorInstance.getElementLevel().getParent().getChildren();
        }
        updateIndicatorsSystemElementsOrdersInLevelRemovingElement(ctx, elementsAtLevel, indicatorInstance.getElementLevel().getOrderInLevel());
    }

    @Override
    public List<IndicatorInstanceDto> findIndicatorsInstances(ServiceContext ctx, String indicatorsSystemUuid, String indicatorsSystemVersion) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindIndicatorsInstances(indicatorsSystemUuid, indicatorsSystemVersion, null);

        // Retrieve indicators instances and transform
        List<IndicatorInstance> indicatorsInstances = getIndicatorsSystemsService().findIndicatorsInstances(ctx, indicatorsSystemUuid, indicatorsSystemVersion);
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
        dto2DoMapper.indicatorInstanceDtoToDo(indicatorInstanceDto, indicatorInstance);
        getIndicatorsSystemsService().updateIndicatorInstance(ctx, indicatorInstance);
    }
    
    @Override
    public void updateIndicatorInstanceLocation(ServiceContext ctx, String uuid, String parentTargetUuid, Long orderInLevel) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkUpdateIndicatorInstanceLocation(uuid, parentTargetUuid, orderInLevel, null);

        // Update location
        ElementLevel elementLevel = getIndicatorsSystemsService().retrieveIndicatorInstance(ctx, uuid).getElementLevel();
        updateElementLevelLocation(ctx, elementLevel, parentTargetUuid, orderInLevel);
    }

    // TODO listado de dudas de Ri a Alberto
    // TODO metadatos: subjectCode es una lista. Se debe permitir realizar búsquedas por este campo.
    public IndicatorDto createIndicator(ServiceContext ctx, IndicatorDto indicatorDto) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkCreateIndicator(indicatorDto, null);
        checkIndicatorCodeUnique(ctx, indicatorDto.getCode(), null);

        // Transform
        Indicator indicator = new Indicator();
        indicator.setDiffusionVersion(null);
        dto2DoMapper.indicatorDtoToDo(indicatorDto, indicator);
        // Version in draft
        IndicatorVersion draftVersion = dto2DoMapper.indicatorDtoToDo(indicatorDto);
        draftVersion.setState(IndicatorStateEnum.DRAFT);
        draftVersion.setVersionNumber(ServiceUtils.generateVersionNumber(null, VersiontTypeEnum.MAJOR));

        // Create
        IndicatorVersion indicatorVersionCreated = getIndicatorsService().createIndicatorVersion(ctx, indicator, draftVersion);

        // Transform to Dto
        indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersionCreated);
        return indicatorDto;
    }

    public IndicatorDto retrieveIndicator(ServiceContext ctx, String uuid, String version) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicator(uuid, version, null);

        // Retrieve version requested or last version
        IndicatorVersion indicatorVersion = null;
        if (version == null) {
            // Retrieve last version
            Indicator indicator = getIndicatorsService().retrieveIndicator(ctx, uuid);
            version = indicator.getProductionVersion() != null ? indicator.getProductionVersion().getVersionNumber() : indicator.getDiffusionVersion().getVersionNumber();
        }
        indicatorVersion = getIndicatorsService().retrieveIndicatorVersion(ctx, uuid, version);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersion);
        return indicatorDto;
    }

    @Override
    public IndicatorDto retrieveIndicatorPublished(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorPublished(uuid, null);

        // Retrieve published version
        IndicatorVersion publishedIndicatorVersion = retrieveIndicatorStateInDiffusion(ctx, uuid, false);
        if (publishedIndicatorVersion == null || !IndicatorStateEnum.PUBLISHED.equals(publishedIndicatorVersion.getState())) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_STATE, uuid, new IndicatorStateEnum[]{IndicatorStateEnum.PUBLISHED});
        }

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(publishedIndicatorVersion);
        return indicatorDto;
    }

    public void deleteIndicator(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkDeleteIndicator(uuid, null);

        // Retrieve version in production
        IndicatorVersion indicatorVersion = retrieveIndicatorStateInProduction(ctx, uuid, true);

        // Delete whole indicator or only last version
        if (IndicatorsConstants.VERSION_NUMBER_INITIAL.equals(indicatorVersion.getVersionNumber())) {
            // If indicator is not published or archived, delete whole indicator

            // Check not exists any indicator instance for this indicator
            if (indicatorVersion.getIndicator().getIndicatorsInstances().size() != 0) {
                throw new MetamacException(ServiceExceptionType.INDICATOR_MUST_NOT_BE_IN_INDICATORS_SYSTEMS, uuid);
            }
            getIndicatorsService().deleteIndicator(ctx, uuid);
        } else {
            getIndicatorsService().deleteIndicatorVersion(ctx, uuid, indicatorVersion.getVersionNumber());
            indicatorVersion.getIndicator().setProductionVersion(null);
            getIndicatorsService().updateIndicator(ctx, indicatorVersion.getIndicator());
        }
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
        dto2DoMapper.indicatorDtoToDo(indicatorDto, indicatorInProduction);

        // Update
        getIndicatorsService().updateIndicatorVersion(ctx, indicatorInProduction);
    }

    @Override
    public void sendIndicatorToProductionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkSendIndicatorToProductionValidation(uuid, null);

        // Retrieve version in draft
        IndicatorVersion indicatorInProduction = retrieveIndicatorStateInProduction(ctx, uuid, false);
        if (indicatorInProduction == null || (!IndicatorStateEnum.DRAFT.equals(indicatorInProduction.getState()) && !IndicatorStateEnum.VALIDATION_REJECTED.equals(indicatorInProduction.getState()))) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_STATE, uuid, new IndicatorStateEnum[]{IndicatorStateEnum.DRAFT, IndicatorStateEnum.VALIDATION_REJECTED});
        }

        // Check indicator has any data source
        if (indicatorInProduction.getDataSources().size() == 0) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_MUST_HAVE_DATA_SOURCES, uuid);
        }

        // Update state
        indicatorInProduction.setState(IndicatorStateEnum.PRODUCTION_VALIDATION);
        indicatorInProduction.setProductionValidationDate(new DateTime());
        indicatorInProduction.setProductionValidationUser(ctx.getUserId());
        getIndicatorsService().updateIndicatorVersion(ctx, indicatorInProduction);
    }

    @Override
    public void sendIndicatorToDiffusionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkSendIndicatorToDiffusionValidation(uuid, null);

        // Retrieve version in production validation
        IndicatorVersion indicatorInProduction = retrieveIndicatorStateInProduction(ctx, uuid, false);
        if (indicatorInProduction == null || !IndicatorStateEnum.PRODUCTION_VALIDATION.equals(indicatorInProduction.getState())) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_STATE, uuid, new IndicatorStateEnum[]{IndicatorStateEnum.PRODUCTION_VALIDATION});
        }

        // Update state
        indicatorInProduction.setState(IndicatorStateEnum.DIFFUSION_VALIDATION);
        indicatorInProduction.setDiffusionValidationDate(new DateTime());
        indicatorInProduction.setDiffusionValidationUser(ctx.getUserId());
        getIndicatorsService().updateIndicatorVersion(ctx, indicatorInProduction);
    }

    @Override
    public void rejectIndicatorValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRejectIndicatorValidation(uuid, null);

        // Retrieve version in production (state can be production or diffusion validation)
        IndicatorVersion indicatorInProduction = retrieveIndicatorStateInProduction(ctx, uuid, false);
        if (indicatorInProduction == null
                || (!IndicatorStateEnum.PRODUCTION_VALIDATION.equals(indicatorInProduction.getState()) && !IndicatorStateEnum.DIFFUSION_VALIDATION.equals(indicatorInProduction.getState()))) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_STATE, uuid, new IndicatorStateEnum[]{IndicatorStateEnum.PRODUCTION_VALIDATION, IndicatorStateEnum.DIFFUSION_VALIDATION});
        }

        // Update state
        indicatorInProduction.setState(IndicatorStateEnum.VALIDATION_REJECTED);
        indicatorInProduction.setProductionValidationDate(null);
        indicatorInProduction.setProductionValidationUser(null);
        indicatorInProduction.setDiffusionValidationDate(null);
        indicatorInProduction.setDiffusionValidationUser(null);
        getIndicatorsService().updateIndicatorVersion(ctx, indicatorInProduction);
    }

    // TODO Implica realizar también INDICADORES-CU-6
    @Override
    public void publishIndicator(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkPublishIndicator(uuid, null);

        // Retrieve version in diffusion validation
        IndicatorVersion indicatorInProduction = retrieveIndicatorStateInProduction(ctx, uuid, false);
        if (indicatorInProduction == null
                || (!IndicatorStateEnum.DIFFUSION_VALIDATION.equals(indicatorInProduction.getState()) && !IndicatorStateEnum.PUBLICATION_FAILED.equals(indicatorInProduction.getState()))) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_STATE, uuid, new IndicatorStateEnum[]{IndicatorStateEnum.DIFFUSION_VALIDATION, IndicatorStateEnum.PUBLICATION_FAILED});
        }

        // Update state
        indicatorInProduction.setState(IndicatorStateEnum.PUBLISHED);
        indicatorInProduction.setPublicationDate(new DateTime());
        indicatorInProduction.setPublicationUser(ctx.getUserId());
        getIndicatorsService().updateIndicatorVersion(ctx, indicatorInProduction);

        Indicator indicator = indicatorInProduction.getIndicator();
        // Remove possible last version in diffusion
        if (indicator.getDiffusionVersion() != null) {
            getIndicatorsService().deleteIndicatorVersion(ctx, uuid, indicator.getDiffusionVersion().getVersionNumber());
        }
        indicator.setDiffusionVersion(new IndicatorVersionInformation(indicatorInProduction.getId(), indicatorInProduction.getVersionNumber()));
        indicator.setProductionVersion(null);

        getIndicatorsService().updateIndicator(ctx, indicator);
    }

    @Override
    public void archiveIndicator(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkArchiveIndicator(uuid, null);

        // Retrieve version published
        IndicatorVersion indicatorInDiffusion = retrieveIndicatorStateInDiffusion(ctx, uuid, false);
        if (indicatorInDiffusion == null || !IndicatorStateEnum.PUBLISHED.equals(indicatorInDiffusion.getState())) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_STATE, uuid, new IndicatorStateEnum[]{IndicatorStateEnum.PUBLISHED});
        }

        // Update state
        indicatorInDiffusion.setState(IndicatorStateEnum.ARCHIVED);
        indicatorInDiffusion.setArchiveDate(new DateTime());
        indicatorInDiffusion.setArchiveUser(ctx.getUserId());
        getIndicatorsService().updateIndicatorVersion(ctx, indicatorInDiffusion);
    }

    @Override
    public IndicatorDto versioningIndicator(ServiceContext ctx, String uuid, VersiontTypeEnum versionType) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkVersioningIndicator(uuid, versionType, null);

        Indicator indicator = getIndicatorsService().retrieveIndicator(ctx, uuid);
        if (indicator.getProductionVersion() != null) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_STATE, uuid, new IndicatorStateEnum[]{IndicatorStateEnum.PUBLISHED, IndicatorStateEnum.ARCHIVED});
        }

        // Initialize new version, copying values of version in diffusion
        IndicatorVersion indicatorVersionDiffusion = retrieveIndicatorStateInDiffusion(ctx, uuid, true);
        IndicatorVersion indicatorNewVersion = DoCopyUtils.copy(indicatorVersionDiffusion);
        indicatorNewVersion.setState(IndicatorStateEnum.DRAFT);
        indicatorNewVersion.setVersionNumber(ServiceUtils.generateVersionNumber(indicatorVersionDiffusion.getVersionNumber(), versionType));

        // Create
        IndicatorVersion indicatorVersionCreated = getIndicatorsService().createIndicatorVersion(ctx, indicator, indicatorNewVersion);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersionCreated);
        return indicatorDto;
    }

    // TODO obtener directamente las últimas versiones con consulta? añadir columna lastVersion?
    @Override
    public List<IndicatorDto> findIndicators(ServiceContext ctx) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindIndicators(null);

        // Find
        List<Indicator> indicators = getIndicatorsService().findIndicators(ctx, null);

        // Transform
        List<IndicatorDto> indicatorDto = new ArrayList<IndicatorDto>();
        for (Indicator indicator : indicators) {
            // Last version
            IndicatorVersionInformation lastVersion = indicator.getProductionVersion() != null ? indicator.getProductionVersion() : indicator.getDiffusionVersion();
            IndicatorVersion indicatorLastVersion = getIndicatorsService().retrieveIndicatorVersion(ctx, indicator.getUuid(), lastVersion.getVersionNumber());
            indicatorDto.add(do2DtoMapper.indicatorDoToDto(indicatorLastVersion));
        }

        return indicatorDto;
    }

    @Override
    public List<IndicatorDto> findIndicatorsPublished(ServiceContext ctx) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindIndicatorsPublished(null);

        // Retrieve published
        List<IndicatorVersion> indicatorsVersions = getIndicatorsService().findIndicatorsVersions(ctx, null, IndicatorStateEnum.PUBLISHED);

        // Transform
        List<IndicatorDto> indicatorDto = new ArrayList<IndicatorDto>();
        for (IndicatorVersion indicatorVersion : indicatorsVersions) {
            indicatorDto.add(do2DtoMapper.indicatorDoToDto(indicatorVersion));
        }

        return indicatorDto;
    }

    @Override
    public DataSourceDto createDataSource(ServiceContext ctx, String indicatorUuid, DataSourceDto dataSourceDto) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkCreateDataSource(indicatorUuid, dataSourceDto, null);

        // Retrieve indicator version and check it is in production
        IndicatorVersion indicatorVersion = retrieveIndicatorStateInProduction(ctx, indicatorUuid, true);

        // Transform
        DataSource dataSource = dto2DoMapper.dataSourceDtoToDo(dataSourceDto);
        dataSource = getIndicatorsService().createDataSource(ctx, dataSource);

        // Create dataSource
        dataSource.setIndicatorVersion(indicatorVersion);
        dataSource = getIndicatorsService().createDataSource(ctx, dataSource);

        // Update indicator adding dataSource
        indicatorVersion.addDataSource(dataSource);
        getIndicatorsService().updateIndicatorVersion(ctx, indicatorVersion);

        // Transform to Dto to return
        dataSourceDto = do2DtoMapper.dataSourceDoToDto(dataSource);
        return dataSourceDto;
    }

    @Override
    public DataSourceDto retrieveDataSource(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveDataSource(uuid, null);

        // Retrieve
        DataSource dataSource = getIndicatorsService().retrieveDataSource(ctx, uuid);
        DataSourceDto dataSourceDto = do2DtoMapper.dataSourceDoToDto(dataSource);
        return dataSourceDto;
    }

    @Override
    public void deleteDataSource(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkDeleteDataSource(uuid, null);

        // Check indicator state
        DataSource dataSource = getIndicatorsService().retrieveDataSource(ctx, uuid);
        checkIndicatorVersionInProduction(dataSource.getIndicatorVersion());

        // Delete
        getIndicatorsService().deleteDataSource(ctx, dataSource);
    }

    @Override
    public List<DataSourceDto> findDataSources(ServiceContext ctx, String indicatorUuid, String indicatorVersion) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindDataSources(indicatorUuid, indicatorVersion, null);

        // Retrieve dataSources and transform
        List<DataSource> dataSources = getIndicatorsService().findDataSources(ctx, indicatorUuid, indicatorVersion);
        List<DataSourceDto> dataSourcesDto = new ArrayList<DataSourceDto>();
        for (DataSource dataSource : dataSources) {
            dataSourcesDto.add(do2DtoMapper.dataSourceDoToDto(dataSource));
        }

        return dataSourcesDto;
    }

    @Override
    public void updateDataSource(ServiceContext ctx, DataSourceDto dataSourceDto) throws MetamacException {

        // Retrieve
        // TODO comprobar parámetros antes?
        DataSource dataSource = getIndicatorsService().retrieveDataSource(ctx, dataSourceDto.getUuid());

        // Validation of parameters
        InvocationValidator.checkUpdateDataSource(dataSourceDto, dataSource, null);

        // Check indicator state
        checkIndicatorVersionInProduction(dataSource.getIndicatorVersion());

        // Transform and update
        dto2DoMapper.dataSourceDtoToDo(dataSourceDto, dataSource);
        getIndicatorsService().updateDataSource(ctx, dataSource);
    }



    /**
     * Retrieves version of an indicators system in production
     */
    private IndicatorsSystemVersion retrieveIndicatorsSystemStateInProduction(ServiceContext ctx, String uuid, boolean throwsExceptionIfNotExistsInProduction) throws MetamacException {
        IndicatorsSystem indicatorsSystem = getIndicatorsSystemsService().retrieveIndicatorsSystem(ctx, uuid);
        if (indicatorsSystem == null) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND, uuid);
        }
        if (indicatorsSystem.getProductionVersion() == null && !throwsExceptionIfNotExistsInProduction) {
            return null; // to throws an specific exception
        }
        if (indicatorsSystem.getProductionVersion() == null) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_IN_PRODUCTION_NOT_FOUND, uuid);
        }
        IndicatorsSystemVersion indicatorsSystemVersionProduction = getIndicatorsSystemsService()
                .retrieveIndicatorsSystemVersion(ctx, uuid, indicatorsSystem.getProductionVersion().getVersionNumber());
        return indicatorsSystemVersionProduction;
    }

    /**
     * Retrieves version of an indicators system in diffusion
     */
    private IndicatorsSystemVersion retrieveIndicatorsSystemStateInDiffusion(ServiceContext ctx, String uuid, boolean throwsExceptionIfNotExistsInDiffusion) throws MetamacException {
        IndicatorsSystem indicatorsSystem = getIndicatorsSystemsService().retrieveIndicatorsSystem(ctx, uuid);
        if (indicatorsSystem.getDiffusionVersion() == null && !throwsExceptionIfNotExistsInDiffusion) {
            return null; // to throws an specific exception
        }
        if (indicatorsSystem.getDiffusionVersion() == null) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_IN_DIFFUSION_NOT_FOUND, uuid);
        }
        IndicatorsSystemVersion indicatorsSystemVersionDiffusion = getIndicatorsSystemsService().retrieveIndicatorsSystemVersion(ctx, uuid, indicatorsSystem.getDiffusionVersion().getVersionNumber());
        return indicatorsSystemVersionDiffusion;
    }

    /**
     * Checks not exists another indicators system with same code. Checks system retrieved not is actual system.
     */
    private void checkIndicatorsSystemCodeUnique(ServiceContext ctx, String code, String actualUuid) throws MetamacException {
        List<IndicatorsSystem> indicatorsSystems = getIndicatorsSystemsService().findIndicatorsSystems(ctx, code);
        if (indicatorsSystems != null && indicatorsSystems.size() != 0 && !indicatorsSystems.get(0).getUuid().equals(actualUuid)) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED, code);
        }
    }

    /**
     * Checks not exists another indicators system with same uri in Gopestat. Checks system retrieved not is actual system.
     */
    private void checkIndicatorsSystemUriGopestatUnique(ServiceContext ctx, String uriGopestat, String actualUuid) throws MetamacException {
        List<IndicatorsSystemVersion> indicatorsSystemVersions = getIndicatorsSystemsService().findIndicatorsSystemVersions(ctx, uriGopestat, null);
        if (indicatorsSystemVersions != null && indicatorsSystemVersions.size() != 0) {
            for (IndicatorsSystemVersion indicatorsSystemVersion : indicatorsSystemVersions) {
                if (!indicatorsSystemVersion.getIndicatorsSystem().getUuid().equals(actualUuid)) {
                    throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_ALREADY_EXIST_URI_GOPESTAT_DUPLICATED, uriGopestat);
                }
            }
        }
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

    private void updateIndicatorsSystemElementsOrdersInLevelAddingElement(ServiceContext ctx, List<ElementLevel> elementsAtLevel, ElementLevel elementToAdd) throws MetamacException {

        // Create a set with all possibles orders. At the end of this method, this set must be empty
        Set<Long> orders = new HashSet<Long>();
        for (int i = 1; i <= elementsAtLevel.size(); i++) {
            orders.add(Long.valueOf(i));
        }

        // Update orders
        for (ElementLevel elementInLevel : elementsAtLevel) {
            // it is possible that element is already added to parent and order is already set
            if (elementInLevel.getElementUuid().equals(elementToAdd.getElementUuid())) {
                // nothing
            } else {
                // Update order
                if (elementInLevel.getOrderInLevel() >= elementToAdd.getOrderInLevel()) {
                    elementInLevel.setOrderInLevel(elementInLevel.getOrderInLevel() + 1);
                    getIndicatorsSystemsService().updateElementLevel(ctx, elementInLevel);
                }
            }

            boolean removed = orders.remove(elementInLevel.getOrderInLevel());
            if (!removed) {
                break; // order incorrect
            }
        }

        // Checks orders
        if (!orders.isEmpty()) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, "ORDER_IN_LEVEL");
        }
    }

    private void updateIndicatorsSystemElementsOrdersInLevelRemovingElement(ServiceContext ctx, List<ElementLevel> elementsAtLevel, Long orderBeforeUpdate) throws MetamacException {
        for (ElementLevel elementInLevel : elementsAtLevel) {
            if (elementInLevel.getOrderInLevel() > orderBeforeUpdate) {
                elementInLevel.setOrderInLevel(elementInLevel.getOrderInLevel() - 1);
                getIndicatorsSystemsService().updateElementLevel(ctx, elementInLevel);
            }
        }
    }

    private void updateIndicatorsSystemElementsOrdersInLevelChangingOrder(ServiceContext ctx, List<ElementLevel> elementsAtLevel, ElementLevel elementToChangeOrder, Long orderBeforeUpdate, Long orderAfterUpdate) throws MetamacException {

        // Checks orders
        if (orderAfterUpdate > elementsAtLevel.size()) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, "ORDER_IN_LEVEL");
        }

        // Update orders
        for (ElementLevel elementAtLevel : elementsAtLevel) {
            if (elementAtLevel.getElementUuid().equals(elementToChangeOrder.getElementUuid())) {
                continue;
            }
            if (orderAfterUpdate < orderBeforeUpdate) {
                if (elementAtLevel.getOrderInLevel() >= orderAfterUpdate && elementAtLevel.getOrderInLevel() < orderBeforeUpdate) {
                    elementAtLevel.setOrderInLevel(elementAtLevel.getOrderInLevel() + 1);
                    getIndicatorsSystemsService().updateElementLevel(ctx, elementAtLevel);
                }
            } else if (orderAfterUpdate > orderBeforeUpdate) {
                if (elementAtLevel.getOrderInLevel() > orderBeforeUpdate && elementAtLevel.getOrderInLevel() <= orderAfterUpdate) {
                    elementAtLevel.setOrderInLevel(elementAtLevel.getOrderInLevel() - 1);
                    getIndicatorsSystemsService().updateElementLevel(ctx, elementAtLevel);
                }
            }
        }
    }

    /**
     * We can not move a dimension to its child
     */
    private void checkDimensionIsNotChildren(ServiceContext ctx, Dimension dimension, String parentTargetUuid) throws MetamacException {

        Dimension dimensionTarget = getIndicatorsSystemsService().retrieveDimension(ctx, parentTargetUuid);
        ElementLevel dimensionParent = dimensionTarget.getElementLevel().getParent();
        while (dimensionParent != null) {
            if (dimensionParent.getDimension() != null && dimensionParent.getElementUuid().equals(dimension.getUuid())) {
                throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, "PARENT_TARGET_UUID");
            }
            dimensionParent = dimensionParent.getParent();
        }
    }

    /**
     * Checks not exists an indicator instance in the level with same indicator uuid
     */
    private void checkIndicatorInstanceUniqueIndicator(String indicatorUuid, List<ElementLevel> elementsLevel) throws MetamacException {
        for (ElementLevel elementLevel : elementsLevel) {
            if (elementLevel.getIndicatorInstance() != null && elementLevel.getIndicatorInstance().getIndicator().getUuid().equals(indicatorUuid)) {
                throw new MetamacException(ServiceExceptionType.INDICATOR_INSTANCE_ALREADY_EXIST_INDICATOR_SAME_LEVEL, indicatorUuid);
            }
        }
    }

    /**
     * Makes validations to sent to production validation
     * 1) Must exists at least one indicator instance
     */
    private void checkIndicatorsSystemToSendToProductionValidation(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {

        // Check exists at least one indicator instance
        if (!getIndicatorsSystemsService().existAnyIndicatorInstance(ctx, uuid, versionNumber)) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_MUST_HAVE_INDICATOR_INSTANCE, uuid);
        }
    }

    /**
     * Makes validations to sent to diffusion validation
     * 1) Validations when send to production validation
     */
    private void checkIndicatorsSystemToSendToDiffusionValidation(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {

        checkIndicatorsSystemToSendToProductionValidation(ctx, uuid, versionNumber);
    }

    /**
     * Makes validations to publish
     * 1) Validations when send to diffusion validation
     */
    private void checkIndicatorsSystemToPublish(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {

        checkIndicatorsSystemToSendToDiffusionValidation(ctx, uuid, versionNumber);
    }

    /**
     * Makes validations to archive
     * 1) Validations when publish
     */
    private void checkIndicatorsSystemToArchive(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {

        checkIndicatorsSystemToPublish(ctx, uuid, versionNumber);
    }

    /**
     * Makes validations to reject
     */
    private void checkIndicatorsSystemToReject(ServiceContext ctx, String uuid, String versionNumber) {

        // Nothing
    }
    
    private void updateElementLevelLocation(ServiceContext ctx, ElementLevel elementLevel, String parentTargetUuid, Long orderInLevel) throws MetamacException {

        // Change order
        Long orderInLevelBefore = elementLevel.getOrderInLevel();
        elementLevel.setOrderInLevel(orderInLevel);

        // Check indicators system state
        IndicatorsSystemVersion indicatorsSystemVersion = elementLevel.getIndicatorsSystemVersion();
        checkIndicatorsSystemVersionInProduction(indicatorsSystemVersion);

        // Check target parent is not children of this dimension (only when element is a dimension)
        if (parentTargetUuid != null && elementLevel.getDimension() != null) {
            checkDimensionIsNotChildren(ctx, elementLevel.getDimension(), parentTargetUuid);
        }

        // Update parent and/or order
        String parentUuidActual = elementLevel.getParentUuid();
        if ((parentUuidActual == null && parentTargetUuid != null) || (parentUuidActual != null && parentTargetUuid == null)
                || (parentUuidActual != null && parentTargetUuid != null && !parentTargetUuid.equals(parentUuidActual))) {

            ElementLevel parentActual = elementLevel.getParent() != null ? elementLevel.getParent() : null;
            ElementLevel parentTarget = parentTargetUuid != null ? getIndicatorsSystemsService().retrieveDimension(ctx, parentTargetUuid).getElementLevel() : null;

            // Update target parent, adding dimension
            List<ElementLevel> elementsInLevel = null;
            if (parentTarget == null) {
                indicatorsSystemVersion.addChildrenFirstLevel(elementLevel);
                getIndicatorsSystemsService().updateIndicatorsSystemVersion(ctx, indicatorsSystemVersion);
                elementsInLevel = indicatorsSystemVersion.getChildrenFirstLevel();
            } else {
                parentTarget.addChildren(elementLevel);
                getIndicatorsSystemsService().updateElementLevel(ctx, parentTarget);
                elementsInLevel = parentTarget.getChildren();
            }
            // Check order is correct and update orders
            updateIndicatorsSystemElementsOrdersInLevelAddingElement(ctx, elementsInLevel, elementLevel);

            // Update dimension, changing parent
            if (parentTarget == null) {
                elementLevel.setIndicatorsSystemVersionFirstLevel(indicatorsSystemVersion);
                elementLevel.setParent(null);
            } else {
                elementLevel.setIndicatorsSystemVersionFirstLevel(null);
                elementLevel.setParent(parentTarget);
            }
            getIndicatorsSystemsService().updateElementLevel(ctx, elementLevel);

            // Update actual parent dimension or indicators system version, removing dimension
            if (parentActual != null) {
                parentActual.getChildren().remove(elementLevel);
                getIndicatorsSystemsService().updateElementLevel(ctx, parentActual);
                // Update order of other dimensions
                updateIndicatorsSystemElementsOrdersInLevelRemovingElement(ctx, parentActual.getChildren(), orderInLevelBefore);
            } else {
                indicatorsSystemVersion.getChildrenFirstLevel().remove(elementLevel);
                getIndicatorsSystemsService().updateIndicatorsSystemVersion(ctx, indicatorsSystemVersion);
                // Update order of other dimensions
                updateIndicatorsSystemElementsOrdersInLevelRemovingElement(ctx, indicatorsSystemVersion.getChildrenFirstLevel(), orderInLevelBefore);
            }

        } else {
            // Same parent, only changes order
            // Check order is correct and update orders
            List<ElementLevel> elementsInLevel = elementLevel.getParent() != null ? elementLevel.getParent().getChildren() : elementLevel
                    .getIndicatorsSystemVersionFirstLevel().getChildrenFirstLevel();
            updateIndicatorsSystemElementsOrdersInLevelChangingOrder(ctx, elementsInLevel, elementLevel, orderInLevelBefore, elementLevel.getOrderInLevel());
            getIndicatorsSystemsService().updateElementLevel(ctx, elementLevel);
        }
    }
    
    /**
     * Retrieves version of an indicator in production
     */
    private IndicatorVersion retrieveIndicatorStateInProduction(ServiceContext ctx, String uuid, boolean throwsExceptionIfNotExistsInProduction) throws MetamacException {
        Indicator indicator = getIndicatorsService().retrieveIndicator(ctx, uuid);
        if (indicator == null) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_FOUND, uuid);
        }
        if (indicator.getProductionVersion() == null && !throwsExceptionIfNotExistsInProduction) {
            return null; // to throws an specific exception
        }
        if (indicator.getProductionVersion() == null) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_IN_PRODUCTION_NOT_FOUND, uuid);
        }
        IndicatorVersion indicatorVersionProduction = getIndicatorsService().retrieveIndicatorVersion(ctx, uuid, indicator.getProductionVersion().getVersionNumber());
        return indicatorVersionProduction;
    }

    /**
     * Retrieves version of an indicator in diffusion
     */
    private IndicatorVersion retrieveIndicatorStateInDiffusion(ServiceContext ctx, String uuid, boolean throwsExceptionIfNotExistsInDiffusion) throws MetamacException {
        Indicator indicator = getIndicatorsService().retrieveIndicator(ctx, uuid);
        if (indicator.getDiffusionVersion() == null && !throwsExceptionIfNotExistsInDiffusion) {
            return null; // to throws an specific exception
        }
        if (indicator.getDiffusionVersion() == null) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_IN_DIFFUSION_NOT_FOUND, uuid);
        }
        IndicatorVersion indicatorVersionDiffusion = getIndicatorsService().retrieveIndicatorVersion(ctx, uuid, indicator.getDiffusionVersion().getVersionNumber());
        return indicatorVersionDiffusion;
    }

    /**
     * Checks not exists another indicator with same code. Checks indicator retrieved not is actual indicator.
     */
    private void checkIndicatorCodeUnique(ServiceContext ctx, String code, String actualUuid) throws MetamacException {
        List<Indicator> indicator = getIndicatorsService().findIndicators(ctx, code);
        if (indicator != null && indicator.size() != 0 && !indicator.get(0).getUuid().equals(actualUuid)) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_ALREADY_EXIST_CODE_DUPLICATED, code);
        }
    }

    /**
     * Checks that the indicator version is in any state in production
     */
    private void checkIndicatorVersionInProduction(IndicatorVersion indicatorVersion) throws MetamacException {
        IndicatorStateEnum state = indicatorVersion.getState();
        boolean inProduction = IndicatorStateEnum.DRAFT.equals(state) || IndicatorStateEnum.VALIDATION_REJECTED.equals(state) || IndicatorStateEnum.PRODUCTION_VALIDATION.equals(state)
                || IndicatorStateEnum.DIFFUSION_VALIDATION.equals(state);
        if (!inProduction) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_VERSION_WRONG_STATE, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());

        }
    }
}
