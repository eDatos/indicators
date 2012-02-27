package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.ArrayList;
import java.util.List;

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
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemStateEnum;
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

    @Override
    public IndicatorsSystemVersion createIndicatorsSystem(ServiceContext ctx, IndicatorsSystem indicatorsSystem, IndicatorsSystemVersion indicatorsSystemVersion) throws MetamacException {
        
        // Validation of parameters
        InvocationValidator.checkCreateIndicatorsSystem(indicatorsSystem, indicatorsSystemVersion, null);
        checkIndicatorsSystemCodeUnique(ctx, indicatorsSystem.getCode(), null);
        checkIndicatorsSystemUriGopestatUnique(ctx, indicatorsSystemVersion.getUriGopestat(), null);        

        // Save indicator
        indicatorsSystem.setDiffusionVersion(null);
        indicatorsSystem = getIndicatorsSystemRepository().save(indicatorsSystem);

        // Save draft version
        indicatorsSystemVersion.setState(IndicatorsSystemStateEnum.DRAFT);
        indicatorsSystemVersion.setVersionNumber(ServiceUtils.generateVersionNumber(null, VersiontTypeEnum.MAJOR));
        indicatorsSystemVersion.setIndicatorsSystem(indicatorsSystem);
        indicatorsSystemVersion = getIndicatorsSystemVersionRepository().save(indicatorsSystemVersion);

        // Update indicator with draft version
        indicatorsSystem.setProductionVersion(new IndicatorsSystemVersionInformation(indicatorsSystemVersion.getId(), indicatorsSystemVersion.getVersionNumber()));
        indicatorsSystem.getVersions().add(indicatorsSystemVersion);
        getIndicatorsSystemRepository().save(indicatorsSystemVersion.getIndicatorsSystem());

        return indicatorsSystemVersion;
    }

    public IndicatorsSystemVersion retrieveIndicatorsSystem(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorsSystem(uuid, versionNumber, null);

        // Retrieve version requested or last version
        if (versionNumber == null) {
            // Retrieve last version
            IndicatorsSystem indicatorsSystem = retrieveIndicatorsSystemBorrar(ctx, uuid);
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
        IndicatorsSystemVersion publishedIndicatorsSystemVersion = retrieveIndicatorsSystemStateInDiffusion(ctx, uuid, false);
        if (publishedIndicatorsSystemVersion == null || !IndicatorsSystemStateEnum.PUBLISHED.equals(publishedIndicatorsSystemVersion.getState())) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE, uuid, new IndicatorsSystemStateEnum[]{IndicatorsSystemStateEnum.PUBLISHED});
        }

        return publishedIndicatorsSystemVersion;
    }

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
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE, indicatorsSystem.getUuid(), new IndicatorsSystemStateEnum[]{IndicatorsSystemStateEnum.PUBLISHED});
        }
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorsSystem(ctx, indicatorsSystem.getUuid(), indicatorsSystem.getDiffusionVersion().getVersionNumber());
        if (!IndicatorsSystemStateEnum.PUBLISHED.equals(indicatorsSystemVersion.getState())) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE, indicatorsSystem.getUuid(), new IndicatorsSystemStateEnum[]{IndicatorsSystemStateEnum.PUBLISHED});
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
    public IndicatorsSystem updateIndicatorsSystem(ServiceContext ctx, IndicatorsSystem indicatorsSystem) throws MetamacException {
        return getIndicatorsSystemRepository().save(indicatorsSystem);
    }

    @Override
    public IndicatorsSystemVersion updateIndicatorsSystemVersion(ServiceContext ctx, IndicatorsSystemVersion indicatorsSystemVersion) throws MetamacException {
        return getIndicatorsSystemVersionRepository().save(indicatorsSystemVersion);
    }

    @Override
    public void deleteIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkDeleteIndicatorsSystem(uuid, null);

        // Retrieve version in production
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorsSystemStateInProduction(ctx, uuid, true);

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
        List<IndicatorsSystemVersion> indicatorsSystemsVersion = findIndicatorsSystemVersions(ctx, null, IndicatorsSystemStateEnum.PUBLISHED);

        // Transform
        List<IndicatorsSystemVersion> indicatorsSystemsVersions = new ArrayList<IndicatorsSystemVersion>();
        for (IndicatorsSystemVersion indicatorsSystemVersion : indicatorsSystemsVersion) {
            indicatorsSystemsVersions.add(indicatorsSystemVersion);
        }

        return indicatorsSystemsVersions;
    }

    @Override
    public List<IndicatorsSystem> findIndicatorsSystems(ServiceContext ctx, String code) throws MetamacException {
        return getIndicatorsSystemRepository().findIndicatorsSystems(code);
    }

    @Override
    public List<IndicatorsSystemVersion> findIndicatorsSystemVersions(ServiceContext ctx, String uriGopestat, IndicatorsSystemStateEnum state) throws MetamacException {
        return getIndicatorsSystemVersionRepository().findIndicatorsSystemVersions(uriGopestat, state);
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
        updateIndicatorsSystemVersion(ctx, indicatorsSystemInProduction);
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
        updateIndicatorsSystemVersion(ctx, indicatorsSystemInProduction);
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
        updateIndicatorsSystemVersion(ctx, indicatorsSystemInProduction);
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
        updateIndicatorsSystemVersion(ctx, indicatorsSystemInProduction);

        IndicatorsSystem indicatorsSystem = indicatorsSystemInProduction.getIndicatorsSystem();
        // Remove possible last version in diffusion
        if (indicatorsSystem.getDiffusionVersion() != null) {
            IndicatorsSystemVersion indicatorDiffusionVersion = retrieveIndicatorsSystem(ctx, uuid, indicatorsSystem.getDiffusionVersion().getVersionNumber());
            indicatorsSystem.getVersions().remove(indicatorDiffusionVersion);
            getIndicatorsSystemRepository().save(indicatorsSystem);
            getIndicatorsSystemVersionRepository().delete(indicatorDiffusionVersion);
        }
        indicatorsSystem.setDiffusionVersion(new IndicatorsSystemVersionInformation(indicatorsSystemInProduction.getId(), indicatorsSystemInProduction.getVersionNumber()));
        indicatorsSystem.setProductionVersion(null);

        updateIndicatorsSystem(ctx, indicatorsSystem);
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
        updateIndicatorsSystemVersion(ctx, indicatorsSystemInDiffusion);
    }

    @Override
    public IndicatorsSystemVersion versioningIndicatorsSystem(ServiceContext ctx, String uuid, VersiontTypeEnum versionType) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkVersioningIndicatorsSystem(uuid, versionType, null);

        IndicatorsSystem indicatorsSystem = retrieveIndicatorsSystemBorrar(ctx, uuid);
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
        indicatorsSystemNewVersion.setIndicatorsSystem(indicatorsSystem);
        indicatorsSystemNewVersion = getIndicatorsSystemVersionRepository().save(indicatorsSystemNewVersion);
        // Update indicator with draft version
        indicatorsSystem.setProductionVersion(new IndicatorsSystemVersionInformation(indicatorsSystemNewVersion.getId(), indicatorsSystemNewVersion.getVersionNumber()));
        indicatorsSystem.getVersions().add(indicatorsSystemNewVersion);
        getIndicatorsSystemRepository().save(indicatorsSystem);

        return indicatorsSystemNewVersion;
    }

    @Override
    public ElementLevel updateElementLevel(ServiceContext ctx, ElementLevel elementLevel) throws MetamacException {
        return getElementLevelRepository().save(elementLevel);
    }

    @Override
    public ElementLevel createDimension(ServiceContext ctx, ElementLevel elementLevel) throws MetamacException {
        return getElementLevelRepository().save(elementLevel);
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
        return getDimensionRepository().save(dimension);
    }

    public void deleteDimension(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkDeleteDimension(uuid, null);

        // Retrieve
        Dimension dimension = retrieveDimension(ctx, uuid);
        ElementLevel elementLevel = dimension.getElementLevel();

        // Check indicators system state
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
        updateIndicatorsSystemElementsOrdersInLevelRemovingElement(ctx, elementsAtLevel, elementLevel.getOrderInLevel());

        // Delete
        getElementLevelRepository().delete(elementLevel);
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
            if (level.getDimension() != null) {
                dimensions.add(level.getDimension());
            }
        }
        return dimensions;
    }

    @Override
    public ElementLevel createIndicatorInstance(ServiceContext ctx, ElementLevel elementLevel) throws MetamacException {
        return getElementLevelRepository().save(elementLevel);
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
        return getIndicatorInstanceRepository().save(indicatorInstance);
    }

    @Override
    public void deleteIndicatorInstance(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkDeleteIndicatorInstance(uuid, null);

        // Retrieve
        IndicatorInstance indicatorInstance = retrieveIndicatorInstance(ctx, uuid);
        ElementLevel elementLevel = indicatorInstance.getElementLevel();

        // Check indicators system state
        IndicatorsSystemVersion indicatorsSystemVersion = elementLevel.getIndicatorsSystemVersion();
        checkIndicatorsSystemVersionInProduction(indicatorsSystemVersion);

        // Delete
        getElementLevelRepository().delete(elementLevel);

        // Update orders of other elements in level
        List<ElementLevel> elementsAtLevel = null;
        if (elementLevel.getParent() == null) {
            elementsAtLevel = indicatorsSystemVersion.getChildrenFirstLevel();
        } else {
            elementsAtLevel = elementLevel.getParent().getChildren();
        }
        elementsAtLevel.remove(elementLevel);
        updateIndicatorsSystemElementsOrdersInLevelRemovingElement(ctx, elementsAtLevel, elementLevel.getOrderInLevel());
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
            if (level.getIndicatorInstance() != null) {
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
        List<IndicatorsSystemVersion> indicatorsSystemVersions = findIndicatorsSystemVersions(ctx, uriGopestat, null);
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

    /**
     * Retrieves version of an indicators system in production
     */
    private IndicatorsSystemVersion retrieveIndicatorsSystemStateInProduction(ServiceContext ctx, String uuid, boolean throwsExceptionIfNotExistsInProduction) throws MetamacException {
        IndicatorsSystem indicatorsSystem = retrieveIndicatorsSystemBorrar(ctx, uuid);
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
    private IndicatorsSystemVersion retrieveIndicatorsSystemStateInDiffusion(ServiceContext ctx, String uuid, boolean throwsExceptionIfNotExistsInDiffusion) throws MetamacException {
        IndicatorsSystem indicatorsSystem = retrieveIndicatorsSystemBorrar(ctx, uuid);
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
    private void checkIndicatorsSystemToSendToProductionValidation(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {

        // Check exists at least one indicator instance
        if (!getIndicatorInstanceRepository().existAnyIndicatorInstance(uuid, versionNumber)) {
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

    private void updateIndicatorsSystemElementsOrdersInLevelRemovingElement(ServiceContext ctx, List<ElementLevel> elementsAtLevel, Long orderBeforeUpdate) throws MetamacException {
        for (ElementLevel elementInLevel : elementsAtLevel) {
            if (elementInLevel.getOrderInLevel() > orderBeforeUpdate) {
                elementInLevel.setOrderInLevel(elementInLevel.getOrderInLevel() - 1);
                updateElementLevel(ctx, elementInLevel);
            }
        }
    }

    // TODO refactor, hacer privado
    public IndicatorsSystem retrieveIndicatorsSystemBorrar(ServiceContext ctx, String uuid) throws MetamacException {

        IndicatorsSystem indicatorsSystem = getIndicatorsSystemRepository().retrieveIndicatorsSystem(uuid);
        if (indicatorsSystem == null) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND, uuid);
        }
        return indicatorsSystem;
    }
}
