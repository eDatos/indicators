package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.IndicatorsConstants;
import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.ElementLevel;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersionInformation;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemProcStatusEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersiontTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceimpl.util.DoCopyUtils;
import es.gobcan.istac.indicators.core.serviceimpl.util.InvocationValidator;
import es.gobcan.istac.indicators.core.serviceimpl.util.ServiceUtils;

/**
 * Implementation of IndicatorsSystemService.
 */
@Service("indicatorsSystemService")
public class IndicatorsSystemsServiceImpl extends IndicatorsSystemsServiceImplBase {

    public IndicatorsSystemsServiceImpl() {
    }

    /**
     * TODO qué datos se almacenarán? Los sistemas de indicadores se obtienen desde Gopestat, y aquí se almacenan como "referencias" a ellas
     */
    @Override
    public IndicatorsSystemVersion createIndicatorsSystem(ServiceContext ctx, IndicatorsSystemVersion indicatorsSystemVersion) throws MetamacException {

        IndicatorsSystem indicatorsSystem = indicatorsSystemVersion.getIndicatorsSystem();
        
        // Validation of parameters
        InvocationValidator.checkCreateIndicatorsSystem(indicatorsSystemVersion, null);
        checkIndicatorsSystemCodeUnique(ctx, indicatorsSystem.getCode(), null);
        checkIndicatorsSystemUriGopestatUnique(ctx, indicatorsSystemVersion.getUriGopestat(), null);

        // Save indicator
        indicatorsSystem.setDiffusionVersion(null);
        indicatorsSystem.setIsPublished(Boolean.FALSE);
        indicatorsSystem = getIndicatorsSystemRepository().save(indicatorsSystem);

        // Save draft version
        indicatorsSystemVersion.setProcStatus(IndicatorsSystemProcStatusEnum.DRAFT);
        indicatorsSystemVersion.setVersionNumber(ServiceUtils.generateVersionNumber(null, VersiontTypeEnum.MAJOR));
        indicatorsSystemVersion.setIndicatorsSystem(indicatorsSystem);
        indicatorsSystemVersion = getIndicatorsSystemVersionRepository().save(indicatorsSystemVersion);

        // Update indicator with draft version
        indicatorsSystem.setProductionVersion(new IndicatorsSystemVersionInformation(indicatorsSystemVersion.getId(), indicatorsSystemVersion.getVersionNumber()));
        indicatorsSystem.getVersions().add(indicatorsSystemVersion);
        getIndicatorsSystemRepository().save(indicatorsSystem);

        return indicatorsSystemVersion;
    }

    @Override
    public IndicatorsSystemVersion retrieveIndicatorsSystem(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorsSystem(uuid, versionNumber, null);

        // Retrieve version requested or last version
        if (versionNumber == null) {
            // Retrieve last version
            IndicatorsSystem indicatorsSystem = retrieveIndicatorsSystem(ctx, uuid);
            versionNumber = indicatorsSystem.getProductionVersion() != null ? indicatorsSystem.getProductionVersion().getVersionNumber() : indicatorsSystem.getDiffusionVersion().getVersionNumber();
        }
        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemVersionRepository().retrieveIndicatorsSystemVersion(uuid, versionNumber);
        if (indicatorsSystemVersion == null) {
            if (versionNumber == null) {
                throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND, uuid);
            } else {
                throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_VERSION_NOT_FOUND, uuid, versionNumber);
            }
        }
        return indicatorsSystemVersion;
    }

    @Override
    public IndicatorsSystemVersion retrieveIndicatorsSystemPublished(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorsSystemPublished(uuid, null);

        // Retrieve published version
        IndicatorsSystemVersion publishedIndicatorsSystemVersion = retrieveIndicatorsSystemProcStatusInDiffusion(ctx, uuid, false);
        if (publishedIndicatorsSystemVersion == null || !IndicatorsSystemProcStatusEnum.PUBLISHED.equals(publishedIndicatorsSystemVersion.getProcStatus())) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, uuid, new IndicatorsSystemProcStatusEnum[]{IndicatorsSystemProcStatusEnum.PUBLISHED});
        }

        return publishedIndicatorsSystemVersion;
    }

    @Override
    public IndicatorsSystemVersion retrieveIndicatorsSystemByCode(ServiceContext ctx, String code, String versionNumber) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorsSystemByCode(code, null);

        // Retrieve indicators system by code
        List<IndicatorsSystem> indicatorsSystems = findIndicatorsSystems(ctx, code);
        if (indicatorsSystems.size() == 0) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND_WITH_CODE, code);
        } else if (indicatorsSystems.size() > 1) {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "Found more than one indicators system with code " + code);
        }

        // Retrieve version requested or last version
        IndicatorsSystem indicatorsSystem = indicatorsSystems.get(0);
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorsSystem(ctx, indicatorsSystem.getUuid(), versionNumber);

        return indicatorsSystemVersion;
    }

    @Override
    public IndicatorsSystemVersion retrieveIndicatorsSystemPublishedByCode(ServiceContext ctx, String code) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorsSystemPublishedByCode(code, null);

        // Retrieve indicators system by code
        List<IndicatorsSystem> indicatorsSystems = findIndicatorsSystems(ctx, code);
        if (indicatorsSystems.size() == 0) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND_WITH_CODE, code);
        } else if (indicatorsSystems.size() > 1) {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "Found more than one indicators system with code " + code);
        }

        // Retrieve only published
        IndicatorsSystem indicatorsSystem = indicatorsSystems.get(0);
        if (indicatorsSystem.getDiffusionVersion() == null) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, indicatorsSystem.getUuid(), new IndicatorsSystemProcStatusEnum[]{IndicatorsSystemProcStatusEnum.PUBLISHED});
        }
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorsSystem(ctx, indicatorsSystem.getUuid(), indicatorsSystem.getDiffusionVersion().getVersionNumber());
        if (!IndicatorsSystemProcStatusEnum.PUBLISHED.equals(indicatorsSystemVersion.getProcStatus())) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, indicatorsSystem.getUuid(), new IndicatorsSystemProcStatusEnum[]{IndicatorsSystemProcStatusEnum.PUBLISHED});
        }

        return indicatorsSystemVersion;
    }

    @Override
    public List<ElementLevel> retrieveIndicatorsSystemStructure(ServiceContext ctx, String uuid, String version) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorsSystemStructure(uuid, version, null);

        // Retrieve version requested or last version
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorsSystem(ctx, uuid, version);

        // Builds structure
        return indicatorsSystemVersion.getChildrenFirstLevel();
    }

    @Override
    public IndicatorsSystemVersion updateIndicatorsSystemVersion(ServiceContext ctx, IndicatorsSystemVersion indicatorsSystemVersion) throws MetamacException {

        // Validation
        InvocationValidator.checkUpdateIndicatorsSystem(indicatorsSystemVersion, null);

        // Check indicators system proc status
        checkIndicatorsSystemVersionInProduction(indicatorsSystemVersion);
        
        // Update
        indicatorsSystemVersion = getIndicatorsSystemVersionRepository().save(indicatorsSystemVersion);
        
        return indicatorsSystemVersion;
    }

    @Override
    public void deleteIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkDeleteIndicatorsSystem(uuid, null);

        // Retrieve version in production
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorsSystemProcStatusInProduction(ctx, uuid, true);

        // Delete whole indicators system or only last version
        if (IndicatorsConstants.VERSION_NUMBER_INITIAL.equals(indicatorsSystemVersion.getVersionNumber())) {
            // If indicators system is not published or archived, delete whole indicators system
            IndicatorsSystem indicatorsSystem = indicatorsSystemVersion.getIndicatorsSystem();
            getIndicatorsSystemRepository().delete(indicatorsSystem);

        } else {
            IndicatorsSystem indicatorsSystem = indicatorsSystemVersion.getIndicatorsSystem();
            indicatorsSystem.getVersions().remove(indicatorsSystemVersion);
            indicatorsSystem.setProductionVersion(null);

            // Update
            getIndicatorsSystemRepository().save(indicatorsSystem);
            getIndicatorsSystemVersionRepository().delete(indicatorsSystemVersion);
        }
    }

    // TODO obtener directamente las últimas versiones con consulta? añadir columna lastVersion?
    @Override
    public List<IndicatorsSystemVersion> findIndicatorsSystems(ServiceContext ctx) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindIndicatorsSystems(null);

        // Find
        List<IndicatorsSystem> indicatorsSystems = findIndicatorsSystems(ctx, null);

        // Transform
        List<IndicatorsSystemVersion> indicatorsSystemsVersions = new ArrayList<IndicatorsSystemVersion>();
        for (IndicatorsSystem indicatorsSystem : indicatorsSystems) {
            // Last version
            IndicatorsSystemVersionInformation lastVersion = indicatorsSystem.getProductionVersion() != null ? indicatorsSystem.getProductionVersion() : indicatorsSystem.getDiffusionVersion();
            IndicatorsSystemVersion indicatorsSystemLastVersion = retrieveIndicatorsSystem(ctx, indicatorsSystem.getUuid(), lastVersion.getVersionNumber());
            indicatorsSystemsVersions.add(indicatorsSystemLastVersion);
        }

        return indicatorsSystemsVersions;
    }

    @Override
    public List<IndicatorsSystemVersion> findIndicatorsSystemsPublished(ServiceContext ctx) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindIndicatorsSystemsPublished(null);

        // Retrieve published
        List<IndicatorsSystemVersion> indicatorsSystemsVersion = getIndicatorsSystemVersionRepository().findIndicatorsSystemVersions(null, IndicatorsSystemProcStatusEnum.PUBLISHED);
        
        // Transform
        List<IndicatorsSystemVersion> indicatorsSystemsVersions = new ArrayList<IndicatorsSystemVersion>();
        for (IndicatorsSystemVersion indicatorsSystemVersion : indicatorsSystemsVersion) {
            indicatorsSystemsVersions.add(indicatorsSystemVersion);
        }

        return indicatorsSystemsVersions;
    }

    @Override
    public IndicatorsSystemVersion sendIndicatorsSystemToProductionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkSendIndicatorsSystemToProductionValidation(uuid, null);

        // Retrieve version in draft
        IndicatorsSystemVersion indicatorsSystemInProduction = retrieveIndicatorsSystemProcStatusInProduction(ctx, uuid, false);
        if (indicatorsSystemInProduction == null
                || (!IndicatorsSystemProcStatusEnum.DRAFT.equals(indicatorsSystemInProduction.getProcStatus()) && !IndicatorsSystemProcStatusEnum.VALIDATION_REJECTED.equals(indicatorsSystemInProduction.getProcStatus()))) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, uuid, new IndicatorsSystemProcStatusEnum[]{IndicatorsSystemProcStatusEnum.DRAFT,
                    IndicatorsSystemProcStatusEnum.VALIDATION_REJECTED});
        }

        // Validate to send to production
        checkIndicatorsSystemToSendToProductionValidation(ctx, indicatorsSystemInProduction);

        // Update proc status
        indicatorsSystemInProduction.setProcStatus(IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION);
        indicatorsSystemInProduction.setProductionValidationDate(new DateTime());
        indicatorsSystemInProduction.setProductionValidationUser(ctx.getUserId());
        indicatorsSystemInProduction = getIndicatorsSystemVersionRepository().save(indicatorsSystemInProduction);
        
        return indicatorsSystemInProduction;
    }

    @Override
    public IndicatorsSystemVersion sendIndicatorsSystemToDiffusionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkSendIndicatorsSystemToDiffusionValidation(uuid, null);

        // Retrieve version in production validation
        IndicatorsSystemVersion indicatorsSystemInProduction = retrieveIndicatorsSystemProcStatusInProduction(ctx, uuid, false);
        if (indicatorsSystemInProduction == null || !IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION.equals(indicatorsSystemInProduction.getProcStatus())) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, uuid, new IndicatorsSystemProcStatusEnum[]{IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION});
        }

        // Validate to send to diffusion
        checkIndicatorsSystemToSendToDiffusionValidation(ctx, indicatorsSystemInProduction);

        // Update proc status
        indicatorsSystemInProduction.setProcStatus(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION);
        indicatorsSystemInProduction.setDiffusionValidationDate(new DateTime());
        indicatorsSystemInProduction.setDiffusionValidationUser(ctx.getUserId());
        indicatorsSystemInProduction = getIndicatorsSystemVersionRepository().save(indicatorsSystemInProduction);
        
        return indicatorsSystemInProduction;
    }

    @Override
    public IndicatorsSystemVersion rejectIndicatorsSystemValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRejectIndicatorsSystemValidation(uuid, null);

        // Retrieve version in production (proc status can be production or diffusion validation)
        IndicatorsSystemVersion indicatorsSystemInProduction = retrieveIndicatorsSystemProcStatusInProduction(ctx, uuid, false);
        if (indicatorsSystemInProduction == null
                || (!IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION.equals(indicatorsSystemInProduction.getProcStatus()) && !IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION
                        .equals(indicatorsSystemInProduction.getProcStatus()))) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, uuid, new IndicatorsSystemProcStatusEnum[]{IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION,
                    IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION});
        }

        // Validate to reject
        checkIndicatorsSystemToReject(ctx, indicatorsSystemInProduction);

        // Update proc status
        indicatorsSystemInProduction.setProcStatus(IndicatorsSystemProcStatusEnum.VALIDATION_REJECTED);
        indicatorsSystemInProduction.setProductionValidationDate(null);
        indicatorsSystemInProduction.setProductionValidationUser(null);
        indicatorsSystemInProduction.setDiffusionValidationDate(null);
        indicatorsSystemInProduction.setDiffusionValidationUser(null);
        indicatorsSystemInProduction = getIndicatorsSystemVersionRepository().save(indicatorsSystemInProduction);
        
        return indicatorsSystemInProduction;
    }

    @Override
    public IndicatorsSystemVersion publishIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkPublishIndicatorsSystem(uuid, null);

        // Retrieve version in diffusion validation
        IndicatorsSystemVersion indicatorsSystemInProduction = retrieveIndicatorsSystemProcStatusInProduction(ctx, uuid, false);
        if (indicatorsSystemInProduction == null || !IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION.equals(indicatorsSystemInProduction.getProcStatus())) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, uuid, new IndicatorsSystemProcStatusEnum[]{IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION});
        }

        // Validate to publish
        checkIndicatorsSystemToPublish(ctx, indicatorsSystemInProduction);

        // Update proc status
        indicatorsSystemInProduction.setProcStatus(IndicatorsSystemProcStatusEnum.PUBLISHED); 
        indicatorsSystemInProduction.setPublicationDate(new DateTime());
        indicatorsSystemInProduction.setPublicationUser(ctx.getUserId());
        indicatorsSystemInProduction = getIndicatorsSystemVersionRepository().save(indicatorsSystemInProduction);

        IndicatorsSystem indicatorsSystem = indicatorsSystemInProduction.getIndicatorsSystem();
        // Remove possible last version in diffusion
        if (indicatorsSystem.getDiffusionVersion() != null) {
            IndicatorsSystemVersion indicatorDiffusionVersion = retrieveIndicatorsSystem(ctx, uuid, indicatorsSystem.getDiffusionVersion().getVersionNumber());
            indicatorsSystem.getVersions().remove(indicatorDiffusionVersion);
            getIndicatorsSystemRepository().save(indicatorsSystem);
            getIndicatorsSystemVersionRepository().delete(indicatorDiffusionVersion);
        }
        indicatorsSystem.setIsPublished(Boolean.TRUE);
        indicatorsSystem.setDiffusionVersion(new IndicatorsSystemVersionInformation(indicatorsSystemInProduction.getId(), indicatorsSystemInProduction.getVersionNumber()));
        indicatorsSystem.setProductionVersion(null);

        // Update indicators system
        getIndicatorsSystemRepository().save(indicatorsSystem);
        
        return indicatorsSystemInProduction;
    }

    @Override
    public IndicatorsSystemVersion archiveIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkArchiveIndicatorsSystem(uuid, null);

        // Retrieve version published
        IndicatorsSystemVersion indicatorsSystemInDiffusion = retrieveIndicatorsSystemProcStatusInDiffusion(ctx, uuid, false);
        if (indicatorsSystemInDiffusion == null || !IndicatorsSystemProcStatusEnum.PUBLISHED.equals(indicatorsSystemInDiffusion.getProcStatus())) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, uuid, new IndicatorsSystemProcStatusEnum[]{IndicatorsSystemProcStatusEnum.PUBLISHED});
        }

        // Validate to archive
        checkIndicatorsSystemToArchive(ctx, indicatorsSystemInDiffusion);

        // Update proc status
        IndicatorsSystem indicatorsSystem = indicatorsSystemInDiffusion.getIndicatorsSystem();
        indicatorsSystem.setIsPublished(Boolean.FALSE);
        getIndicatorsSystemRepository().save(indicatorsSystem);
        
        indicatorsSystemInDiffusion.setProcStatus(IndicatorsSystemProcStatusEnum.ARCHIVED);
        indicatorsSystemInDiffusion.setArchiveDate(new DateTime());
        indicatorsSystemInDiffusion.setArchiveUser(ctx.getUserId());
        indicatorsSystemInDiffusion = getIndicatorsSystemVersionRepository().save(indicatorsSystemInDiffusion);
        
        return indicatorsSystemInDiffusion;
    }

    @Override
    public IndicatorsSystemVersion versioningIndicatorsSystem(ServiceContext ctx, String uuid, VersiontTypeEnum versionType) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkVersioningIndicatorsSystem(uuid, versionType, null);

        IndicatorsSystem indicatorsSystem = retrieveIndicatorsSystem(ctx, uuid);
        if (indicatorsSystem.getProductionVersion() != null) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, uuid, new IndicatorsSystemProcStatusEnum[]{IndicatorsSystemProcStatusEnum.PUBLISHED,
                    IndicatorsSystemProcStatusEnum.ARCHIVED});
        }

        // Initialize new version, copying values of version in diffusion
        IndicatorsSystemVersion indicatorsSystemVersionDiffusion = retrieveIndicatorsSystemProcStatusInDiffusion(ctx, uuid, true);
        IndicatorsSystemVersion indicatorsSystemNewVersion = DoCopyUtils.copy(indicatorsSystemVersionDiffusion);
        indicatorsSystemNewVersion.setProcStatus(IndicatorsSystemProcStatusEnum.DRAFT);
        indicatorsSystemNewVersion.setVersionNumber(ServiceUtils.generateVersionNumber(indicatorsSystemVersionDiffusion.getVersionNumber(), versionType));

        // Create
        indicatorsSystemNewVersion.setIndicatorsSystem(indicatorsSystem);
        indicatorsSystemNewVersion = getIndicatorsSystemVersionRepository().save(indicatorsSystemNewVersion);
        // Update indicator with draft version
        indicatorsSystem.setProductionVersion(new IndicatorsSystemVersionInformation(indicatorsSystemNewVersion.getId(), indicatorsSystemNewVersion.getVersionNumber()));
        indicatorsSystem.getVersions().add(indicatorsSystemNewVersion);
        getIndicatorsSystemRepository().save(indicatorsSystem);

        return indicatorsSystemNewVersion;
    }

    @Override
    public Dimension createDimension(ServiceContext ctx, String indicatorsSystemUuid, Dimension dimension) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkCreateDimension(indicatorsSystemUuid, dimension, null);

        // Create dimension
        ElementLevel elementLevel = dimension.getElementLevel();
        elementLevel = createElementLevel(ctx, indicatorsSystemUuid, elementLevel);

        return elementLevel.getDimension();
    }

    @Override
    public Dimension retrieveDimension(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveDimension(uuid, null);

        // Retrieve
        Dimension dimension = getDimensionRepository().findDimension(uuid);
        if (dimension == null) {
            throw new MetamacException(ServiceExceptionType.DIMENSION_NOT_FOUND, uuid);
        }
        return dimension;
    }

    @Override
    public Dimension updateDimension(ServiceContext ctx, Dimension dimension) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkUpdateDimension(dimension, null);

        // Check indicators system proc status
        IndicatorsSystemVersion indicatorsSystemVersion = dimension.getElementLevel().getIndicatorsSystemVersion();
        checkIndicatorsSystemVersionInProduction(indicatorsSystemVersion);

        // Update
        return getDimensionRepository().save(dimension);
    }

    @Override
    public Dimension updateDimensionLocation(ServiceContext ctx, String uuid, String parentTargetUuid, Long orderInLevel) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkUpdateDimensionLocation(uuid, parentTargetUuid, orderInLevel, null);

        // Update location
        ElementLevel elementLevel = retrieveDimension(ctx, uuid).getElementLevel();
        elementLevel = updateElementLevelLocation(ctx, elementLevel, parentTargetUuid, orderInLevel);
        
        return elementLevel.getDimension();
    }

    public void deleteDimension(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkDeleteDimension(uuid, null);

        // Retrieve
        Dimension dimension = retrieveDimension(ctx, uuid);
        ElementLevel elementLevel = dimension.getElementLevel();

        // Delete
        deleteElementLevel(ctx, elementLevel);
    }

    @Override
    public List<Dimension> findDimensions(ServiceContext ctx, String indicatorsSystemUuid, String indicatorsSystemVersionNumber) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindDimensions(indicatorsSystemUuid, indicatorsSystemVersionNumber, null);

        // Retrieve dimensions
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorsSystem(ctx, indicatorsSystemUuid, indicatorsSystemVersionNumber);
        List<ElementLevel> levels = indicatorsSystemVersion.getChildrenAllLevels();
        List<Dimension> dimensions = new ArrayList<Dimension>();
        for (ElementLevel level : levels) {
            if (level.isDimension()) {
                dimensions.add(level.getDimension());
            }
        }
        return dimensions;
    }

    // TODO atributos de granularidadesId como foreign keys a las tablas de granularidades?
    // TODO Enum granularidades temporales: falta semestres? Pte Alberto
    @Override
    public IndicatorInstance createIndicatorInstance(ServiceContext ctx, String indicatorsSystemUuid, IndicatorInstance indicatorInstance) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkCreateIndicatorInstance(indicatorsSystemUuid, indicatorInstance, null);

        // Create dimension
        ElementLevel elementLevel = indicatorInstance.getElementLevel();
        elementLevel = createElementLevel(ctx, indicatorsSystemUuid, elementLevel);

        return elementLevel.getIndicatorInstance();
    }

    @Override
    public IndicatorInstance retrieveIndicatorInstance(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorInstance(uuid, null);

        // Retrieve
        IndicatorInstance indicatorInstance = getIndicatorInstanceRepository().findIndicatorInstance(uuid);
        if (indicatorInstance == null) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_INSTANCE_NOT_FOUND, uuid);
        }
        return indicatorInstance;
    }

    @Override
    public IndicatorInstance updateIndicatorInstance(ServiceContext ctx, IndicatorInstance indicatorInstance) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkUpdateIndicatorInstance(indicatorInstance, null);

        // Check indicators system proc status
        IndicatorsSystemVersion indicatorsSystemVersion = indicatorInstance.getElementLevel().getIndicatorsSystemVersion();
        checkIndicatorsSystemVersionInProduction(indicatorsSystemVersion);

        // Update
        return getIndicatorInstanceRepository().save(indicatorInstance);
    }

    @Override
    public IndicatorInstance updateIndicatorInstanceLocation(ServiceContext ctx, String uuid, String parentTargetUuid, Long orderInLevel) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkUpdateIndicatorInstanceLocation(uuid, parentTargetUuid, orderInLevel, null);

        // Update location
        ElementLevel elementLevel = retrieveIndicatorInstance(ctx, uuid).getElementLevel();
        elementLevel = updateElementLevelLocation(ctx, elementLevel, parentTargetUuid, orderInLevel);
        
        return elementLevel.getIndicatorInstance();
    }

    @Override
    public void deleteIndicatorInstance(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkDeleteIndicatorInstance(uuid, null);

        // Retrieve
        IndicatorInstance indicatorInstance = retrieveIndicatorInstance(ctx, uuid);
        ElementLevel elementLevel = indicatorInstance.getElementLevel();

        // Delete
        deleteElementLevel(ctx, elementLevel);
    }

    @Override
    public List<IndicatorInstance> findIndicatorsInstances(ServiceContext ctx, String indicatorsSystemUuid, String indicatorsSystemVersionNumber) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindIndicatorsInstances(indicatorsSystemUuid, indicatorsSystemVersionNumber, null);

        // Retrieve
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorsSystem(ctx, indicatorsSystemUuid, indicatorsSystemVersionNumber);
        List<ElementLevel> levels = indicatorsSystemVersion.getChildrenAllLevels();
        List<IndicatorInstance> indicatorsInstances = new ArrayList<IndicatorInstance>();
        for (ElementLevel level : levels) {
            if (level.isIndicatorInstance()) {
                indicatorsInstances.add(level.getIndicatorInstance());
            }
        }
        return indicatorsInstances;
    }

    /**
     * Checks not exists another indicators system with same code. Checks system retrieved not is actual system.
     */
    private void checkIndicatorsSystemCodeUnique(ServiceContext ctx, String code, String actualUuid) throws MetamacException {
        List<IndicatorsSystem> indicatorsSystems = findIndicatorsSystems(ctx, code);
        if (indicatorsSystems != null && indicatorsSystems.size() != 0 && !indicatorsSystems.get(0).getUuid().equals(actualUuid)) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED, code);
        }
    }

    /**
     * Checks not exists another indicators system with same uri in Gopestat. Checks system retrieved not is actual system.
     */
    private void checkIndicatorsSystemUriGopestatUnique(ServiceContext ctx, String uriGopestat, String actualUuid) throws MetamacException {
        if (uriGopestat != null) {
            List<IndicatorsSystemVersion> indicatorsSystemVersions = getIndicatorsSystemVersionRepository().findIndicatorsSystemVersions(uriGopestat, null);
            if (indicatorsSystemVersions != null && indicatorsSystemVersions.size() != 0) {
                for (IndicatorsSystemVersion indicatorsSystemVersion : indicatorsSystemVersions) {
                    if (!indicatorsSystemVersion.getIndicatorsSystem().getUuid().equals(actualUuid)) {
                        throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_ALREADY_EXIST_URI_GOPESTAT_DUPLICATED, uriGopestat);
                    }
                }
            }
        }
    }

    /**
     * Checks that the indicators system version is in any proc status in production
     */
    private void checkIndicatorsSystemVersionInProduction(IndicatorsSystemVersion indicatorsSystemVersion) throws MetamacException {
        IndicatorsSystemProcStatusEnum procStatus = indicatorsSystemVersion.getProcStatus();
        boolean inProduction = IndicatorsSystemProcStatusEnum.DRAFT.equals(procStatus) || IndicatorsSystemProcStatusEnum.VALIDATION_REJECTED.equals(procStatus)
                || IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus) || IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus);
        if (!inProduction) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, indicatorsSystemVersion.getIndicatorsSystem().getUuid(), new IndicatorsSystemProcStatusEnum[]{
                    IndicatorsSystemProcStatusEnum.DRAFT, IndicatorsSystemProcStatusEnum.VALIDATION_REJECTED, IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION, IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION});
        }
    }

    /**
     * Retrieves version of an indicators system in production
     */
    private IndicatorsSystemVersion retrieveIndicatorsSystemProcStatusInProduction(ServiceContext ctx, String uuid, boolean throwsExceptionIfNotExistsInProduction) throws MetamacException {
        IndicatorsSystem indicatorsSystem = retrieveIndicatorsSystem(ctx, uuid);
        if (indicatorsSystem.getProductionVersion() == null && !throwsExceptionIfNotExistsInProduction) {
            return null; // to throws an specific exception
        }
        if (indicatorsSystem.getProductionVersion() == null) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_IN_PRODUCTION_NOT_FOUND, uuid);
        }
        IndicatorsSystemVersion indicatorsSystemVersionProduction = retrieveIndicatorsSystem(ctx, uuid, indicatorsSystem.getProductionVersion().getVersionNumber());
        return indicatorsSystemVersionProduction;
    }

    /**
     * Retrieves version of an indicators system in diffusion
     */
    private IndicatorsSystemVersion retrieveIndicatorsSystemProcStatusInDiffusion(ServiceContext ctx, String uuid, boolean throwsExceptionIfNotExistsInDiffusion) throws MetamacException {
        IndicatorsSystem indicatorsSystem = retrieveIndicatorsSystem(ctx, uuid);
        if (indicatorsSystem.getDiffusionVersion() == null && !throwsExceptionIfNotExistsInDiffusion) {
            return null; // to throws an specific exception
        }
        if (indicatorsSystem.getDiffusionVersion() == null) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_IN_DIFFUSION_NOT_FOUND, uuid);
        }
        IndicatorsSystemVersion indicatorsSystemVersionDiffusion = retrieveIndicatorsSystem(ctx, uuid, indicatorsSystem.getDiffusionVersion().getVersionNumber());
        return indicatorsSystemVersionDiffusion;
    }

    /**
     * Makes validations to sent to production validation
     * 1) Must exists at least one indicator instance
     */
    private void checkIndicatorsSystemToSendToProductionValidation(ServiceContext ctx, IndicatorsSystemVersion indicatorsSystemVersion) throws MetamacException {

        String uuid = indicatorsSystemVersion.getIndicatorsSystem().getUuid();
        String versionNumber = indicatorsSystemVersion.getVersionNumber();
        
        // Check exists at least one indicator instance
        if (!getIndicatorInstanceRepository().existAnyIndicatorInstance(uuid, versionNumber)) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_MUST_HAVE_INDICATOR_INSTANCE, uuid);
        }
    }

    /**
     * Makes validations to sent to diffusion validation
     * 1) Validations when send to production validation
     */
    private void checkIndicatorsSystemToSendToDiffusionValidation(ServiceContext ctx, IndicatorsSystemVersion indicatorsSystemVersion) throws MetamacException {

        checkIndicatorsSystemToSendToProductionValidation(ctx, indicatorsSystemVersion);
    }

    /**
     * Makes validations to publish
     * 1) Validations when send to diffusion validation
     * 2) All indicators have one version PUBLISHED
     */
    private void checkIndicatorsSystemToPublish(ServiceContext ctx, IndicatorsSystemVersion indicatorsSystemVersion) throws MetamacException {

        // Validation 1
        checkIndicatorsSystemToSendToDiffusionValidation(ctx, indicatorsSystemVersion);
        
        // Validation 2
        List<String> indicatorsUuid = getIndicatorInstanceRepository().findIndicatorsLinkedWithIndicatorsSystemVersion(indicatorsSystemVersion.getId());
        List<String> indicatorsNotPublishedUuid = getIndicatorRepository().filterIndicatorsNotPublished(indicatorsUuid);
        if (indicatorsNotPublishedUuid.size() != 0) {
            String[] indicatorsNotPublishedUuidArray = (String[]) indicatorsNotPublishedUuid.toArray(new String[indicatorsNotPublishedUuid.size()]);
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_MUST_HAVE_ALL_INDICATORS_PUBLISHED, indicatorsSystemVersion.getIndicatorsSystem().getUuid(), indicatorsNotPublishedUuidArray);
        }
    }

    /**
     * Makes validations to archive
     * 1) Validations when publish
     */
    private void checkIndicatorsSystemToArchive(ServiceContext ctx, IndicatorsSystemVersion indicatorsSystemVersion) throws MetamacException {

        checkIndicatorsSystemToPublish(ctx, indicatorsSystemVersion);
    }

    /**
     * Makes validations to reject
     */
    private void checkIndicatorsSystemToReject(ServiceContext ctx, IndicatorsSystemVersion indicatorsSystemVersion) throws MetamacException {

        // Nothing
    }

    /**
     * Create element level: dimension or indicator instance
     */
    private ElementLevel createElementLevel(ServiceContext ctx, String indicatorsSystemUuid, ElementLevel elementLevel) throws MetamacException {

        // Retrieve indicators system version and check it is in production
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorsSystemProcStatusInProduction(ctx, indicatorsSystemUuid, true);

        if (elementLevel.getParent() == null) {
            // Add to first level in indicators system

            // Checks same indicator uuid is not already in level
            if (elementLevel.isIndicatorInstance()) {
                checkIndicatorInstanceUniqueIndicator(elementLevel.getIndicatorInstance().getIndicator().getUuid(), indicatorsSystemVersion.getChildrenFirstLevel());
            }

            // Create element level with dimension
            elementLevel.setIndicatorsSystemVersion(indicatorsSystemVersion);
            elementLevel.setIndicatorsSystemVersionFirstLevel(indicatorsSystemVersion);
            elementLevel.setParent(null);
            elementLevel = getElementLevelRepository().save(elementLevel);

            // Update indicators system adding element
            indicatorsSystemVersion.addChildrenFirstLevel(elementLevel);
            indicatorsSystemVersion.addChildrenAllLevel(elementLevel);
            indicatorsSystemVersion = getIndicatorsSystemVersionRepository().save(indicatorsSystemVersion);

            // Check order and update order of other elements in this level
            updateIndicatorsSystemElementsOrdersInLevelAddingElement(ctx, indicatorsSystemVersion.getChildrenFirstLevel(), elementLevel);
        } else {
            // Add to parent element

            ElementLevel elementLevelParent = elementLevel.getParent();

            // Check dimension parent belogs to indicators system provided
            IndicatorsSystemVersion indicatorsSystemVersionOfDimensionParent = elementLevelParent.getIndicatorsSystemVersion();
            if (!indicatorsSystemVersionOfDimensionParent.getIndicatorsSystem().getUuid().equals(indicatorsSystemUuid)) {
                throw new MetamacException(ServiceExceptionType.DIMENSION_NOT_FOUND_IN_INDICATORS_SYSTEM, elementLevel.getParentUuid(), indicatorsSystemUuid);
            }

            // Checks same indicator uuid is not already in level
            if (elementLevel.isIndicatorInstance()) {
                checkIndicatorInstanceUniqueIndicator(elementLevel.getIndicatorInstance().getIndicator().getUuid(), elementLevelParent.getChildren());
            }

            // Create element level with dimension
            elementLevel.setIndicatorsSystemVersionFirstLevel(null);
            elementLevel.setIndicatorsSystemVersion(indicatorsSystemVersion);
            elementLevel.setParent(elementLevelParent);
            elementLevel = getElementLevelRepository().save(elementLevel);

            // Update parent level adding element
            elementLevelParent.addChildren(elementLevel);
            elementLevelParent = updateElementLevel(elementLevelParent);

            // Update order of other elements in this level
            updateIndicatorsSystemElementsOrdersInLevelAddingElement(ctx, elementLevelParent.getChildren(), elementLevel);

            // Update indicators system adding element to all children
            indicatorsSystemVersion.addChildrenAllLevel(elementLevel);
            updateIndicatorsSystemVersion(ctx, indicatorsSystemVersion);
        }
        return elementLevel;
    }

    /**
     * Checks not exists an indicator instance in the level with same indicator uuid
     */
    private void checkIndicatorInstanceUniqueIndicator(String indicatorUuid, List<ElementLevel> elementsLevel) throws MetamacException {
        for (ElementLevel elementLevel : elementsLevel) {
            if (elementLevel.isIndicatorInstance() && elementLevel.getIndicatorInstance().getIndicator().getUuid().equals(indicatorUuid)) {
                throw new MetamacException(ServiceExceptionType.INDICATOR_INSTANCE_ALREADY_EXIST_INDICATOR_SAME_LEVEL, indicatorUuid);
            }
        }
    }

    private ElementLevel updateElementLevelLocation(ServiceContext ctx, ElementLevel elementLevel, String parentTargetUuid, Long orderInLevel) throws MetamacException {

        // Change order
        Long orderInLevelBefore = elementLevel.getOrderInLevel();
        elementLevel.setOrderInLevel(orderInLevel);

        // Check indicators system proc status
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
            ElementLevel parentTarget = parentTargetUuid != null ? retrieveDimension(ctx, parentTargetUuid).getElementLevel() : null;

            // Update actual parent dimension or indicators system version, removing dimension
            if (parentActual != null) {
                // Update order of other dimensions
                elementLevel.setParent(null);
                updateIndicatorsSystemElementsOrdersInLevelRemovingElement(ctx, parentActual.getChildren(), elementLevel, orderInLevelBefore);
            } else {
                // Update order of other dimensions
                elementLevel.setIndicatorsSystemVersionFirstLevel(null);
                updateIndicatorsSystemElementsOrdersInLevelRemovingElement(ctx, indicatorsSystemVersion.getChildrenFirstLevel(), elementLevel, orderInLevelBefore);
            }

            // Update target parent, adding dimension
            List<ElementLevel> elementsInLevel = null;
            if (parentTarget == null) {
                indicatorsSystemVersion.addChildrenFirstLevel(elementLevel);
                updateIndicatorsSystemVersion(ctx, indicatorsSystemVersion);
                elementsInLevel = indicatorsSystemVersion.getChildrenFirstLevel();
            } else {
                parentTarget.addChildren(elementLevel);
                updateElementLevel(parentTarget);
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
            elementLevel = updateElementLevel(elementLevel);
        } else {
            // Same parent, only changes order
            // Check order is correct and update orders
            List<ElementLevel> elementsInLevel = elementLevel.getParent() != null ? elementLevel.getParent().getChildren() : elementLevel.getIndicatorsSystemVersionFirstLevel()
                    .getChildrenFirstLevel();
            updateIndicatorsSystemElementsOrdersInLevelChangingOrder(ctx, elementsInLevel, elementLevel, orderInLevelBefore, elementLevel.getOrderInLevel());
            elementLevel = updateElementLevel(elementLevel);
        }
        
        return elementLevel;
    }

    private void deleteElementLevel(ServiceContext ctx, ElementLevel elementLevel) throws MetamacException {

        // Check indicators system proc status
        IndicatorsSystemVersion indicatorsSystemVersion = elementLevel.getIndicatorsSystemVersion();
        checkIndicatorsSystemVersionInProduction(indicatorsSystemVersion);

        // Update orders of other elements in level
        List<ElementLevel> elementsAtLevel = null;
        if (elementLevel.getParent() == null) {
            elementsAtLevel = indicatorsSystemVersion.getChildrenFirstLevel();
        } else {
            elementsAtLevel = elementLevel.getParent().getChildren();
        }
        elementsAtLevel.remove(elementLevel);
        updateIndicatorsSystemElementsOrdersInLevelRemovingElement(ctx, elementsAtLevel, elementLevel, elementLevel.getOrderInLevel());

        // Delete
        getElementLevelRepository().delete(elementLevel);
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
                    updateElementLevel(elementInLevel);
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

    private void updateIndicatorsSystemElementsOrdersInLevelRemovingElement(ServiceContext ctx, List<ElementLevel> elementsAtLevel, ElementLevel elementToRemove, Long orderBeforeUpdate)
            throws MetamacException {
        for (ElementLevel elementInLevel : elementsAtLevel) {
            if (elementInLevel.getElementUuid().equals(elementToRemove.getElementUuid())) {
                // nothing
            } else if (elementInLevel.getOrderInLevel() > orderBeforeUpdate) {
                elementInLevel.setOrderInLevel(elementInLevel.getOrderInLevel() - 1);
                updateElementLevel(elementInLevel);
            }
        }
    }

    private void updateIndicatorsSystemElementsOrdersInLevelChangingOrder(ServiceContext ctx, List<ElementLevel> elementsAtLevel, ElementLevel elementToChangeOrder, Long orderBeforeUpdate,
            Long orderAfterUpdate) throws MetamacException {

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
                    updateElementLevel(elementAtLevel);
                }
            } else if (orderAfterUpdate > orderBeforeUpdate) {
                if (elementAtLevel.getOrderInLevel() > orderBeforeUpdate && elementAtLevel.getOrderInLevel() <= orderAfterUpdate) {
                    elementAtLevel.setOrderInLevel(elementAtLevel.getOrderInLevel() - 1);
                    updateElementLevel(elementAtLevel);
                }
            }
        }
    }

    /**
     * We can not move a dimension to its child
     */
    private void checkDimensionIsNotChildren(ServiceContext ctx, Dimension dimension, String parentTargetUuid) throws MetamacException {

        Dimension dimensionTarget = retrieveDimension(ctx, parentTargetUuid);
        ElementLevel dimensionParent = dimensionTarget.getElementLevel().getParent();
        while (dimensionParent != null) {
            if (dimensionParent.isDimension() && dimensionParent.getElementUuid().equals(dimension.getUuid())) {
                throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, "PARENT_TARGET_UUID");
            }
            dimensionParent = dimensionParent.getParent();
        }
    }

    private IndicatorsSystem retrieveIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException {

        IndicatorsSystem indicatorsSystem = getIndicatorsSystemRepository().retrieveIndicatorsSystem(uuid);
        if (indicatorsSystem == null) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND, uuid);
        }
        return indicatorsSystem;
    }
    
    private List<IndicatorsSystem> findIndicatorsSystems(ServiceContext ctx, String code) throws MetamacException {
        return getIndicatorsSystemRepository().findIndicatorsSystems(code);
    }
    
    private ElementLevel updateElementLevel(ElementLevel elementLevel) throws MetamacException {
        return getElementLevelRepository().save(elementLevel);
    }
}
