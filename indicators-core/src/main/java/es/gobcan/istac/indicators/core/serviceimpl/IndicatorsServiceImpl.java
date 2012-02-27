package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.IndicatorsConstants;
import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionInformation;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorStateEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceimpl.util.InvocationValidator;

/**
 * Implementation of IndicatorsService.
 */
@Service("indicatorsService")
public class IndicatorsServiceImpl extends IndicatorsServiceImplBase {

    public IndicatorsServiceImpl() {
    }

    @Override
    public IndicatorVersion createIndicatorVersion(ServiceContext ctx, Indicator indicator, IndicatorVersion indicatorDraft) throws MetamacException {

        // Save indicator
        indicator = getIndicatorRepository().save(indicator);

        // Save draft version
        indicatorDraft.setIndicator(indicator);
        indicatorDraft = getIndicatorVersionRepository().save(indicatorDraft);

        // Update indicator with draft version
        indicator.setProductionVersion(new IndicatorVersionInformation(indicatorDraft.getId(), indicatorDraft.getVersionNumber()));
        indicator.getVersions().add(indicatorDraft);
        getIndicatorRepository().save(indicatorDraft.getIndicator());

        return indicatorDraft;
    }

    @Override
    public Indicator retrieveIndicator(ServiceContext ctx, String uuid) throws MetamacException {
        Indicator indicator = getIndicatorRepository().retrieveIndicator(uuid);
        if (indicator == null) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_FOUND, uuid);
        }
        return indicator;
    }

    @Override
    public IndicatorVersion retrieveIndicatorVersion(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {
        IndicatorVersion indicatorVersion = getIndicatorVersionRepository().retrieveIndicatorVersion(uuid, versionNumber);
        if (indicatorVersion == null) {
            if (versionNumber == null) {
                throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_FOUND, uuid);
            } else {
                throw new MetamacException(ServiceExceptionType.INDICATOR_VERSION_NOT_FOUND, uuid, versionNumber);
            }
        }
        return indicatorVersion;
    }

    @Override
    public void updateIndicator(ServiceContext ctx, Indicator indicator) throws MetamacException {
        getIndicatorRepository().save(indicator);
    }

    @Override
    public void updateIndicatorVersion(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        getIndicatorVersionRepository().save(indicatorVersion);
    }
    
    @Override
    public void deleteIndicator(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkDeleteIndicator(uuid, null);

        // Retrieve version in production
        IndicatorVersion indicatorVersion = retrieveIndicatorStateInProduction(ctx, uuid, true);

        // Delete whole indicator or only last version
        if (IndicatorsConstants.VERSION_NUMBER_INITIAL.equals(indicatorVersion.getVersionNumber())) {
            // If indicator is not published or archived, delete whole indicator
            Indicator indicator = indicatorVersion.getIndicator();

            // Check not exists any indicator instance for this indicator
            if (indicator.getIndicatorsInstances().size() != 0) {
                throw new MetamacException(ServiceExceptionType.INDICATOR_MUST_NOT_BE_IN_INDICATORS_SYSTEMS, uuid);
            }
            getIndicatorRepository().delete(indicator);
        } else {
            Indicator indicator = indicatorVersion.getIndicator();
            indicator.getVersions().remove(indicatorVersion);
            indicator.setProductionVersion(null);

            // Update
            getIndicatorRepository().save(indicator);
            getIndicatorVersionRepository().delete(indicatorVersion);
        }
    }

    @Override
    public List<Indicator> findIndicators(ServiceContext ctx, String code) throws MetamacException {
        return getIndicatorRepository().findIndicators(code);
    }

    @Override
    public List<IndicatorVersion> findIndicatorsVersions(ServiceContext ctx, String uriGopestat, IndicatorStateEnum state) throws MetamacException {
        return getIndicatorVersionRepository().findIndicatorsVersions(state);
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

        // Validate to send to production
        checkIndicatorsToSendToProductionValidation(ctx, indicatorInProduction);

        // Update state
        indicatorInProduction.setState(IndicatorStateEnum.PRODUCTION_VALIDATION);
        indicatorInProduction.setProductionValidationDate(new DateTime());
        indicatorInProduction.setProductionValidationUser(ctx.getUserId());
        updateIndicatorVersion(ctx, indicatorInProduction);
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
        
        // Validate to send to diffusion
        checkIndicatorsToSendToDiffusionValidation(ctx, indicatorInProduction);

        // Update state
        indicatorInProduction.setState(IndicatorStateEnum.DIFFUSION_VALIDATION);
        indicatorInProduction.setDiffusionValidationDate(new DateTime());
        indicatorInProduction.setDiffusionValidationUser(ctx.getUserId());
        updateIndicatorVersion(ctx, indicatorInProduction);
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
        
        // Validate to reject
        checkIndicatorsToReject(ctx, indicatorInProduction);

        // Update state
        indicatorInProduction.setState(IndicatorStateEnum.VALIDATION_REJECTED);
        indicatorInProduction.setProductionValidationDate(null);
        indicatorInProduction.setProductionValidationUser(null);
        indicatorInProduction.setDiffusionValidationDate(null);
        indicatorInProduction.setDiffusionValidationUser(null);
        updateIndicatorVersion(ctx, indicatorInProduction);
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
        
        // Validate to publish
        checkIndicatorsToPublish(ctx, indicatorInProduction);

        // Update state
        indicatorInProduction.setState(IndicatorStateEnum.PUBLISHED);
        indicatorInProduction.setPublicationDate(new DateTime());
        indicatorInProduction.setPublicationUser(ctx.getUserId());
        updateIndicatorVersion(ctx, indicatorInProduction);

        Indicator indicator = indicatorInProduction.getIndicator();
        // Remove possible last version in diffusion
        if (indicator.getDiffusionVersion() != null) {
            IndicatorVersion indicatorDiffusionVersion = retrieveIndicatorVersion(ctx, uuid, indicator.getDiffusionVersion().getVersionNumber());
            indicator.getVersions().remove(indicatorDiffusionVersion);
            getIndicatorRepository().save(indicator);
            getIndicatorVersionRepository().delete(indicatorDiffusionVersion);
        }
        indicator.setDiffusionVersion(new IndicatorVersionInformation(indicatorInProduction.getId(), indicatorInProduction.getVersionNumber()));
        indicator.setProductionVersion(null);

        updateIndicator(ctx, indicator);
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
        
        // Validate to archive
        checkIndicatorsToArchive(ctx, indicatorInDiffusion);

        // Update state
        indicatorInDiffusion.setState(IndicatorStateEnum.ARCHIVED);
        indicatorInDiffusion.setArchiveDate(new DateTime());
        indicatorInDiffusion.setArchiveUser(ctx.getUserId());
        updateIndicatorVersion(ctx, indicatorInDiffusion);
    }

    @Override
    public DataSource createDataSource(ServiceContext ctx, DataSource dataSource) throws MetamacException {
        return getDataSourceRepository().save(dataSource);
    }
    
    @Override
    public DataSource retrieveDataSource(ServiceContext ctx, String uuid) throws MetamacException {
        DataSource dataSource = getDataSourceRepository().findDataSource(uuid);
        if (dataSource == null) {
            throw new MetamacException(ServiceExceptionType.DATA_SOURCE_NOT_FOUND, uuid);
        }
        return dataSource;
    }

    @Override
    public DataSource updateDataSource(ServiceContext ctx, DataSource dataSource) throws MetamacException {
        return getDataSourceRepository().save(dataSource);
    }

    @Override
    public void deleteDataSource(ServiceContext ctx, String uuid) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkDeleteDataSource(uuid, null);

        // Check indicator state
        DataSource dataSource = retrieveDataSource(ctx, uuid);
        checkIndicatorVersionInProduction(dataSource.getIndicatorVersion());

        // Delete
        getDataSourceRepository().delete(dataSource);
    }

    @Override
    public List<DataSource> findDataSources(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber) throws MetamacException {
        IndicatorVersion indicatorVersion = retrieveIndicatorVersion(ctx, indicatorUuid, indicatorVersionNumber);
        return indicatorVersion.getDataSources();
    }
    
    /**
     * Retrieves version of an indicator in production
     */
    private IndicatorVersion retrieveIndicatorStateInProduction(ServiceContext ctx, String uuid, boolean throwsExceptionIfNotExistsInProduction) throws MetamacException {
        Indicator indicator = retrieveIndicator(ctx, uuid);
        if (indicator.getProductionVersion() == null && !throwsExceptionIfNotExistsInProduction) {
            return null; // to throws an specific exception
        }
        if (indicator.getProductionVersion() == null) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_IN_PRODUCTION_NOT_FOUND, uuid);
        }
        IndicatorVersion indicatorVersionProduction = retrieveIndicatorVersion(ctx, uuid, indicator.getProductionVersion().getVersionNumber());
        return indicatorVersionProduction;
    }
    

    /**
     * Retrieves version of an indicator in diffusion
     */
    private IndicatorVersion retrieveIndicatorStateInDiffusion(ServiceContext ctx, String uuid, boolean throwsExceptionIfNotExistsInDiffusion) throws MetamacException {
        Indicator indicator = retrieveIndicator(ctx, uuid);
        if (indicator.getDiffusionVersion() == null && !throwsExceptionIfNotExistsInDiffusion) {
            return null; // to throws an specific exception
        }
        if (indicator.getDiffusionVersion() == null) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_IN_DIFFUSION_NOT_FOUND, uuid);
        }
        IndicatorVersion indicatorVersionDiffusion = retrieveIndicatorVersion(ctx, uuid, indicator.getDiffusionVersion().getVersionNumber());
        return indicatorVersionDiffusion;
    }
    


    /**
     * Makes validations to sent to production validation
     * 1) Must exists at least one indicator instance
     */
    private void checkIndicatorsToSendToProductionValidation(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {

        // Check indicator has at least one data source
        if (indicatorVersion.getDataSources().size() == 0) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_MUST_HAVE_DATA_SOURCES, indicatorVersion.getIndicator().getUuid());
        }
    }

    /**
     * Makes validations to sent to diffusion validation
     * 1) Validations when send to production validation
     */
    private void checkIndicatorsToSendToDiffusionValidation(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {

        checkIndicatorsToSendToProductionValidation(ctx, indicatorVersion);
    }

    /**
     * Makes validations to publish
     * 1) Validations when send to diffusion validation
     */
    private void checkIndicatorsToPublish(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {

        checkIndicatorsToSendToDiffusionValidation(ctx, indicatorVersion);
    }

    /**
     * Makes validations to archive
     * 1) Validations when publish
     */
    private void checkIndicatorsToArchive(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {

        checkIndicatorsToPublish(ctx, indicatorVersion);
    }

    /**
     * Makes validations to reject
     */
    private void checkIndicatorsToReject(ServiceContext ctx, IndicatorVersion indicatorVersion) {

        // Nothing
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
