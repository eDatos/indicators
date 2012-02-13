package es.gobcan.istac.indicators.core.serviceimpl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersionInformation;
import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemStateEnum;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemVersionEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.mapper.Do2DtoMapper;
import es.gobcan.istac.indicators.core.mapper.Dto2DoMapper;
import es.gobcan.istac.indicators.core.serviceimpl.util.DoCopyUtils;
import es.gobcan.istac.indicators.core.serviceimpl.util.InvocationValidator;

/**
 * Implementation of IndicatorsSystemServiceFacade.
 * TODO no extender los DTO de auditableDto, porque tienen el Id
 */
@Service("indicatorsSystemServiceFacade")
public class IndicatorsSystemServiceFacadeImpl extends IndicatorsSystemServiceFacadeImplBase {

    @Autowired
    private Do2DtoMapper        do2DtoMapper;

    @Autowired
    private Dto2DoMapper        dto2DoMapper;

    private static final String VERSION_NUMBER_INITIAL = "1.000";
    private final NumberFormat  formatterMajor         = new DecimalFormat("0");
    private final NumberFormat  formatterMinor         = new DecimalFormat("000");

    public IndicatorsSystemServiceFacadeImpl() {
    }

    /**
     * TODO Devolver una uri, en lugar del uuid (ojo! uris rests?)
     * TODO qué datos se almacenarán? Los sistemas de indicadores se obtienen desde Gopestat, y aquí se almacenan como "referencias" a ellas
     */
    public IndicatorsSystemDto createIndicatorsSystem(ServiceContext ctx, IndicatorsSystemDto indicatorsSystemDto) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkCreateIndicatorsSystem(indicatorsSystemDto, null);
        validateCodeUnique(ctx, indicatorsSystemDto.getCode(), null);
        validateUriGopestatUnique(ctx, indicatorsSystemDto.getUriGopestat(), null);

        // Transform
        IndicatorsSystem indicatorsSystem = new IndicatorsSystem();
        indicatorsSystem.setDiffusionVersion(null);
        dto2DoMapper.indicatorsSystemDtoToDo(indicatorsSystemDto, indicatorsSystem, ctx);
        // Version in draft
        IndicatorsSystemVersion draftVersion = dto2DoMapper.indicatorsSystemDtoToDo(indicatorsSystemDto, ctx);
        draftVersion.setState(IndicatorsSystemStateEnum.DRAFT);
        draftVersion.setVersionNumber(getVersionNumber(null, IndicatorsSystemVersionEnum.MAJOR));

        // Create
        IndicatorsSystemVersion indicatorsSystemVersionCreated = getIndicatorsSystemService().createIndicatorsSystemVersion(ctx, indicatorsSystem, draftVersion);

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
            IndicatorsSystem indicatorsSystem = getIndicatorsSystemService().retrieveIndicatorsSystem(ctx, uuid);
            version = indicatorsSystem.getProductionVersion() != null ? indicatorsSystem.getProductionVersion().getVersionNumber() : indicatorsSystem.getDiffusionVersion().getVersionNumber();
        }
        indicatorsSystemVersion = getIndicatorsSystemService().retrieveIndicatorsSystemVersion(ctx, uuid, version);

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
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getErrorCode(), ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getMessageForReasonType(),
                    uuid, IndicatorsSystemStateEnum.PUBLISHED);
        }

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(publishedIndicatorsSystemVersion);
        return indicatorsSystemDto;
    }

    // TODO comprobar que borra dimensiones, instancias de indicadores. No borra indicadores asociados mediante instancias de indicadores
    public void deleteIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkDeleteIndicatorsSystem(uuid, null);

        // Retrieve version in production
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorsSystemStateInProduction(ctx, uuid, true);

        // Delete whole indicators system or only last version
        if (VERSION_NUMBER_INITIAL.equals(indicatorsSystemVersion.getVersionNumber())) {
            // If indicator system is not published or archived, delete whole indicators system
            getIndicatorsSystemService().deleteIndicatorsSystem(ctx, uuid);
        } else {
            getIndicatorsSystemService().deleteIndicatorsSystemVersion(ctx, uuid, indicatorsSystemVersion.getVersionNumber());
            indicatorsSystemVersion.getIndicatorsSystem().setProductionVersion(null);
            getIndicatorsSystemService().updateIndicatorsSystem(ctx, indicatorsSystemVersion.getIndicatorsSystem());
        }
    }

    public void updateIndicatorsSystem(ServiceContext ctx, IndicatorsSystemDto indicatorsSystemDto) throws MetamacException {

        // Retrieve version in production
        IndicatorsSystemVersion indicatorsSystemInProduction = retrieveIndicatorsSystemStateInProduction(ctx, indicatorsSystemDto.getUuid(), true);
        if (!indicatorsSystemInProduction.getVersionNumber().equals(indicatorsSystemDto.getVersionNumber())) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_VERSION_WRONG_STATE.getErrorCode(),
                    ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_VERSION_WRONG_STATE.getMessageForReasonType(), indicatorsSystemDto.getUuid(), indicatorsSystemDto.getVersionNumber());
        }

        // Validation
        InvocationValidator.checkUpdateIndicatorsSystem(indicatorsSystemDto, indicatorsSystemInProduction, null);

        // Transform
        // TODO atributos de IndicatorsSystem actualizables? ej: code
        dto2DoMapper.indicatorsSystemDtoToDo(indicatorsSystemDto, indicatorsSystemInProduction, ctx);

        // Update
        getIndicatorsSystemService().updateIndicatorsSystemVersion(ctx, indicatorsSystemInProduction);
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
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getErrorCode(), ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getMessageForReasonType(),
                    uuid, new IndicatorsSystemStateEnum[]{IndicatorsSystemStateEnum.DRAFT, IndicatorsSystemStateEnum.VALIDATION_REJECTED});
        }

        // Update state
        indicatorsSystemInProduction.setState(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION);
        indicatorsSystemInProduction.setProductionValidationDate(new DateTime());
        indicatorsSystemInProduction.setProductionValidationUser(ctx.getUserId());
        getIndicatorsSystemService().updateIndicatorsSystemVersion(ctx, indicatorsSystemInProduction);
    }

    @Override
    public void sendIndicatorsSystemToDiffusionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkSendIndicatorsSystemToDiffusionValidation(uuid, null);

        // Retrieve version in production validation
        IndicatorsSystemVersion indicatorsSystemInProduction = retrieveIndicatorsSystemStateInProduction(ctx, uuid, false);
        if (indicatorsSystemInProduction == null || !IndicatorsSystemStateEnum.PRODUCTION_VALIDATION.equals(indicatorsSystemInProduction.getState())) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getErrorCode(), ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getMessageForReasonType(),
                    uuid, IndicatorsSystemStateEnum.PRODUCTION_VALIDATION);
        }

        // Update state
        indicatorsSystemInProduction.setState(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION);
        indicatorsSystemInProduction.setDiffusionValidationDate(new DateTime());
        indicatorsSystemInProduction.setDiffusionValidationUser(ctx.getUserId());
        getIndicatorsSystemService().updateIndicatorsSystemVersion(ctx, indicatorsSystemInProduction);
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
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getErrorCode(), ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getMessageForReasonType(),
                    uuid, new IndicatorsSystemStateEnum[]{IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, IndicatorsSystemStateEnum.DIFFUSION_VALIDATION});
        }

        // Update state
        indicatorsSystemInProduction.setState(IndicatorsSystemStateEnum.VALIDATION_REJECTED);
        indicatorsSystemInProduction.setProductionValidationDate(null);
        indicatorsSystemInProduction.setProductionValidationUser(null);
        indicatorsSystemInProduction.setDiffusionValidationDate(null);
        indicatorsSystemInProduction.setDiffusionValidationUser(null);
        getIndicatorsSystemService().updateIndicatorsSystemVersion(ctx, indicatorsSystemInProduction);
    }

    @Override
    public void publishIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkPublishIndicatorsSystem(uuid, null);

        // Retrieve version in diffusion validation
        IndicatorsSystemVersion indicatorsSystemInProduction = retrieveIndicatorsSystemStateInProduction(ctx, uuid, false);
        if (indicatorsSystemInProduction == null || !IndicatorsSystemStateEnum.DIFFUSION_VALIDATION.equals(indicatorsSystemInProduction.getState())) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getErrorCode(), ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getMessageForReasonType(),
                    uuid, IndicatorsSystemStateEnum.DIFFUSION_VALIDATION);
        }

        // Update state
        indicatorsSystemInProduction.setState(IndicatorsSystemStateEnum.PUBLISHED);
        indicatorsSystemInProduction.setPublicationDate(new DateTime());
        indicatorsSystemInProduction.setPublicationUser(ctx.getUserId());
        getIndicatorsSystemService().updateIndicatorsSystemVersion(ctx, indicatorsSystemInProduction);

        IndicatorsSystem indicatorsSystem = indicatorsSystemInProduction.getIndicatorsSystem();
        // Remove possible last version in diffusion
        if (indicatorsSystem.getDiffusionVersion() != null) {
            getIndicatorsSystemService().deleteIndicatorsSystemVersion(ctx, uuid, indicatorsSystem.getDiffusionVersion().getVersionNumber());
        }
        indicatorsSystem.setDiffusionVersion(new IndicatorsSystemVersionInformation(indicatorsSystemInProduction.getId(), indicatorsSystemInProduction.getVersionNumber()));
        indicatorsSystem.setProductionVersion(null);

        getIndicatorsSystemService().updateIndicatorsSystem(ctx, indicatorsSystem);
    }

    @Override
    public void archiveIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkArchiveIndicatorsSystem(uuid, null);

        // Retrieve version published
        IndicatorsSystemVersion indicatorsSystemInDiffusion = retrieveIndicatorsSystemStateInDiffusion(ctx, uuid, false);
        if (indicatorsSystemInDiffusion == null || !IndicatorsSystemStateEnum.PUBLISHED.equals(indicatorsSystemInDiffusion.getState())) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getErrorCode(), ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getMessageForReasonType(),
                    uuid, IndicatorsSystemStateEnum.PUBLISHED);
        }

        // Update state
        indicatorsSystemInDiffusion.setState(IndicatorsSystemStateEnum.ARCHIVED);
        indicatorsSystemInDiffusion.setArchiveDate(new DateTime());
        indicatorsSystemInDiffusion.setArchiveUser(ctx.getUserId());
        getIndicatorsSystemService().updateIndicatorsSystemVersion(ctx, indicatorsSystemInDiffusion);
    }

    // TODO versionar dimensiones, indicadores...
    @Override
    public IndicatorsSystemDto versioningIndicatorsSystem(ServiceContext ctx, String uuid, IndicatorsSystemVersionEnum versionType) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkVersioningIndicatorsSystem(uuid, versionType, null);

        IndicatorsSystem indicatorsSystem = getIndicatorsSystemService().retrieveIndicatorsSystem(ctx, uuid);
        if (indicatorsSystem.getProductionVersion() != null) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getErrorCode(), ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getMessageForReasonType(),
                    uuid, new IndicatorsSystemStateEnum[]{IndicatorsSystemStateEnum.PUBLISHED, IndicatorsSystemStateEnum.ARCHIVED});
        }

        // Initialize new version, copying values of version in diffusion
        IndicatorsSystemVersion indicatorsSystemVersionDiffusion = retrieveIndicatorsSystemStateInDiffusion(ctx, uuid, true);
        IndicatorsSystemVersion indicatorsSystemNewVersion = DoCopyUtils.copy(indicatorsSystemVersionDiffusion);
        indicatorsSystemNewVersion.setState(IndicatorsSystemStateEnum.DRAFT);
        indicatorsSystemNewVersion.setVersionNumber(getVersionNumber(indicatorsSystemVersionDiffusion.getVersionNumber(), versionType));

        // Create
        IndicatorsSystemVersion indicatorsSystemVersionCreated = getIndicatorsSystemService().createIndicatorsSystemVersion(ctx, indicatorsSystem, indicatorsSystemNewVersion);

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
        List<IndicatorsSystem> indicatorsSystems = getIndicatorsSystemService().findIndicatorsSystems(ctx, null);

        // Transform
        List<IndicatorsSystemDto> indicatorsSystemsDto = new ArrayList<IndicatorsSystemDto>();
        for (IndicatorsSystem indicatorsSystem : indicatorsSystems) {
            // Last version
            IndicatorsSystemVersionInformation lastVersion = indicatorsSystem.getProductionVersion() != null ? indicatorsSystem.getProductionVersion() : indicatorsSystem.getDiffusionVersion();
            IndicatorsSystemVersion indicatorsSystemLastVersion = getIndicatorsSystemService().retrieveIndicatorsSystemVersion(ctx, indicatorsSystem.getUuid(), lastVersion.getVersionNumber());
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
        List<IndicatorsSystemVersion> indicatorsSystemsVersion = getIndicatorsSystemService().findIndicatorsSystemVersions(ctx, null, IndicatorsSystemStateEnum.PUBLISHED);

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
        Dimension dimension = dto2DoMapper.dimensionDtoToDo(dimensionDto);
        dimension.setIndicatorsSystemVersion(indicatorsSystemVersion);
        dimension = getIndicatorsSystemService().createDimension(ctx, dimension);

        // Create dimension, adding to indicators system or to parent dimension
        if (dimensionDto.getParentDimensionUuid() == null) {
            // Create dimension
            dimension = getIndicatorsSystemService().createDimension(ctx, dimension);

            // Update indicators system adding dimension
            indicatorsSystemVersion.addDimension(dimension);
            getIndicatorsSystemService().updateIndicatorsSystemVersion(ctx, indicatorsSystemVersion);

        } else {
            // If provided, retrieve dimension parent and checks belongs to indicator system version retrieved
            Dimension dimensionParent = getIndicatorsSystemService().retrieveDimension(ctx, dimensionDto.getParentDimensionUuid());
            // Check dimension parent belogs to indicators system provided
            IndicatorsSystemVersion indicatorsSystemVersionOfDimensionParent = retrieveIndicatorSystemVersionOfDimension(dimensionParent);
            if (!indicatorsSystemVersionOfDimensionParent.getIndicatorsSystem().getUuid().equals(indicatorsSystemUuid)) {
                throw new MetamacException(ServiceExceptionType.SERVICE_DIMENSION_NOT_FOUND_IN_INDICATORS_SYSTEM.getErrorCode(),
                        ServiceExceptionType.SERVICE_DIMENSION_NOT_FOUND_IN_INDICATORS_SYSTEM.getMessageForReasonType(), dimensionDto.getParentDimensionUuid(), indicatorsSystemUuid);
            }
            // Create subdimension
            dimension.setParent(dimensionParent);
            dimension = getIndicatorsSystemService().createDimension(ctx, dimension);

            // Add subdimension to parent dimension
            dimensionParent.addSubdimension(dimension);
            getIndicatorsSystemService().updateDimension(ctx, dimensionParent);
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
        Dimension dimension = getIndicatorsSystemService().retrieveDimension(ctx, uuid);
        DimensionDto dimensionDto = do2DtoMapper.dimensionDoToDto(dimension);
        return dimensionDto;
    }

    @Override
    public void deleteDimension(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkDeleteDimension(uuid, null);

        // Check indicators system state
        Dimension dimension = getIndicatorsSystemService().retrieveDimension(ctx, uuid);
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorSystemVersionOfDimension(dimension);
        checkIndicatorSystemVersionInProduction(indicatorsSystemVersion);
        
        // Delete
        getIndicatorsSystemService().deleteDimension(ctx, dimension);
    }
    
    @Override
    public List<DimensionDto> findDimensions(ServiceContext ctx, String indicatorsSystemUuid, String indicatorsSystemVersion) throws MetamacException {
        
        // Validation of parameters
        InvocationValidator.checkFindDimensions(indicatorsSystemUuid, indicatorsSystemVersion, null);
        
        // Retrieve dimensions and transform
        List<Dimension> dimensions = getIndicatorsSystemService().findDimensions(ctx, indicatorsSystemUuid, indicatorsSystemVersion);
        List<DimensionDto> dimensionsDto = new ArrayList<DimensionDto>();
        for (Dimension dimension : dimensions) {
            dimensionsDto.add(do2DtoMapper.dimensionDoToDto(dimension));
        }

        return dimensionsDto;
    }

    // TODO updateDimension: no permitir cambiar de dimensión padre ni el orden

    private String getVersionNumber(String actualVersionNumber, IndicatorsSystemVersionEnum versionType) throws MetamacException {

        if (actualVersionNumber == null) {
            return VERSION_NUMBER_INITIAL;
        }

        String[] versionNumberSplited = actualVersionNumber.split("\\.");
        Integer versionNumberMajor = Integer.valueOf(versionNumberSplited[0]);
        Integer versionNumberMinor = Integer.valueOf(versionNumberSplited[1]);

        if (IndicatorsSystemVersionEnum.MAJOR.equals(versionType)) {
            versionNumberMajor++;
            versionNumberMinor = 0;
        } else if (IndicatorsSystemVersionEnum.MINOR.equals(versionType)) {
            versionNumberMinor++;
        } else {
            throw new MetamacException(ServiceExceptionType.SERVICE_INVALID_PARAMETER_UNEXPECTED.getErrorCode(), ServiceExceptionType.SERVICE_INVALID_PARAMETER_UNEXPECTED.getMessageForReasonType(),
                    versionType, IndicatorsSystemVersionEnum.class);
        }
        return (new StringBuilder()).append(formatterMajor.format(versionNumberMajor)).append(".").append(formatterMinor.format(versionNumberMinor)).toString();
    }

    /**
     * Retrieves version of an indicators system in production
     */
    private IndicatorsSystemVersion retrieveIndicatorsSystemStateInProduction(ServiceContext ctx, String uuid, boolean throwsExceptionIfNotExistsInProduction) throws MetamacException {
        IndicatorsSystem indicatorsSystem = getIndicatorsSystemService().retrieveIndicatorsSystem(ctx, uuid);
        if (indicatorsSystem == null) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND.getErrorCode(), ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND.getMessageForReasonType(),
                    uuid);
        }
        if (indicatorsSystem.getProductionVersion() == null && !throwsExceptionIfNotExistsInProduction) {
            return null; // to throws an specific exception
        }
        if (indicatorsSystem.getProductionVersion() == null) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_IN_PRODUCTION_NOT_FOUND.getErrorCode(),
                    ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_IN_PRODUCTION_NOT_FOUND.getMessageForReasonType(), uuid);
        }
        IndicatorsSystemVersion indicatorsSystemVersionProduction = getIndicatorsSystemService().retrieveIndicatorsSystemVersion(ctx, uuid, indicatorsSystem.getProductionVersion().getVersionNumber());
        return indicatorsSystemVersionProduction;
    }

    /**
     * Retrieves version of an indicators system in diffusion
     */
    private IndicatorsSystemVersion retrieveIndicatorsSystemStateInDiffusion(ServiceContext ctx, String uuid, boolean throwsExceptionIfNotExistsInDiffusion) throws MetamacException {
        IndicatorsSystem indicatorsSystem = getIndicatorsSystemService().retrieveIndicatorsSystem(ctx, uuid);
        if (indicatorsSystem.getDiffusionVersion() == null && !throwsExceptionIfNotExistsInDiffusion) {
            return null; // to throws an specific exception
        }
        if (indicatorsSystem.getDiffusionVersion() == null) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_IN_DIFFUSION_NOT_FOUND.getErrorCode(),
                    ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_IN_DIFFUSION_NOT_FOUND.getMessageForReasonType(), uuid);
        }
        IndicatorsSystemVersion indicatorsSystemVersionDiffusion = getIndicatorsSystemService().retrieveIndicatorsSystemVersion(ctx, uuid, indicatorsSystem.getDiffusionVersion().getVersionNumber());
        return indicatorsSystemVersionDiffusion;
    }

    /**
     * Checks not exists another indicator system with same code. Checks system retrieved not is actual system.
     */
    private void validateCodeUnique(ServiceContext ctx, String code, String actualUuid) throws MetamacException {
        List<IndicatorsSystem> indicatorsSystems = getIndicatorsSystemService().findIndicatorsSystems(ctx, code);
        if (indicatorsSystems != null && indicatorsSystems.size() != 0 && !indicatorsSystems.get(0).getUuid().equals(actualUuid)) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED.getErrorCode(),
                    ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED.getMessageForReasonType(), code);
        }
    }

    /**
     * Checks not exists another indicator system with same uri in Gopestat. Checks system retrieved not is actual system.
     */
    private void validateUriGopestatUnique(ServiceContext ctx, String uriGopestat, String actualUuid) throws MetamacException {
        List<IndicatorsSystemVersion> indicatorsSystemVersions = getIndicatorsSystemService().findIndicatorsSystemVersions(ctx, uriGopestat, null);
        if (indicatorsSystemVersions != null && indicatorsSystemVersions.size() != 0) {
            for (IndicatorsSystemVersion indicatorsSystemVersion : indicatorsSystemVersions) {
                if (!indicatorsSystemVersion.getIndicatorsSystem().getUuid().equals(actualUuid)) {
                    throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_ALREADY_EXIST_URI_GOPESTAT_DUPLICATED.getErrorCode(),
                            ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_ALREADY_EXIST_URI_GOPESTAT_DUPLICATED.getMessageForReasonType(), uriGopestat);
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
        boolean inProduction = IndicatorsSystemStateEnum.DRAFT.equals(state) || IndicatorsSystemStateEnum.VALIDATION_REJECTED.equals(state) || IndicatorsSystemStateEnum.PRODUCTION_VALIDATION.equals(state)
                || IndicatorsSystemStateEnum.DIFFUSION_VALIDATION.equals(state);
        if (!inProduction) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getErrorCode(),
                    ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getMessageForReasonType(), indicatorsSystemVersion.getIndicatorsSystem().getUuid(), indicatorsSystemVersion.getVersionNumber());

        }
    }
}
