package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.ConditionRoot;
import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorProperties;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionInformation;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionProperties;
import es.gobcan.istac.indicators.core.domain.Quantity;
import es.gobcan.istac.indicators.core.domain.QuantityUnit;
import es.gobcan.istac.indicators.core.domain.Subject;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorProcStatusEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionParameters;
import es.gobcan.istac.indicators.core.error.ServiceExceptionParametersInternal;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.repositoryimpl.finders.SubjectIndicatorResult;
import es.gobcan.istac.indicators.core.serviceimpl.util.DoCopyUtils;
import es.gobcan.istac.indicators.core.serviceimpl.util.InvocationValidator;
import es.gobcan.istac.indicators.core.serviceimpl.util.PublishIndicatorResult;
import es.gobcan.istac.indicators.core.serviceimpl.util.ServiceUtils;

/**
 * Implementation of IndicatorsService
 */
@Service("indicatorsService")
public class IndicatorsServiceImpl extends IndicatorsServiceImplBase {

    public IndicatorsServiceImpl() {
    }

    @Override
    public IndicatorVersion createIndicator(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {

        Indicator indicator = indicatorVersion.getIndicator();

        // Validation of parameters
        InvocationValidator.checkCreateIndicator(indicatorVersion, null);
        checkIndicatorCodeUnique(ctx, indicator.getCode());

        // Save indicator
        indicator.setDiffusionVersion(null);
        indicator.setIsPublished(Boolean.FALSE);
        indicator = getIndicatorRepository().save(indicator);

        // Save draft version
        indicatorVersion.setProcStatus(IndicatorProcStatusEnum.DRAFT);
        indicatorVersion.setIsLastVersion(Boolean.TRUE);
        indicatorVersion.setInconsistentData(Boolean.FALSE);
        indicatorVersion.setNeedsUpdate(Boolean.FALSE);
        indicatorVersion.setVersionNumber(ServiceUtils.generateVersionNumber(null, VersionTypeEnum.MAJOR));
        indicatorVersion.setIndicator(indicator);
        indicatorVersion = getIndicatorVersionRepository().save(indicatorVersion);

        // Update indicator with draft version
        indicator.setProductionVersion(new IndicatorVersionInformation(indicatorVersion.getId(), indicatorVersion.getVersionNumber()));
        indicator.getVersions().add(indicatorVersion);
        getIndicatorRepository().save(indicator);

        return indicatorVersion;
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
    public IndicatorVersion retrieveIndicator(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicator(uuid, versionNumber, null);

        // Retrieve version requested or last version
        if (versionNumber == null) {
            // Retrieve last version
            Indicator indicator = retrieveIndicator(ctx, uuid);
            versionNumber = indicator.getProductionVersion() != null ? indicator.getProductionVersion().getVersionNumber() : indicator.getDiffusionVersion().getVersionNumber();
        }
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
    public IndicatorVersion retrieveIndicatorPublished(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorPublished(uuid, null);

        // Retrieve published version
        IndicatorVersion publishedIndicatorVersion = retrieveIndicatorProcStatusInDiffusion(ctx, uuid, false);
        if (publishedIndicatorVersion == null || !IndicatorProcStatusEnum.PUBLISHED.equals(publishedIndicatorVersion.getProcStatus())) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, uuid, new IndicatorProcStatusEnum[]{IndicatorProcStatusEnum.PUBLISHED});
        }

        return publishedIndicatorVersion;
    }

    @Override
    public IndicatorVersion retrieveIndicatorByCode(ServiceContext ctx, String code, String versionNumber) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorByCode(code, null);

        // Prepare criteria (indicator version by code, version requested or last version)
        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
        ConditionRoot<IndicatorVersion> conditionRoot = ConditionalCriteriaBuilder.criteriaFor(IndicatorVersion.class);
        conditionRoot.withProperty(new LeafProperty<IndicatorVersion>("indicator", "code", false, IndicatorVersion.class)).eq(code);
        if (versionNumber != null) {
            conditionRoot.withProperty(IndicatorVersionProperties.versionNumber()).eq(versionNumber);
        } else {
            conditionRoot.withProperty(IndicatorVersionProperties.isLastVersion()).eq(Boolean.TRUE);
        }
        List<ConditionalCriteria> conditions = conditionRoot.distinctRoot().build();

        // Find
        PagedResult<IndicatorVersion> result = getIndicatorVersionRepository().findByCondition(conditions, pagingParameter);

        if (result.getValues().size() == 0) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_FOUND_WITH_CODE, code);
        }

        // Return unique result
        return result.getValues().get(0);
    }

    @Override
    public IndicatorVersion retrieveIndicatorPublishedByCode(ServiceContext ctx, String code) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorPublishedByCode(code, null);

        // Prepare criteria (indicator version by code, published)
        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
        ConditionRoot<IndicatorVersion> conditionRoot = ConditionalCriteriaBuilder.criteriaFor(IndicatorVersion.class);
        conditionRoot.withProperty(new LeafProperty<IndicatorVersion>("indicator", "code", false, IndicatorVersion.class)).eq(code);
        conditionRoot.withProperty(IndicatorVersionProperties.procStatus()).eq(IndicatorProcStatusEnum.PUBLISHED);
        List<ConditionalCriteria> conditions = conditionRoot.distinctRoot().build();

        // Find
        PagedResult<IndicatorVersion> result = getIndicatorVersionRepository().findByCondition(conditions, pagingParameter);

        if (result.getValues().size() == 0) {
            // Try retrieve any version with code, to throws specific exception
            IndicatorVersion indicatorVersionLastVersion = retrieveIndicatorByCode(ctx, code, null);
            if (indicatorVersionLastVersion != null) {
                throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, indicatorVersionLastVersion.getIndicator().getUuid(),
                        new IndicatorProcStatusEnum[]{IndicatorProcStatusEnum.PUBLISHED});
            }
        }

        // Return unique result
        return result.getValues().get(0);
    }

    @Override
    // CAUTION: This method should never be called from any facade. This is just for internal use
    public Indicator updateIndicator(ServiceContext ctx, Indicator indicator) throws MetamacException {
        // Validation
        InvocationValidator.checkUpdateIndicator(indicator, null);

        // Update
        indicator = getIndicatorRepository().save(indicator);
        return indicator;
    }

    @Override
    public IndicatorVersion updateIndicatorVersion(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {

        // Validation
        InvocationValidator.checkUpdateIndicatorVersion(indicatorVersion, null);

        // Validate indicators system proc status and linked indicators
        checkIndicatorVersionInProduction(indicatorVersion);
        checkIndicatorsLinkedInQuantity(indicatorVersion.getQuantity(), indicatorVersion.getIndicator().getUuid(), Boolean.FALSE, ServiceExceptionParameters.INDICATOR);

        // Update
        indicatorVersion = getIndicatorVersionRepository().save(indicatorVersion);
        return indicatorVersion;
    }

    @Override
    public void deleteIndicator(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkDeleteIndicator(uuid, null);

        // Retrieve version in production
        IndicatorVersion indicatorVersion = retrieveIndicatorProcStatusInProduction(ctx, uuid, true);

        // Delete whole indicator or only last version
        if (IndicatorsConstants.VERSION_NUMBER_INITIAL.equals(indicatorVersion.getVersionNumber())) {
            // If indicator is not published or archived, delete whole indicator
            Indicator indicator = indicatorVersion.getIndicator();

            // Check not exists any indicator instance for this indicator
            if (indicator.getIndicatorsInstances().size() != 0) {
                throw new MetamacException(ServiceExceptionType.INDICATOR_MUST_NOT_BE_IN_INDICATORS_SYSTEMS, uuid);
            }
            // Check not exists any indicator with this indicator as numerator, denominator or base quantity
            IndicatorVersion indicatorVersionLinked = getIndicatorVersionRepository().findOneIndicatorVersionLinkedToIndicator(uuid);
            if (indicatorVersionLinked != null) {
                throw new MetamacException(ServiceExceptionType.INDICATOR_MUST_NOT_BE_LINKED_TO_OTHER_INDICATOR, uuid, indicatorVersionLinked.getIndicator().getUuid());
            }

            getIndicatorRepository().delete(indicator);
        } else {
            Indicator indicator = indicatorVersion.getIndicator();
            indicator.getVersions().remove(indicatorVersion);
            indicator.setProductionVersion(null);
            indicator.getVersions().get(0).setIsLastVersion(Boolean.TRUE); // another version is now last version

            // Update
            getIndicatorRepository().save(indicator);
            getIndicatorVersionRepository().delete(indicatorVersion);
        }
    }

    @Override
    public PagedResult<IndicatorVersion> findIndicators(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindIndicators(conditions, pagingParameter, null);

        // Retrieve last versions
        if (conditions == null) {
            conditions = new ArrayList<ConditionalCriteria>();
        }
        conditions.add(ConditionalCriteria.equal(IndicatorVersionProperties.isLastVersion(), Boolean.TRUE));

        // Find
        PagedResult<IndicatorVersion> indicatorsVersions = getIndicatorVersionRepository().findByCondition(conditions, pagingParameter);
        return indicatorsVersions;
    }

    @Override
    public PagedResult<IndicatorVersion> findIndicatorsPublished(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindIndicatorsPublished(conditions, pagingParameter, null);

        // Retrieve published
        if (conditions == null) {
            conditions = new ArrayList<ConditionalCriteria>();
        }
        conditions.add(ConditionalCriteria.equal(IndicatorVersionProperties.procStatus(), IndicatorProcStatusEnum.PUBLISHED));

        // Find
        PagedResult<IndicatorVersion> indicatorsVersions = getIndicatorVersionRepository().findByCondition(conditions, pagingParameter);
        return indicatorsVersions;
    }

    @Override
    public IndicatorVersion sendIndicatorToProductionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkSendIndicatorToProductionValidation(uuid, null);

        // Retrieve version in draft
        IndicatorVersion indicatorInProduction = retrieveIndicatorProcStatusInProduction(ctx, uuid, false);

        // Validate to send to production
        checkIndicatorToSendToProductionValidation(ctx, uuid, indicatorInProduction);

        // Update proc status
        indicatorInProduction.setProcStatus(IndicatorProcStatusEnum.PRODUCTION_VALIDATION);
        indicatorInProduction.setProductionValidationDate(new DateTime());
        indicatorInProduction.setProductionValidationUser(ctx.getUserId());
        indicatorInProduction = getIndicatorVersionRepository().save(indicatorInProduction);

        return indicatorInProduction;
    }

    @Override
    public IndicatorVersion rejectIndicatorProductionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRejectIndicatorProductionValidation(uuid, null);

        // Retrieve version in production
        IndicatorVersion indicatorInProduction = retrieveIndicatorProcStatusInProduction(ctx, uuid, false);

        // Validate to reject
        checkIndicatorToRejectProductionValidation(ctx, uuid, indicatorInProduction);

        // Update proc status
        indicatorInProduction.setProcStatus(IndicatorProcStatusEnum.VALIDATION_REJECTED);
        indicatorInProduction.setProductionValidationDate(null);
        indicatorInProduction.setProductionValidationUser(null);
        indicatorInProduction.setDiffusionValidationDate(null);
        indicatorInProduction.setDiffusionValidationUser(null);
        indicatorInProduction = getIndicatorVersionRepository().save(indicatorInProduction);

        return indicatorInProduction;
    }

    @Override
    public IndicatorVersion sendIndicatorToDiffusionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkSendIndicatorToDiffusionValidation(uuid, null);

        // Retrieve version in production validation
        IndicatorVersion indicatorInProduction = retrieveIndicatorProcStatusInProduction(ctx, uuid, false);

        // Validate to send to diffusion
        checkIndicatorToSendToDiffusionValidation(ctx, uuid, indicatorInProduction);

        // Update proc status
        indicatorInProduction.setProcStatus(IndicatorProcStatusEnum.DIFFUSION_VALIDATION);
        indicatorInProduction.setDiffusionValidationDate(new DateTime());
        indicatorInProduction.setDiffusionValidationUser(ctx.getUserId());
        indicatorInProduction = getIndicatorVersionRepository().save(indicatorInProduction);

        return indicatorInProduction;
    }

    @Override
    public IndicatorVersion rejectIndicatorDiffusionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRejectIndicatorDiffusionValidation(uuid, null);

        // Retrieve version in production
        IndicatorVersion indicatorInProduction = retrieveIndicatorProcStatusInProduction(ctx, uuid, false);

        // Validate to reject
        checkIndicatorToRejectDiffusionValidation(ctx, uuid, indicatorInProduction);

        // Update proc status
        indicatorInProduction.setProcStatus(IndicatorProcStatusEnum.VALIDATION_REJECTED);
        indicatorInProduction.setProductionValidationDate(null);
        indicatorInProduction.setProductionValidationUser(null);
        indicatorInProduction.setDiffusionValidationDate(null);
        indicatorInProduction.setDiffusionValidationUser(null);
        indicatorInProduction = getIndicatorVersionRepository().save(indicatorInProduction);

        return indicatorInProduction;
    }

    @Override
    public PublishIndicatorResult publishIndicator(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkPublishIndicator(uuid, null);

        // Retrieve version in diffusion validation
        IndicatorVersion indicatorInProduction = retrieveIndicatorProcStatusInProduction(ctx, uuid, false);

        // Validate to publish
        checkIndicatorToPublish(ctx, uuid, indicatorInProduction);

        // Populate data
        try {
            getIndicatorsDataService().populateIndicatorData(ctx, uuid, indicatorInProduction.getVersionNumber());
        } catch (MetamacException e) {
            indicatorInProduction.setProcStatus(IndicatorProcStatusEnum.PUBLICATION_FAILED);
            indicatorInProduction.setPublicationFailedDate(new DateTime());
            indicatorInProduction.setPublicationUser(ctx.getUserId());
            indicatorInProduction = getIndicatorVersionRepository().save(indicatorInProduction);

            PublishIndicatorResult publishIndicatorResult = new PublishIndicatorResult();
            publishIndicatorResult.setIndicatorVersion(indicatorInProduction);
            publishIndicatorResult.setPublicationFailedReason(e);
            return publishIndicatorResult;
        }

        // Update proc status
        indicatorInProduction.setProcStatus(IndicatorProcStatusEnum.PUBLISHED);
        indicatorInProduction.setPublicationDate(new DateTime());
        indicatorInProduction.setPublicationUser(ctx.getUserId());
        indicatorInProduction.setPublicationFailedDate(null); // remove posible failed information
        indicatorInProduction.setPublicationFailedUser(null);
        indicatorInProduction = getIndicatorVersionRepository().save(indicatorInProduction);

        Indicator indicator = indicatorInProduction.getIndicator();
        // Remove possible last version in diffusion
        if (indicator.getDiffusionVersion() != null) {
            IndicatorVersion indicatorDiffusionVersion = retrieveIndicator(ctx, uuid, indicator.getDiffusionVersion().getVersionNumber());
            indicator.getVersions().remove(indicatorDiffusionVersion);
            getIndicatorRepository().save(indicator);
            getIndicatorVersionRepository().delete(indicatorDiffusionVersion);
        }
        indicator.setIsPublished(Boolean.TRUE);
        indicator.setDiffusionVersion(new IndicatorVersionInformation(indicatorInProduction.getId(), indicatorInProduction.getVersionNumber()));
        indicator.setProductionVersion(null);

        getIndicatorRepository().save(indicator);

        PublishIndicatorResult publishIndicatorResult = new PublishIndicatorResult();
        publishIndicatorResult.setIndicatorVersion(indicatorInProduction);
        return publishIndicatorResult;
    }

    @Override
    public IndicatorVersion archiveIndicator(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkArchiveIndicator(uuid, null);

        // Retrieve version published
        IndicatorVersion indicatorInDiffusion = retrieveIndicatorProcStatusInDiffusion(ctx, uuid, false);

        // Validate to archive
        checkIndicatorToArchive(ctx, uuid, indicatorInDiffusion);

        // Update proc status
        Indicator indicator = indicatorInDiffusion.getIndicator();
        indicator.setIsPublished(Boolean.FALSE);
        getIndicatorRepository().save(indicator);

        indicatorInDiffusion.setProcStatus(IndicatorProcStatusEnum.ARCHIVED);
        indicatorInDiffusion.setArchiveDate(new DateTime());
        indicatorInDiffusion.setArchiveUser(ctx.getUserId());
        indicatorInDiffusion = getIndicatorVersionRepository().save(indicatorInDiffusion);

        return indicatorInDiffusion;
    }

    @Override
    public IndicatorVersion versioningIndicator(ServiceContext ctx, String uuid, VersionTypeEnum versionType) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkVersioningIndicator(uuid, versionType, null);

        // Retrieve
        Indicator indicator = retrieveIndicator(ctx, uuid);
        if (indicator.getProductionVersion() != null) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, uuid, new IndicatorProcStatusEnum[]{IndicatorProcStatusEnum.PUBLISHED, IndicatorProcStatusEnum.ARCHIVED});
        }

        // Initialize new version, copying values of version in diffusion
        IndicatorVersion indicatorVersionDiffusion = retrieveIndicatorProcStatusInDiffusion(ctx, uuid, true);
        IndicatorVersion indicatorNewVersion = DoCopyUtils.copy(indicatorVersionDiffusion);
        indicatorNewVersion.setProcStatus(IndicatorProcStatusEnum.DRAFT);
        indicatorNewVersion.setVersionNumber(ServiceUtils.generateVersionNumber(indicatorVersionDiffusion.getVersionNumber(), versionType));
        indicatorNewVersion.setIsLastVersion(Boolean.TRUE);

        // Update diffusion version
        indicatorVersionDiffusion.setIsLastVersion(Boolean.FALSE);
        indicatorVersionDiffusion = getIndicatorVersionRepository().save(indicatorVersionDiffusion);

        // Create draft version
        indicatorNewVersion.setIndicator(indicator);
        indicatorNewVersion = getIndicatorVersionRepository().save(indicatorNewVersion);

        // Update indicator with draft version
        indicator.setProductionVersion(new IndicatorVersionInformation(indicatorNewVersion.getId(), indicatorNewVersion.getVersionNumber()));
        indicator.getVersions().add(indicatorNewVersion);
        getIndicatorRepository().save(indicator);

        return indicatorNewVersion;
    }

    @Override
    public DataSource createDataSource(ServiceContext ctx, String indicatorUuid, DataSource dataSource) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkCreateDataSource(indicatorUuid, dataSource, null);

        // Validate indicators system proc status and linked indicators
        IndicatorVersion indicatorVersion = retrieveIndicatorProcStatusInProduction(ctx, indicatorUuid, true);
        checkIndicatorsLinkedInDatasource(dataSource, indicatorVersion);

        // Create dataSource
        dataSource.setIndicatorVersion(indicatorVersion);
        dataSource = getDataSourceRepository().save(dataSource);

        // Update indicator adding dataSource
        indicatorVersion.addDataSource(dataSource);
        indicatorVersion.setInconsistentData(Boolean.TRUE);
        getIndicatorVersionRepository().save(indicatorVersion);

        return dataSource;
    }

    @Override
    public DataSource retrieveDataSource(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveDataSource(uuid, null);

        // Retrieve
        DataSource dataSource = getDataSourceRepository().findDataSource(uuid);
        if (dataSource == null) {
            throw new MetamacException(ServiceExceptionType.DATA_SOURCE_NOT_FOUND, uuid);
        }
        return dataSource;
    }

    @Override
    public DataSource updateDataSource(ServiceContext ctx, DataSource dataSource) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkUpdateDataSource(dataSource, null);

        // Check indicator proc status and linked indicators
        checkIndicatorVersionInProduction(dataSource.getIndicatorVersion());
        checkIndicatorsLinkedInDatasource(dataSource, dataSource.getIndicatorVersion());

        // Update
        DataSource updatedDataSource = getDataSourceRepository().save(dataSource);

        IndicatorVersion indicatorVersion = updatedDataSource.getIndicatorVersion();
        indicatorVersion.setInconsistentData(Boolean.TRUE);
        getIndicatorVersionRepository().save(indicatorVersion);

        return updatedDataSource;
    }

    @Override
    public void deleteDataSource(ServiceContext ctx, String uuid) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkDeleteDataSource(uuid, null);

        // Check indicator proc status
        DataSource dataSource = retrieveDataSource(ctx, uuid);
        IndicatorVersion indicatorVersion = dataSource.getIndicatorVersion();
        checkIndicatorVersionInProduction(indicatorVersion);

        // Delete
        getDataSourceRepository().delete(dataSource);

        indicatorVersion.setInconsistentData(Boolean.TRUE);
        getIndicatorVersionRepository().save(indicatorVersion);
    }

    @Override
    public List<DataSource> retrieveDataSourcesByIndicator(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveDataSourcesByIndicator(indicatorUuid, indicatorVersionNumber, null);

        // Retrieve dataSources and transform
        IndicatorVersion indicatorVersion = retrieveIndicator(ctx, indicatorUuid, indicatorVersionNumber);
        return indicatorVersion.getDataSources();
    }

    @Override
    public QuantityUnit retrieveQuantityUnit(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveQuantityUnit(uuid, null);

        // Retrieve
        QuantityUnit quantityUnit = getQuantityUnitRepository().retrieveQuantityUnit(uuid);
        if (quantityUnit == null) {
            throw new MetamacException(ServiceExceptionType.QUANTITY_UNIT_NOT_FOUND, uuid);
        }
        return quantityUnit;
    }

    @Override
    public List<QuantityUnit> retrieveQuantityUnits(ServiceContext ctx) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveQuantityUnits(null);

        // Find
        List<QuantityUnit> quantityUnits = getQuantityUnitRepository().findAll();
        return quantityUnits;
    }

    /**
     * This operation retrieve subject from table view. Won't be accesible in public web application.
     */
    @Override
    public Subject retrieveSubject(ServiceContext ctx, String code) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveSubject(code, null);

        // Retrieve
        Subject subject = getSubjectRepository().retrieveSubject(code);
        if (subject == null) {
            throw new MetamacException(ServiceExceptionType.SUBJECT_NOT_FOUND, code);
        }
        return subject;
    }

    /**
     * This operation retrieves subjects from table view. Won't be accesible in public web application.
     */
    @Override
    public List<Subject> retrieveSubjects(ServiceContext ctx) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveSubjects(null);

        // Find
        List<Subject> subjects = getSubjectRepository().findSubjects();
        return subjects;
    }

    /**
     * This operation retrieves subjects from indicators table
     */
    @Override
    public List<SubjectIndicatorResult> retrieveSubjectsInPublishedIndicators(ServiceContext ctx) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveSubjectsInPublishedIndicators(null);

        // Find
        List<SubjectIndicatorResult> subjects = getIndicatorVersionRepository().findSubjectsInPublishedIndicators();
        return subjects;
    }

    /**
     * Checks not exists another indicator with same code
     */
    private void checkIndicatorCodeUnique(ServiceContext ctx, String code) throws MetamacException {

        // Prepare criteria
        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
        ConditionRoot<Indicator> conditionRoot = ConditionalCriteriaBuilder.criteriaFor(Indicator.class);
        conditionRoot.withProperty(IndicatorProperties.code()).ignoreCaseEq(code);
        List<ConditionalCriteria> conditions = conditionRoot.distinctRoot().build();

        // Find
        PagedResult<Indicator> result = getIndicatorRepository().findByCondition(conditions, pagingParameter);
        if (result.getValues().size() != 0) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_ALREADY_EXIST_CODE_DUPLICATED, code);
        }
    }

    /**
     * Retrieves version of an indicator in production
     */
    private IndicatorVersion retrieveIndicatorProcStatusInProduction(ServiceContext ctx, String uuid, boolean throwsExceptionIfNotExistsInProduction) throws MetamacException {
        Indicator indicator = retrieveIndicator(ctx, uuid);
        if (indicator.getProductionVersion() == null && !throwsExceptionIfNotExistsInProduction) {
            return null; // to throws an specific exception
        }
        if (indicator.getProductionVersion() == null) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_IN_PRODUCTION_NOT_FOUND, uuid);
        }
        IndicatorVersion indicatorVersionProduction = retrieveIndicator(ctx, uuid, indicator.getProductionVersion().getVersionNumber());
        return indicatorVersionProduction;
    }

    /**
     * Retrieves version of an indicator in diffusion
     */
    private IndicatorVersion retrieveIndicatorProcStatusInDiffusion(ServiceContext ctx, String uuid, boolean throwsExceptionIfNotExistsInDiffusion) throws MetamacException {
        Indicator indicator = retrieveIndicator(ctx, uuid);
        if (indicator.getDiffusionVersion() == null && !throwsExceptionIfNotExistsInDiffusion) {
            return null; // to throws an specific exception
        }
        if (indicator.getDiffusionVersion() == null) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_IN_DIFFUSION_NOT_FOUND, uuid);
        }
        IndicatorVersion indicatorVersionDiffusion = retrieveIndicator(ctx, uuid, indicator.getDiffusionVersion().getVersionNumber());
        return indicatorVersionDiffusion;
    }

    /**
     * Makes validations to sent to production validation.
     * Checks actual processing status and if it is correct checks conditions to send to production validation
     */
    private void checkIndicatorToSendToProductionValidation(ServiceContext ctx, String uuid, IndicatorVersion indicatorVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        if (indicatorVersion == null
                || (!IndicatorProcStatusEnum.DRAFT.equals(indicatorVersion.getProcStatus()) && !IndicatorProcStatusEnum.VALIDATION_REJECTED.equals(indicatorVersion.getProcStatus()))) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, uuid, new IndicatorProcStatusEnum[]{IndicatorProcStatusEnum.DRAFT,
                    IndicatorProcStatusEnum.VALIDATION_REJECTED}));
        } else {
            // Check other conditions
            checkConditionsToSendToProductionValidation(indicatorVersion, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to reject production validation
     * - Check actual processing status
     */
    private void checkIndicatorToRejectProductionValidation(ServiceContext ctx, String uuid, IndicatorVersion indicatorVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        if (indicatorVersion == null || !IndicatorProcStatusEnum.PRODUCTION_VALIDATION.equals(indicatorVersion.getProcStatus())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, uuid, new IndicatorProcStatusEnum[]{IndicatorProcStatusEnum.PRODUCTION_VALIDATION}));
        } else {
            // nothing more to check
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to sent to diffusion validation
     * Checks actual processing status and if it is correct checks same conditions to send to production validation
     */
    private void checkIndicatorToSendToDiffusionValidation(ServiceContext ctx, String uuid, IndicatorVersion indicatorVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        if (indicatorVersion == null || !IndicatorProcStatusEnum.PRODUCTION_VALIDATION.equals(indicatorVersion.getProcStatus())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, uuid, new IndicatorProcStatusEnum[]{IndicatorProcStatusEnum.PRODUCTION_VALIDATION}));
        } else {
            // Check other conditions
            checkConditionsToSendToProductionValidation(indicatorVersion, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to reject
     * - Check actual processing status
     */
    private void checkIndicatorToRejectDiffusionValidation(ServiceContext ctx, String uuid, IndicatorVersion indicatorVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        if (indicatorVersion == null || !IndicatorProcStatusEnum.DIFFUSION_VALIDATION.equals(indicatorVersion.getProcStatus())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, uuid, new IndicatorProcStatusEnum[]{IndicatorProcStatusEnum.DIFFUSION_VALIDATION}));
        } else {
            // nothing more to check
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to publish
     * Checks actual processing status.
     * If state is correct checks same conditions to send to production validation and additional conditions to publish
     */
    private void checkIndicatorToPublish(ServiceContext ctx, String uuid, IndicatorVersion indicatorVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        if (indicatorVersion == null
                || (!IndicatorProcStatusEnum.DIFFUSION_VALIDATION.equals(indicatorVersion.getProcStatus()) && !IndicatorProcStatusEnum.PUBLICATION_FAILED.equals(indicatorVersion.getProcStatus()))) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, uuid, new IndicatorProcStatusEnum[]{IndicatorProcStatusEnum.DIFFUSION_VALIDATION,
                    IndicatorProcStatusEnum.PUBLICATION_FAILED}));
        } else {
            // Check other conditions
            checkConditionsToPublish(ctx, indicatorVersion, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to archive
     * - Check actual processing status
     */
    private void checkIndicatorToArchive(ServiceContext ctx, String uuid, IndicatorVersion indicatorVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        if (indicatorVersion == null || !IndicatorProcStatusEnum.PUBLISHED.equals(indicatorVersion.getProcStatus())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, uuid, new IndicatorProcStatusEnum[]{IndicatorProcStatusEnum.PUBLISHED}));
        } else {
            // nothing more to check
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * - Check quantity is complete
     * - Must exists at least one datasource
     */
    private void checkConditionsToSendToProductionValidation(IndicatorVersion indicatorVersion, List<MetamacExceptionItem> exceptions) {

        // Check quantity is complete
        InvocationValidator.checkQuantity(indicatorVersion.getQuantity(), ServiceExceptionParameters.INDICATOR, true, exceptions);

        // Check indicator has at least one data source
        if (indicatorVersion.getDataSources().size() == 0) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.INDICATOR_MUST_HAVE_DATA_SOURCES, indicatorVersion.getIndicator().getUuid()));
        }
    }

    /**
     * - Validations when send to diffusion validation
     * - Checks dataset repository is completed
     * - Checks linked indicators are published
     */
    private void checkConditionsToPublish(ServiceContext ctx, IndicatorVersion indicatorVersion, List<MetamacExceptionItem> exceptions) {

        checkConditionsToSendToProductionValidation(indicatorVersion, exceptions);

        // Check linked indicators
        checkQuantityIndicatorsPublished(ctx, indicatorVersion, exceptions);
    }

    /**
     * Checks linked indicators are published:
     * a) Checks numerator and denominator are published (for indicator and all datasources)
     * b) Checks base quantity is published (if it is not own indicator) (for indicator and all datasources)
     */
    private void checkQuantityIndicatorsPublished(ServiceContext ctx, IndicatorVersion indicatorVersion, List<MetamacExceptionItem> exceptions) {

        List<String> indicatorsUuidLinked = null;

        // Quantities of datasources
        indicatorsUuidLinked = getDataSourceRepository().findIndicatorsLinkedWithIndicatorVersion(indicatorVersion.getId());

        // Quantity of indicator
        if (indicatorVersion.getQuantity().getNumerator() != null) {
            indicatorsUuidLinked.add(indicatorVersion.getQuantity().getNumerator().getUuid());
        }
        if (indicatorVersion.getQuantity().getDenominator() != null) {
            indicatorsUuidLinked.add(indicatorVersion.getQuantity().getDenominator().getUuid());
        }
        if (indicatorVersion.getQuantity().getBaseQuantity() != null) {
            indicatorsUuidLinked.add(indicatorVersion.getQuantity().getBaseQuantity().getUuid());
        }

        // Remove possible own indicator, because this can be as base quantity and it is not published yet
        for (Iterator<String> iterator = indicatorsUuidLinked.iterator(); iterator.hasNext();) {
            String indicatorUuidLinked = (String) iterator.next();
            if (indicatorUuidLinked.equals(indicatorVersion.getIndicator().getUuid())) {
                iterator.remove();
            }
        }

        // Checks published
        if (indicatorsUuidLinked.size() != 0) {
            List<String> indicatorsNotPublishedUuid = getIndicatorRepository().filterIndicatorsNotPublished(indicatorsUuidLinked);
            if (indicatorsNotPublishedUuid.size() != 0) {
                String[] indicatorsNotPublishedUuidArray = (String[]) indicatorsNotPublishedUuid.toArray(new String[indicatorsNotPublishedUuid.size()]);
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.INDICATOR_MUST_HAVE_ALL_LINKED_INDICATORS_PUBLISHED, indicatorVersion.getIndicator().getUuid(),
                        indicatorsNotPublishedUuidArray));
            }
        }
    }

    /**
     * Checks that the indicator version is in any proc status in production
     */
    private void checkIndicatorVersionInProduction(IndicatorVersion indicatorVersion) throws MetamacException {
        IndicatorProcStatusEnum procStatus = indicatorVersion.getProcStatus();
        boolean inProduction = IndicatorProcStatusEnum.DRAFT.equals(procStatus) || IndicatorProcStatusEnum.VALIDATION_REJECTED.equals(procStatus)
                || IndicatorProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus) || IndicatorProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)
                || IndicatorProcStatusEnum.PUBLICATION_FAILED.equals(procStatus);
        if (!inProduction) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, indicatorVersion.getIndicator().getUuid(), new IndicatorProcStatusEnum[]{IndicatorProcStatusEnum.DRAFT,
                    IndicatorProcStatusEnum.VALIDATION_REJECTED, IndicatorProcStatusEnum.PRODUCTION_VALIDATION, IndicatorProcStatusEnum.DIFFUSION_VALIDATION,
                    IndicatorProcStatusEnum.PUBLICATION_FAILED});
        }
    }

    private void checkIndicatorsLinkedInDatasource(DataSource dataSource, IndicatorVersion indicatorVersion) throws MetamacException {
        if (dataSource.getAnnualPuntualRate() != null) {
            checkIndicatorsLinkedInQuantity(dataSource.getAnnualPuntualRate().getQuantity(), indicatorVersion.getIndicator().getUuid(), Boolean.TRUE,
                    ServiceExceptionParameters.DATA_SOURCE_ANNUAL_PUNTUAL_RATE);
        }
        if (dataSource.getInterperiodPuntualRate() != null) {
            checkIndicatorsLinkedInQuantity(dataSource.getInterperiodPuntualRate().getQuantity(), indicatorVersion.getIndicator().getUuid(), Boolean.TRUE,
                    ServiceExceptionParameters.DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE);
        }
        if (dataSource.getAnnualPercentageRate() != null) {
            checkIndicatorsLinkedInQuantity(dataSource.getAnnualPercentageRate().getQuantity(), indicatorVersion.getIndicator().getUuid(), Boolean.TRUE,
                    ServiceExceptionParameters.DATA_SOURCE_ANNUAL_PERCENTAGE_RATE);
        }
        if (dataSource.getInterperiodPercentageRate() != null) {
            checkIndicatorsLinkedInQuantity(dataSource.getInterperiodPercentageRate().getQuantity(), indicatorVersion.getIndicator().getUuid(), Boolean.TRUE,
                    ServiceExceptionParameters.DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE);
        }
    }

    /**
     * Numerator and denominator never must be own indicator.
     * Base quantity never must be own indicator, except when it is a quantity of a datasource and it is change rate. In this case it always must be own indicator
     */
    private void checkIndicatorsLinkedInQuantity(Quantity quantity, String indicatorUuid, Boolean isDataSource, String parameterName) throws MetamacException {
        if (quantity.getNumerator() != null && quantity.getNumerator().getUuid().equals(indicatorUuid)) {
            throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, parameterName + ServiceExceptionParametersInternal.QUANTITY_NUMERATOR_INDICATOR_UUID);
        }
        if (quantity.getDenominator() != null && quantity.getDenominator().getUuid().equals(indicatorUuid)) {
            throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, parameterName + ServiceExceptionParametersInternal.QUANTITY_DENOMINATOR_INDICATOR_UUID);
        }
        if (quantity.getBaseQuantity() != null) {
            if (isDataSource) {
                if (!quantity.getBaseQuantity().getUuid().equals(indicatorUuid)) {
                    throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, parameterName + ServiceExceptionParametersInternal.QUANTITY_BASE_QUANTITY_INDICATOR_UUID);
                }
            } else {
                if (quantity.getBaseQuantity().getUuid().equals(indicatorUuid)) {
                    throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, parameterName + ServiceExceptionParametersInternal.QUANTITY_BASE_QUANTITY_INDICATOR_UUID);
                }
            }
        }
    }
}
