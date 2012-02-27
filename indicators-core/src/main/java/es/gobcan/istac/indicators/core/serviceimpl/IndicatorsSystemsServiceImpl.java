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
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceimpl.util.InvocationValidator;

/**
 * Implementation of IndicatorsSystemService.
 */
@Service("indicatorsSystemService")
public class IndicatorsSystemsServiceImpl extends IndicatorsSystemsServiceImplBase {

    public IndicatorsSystemsServiceImpl() {
    }

    @Override
    public IndicatorsSystemVersion createIndicatorsSystemVersion(ServiceContext ctx, IndicatorsSystem indicatorsSystem, IndicatorsSystemVersion indicatorsSystemDraft) throws MetamacException {

        // Save indicator
        indicatorsSystem = getIndicatorsSystemRepository().save(indicatorsSystem);

        // Save draft version
        indicatorsSystemDraft.setIndicatorsSystem(indicatorsSystem);
        indicatorsSystemDraft = getIndicatorsSystemVersionRepository().save(indicatorsSystemDraft);

        // Update indicator with draft version
        indicatorsSystem.setProductionVersion(new IndicatorsSystemVersionInformation(indicatorsSystemDraft.getId(), indicatorsSystemDraft.getVersionNumber()));
        indicatorsSystem.getVersions().add(indicatorsSystemDraft);
        getIndicatorsSystemRepository().save(indicatorsSystemDraft.getIndicatorsSystem());

        return indicatorsSystemDraft;
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
    public ElementLevel updateElementLevel(ServiceContext ctx, ElementLevel elementLevel) throws MetamacException {
        return getElementLevelRepository().save(elementLevel);
    }

    @Override
    public ElementLevel createDimension(ServiceContext ctx, ElementLevel elementLevel) throws MetamacException {
        return getElementLevelRepository().save(elementLevel);
    }

    @Override
    public Dimension retrieveDimension(ServiceContext ctx, String uuid) throws MetamacException {
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
