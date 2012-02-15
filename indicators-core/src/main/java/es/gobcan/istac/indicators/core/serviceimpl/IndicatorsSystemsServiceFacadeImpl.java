package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.IndicatorsConstants;
import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersionInformation;
import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
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
 * TODO no extender los DTO de auditableDto, porque tienen el Id
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

    // TODO validación: El indicador debe tener al menos un origen de datos asociado.
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

        // Update state
        indicatorsSystemInProduction.setState(IndicatorsSystemStateEnum.VALIDATION_REJECTED);
        indicatorsSystemInProduction.setProductionValidationDate(null);
        indicatorsSystemInProduction.setProductionValidationUser(null);
        indicatorsSystemInProduction.setDiffusionValidationDate(null);
        indicatorsSystemInProduction.setDiffusionValidationUser(null);
        getIndicatorsSystemsService().updateIndicatorsSystemVersion(ctx, indicatorsSystemInProduction);
    }

    // TODO comprobar que todos los indicadores tienen alguna versión en difusión
    @Override
    public void publishIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkPublishIndicatorsSystem(uuid, null);

        // Retrieve version in diffusion validation
        IndicatorsSystemVersion indicatorsSystemInProduction = retrieveIndicatorsSystemStateInProduction(ctx, uuid, false);
        if (indicatorsSystemInProduction == null || !IndicatorsSystemStateEnum.DIFFUSION_VALIDATION.equals(indicatorsSystemInProduction.getState())) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE, uuid, new IndicatorsSystemStateEnum[]{IndicatorsSystemStateEnum.DIFFUSION_VALIDATION});
        }

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

        // Update state
        indicatorsSystemInDiffusion.setState(IndicatorsSystemStateEnum.ARCHIVED);
        indicatorsSystemInDiffusion.setArchiveDate(new DateTime());
        indicatorsSystemInDiffusion.setArchiveUser(ctx.getUserId());
        getIndicatorsSystemsService().updateIndicatorsSystemVersion(ctx, indicatorsSystemInDiffusion);
    }

    // TODO versionar instancias de indicadores...
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

    // TODO al cambiar el orden de una dimensión tener en cuenta tb las instancias de indicadores
    @Override
    public DimensionDto createDimension(ServiceContext ctx, String indicatorsSystemUuid, DimensionDto dimensionDto) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkCreateDimension(indicatorsSystemUuid, dimensionDto, null);

        // Retrieve indicators system version and check it is in production
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorsSystemStateInProduction(ctx, indicatorsSystemUuid, true);

        // Transform
        Dimension dimension = dto2DoMapper.dimensionDtoToDo(dimensionDto);
        dimension = getIndicatorsSystemsService().createDimension(ctx, dimension);

        // Create dimension, adding to indicators system root level or to parent dimension
        if (dimensionDto.getParentDimensionUuid() == null) {

            // Check order is correct
            checkDimensionsOrder(indicatorsSystemVersion.getDimensions(), Boolean.FALSE, dimension.getOrderInLevel());

            // Create dimension
            dimension.setIndicatorsSystemVersion(indicatorsSystemVersion);
            dimension.setParent(null);
            dimension = getIndicatorsSystemsService().createDimension(ctx, dimension);

            // Update indicators system adding dimension
            indicatorsSystemVersion.addDimension(dimension);
            getIndicatorsSystemsService().updateIndicatorsSystemVersion(ctx, indicatorsSystemVersion);

            // Check order and update order of other dimensions in this level
            updateDimensionsOrdersAddingDimension(ctx, indicatorsSystemVersion.getDimensions(), dimension);
        } else {
            // If provided, retrieve dimension parent and checks belongs to indicators system version retrieved
            Dimension dimensionParent = getIndicatorsSystemsService().retrieveDimension(ctx, dimensionDto.getParentDimensionUuid());

            // Check order is correct
            checkDimensionsOrder(dimensionParent.getSubdimensions(), Boolean.FALSE, dimension.getOrderInLevel());

            // Check dimension parent belogs to indicators system provided
            IndicatorsSystemVersion indicatorsSystemVersionOfDimensionParent = retrieveIndicatorSystemVersionOfDimension(dimensionParent);
            if (!indicatorsSystemVersionOfDimensionParent.getIndicatorsSystem().getUuid().equals(indicatorsSystemUuid)) {
                throw new MetamacException(ServiceExceptionType.DIMENSION_NOT_FOUND_IN_INDICATORS_SYSTEM, dimensionDto.getParentDimensionUuid(), indicatorsSystemUuid);
            }
            // Create subdimension
            dimension.setIndicatorsSystemVersion(null);
            dimension.setParent(dimensionParent);
            dimension = getIndicatorsSystemsService().createDimension(ctx, dimension);

            // Update order of other dimensions in this level
            updateDimensionsOrdersAddingDimension(ctx, dimensionParent.getSubdimensions(), dimension);

            // Add subdimension to parent dimension
            dimensionParent.addSubdimension(dimension);
            getIndicatorsSystemsService().updateDimension(ctx, dimensionParent);
        }

        // Transform to Dto to return
        dimensionDto = do2DtoMapper.dimensionDoToDto(dimension);
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
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorSystemVersionOfDimension(dimension);
        checkIndicatorSystemVersionInProduction(indicatorsSystemVersion);

        // Delete
        getIndicatorsSystemsService().deleteDimension(ctx, dimension);
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
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorSystemVersionOfDimension(dimension);
        checkIndicatorSystemVersionInProduction(indicatorsSystemVersion);

        // Transform and update
        dto2DoMapper.dimensionDtoToDo(dimensionDto, dimension);
        getIndicatorsSystemsService().updateDimension(ctx, dimension);
    }

    // TODO al cambiar el orden de una dimensión tener en cuenta tb las instancias de indicadores
    @Override
    public void updateDimensionLocation(ServiceContext ctx, String uuid, String parentTargetUuid, Long orderInLevel) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkUpdateDimensionLocation(uuid, parentTargetUuid, orderInLevel, null);

        // Retrieve and transform
        Dimension dimension = getIndicatorsSystemsService().retrieveDimension(ctx, uuid);
        Long orderInLevelBefore = dimension.getOrderInLevel();
        dimension.setOrderInLevel(orderInLevel);

        // Check indicators system state
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorSystemVersionOfDimension(dimension);
        checkIndicatorSystemVersionInProduction(indicatorsSystemVersion);

        // Check target parent is not children of this dimension
        if (parentTargetUuid != null) {
            checkDimensionIsNotChildren(ctx, dimension, parentTargetUuid);
        }

        // Update parent and/or order
        String parentUuidActual = dimension.getParent() != null ? dimension.getParent().getUuid() : null;
        if ((parentUuidActual == null && parentTargetUuid != null) || (parentUuidActual != null && parentTargetUuid == null)
                || (parentUuidActual != null && parentTargetUuid != null && !parentTargetUuid.equals(parentUuidActual))) {

            Dimension dimensionParentActual = dimension.getParent();
            Dimension dimensionParentTarget = parentTargetUuid != null ? getIndicatorsSystemsService().retrieveDimension(ctx, parentTargetUuid) : null;

            // Update target parent, adding dimension
            List<Dimension> dimensionsInLevel = null;
            if (dimensionParentTarget == null) {
                indicatorsSystemVersion.addDimension(dimension);
                getIndicatorsSystemsService().updateIndicatorsSystemVersion(ctx, indicatorsSystemVersion);
                dimensionsInLevel = indicatorsSystemVersion.getDimensions();
            } else {
                dimensionParentTarget.addSubdimension(dimension);
                getIndicatorsSystemsService().updateDimension(ctx, dimensionParentTarget);
                dimensionsInLevel = dimensionParentTarget.getSubdimensions();
            }
            // Check order is correct and update orders
            checkDimensionsOrder(dimensionsInLevel, Boolean.TRUE, dimension.getOrderInLevel());
            updateDimensionsOrdersAddingDimension(ctx, dimensionsInLevel, dimension);

            // Update dimension, changing parent
            if (dimensionParentTarget == null) {
                dimension.setIndicatorsSystemVersion(indicatorsSystemVersion);
                dimension.setParent(null);
            } else {
                dimension.setIndicatorsSystemVersion(null);
                dimension.setParent(dimensionParentTarget);
            }
            getIndicatorsSystemsService().updateDimension(ctx, dimension);

            // Update actual parent dimension or indicators system version, removing dimension
            if (dimensionParentActual != null) {
                dimensionParentActual.getSubdimensions().remove(dimension);
                getIndicatorsSystemsService().updateDimension(ctx, dimensionParentActual);
                // Update order of other dimensions
                updateDimensionsOrdersRemovingDimension(ctx, dimensionParentActual.getSubdimensions(), orderInLevelBefore);
            } else {
                indicatorsSystemVersion.getDimensions().remove(dimension);
                getIndicatorsSystemsService().updateIndicatorsSystemVersion(ctx, indicatorsSystemVersion);
                // Update order of other dimensions
                updateDimensionsOrdersRemovingDimension(ctx, indicatorsSystemVersion.getDimensions(), orderInLevelBefore);
            }

        } else {
            // Same parent, only changes order
            // Check order is correct and update orders
            List<Dimension> dimensionsInLevel = dimension.getParent() != null ? dimension.getParent().getSubdimensions() : dimension.getIndicatorsSystemVersion().getDimensions();
            checkDimensionsOrder(dimensionsInLevel, Boolean.TRUE, dimension.getOrderInLevel());
            updateDimensionsOrdersChangingOrder(ctx, dimensionsInLevel, dimension.getUuid(), orderInLevelBefore, dimension.getOrderInLevel());
            getIndicatorsSystemsService().updateDimension(ctx, dimension);
        }
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

    private IndicatorsSystemVersion retrieveIndicatorSystemVersionOfDimension(Dimension dimension) {
        while (dimension.getIndicatorsSystemVersion() == null) {
            dimension = dimension.getParent();
        }
        return dimension.getIndicatorsSystemVersion();
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

    private void checkDimensionsOrder(List<Dimension> dimensionsInLevel, Boolean dimensionAlreadyAdded, Long orderInLevel) throws MetamacException {
        Long orderMaximum = !dimensionAlreadyAdded ? Long.valueOf(dimensionsInLevel.size() + 1) : Long.valueOf(dimensionsInLevel.size());
        if (orderInLevel > orderMaximum) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, "ORDER_IN_LEVEL");
        }
    }

    private void updateDimensionsOrdersAddingDimension(ServiceContext ctx, List<Dimension> dimensionsInLevel, Dimension dimension) throws MetamacException {
        for (Dimension dimensionInLevel : dimensionsInLevel) {
            if (dimensionInLevel.getUuid().equals(dimension.getUuid())) {
                continue; // it is possible dimension is already added to parent
            }
            if (dimensionInLevel.getOrderInLevel() >= dimension.getOrderInLevel()) {
                dimensionInLevel.setOrderInLevel(dimensionInLevel.getOrderInLevel() + 1);
                getIndicatorsSystemsService().updateDimension(ctx, dimensionInLevel);
            }
        }
    }

    private void updateDimensionsOrdersRemovingDimension(ServiceContext ctx, List<Dimension> dimensionsInLevel, Long orderBeforeUpdate) throws MetamacException {
        for (Dimension dimensionInLevel : dimensionsInLevel) {
            if (dimensionInLevel.getOrderInLevel() > orderBeforeUpdate) {
                dimensionInLevel.setOrderInLevel(dimensionInLevel.getOrderInLevel() - 1);
                getIndicatorsSystemsService().updateDimension(ctx, dimensionInLevel);
            }
        }
    }

    private void updateDimensionsOrdersChangingOrder(ServiceContext ctx, List<Dimension> dimensionsInLevel, String dimensionUuid, Long orderBeforeUpdate, Long orderAfterUpdate)
            throws MetamacException {
        for (Dimension dimensionInLevel : dimensionsInLevel) {
            if (dimensionInLevel.getUuid().equals(dimensionUuid)) {
                continue;
            }
            if (orderAfterUpdate < orderBeforeUpdate) {
                if (dimensionInLevel.getOrderInLevel() >= orderAfterUpdate && dimensionInLevel.getOrderInLevel() < orderBeforeUpdate) {
                    dimensionInLevel.setOrderInLevel(dimensionInLevel.getOrderInLevel() + 1);
                    getIndicatorsSystemsService().updateDimension(ctx, dimensionInLevel);
                }
            } else if (orderAfterUpdate > orderBeforeUpdate) {
                if (dimensionInLevel.getOrderInLevel() > orderBeforeUpdate && dimensionInLevel.getOrderInLevel() <= orderAfterUpdate) {
                    dimensionInLevel.setOrderInLevel(dimensionInLevel.getOrderInLevel() - 1);
                    getIndicatorsSystemsService().updateDimension(ctx, dimensionInLevel);
                }
            }
        }
    }

    /**
     * We can not move a dimension to its child
     */
    private void checkDimensionIsNotChildren(ServiceContext ctx, Dimension dimension, String parentTargetUuid) throws MetamacException {

        Dimension dimensionTarget = getIndicatorsSystemsService().retrieveDimension(ctx, parentTargetUuid);
        Dimension dimensionParent = dimensionTarget.getParent();
        while (dimensionParent != null) {
            if (dimensionParent.getUuid().equals(dimension.getUuid())) {
                throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, "PARENT_TARGET_UUID");
            }
            dimensionParent = dimensionParent.getParent();
        }
    }
}
