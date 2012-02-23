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
import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.ElementLevel;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersionInformation;
import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemStructureDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemStateEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersiontTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.mapper.Do2DtoMapper;
import es.gobcan.istac.indicators.core.mapper.Dto2DoMapper;
import es.gobcan.istac.indicators.core.serviceimpl.util.DoCopyUtils;
import es.gobcan.istac.indicators.core.serviceimpl.util.InvocationValidator;
import es.gobcan.istac.indicators.core.serviceimpl.util.ServiceUtils;

/**
 * Implementation of IndicatorsSystemServiceFacade.
 * TODO En los "create", ¿devolver una uri, en lugar del uuid? (ojo! uris rests?, o serán con el code?)
 */
@Service("indicatorsSystemServiceFacade")
public class IndicatorsSystemsServiceFacadeImpl extends IndicatorsSystemsServiceFacadeImplBase {

    @Autowired
    private Do2DtoMapper do2DtoMapper;

    @Autowired
    private Dto2DoMapper dto2DoMapper;

    public IndicatorsSystemsServiceFacadeImpl() {
    }

    /**
     * TODO qué datos se almacenarán? Los sistemas de indicadores se obtienen desde Gopestat, y aquí se almacenan como "referencias" a ellas
     */
    public IndicatorsSystemDto createIndicatorsSystem(ServiceContext ctx, IndicatorsSystemDto indicatorsSystemDto) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkCreateIndicatorsSystem(indicatorsSystemDto, null);
        validateCodeUnique(ctx, indicatorsSystemDto.getCode(), null);
        validateUriGopestatUnique(ctx, indicatorsSystemDto.getUriGopestat(), null);

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

    // TODO comprobar que borra instancias de indicadores. No borra indicadores asociados mediante instancias de indicadores
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
        validateIndicatorsSystemToSendToProductionValidation(ctx, uuid, indicatorsSystemInProduction.getVersionNumber());

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
        validateIndicatorsSystemToSendToDiffusionValidation(ctx, uuid, indicatorsSystemInProduction.getVersionNumber());

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
        validateIndicatorsSystemToReject(ctx, uuid, indicatorsSystemInProduction.getVersionNumber());

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
        validateIndicatorsSystemToPublish(ctx, uuid, indicatorsSystemInProduction.getVersionNumber());

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
        validateIndicatorsSystemToArchive(ctx, uuid, indicatorsSystemInDiffusion.getVersionNumber());

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

    // TODO paginación
    // TODO criteria
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

    // TODO paginación
    // TODO criteria
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
            updateOrdersInLevelAddingElement(ctx, indicatorsSystemVersion.getChildrenFirstLevel(), elementLevel);
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
            updateOrdersInLevelAddingElement(ctx, elementLevelParent.getChildren(), elementLevel);

            // Update indicators system adding element to all children
            indicatorsSystemVersion.addChildrenAllLevel(elementLevel);
            getIndicatorsSystemsService().updateIndicatorsSystemVersion(ctx, indicatorsSystemVersion);
        }

        // Transform to Dto to return
        dimensionDto = do2DtoMapper.dimensionDoToDto(elementLevel.getDimension(), Boolean.TRUE);
        return dimensionDto;
    }

    @Override
    public DimensionDto retrieveDimension(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveDimension(uuid, null);

        // Retrieve
        Dimension dimension = getIndicatorsSystemsService().retrieveDimension(ctx, uuid);
        DimensionDto dimensionDto = do2DtoMapper.dimensionDoToDto(dimension, Boolean.TRUE);
        return dimensionDto;
    }

    @Override
    public void deleteDimension(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkDeleteDimension(uuid, null);

        // Check indicators system state
        Dimension dimension = getIndicatorsSystemsService().retrieveDimension(ctx, uuid);
        IndicatorsSystemVersion indicatorsSystemVersion = dimension.getElementLevel().getIndicatorsSystemVersion();
        checkIndicatorSystemVersionInProduction(indicatorsSystemVersion);

        // Delete
        getIndicatorsSystemsService().deleteDimension(ctx, dimension.getElementLevel());

        // Update orders of other elements in level
        List<ElementLevel> elementsAtLevel = null;
        if (dimension.getElementLevel().getParent() == null) {
            elementsAtLevel = indicatorsSystemVersion.getChildrenFirstLevel();
        } else {
            elementsAtLevel = dimension.getElementLevel().getParent().getChildren();
        }
        updateOrdersInLevelRemovingElement(ctx, elementsAtLevel, dimension.getElementLevel().getOrderInLevel());
    }

    @Override
    public List<DimensionDto> findDimensions(ServiceContext ctx, String indicatorsSystemUuid, String indicatorsSystemVersion) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindDimensions(indicatorsSystemUuid, indicatorsSystemVersion, null);

        // Retrieve dimensions and transform
        List<Dimension> dimensions = getIndicatorsSystemsService().findDimensions(ctx, indicatorsSystemUuid, indicatorsSystemVersion);
        List<DimensionDto> dimensionsDto = new ArrayList<DimensionDto>();
        for (Dimension dimension : dimensions) {
            dimensionsDto.add(do2DtoMapper.dimensionDoToDto(dimension, Boolean.TRUE));
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
        checkIndicatorSystemVersionInProduction(indicatorsSystemVersion);

        // Transform and update
        dto2DoMapper.dimensionDtoToDo(dimensionDto, dimension);
        getIndicatorsSystemsService().updateDimension(ctx, dimension);
    }

    @Override
    public void updateDimensionLocation(ServiceContext ctx, String uuid, String parentTargetUuid, Long orderInLevel) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkUpdateDimensionLocation(uuid, parentTargetUuid, orderInLevel, null);

        // Retrieve and transform
        Dimension dimension = getIndicatorsSystemsService().retrieveDimension(ctx, uuid);
        ElementLevel elementLevel = dimension.getElementLevel();
        Long orderInLevelBefore = elementLevel.getOrderInLevel();
        elementLevel.setOrderInLevel(orderInLevel);

        // Check indicators system state
        IndicatorsSystemVersion indicatorsSystemVersion = elementLevel.getIndicatorsSystemVersion();
        checkIndicatorSystemVersionInProduction(indicatorsSystemVersion);

        // Check target parent is not children of this dimension
        if (parentTargetUuid != null) {
            checkDimensionIsNotChildren(ctx, dimension, parentTargetUuid);
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
            updateOrdersInLevelAddingElement(ctx, elementsInLevel, elementLevel);

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
                parentActual.getChildren().remove(dimension);
                // getIndicatorsSystemsService().updateDimension(ctx, dimensionParentActual);
                getIndicatorsSystemsService().updateElementLevel(ctx, parentActual);
                // Update order of other dimensions
                updateOrdersInLevelRemovingElement(ctx, parentActual.getChildren(), orderInLevelBefore);
            } else {
                indicatorsSystemVersion.getChildrenFirstLevel().remove(dimension);
                getIndicatorsSystemsService().updateIndicatorsSystemVersion(ctx, indicatorsSystemVersion);
                // Update order of other dimensions
                updateOrdersInLevelRemovingElement(ctx, indicatorsSystemVersion.getChildrenFirstLevel(), orderInLevelBefore);
            }

        } else {
            // Same parent, only changes order
            // Check order is correct and update orders
            List<ElementLevel> elementsInLevel = dimension.getElementLevel().getParent() != null ? dimension.getElementLevel().getParent().getChildren() : dimension.getElementLevel()
                    .getIndicatorsSystemVersionFirstLevel().getChildrenFirstLevel();
            updateOrdersInLevelChangingOrder(ctx, elementsInLevel, dimension.getUuid(), orderInLevelBefore, dimension.getElementLevel().getOrderInLevel());
            getIndicatorsSystemsService().updateElementLevel(ctx, elementLevel);
        }
    }

    // TODO atributos de granularidadesId como foreign keys a las tablas de granularidades?
    @Override
    public IndicatorInstanceDto createIndicatorInstance(ServiceContext ctx, String indicatorsSystemUuid, IndicatorInstanceDto indicatorInstanceDto) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkCreateIndicatorInstance(indicatorsSystemUuid, indicatorInstanceDto, null);

        // Retrieve indicators system version and check it is in production
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorsSystemStateInProduction(ctx, indicatorsSystemUuid, true);

        // Transform
        ElementLevel elementLevel = dto2DoMapper.indicatorInstanceDtoToDo(indicatorInstanceDto);

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
            updateOrdersInLevelAddingElement(ctx, indicatorsSystemVersion.getChildrenFirstLevel(), elementLevel);
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
            updateOrdersInLevelAddingElement(ctx, elementLevelParent.getChildren(), elementLevel);

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
        checkIndicatorSystemVersionInProduction(indicatorsSystemVersion);

        // Delete
        getIndicatorsSystemsService().deleteIndicatorInstance(ctx, indicatorInstance.getElementLevel());

        // Update orders of other elements in level
        List<ElementLevel> elementsAtLevel = null;
        if (indicatorInstance.getElementLevel().getParent() == null) {
            elementsAtLevel = indicatorsSystemVersion.getChildrenFirstLevel();
        } else {
            elementsAtLevel = indicatorInstance.getElementLevel().getParent().getChildren();
        }
        updateOrdersInLevelRemovingElement(ctx, elementsAtLevel, indicatorInstance.getElementLevel().getOrderInLevel());
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
    public List<IndicatorInstanceDto> findIndicatorsInstancesByDimension(ServiceContext ctx, String dimensionUuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindIndicatorsInstancesByDimension(dimensionUuid, null);

        // Retrieve indicators instances and transform
        List<IndicatorInstance> indicatorsInstances = getIndicatorsSystemsService().findIndicatorsInstancesByDimension(ctx, dimensionUuid);
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
        checkIndicatorSystemVersionInProduction(indicatorsSystemVersion);

        // Transform and update
        dto2DoMapper.indicatorInstanceDtoToDo(indicatorInstanceDto, indicatorInstance);
        getIndicatorsSystemsService().updateIndicatorInstance(ctx, indicatorInstance);
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
    private void validateCodeUnique(ServiceContext ctx, String code, String actualUuid) throws MetamacException {
        List<IndicatorsSystem> indicatorsSystems = getIndicatorsSystemsService().findIndicatorsSystems(ctx, code);
        if (indicatorsSystems != null && indicatorsSystems.size() != 0 && !indicatorsSystems.get(0).getUuid().equals(actualUuid)) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED, code);
        }
    }

    /**
     * Checks not exists another indicators system with same uri in Gopestat. Checks system retrieved not is actual system.
     */
    private void validateUriGopestatUnique(ServiceContext ctx, String uriGopestat, String actualUuid) throws MetamacException {
        List<IndicatorsSystemVersion> indicatorsSystemVersions = getIndicatorsSystemsService().findIndicatorsSystemVersions(ctx, uriGopestat, null);
        if (indicatorsSystemVersions != null && indicatorsSystemVersions.size() != 0) {
            for (IndicatorsSystemVersion indicatorsSystemVersion : indicatorsSystemVersions) {
                if (!indicatorsSystemVersion.getIndicatorsSystem().getUuid().equals(actualUuid)) {
                    throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_ALREADY_EXIST_URI_GOPESTAT_DUPLICATED, uriGopestat);
                }
            }
        }
    }

    private void checkIndicatorSystemVersionInProduction(IndicatorsSystemVersion indicatorsSystemVersion) throws MetamacException {
        IndicatorsSystemStateEnum state = indicatorsSystemVersion.getState();
        boolean inProduction = IndicatorsSystemStateEnum.DRAFT.equals(state) || IndicatorsSystemStateEnum.VALIDATION_REJECTED.equals(state)
                || IndicatorsSystemStateEnum.PRODUCTION_VALIDATION.equals(state) || IndicatorsSystemStateEnum.DIFFUSION_VALIDATION.equals(state);
        if (!inProduction) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE, indicatorsSystemVersion.getIndicatorsSystem().getUuid(), new IndicatorsSystemStateEnum[]{
                    IndicatorsSystemStateEnum.DRAFT, IndicatorsSystemStateEnum.VALIDATION_REJECTED, IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, IndicatorsSystemStateEnum.DIFFUSION_VALIDATION});

        }
    }

    private void updateOrdersInLevelAddingElement(ServiceContext ctx, List<ElementLevel> elementsAtLevel, ElementLevel elementToAdd) throws MetamacException {

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

    private void updateOrdersInLevelRemovingElement(ServiceContext ctx, List<ElementLevel> elementsAtLevel, Long orderBeforeUpdate) throws MetamacException {
        for (ElementLevel elementInLevel : elementsAtLevel) {
            if (elementInLevel.getOrderInLevel() > orderBeforeUpdate) {
                elementInLevel.setOrderInLevel(elementInLevel.getOrderInLevel() - 1);
                getIndicatorsSystemsService().updateElementLevel(ctx, elementInLevel);
            }
        }
    }

    private void updateOrdersInLevelChangingOrder(ServiceContext ctx, List<ElementLevel> elementsAtLevel, String dimensionUuid, Long orderBeforeUpdate, Long orderAfterUpdate) throws MetamacException {

        // Checks orders
        if (orderAfterUpdate > elementsAtLevel.size()) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, "ORDER_IN_LEVEL");
        }

        // Update orders
        for (ElementLevel elementInLevel : elementsAtLevel) {
            if (elementInLevel.getDimension() != null && elementInLevel.getElementUuid().equals(dimensionUuid)) {
                continue;
            }
            if (orderAfterUpdate < orderBeforeUpdate) {
                if (elementInLevel.getOrderInLevel() >= orderAfterUpdate && elementInLevel.getOrderInLevel() < orderBeforeUpdate) {
                    elementInLevel.setOrderInLevel(elementInLevel.getOrderInLevel() + 1);
                    getIndicatorsSystemsService().updateElementLevel(ctx, elementInLevel);
                }
            } else if (orderAfterUpdate > orderBeforeUpdate) {
                if (elementInLevel.getOrderInLevel() > orderBeforeUpdate && elementInLevel.getOrderInLevel() <= orderAfterUpdate) {
                    elementInLevel.setOrderInLevel(elementInLevel.getOrderInLevel() - 1);
                    getIndicatorsSystemsService().updateElementLevel(ctx, elementInLevel);
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
            if (elementLevel.getIndicatorInstance() != null && elementLevel.getIndicatorInstance().getIndicatorUuid().equalsIgnoreCase(indicatorUuid)) {
                throw new MetamacException(ServiceExceptionType.INDICATOR_INSTANCE_ALREADY_EXIST_INDICATOR_SAME_LEVEL, indicatorUuid);
            }
        }
    }

    /**
     * Makes validations to sent to production validation
     * 1) Must exists at least one indicator instance
     */
    private void validateIndicatorsSystemToSendToProductionValidation(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {

        // Check exists at least one indicator instance
        if (!getIndicatorsSystemsService().existAnyIndicatorInstance(ctx, uuid, versionNumber)) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_MUST_HAVE_INDICATOR_INSTANCE, uuid);
        }
    }

    /**
     * Makes validations to sent to diffusion validation
     * 1) Validations when send to production validation
     */
    private void validateIndicatorsSystemToSendToDiffusionValidation(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {

        validateIndicatorsSystemToSendToProductionValidation(ctx, uuid, versionNumber);
    }

    /**
     * Makes validations to publish
     * 1) Validations when send to diffusion validation
     */
    private void validateIndicatorsSystemToPublish(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {

        validateIndicatorsSystemToSendToDiffusionValidation(ctx, uuid, versionNumber);
    }

    /**
     * Makes validations to archive
     * 1) Validations when publish
     */
    private void validateIndicatorsSystemToArchive(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {

        validateIndicatorsSystemToPublish(ctx, uuid, versionNumber);
    }

    /**
     * Makes validations to reject
     */
    private void validateIndicatorsSystemToReject(ServiceContext ctx, String uuid, String versionNumber) {

        // Nothing
    }
}
