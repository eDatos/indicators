package es.gobcan.istac.indicators.core.serviceimpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.ConditionRoot;
import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.hibernate.exception.ConstraintViolationException;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.ElementLevel;
import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalGranularityProperties;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.GeographicalValueProperties;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorInstanceProperties;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemHistory;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemProperties;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersionInformation;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersionProperties;
import es.gobcan.istac.indicators.core.domain.MeasureValue;
import es.gobcan.istac.indicators.core.domain.TimeGranularity;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.domain.Translation;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemProcStatusEnum;
import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.TimeGranularityEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionParameters;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceimpl.util.DoCopyUtils;
import es.gobcan.istac.indicators.core.serviceimpl.util.InvocationValidator;
import es.gobcan.istac.indicators.core.serviceimpl.util.ServiceUtils;
import es.gobcan.istac.indicators.core.serviceimpl.util.TimeVariableUtils;
import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;

/**
 * Implementation of IndicatorsSystemService.
 */
@Service("indicatorsSystemService")
public class IndicatorsSystemsServiceImpl extends IndicatorsSystemsServiceImplBase {

    public IndicatorsSystemsServiceImpl() {
    }

    // --------------------------------------------------------------------------------------------
    // INDICATOR SYSTEM
    // --------------------------------------------------------------------------------------------

    @Override
    public IndicatorsSystemVersion createIndicatorsSystem(ServiceContext ctx, IndicatorsSystemVersion indicatorsSystemVersion) throws MetamacException {

        IndicatorsSystem indicatorsSystem = indicatorsSystemVersion.getIndicatorsSystem();

        // Validation of parameters
        InvocationValidator.checkCreateIndicatorsSystem(indicatorsSystemVersion, null);
        checkIndicatorsSystemCodeUnique(indicatorsSystem.getCode());

        // Save indicator
        indicatorsSystem.setDiffusionVersion(null);
        indicatorsSystem.setIsPublished(Boolean.FALSE);
        indicatorsSystem = getIndicatorsSystemRepository().save(indicatorsSystem);

        // Save draft version
        indicatorsSystemVersion.setProcStatus(IndicatorsSystemProcStatusEnum.DRAFT);
        indicatorsSystemVersion.setIsLastVersion(Boolean.TRUE);
        indicatorsSystemVersion.setVersionNumber(ServiceUtils.generateVersionNumber(null, VersionTypeEnum.MAJOR));
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
            IndicatorsSystem indicatorsSystem = retrieveIndicatorsSystem(uuid);
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
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, uuid, new String[]{ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_PUBLISHED});
        }

        return publishedIndicatorsSystemVersion;
    }

    @Override
    public IndicatorsSystemVersion retrieveIndicatorsSystemByCode(ServiceContext ctx, String code, String versionNumber) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorsSystemByCode(code, null);

        // Prepare criteria (indicators system version by code, version requested or last version)
        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
        ConditionRoot<IndicatorsSystemVersion> conditionRoot = ConditionalCriteriaBuilder.criteriaFor(IndicatorsSystemVersion.class);
        conditionRoot.withProperty(new LeafProperty<IndicatorsSystemVersion>("indicatorsSystem", "code", false, IndicatorsSystemVersion.class)).eq(code);
        if (versionNumber != null) {
            conditionRoot.withProperty(IndicatorsSystemVersionProperties.versionNumber()).eq(versionNumber);
        } else {
            conditionRoot.withProperty(IndicatorsSystemVersionProperties.isLastVersion()).eq(Boolean.TRUE);
        }
        List<ConditionalCriteria> conditions = conditionRoot.distinctRoot().build();

        // Find
        PagedResult<IndicatorsSystemVersion> result = getIndicatorsSystemVersionRepository().findByCondition(conditions, pagingParameter);

        if (result.getValues().size() == 0) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND_WITH_CODE, code);
        }

        // Return unique result
        return result.getValues().get(0);
    }

    @Override
    public IndicatorsSystemVersion retrieveIndicatorsSystemPublishedByCode(ServiceContext ctx, String code) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorsSystemPublishedByCode(code, null);

        // Prepare criteria (indicators system version by code, published)
        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
        ConditionRoot<IndicatorsSystemVersion> conditionRoot = ConditionalCriteriaBuilder.criteriaFor(IndicatorsSystemVersion.class);
        conditionRoot.withProperty(new LeafProperty<IndicatorsSystemVersion>("indicatorsSystem", "code", false, IndicatorsSystemVersion.class)).eq(code);
        conditionRoot.withProperty(IndicatorsSystemVersionProperties.procStatus()).eq(IndicatorsSystemProcStatusEnum.PUBLISHED);
        List<ConditionalCriteria> conditions = conditionRoot.distinctRoot().build();

        // Find
        PagedResult<IndicatorsSystemVersion> result = getIndicatorsSystemVersionRepository().findByCondition(conditions, pagingParameter);

        if (result.getValues().size() == 0) {
            // Try retrieve any version with code, to throws specific exception
            IndicatorsSystemVersion indicatorsSystemVersionLastVersion = retrieveIndicatorsSystemByCode(ctx, code, null);
            if (indicatorsSystemVersionLastVersion != null) {
                throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, indicatorsSystemVersionLastVersion.getIndicatorsSystem().getUuid(),
                        new String[]{ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_PUBLISHED});
            }
        }

        // Return unique result
        return result.getValues().get(0);
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
            // another version is now last version
            indicatorsSystem.getVersions().get(0).setIsLastVersion(Boolean.TRUE);

            // Update
            getIndicatorsSystemRepository().save(indicatorsSystem);
            getIndicatorsSystemVersionRepository().delete(indicatorsSystemVersion);
        }
    }

    @Override
    public PagedResult<IndicatorsSystemVersion> findIndicatorsSystems(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindIndicatorsSystems(conditions, pagingParameter, null);

        // Retrieve last versions
        if (conditions == null) {
            conditions = new ArrayList<ConditionalCriteria>();
        }
        conditions.add(ConditionalCriteria.equal(IndicatorsSystemVersionProperties.isLastVersion(), Boolean.TRUE));

        // Find
        return getIndicatorsSystemVersionRepository().findByCondition(conditions, pagingParameter);
    }

    @Override
    public PagedResult<IndicatorsSystemVersion> findIndicatorsSystemsPublished(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindIndicatorsSystemsPublished(conditions, pagingParameter, null);

        // Retrieve published
        if (conditions == null) {
            conditions = new ArrayList<ConditionalCriteria>();
        }
        conditions.add(ConditionalCriteria.equal(IndicatorsSystemVersionProperties.procStatus(), IndicatorsSystemProcStatusEnum.PUBLISHED));

        // Find
        return getIndicatorsSystemVersionRepository().findByCondition(conditions, pagingParameter);
    }

    @Override
    public List<IndicatorsSystemVersion> retrieveIndicatorsSystemPublishedForIndicator(ServiceContext ctx, String indicatorUuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorsSystemPublishedForIndicator(indicatorUuid, null);

        // Find
        return getIndicatorsSystemVersionRepository().retrieveIndicatorsSystemPublishedForIndicator(indicatorUuid);
    }

    @Override
    public IndicatorsSystemVersion sendIndicatorsSystemToProductionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkSendIndicatorsSystemToProductionValidation(uuid, null);

        // Retrieve version in draft
        IndicatorsSystemVersion indicatorsSystemInProduction = retrieveIndicatorsSystemProcStatusInProduction(ctx, uuid, false);

        // Validate to send to production
        checkIndicatorsSystemToSendToProductionValidation(uuid, indicatorsSystemInProduction);

        // Update proc status
        indicatorsSystemInProduction.setProcStatus(IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION);
        indicatorsSystemInProduction.setProductionValidationDate(new DateTime());
        indicatorsSystemInProduction.setProductionValidationUser(ctx.getUserId());
        indicatorsSystemInProduction = getIndicatorsSystemVersionRepository().save(indicatorsSystemInProduction);

        return indicatorsSystemInProduction;
    }

    @Override
    public IndicatorsSystemVersion rejectIndicatorsSystemProductionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRejectIndicatorsSystemProductionValidation(uuid, null);

        // Retrieve version in production
        IndicatorsSystemVersion indicatorsSystemInProduction = retrieveIndicatorsSystemProcStatusInProduction(ctx, uuid, false);

        // Validate to reject
        checkIndicatorsSystemToRejectProductionValidation(uuid, indicatorsSystemInProduction);

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
    public IndicatorsSystemVersion sendIndicatorsSystemToDiffusionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkSendIndicatorsSystemToDiffusionValidation(uuid, null);

        // Retrieve version in production validation
        IndicatorsSystemVersion indicatorsSystemInProduction = retrieveIndicatorsSystemProcStatusInProduction(ctx, uuid, false);

        // Validate to send to diffusion
        checkIndicatorsSystemToSendToDiffusionValidation(uuid, indicatorsSystemInProduction);

        // Update proc status
        indicatorsSystemInProduction.setProcStatus(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION);
        indicatorsSystemInProduction.setDiffusionValidationDate(new DateTime());
        indicatorsSystemInProduction.setDiffusionValidationUser(ctx.getUserId());
        indicatorsSystemInProduction = getIndicatorsSystemVersionRepository().save(indicatorsSystemInProduction);

        return indicatorsSystemInProduction;
    }

    @Override
    public IndicatorsSystemVersion rejectIndicatorsSystemDiffusionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRejectIndicatorsSystemDiffusionValidation(uuid, null);

        // Retrieve version in production
        IndicatorsSystemVersion indicatorsSystemInProduction = retrieveIndicatorsSystemProcStatusInProduction(ctx, uuid, false);

        // Validate to reject
        checkIndicatorsSystemToRejectDiffusionValidation(uuid, indicatorsSystemInProduction);

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

        // Validate to publish
        checkIndicatorsSystemToPublish(uuid, indicatorsSystemInProduction);

        // Update proc status
        indicatorsSystemInProduction.setProcStatus(IndicatorsSystemProcStatusEnum.PUBLISHED);
        indicatorsSystemInProduction.setPublicationDate(new DateTime());
        indicatorsSystemInProduction.setPublicationUser(ctx.getUserId());
        indicatorsSystemInProduction = getIndicatorsSystemVersionRepository().save(indicatorsSystemInProduction);

        IndicatorsSystem indicatorsSystem = indicatorsSystemInProduction.getIndicatorsSystem();
        // Remove possible last version in diffusion
        if (indicatorsSystem.getDiffusionVersion() != null) {
            IndicatorsSystemVersion indicatorDiffusionVersion = retrieveIndicatorsSystem(ctx, uuid, indicatorsSystem.getDiffusionVersion().getVersionNumber());

            deleteIndicatorInstanceLastValuesCacheInIndicatorsSystem(indicatorDiffusionVersion);

            indicatorsSystem.getVersions().remove(indicatorDiffusionVersion);
            getIndicatorsSystemRepository().save(indicatorsSystem);
            getIndicatorsSystemVersionRepository().delete(indicatorDiffusionVersion);
        }
        indicatorsSystem.setIsPublished(Boolean.TRUE);
        indicatorsSystem.setDiffusionVersion(new IndicatorsSystemVersionInformation(indicatorsSystemInProduction.getId(), indicatorsSystemInProduction.getVersionNumber()));
        indicatorsSystem.setProductionVersion(null);

        // Update indicators system
        indicatorsSystem = getIndicatorsSystemRepository().save(indicatorsSystem);

        // Add new version to history
        IndicatorsSystemHistory indicatorsSystemHistory = new IndicatorsSystemHistory(indicatorsSystem.getDiffusionVersion().getVersionNumber(), indicatorsSystem,
                indicatorsSystemInProduction.getPublicationDate());
        getIndicatorsSystemHistoryRepository().save(indicatorsSystemHistory);

        createIndicatorInstanceLastValuesCacheInIndicatorsSystem(ctx, indicatorsSystemInProduction);

        return indicatorsSystemInProduction;
    }

    @Override
    public IndicatorsSystemVersion archiveIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkArchiveIndicatorsSystem(uuid, null);

        // Retrieve version published
        IndicatorsSystemVersion indicatorsSystemInDiffusion = retrieveIndicatorsSystemProcStatusInDiffusion(ctx, uuid, false);

        // Validate to archive
        checkIndicatorsSystemToArchive(uuid, indicatorsSystemInDiffusion);

        // delete cache table for every indicator instance*/
        deleteIndicatorInstanceLastValuesCacheInIndicatorsSystem(indicatorsSystemInDiffusion);

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
    public IndicatorsSystemVersion versioningIndicatorsSystem(ServiceContext ctx, String uuid, VersionTypeEnum versionType) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkVersioningIndicatorsSystem(uuid, versionType, null);

        IndicatorsSystem indicatorsSystem = retrieveIndicatorsSystem(uuid);
        if (indicatorsSystem.getProductionVersion() != null) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, uuid, new String[]{ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_PUBLISHED,
                    ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_ARCHIVED});
        }

        // Initialize new version, copying values of version in diffusion
        IndicatorsSystemVersion indicatorsSystemVersionDiffusion = retrieveIndicatorsSystemProcStatusInDiffusion(ctx, uuid, true);
        IndicatorsSystemVersion indicatorsSystemNewVersion = DoCopyUtils.copy(indicatorsSystemVersionDiffusion);
        indicatorsSystemNewVersion.setProcStatus(IndicatorsSystemProcStatusEnum.DRAFT);
        indicatorsSystemNewVersion.setVersionNumber(ServiceUtils.generateVersionNumber(indicatorsSystemVersionDiffusion.getVersionNumber(), versionType));
        indicatorsSystemNewVersion.setIsLastVersion(Boolean.TRUE);

        // Update diffusion version
        indicatorsSystemVersionDiffusion.setIsLastVersion(Boolean.FALSE);
        getIndicatorsSystemVersionRepository().save(indicatorsSystemVersionDiffusion);

        // Create draft version
        indicatorsSystemNewVersion.setIndicatorsSystem(indicatorsSystem);
        indicatorsSystemNewVersion = getIndicatorsSystemVersionRepository().save(indicatorsSystemNewVersion);

        // Update indicator with draft version
        indicatorsSystem.setProductionVersion(new IndicatorsSystemVersionInformation(indicatorsSystemNewVersion.getId(), indicatorsSystemNewVersion.getVersionNumber()));
        indicatorsSystem.getVersions().add(indicatorsSystemNewVersion);
        getIndicatorsSystemRepository().save(indicatorsSystem);

        return indicatorsSystemNewVersion;
    }

    @Override
    public IndicatorsSystemVersion retrieveIndicatorsSystemByDimension(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorsSystemByDimension(ctx, uuid, null);

        // Retrieve indicators system version
        Dimension dimension = retrieveDimension(ctx, uuid);
        IndicatorsSystemVersion indicatorsSystemVersion = dimension.getElementLevel().getIndicatorsSystemVersion();
        return indicatorsSystemVersion;
    }

    @Override
    public IndicatorsSystemVersion retrieveIndicatorsSystemByIndicatorInstance(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorsSystemByIndicatorInstance(ctx, uuid, null);

        // Retrieve indicators system version
        IndicatorInstance indicatorInstance = retrieveIndicatorInstance(ctx, uuid);
        IndicatorsSystemVersion indicatorsSystemVersion = indicatorInstance.getElementLevel().getIndicatorsSystemVersion();
        return indicatorsSystemVersion;
    }

    @Override
    public List<IndicatorsSystemHistory> findIndicatorsSystemHistory(ServiceContext ctx, String uuid, int maxResults) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindIndicatorsSystemHistory(uuid, maxResults, null);

        List<IndicatorsSystemHistory> indicatorsSystemHistory = getIndicatorsSystemHistoryRepository().findIndicatorsSystemHistory(uuid, maxResults);

        return indicatorsSystemHistory;
    }

    @Override
    public PagedResult<IndicatorsSystemHistory> findIndicatorsSystemsHistory(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkFindIndicatorsSystemsHistory(conditions, pagingParameter, null);

        // Retrieve last versions
        if (conditions == null) {
            conditions = new ArrayList<ConditionalCriteria>();
        }

        // Find
        return getIndicatorsSystemHistoryRepository().findByCondition(conditions, pagingParameter);
    }

    // --------------------------------------------------------------------------------------------
    // DIMENSION
    // --------------------------------------------------------------------------------------------

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

    @Override
    public void deleteDimension(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkDeleteDimension(uuid, null);

        // Retrieve
        Dimension dimension = retrieveDimension(ctx, uuid);
        ElementLevel elementLevel = dimension.getElementLevel();

        // Delete
        deleteElementLevel(elementLevel);
    }

    @Override
    public List<Dimension> retrieveDimensionsByIndicatorsSystem(ServiceContext ctx, String indicatorsSystemUuid, String indicatorsSystemVersionNumber) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveDimensionsByIndicatorsSystem(indicatorsSystemUuid, indicatorsSystemVersionNumber, null);

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

    // --------------------------------------------------------------------------------------------
    // INDICATOR INSTANCE
    // --------------------------------------------------------------------------------------------

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
    public IndicatorInstance retrieveIndicatorInstancePublishedByCode(ServiceContext ctx, String code) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorInstancePublishedByCode(code, null);

        // Retrieve
        IndicatorInstance indicatorInstance = getIndicatorInstanceRepository().findIndicatorInstancePublishedByCode(code);
        if (indicatorInstance == null) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_INSTANCE_NOT_FOUND_WITH_CODE, code);
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
        deleteElementLevel(elementLevel);
    }

    @Override
    public PagedResult<IndicatorInstance> findIndicatorsInstancesInPublishedIndicatorsSystems(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindIndicatorsInstancesInPublishedIndicatorsSystems(conditions, pagingParameter, null);

        // Retrieve published
        if (conditions == null) {
            conditions = new ArrayList<ConditionalCriteria>();
        }
        conditions.add(ConditionalCriteria.equal(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().procStatus(), IndicatorsSystemProcStatusEnum.PUBLISHED));

        // Find
        return getIndicatorInstanceRepository().findByCondition(conditions, pagingParameter);
    }

    @Override
    public PagedResult<IndicatorInstance> findIndicatorsInstancesInLastVersionIndicatorsSystems(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindIndicatorsInstancesInLastVersionIndicatorsSystems(conditions, pagingParameter, null);

        // Retrieve published
        if (conditions == null) {
            conditions = new ArrayList<ConditionalCriteria>();
        }
        conditions.add(ConditionalCriteria.equal(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().isLastVersion(), Boolean.TRUE));

        // Find
        return getIndicatorInstanceRepository().findByCondition(conditions, pagingParameter);
    }

    @Override
    public List<IndicatorInstance> retrieveIndicatorsInstancesByIndicatorsSystem(ServiceContext ctx, String indicatorsSystemUuid, String indicatorsSystemVersionNumber) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorsInstancesByIndicatorsSystem(indicatorsSystemUuid, indicatorsSystemVersionNumber, null);

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

    private void deleteIndicatorInstanceLastValuesCacheInIndicatorsSystem(IndicatorsSystemVersion indicatorsSystemVersion) {
        for (ElementLevel level : indicatorsSystemVersion.getChildrenAllLevels()) {
            IndicatorInstance instance = level.getIndicatorInstance();
            if (instance != null) {
                getIndicatorInstanceLastValueCacheRepository().deleteWithIndicatorInstance(instance.getUuid());
            }
        }
    }

    private void createIndicatorInstanceLastValuesCacheInIndicatorsSystem(ServiceContext ctx, IndicatorsSystemVersion indicatorsSystemVersion) throws MetamacException {
        for (ElementLevel level : indicatorsSystemVersion.getChildrenAllLevels()) {
            IndicatorInstance instance = level.getIndicatorInstance();
            if (instance != null) {
                getIndicatorsDataService().buildIndicatorInstanceLatestValuesCache(ctx, instance);
            }
        }
    }

    // --------------------------------------------------------------------------------------------
    // GEOGRAPHICAL VALUE
    // --------------------------------------------------------------------------------------------

    @Override
    public GeographicalValue retrieveGeographicalValue(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveGeographicalValue(uuid, null);

        // Retrieve
        GeographicalValue geographicalValue = getGeographicalValueRepository().retrieveGeographicalValue(uuid);
        if (geographicalValue == null) {
            throw new MetamacException(ServiceExceptionType.GEOGRAPHICAL_VALUE_NOT_FOUND, uuid);
        }
        return geographicalValue;
    }

    @Override
    public GeographicalValue retrieveGeographicalValueByCode(ServiceContext ctx, String code) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveGeographicalValueByCode(code, null);

        // Retrieve
        GeographicalValue geographicalValue = getGeographicalValueRepository().findGeographicalValueByCode(code);
        if (geographicalValue == null) {
            throw new MetamacException(ServiceExceptionType.GEOGRAPHICAL_VALUE_NOT_FOUND_WITH_CODE, code);
        }
        return geographicalValue;
    }

    @Override
    public PagedResult<GeographicalValue> findGeographicalValues(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkFindGeographicalValues(null, conditions, pagingParameter);

        // Find
        PagedResult<GeographicalValue> result = getGeographicalValueRepository().findByCondition(conditions, pagingParameter);
        return result;
    }

    @Override
    public GeographicalValue createGeographicalValue(ServiceContext ctx, GeographicalValue geographicalValue) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkCreateGeographicalValue(null, geographicalValue);
        validateGeographicalValueUniqueFields(ctx, geographicalValue);

        // Repository operation
        return getGeographicalValueRepository().save(geographicalValue);
    }

    @Override
    public void deleteGeographicalValue(ServiceContext ctx, String geographicalValueUuid) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkDeleteGeographicalValue(null, geographicalValueUuid);

        // Repository operation
        GeographicalValue geographicalValue = retrieveGeographicalValue(ctx, geographicalValueUuid);
        getGeographicalValueRepository().delete(geographicalValue);
    }

    @Override
    public GeographicalValue updateGeographicalValue(ServiceContext ctx, GeographicalValue geographicalValue) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkUpdateGeographicalValue(null, geographicalValue);
        validateGeographicalValueUniqueFields(ctx, geographicalValue);

        // Repository operation
        return getGeographicalValueRepository().save(geographicalValue);
    }

    private void validateGeographicalValueUniqueFields(ServiceContext ctx, GeographicalValue geographicalValue) throws MetamacException {
        // Prepare criteria
        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);

        List<ConditionalCriteria> conditions = new ArrayList<ConditionalCriteria>();

        // Order and code
        conditions.add(ConditionalCriteria.or(ConditionalCriteria.ignoreCaseEqual(GeographicalValueProperties.code(), geographicalValue.getCode()),
                ConditionalCriteria.ignoreCaseEqual(GeographicalValueProperties.order(), geographicalValue.getOrder())));

        // ID: if necessary if we are updating the entity
        if (geographicalValue.getId() != null) {
            conditions.add(ConditionalCriteria.not(ConditionalCriteria.equal(GeographicalValueProperties.id(), geographicalValue.getId())));
        }

        // Find
        try {
            PagedResult<GeographicalValue> result = getGeographicalValueRepository().findByCondition(conditions, pagingParameter);
            if (result.getValues().size() != 0 && result.getValues().get(0).getCode().equalsIgnoreCase(geographicalValue.getCode())) {
                throw new MetamacException(ServiceExceptionType.GEOGRAPHICAL_VALUE_ALREADY_EXISTS_CODE_DUPLICATED, geographicalValue.getCode());
            } else if (result.getValues().size() != 0 && result.getValues().get(0).getOrder().equalsIgnoreCase(geographicalValue.getOrder())) {
                throw new MetamacException(ServiceExceptionType.GEOGRAPHICAL_VALUE_ALREADY_EXISTS_ORDER_DUPLICATED, geographicalValue.getOrder());
            } else if (result.getValues().size() != 0) {
                throw new MetamacException(ServiceExceptionType.UNKNOWN, "validations of unique fields failed for an unknown reason");
            }
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                String specificException = ((SQLException) e.getCause().getCause()).getMessage();
                if (specificException.contains("GEOGR_VALUES_ORDER")) {
                    throw new MetamacException(ServiceExceptionType.GEOGRAPHICAL_VALUE_ALREADY_EXISTS_ORDER_DUPLICATED, geographicalValue.getOrder());
                } else if (specificException.contains("GEOGR_VALUES_CODE")) {
                    throw new MetamacException(ServiceExceptionType.GEOGRAPHICAL_VALUE_ALREADY_EXISTS_CODE_DUPLICATED, geographicalValue.getCode());
                } else {
                    throw new MetamacException(ServiceExceptionType.UNKNOWN, "validations of unique fields failed for an unknown reason");
                }
            } else {
                throw new MetamacException(e, ServiceExceptionType.UNKNOWN, "validations of unique fields failed for an unknown reason");
            }
        }
    }

    // --------------------------------------------------------------------------------------------
    // GEOGRAPHICAL GRANULARITY
    // --------------------------------------------------------------------------------------------

    @Override
    public PagedResult<GeographicalGranularity> findGeographicalGranularities(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkFindGeographicalGranularities(null, conditions, pagingParameter);

        // Find
        PagedResult<GeographicalGranularity> result = getGeographicalGranularityRepository().findByCondition(conditions, pagingParameter);
        return result;
    }

    @Override
    public GeographicalGranularity retrieveGeographicalGranularity(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveGeographicalGranularity(uuid, null);

        // Retrieve
        GeographicalGranularity geographicalGranularity = getGeographicalGranularityRepository().retrieveGeographicalGranularity(uuid);
        if (geographicalGranularity == null) {
            throw new MetamacException(ServiceExceptionType.GEOGRAPHICAL_GRANULARITY_NOT_FOUND, uuid);
        }
        return geographicalGranularity;
    }

    @Override
    public GeographicalGranularity retrieveGeographicalGranularityByCode(ServiceContext ctx, String code) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveGeographicalGranularityByCode(code, null);

        // Retrieve
        GeographicalGranularity geographicalGranularity = getGeographicalGranularityRepository().findGeographicalGranularityByCode(code);
        if (geographicalGranularity == null) {
            throw new MetamacException(ServiceExceptionType.GEOGRAPHICAL_GRANULARITY_NOT_FOUND_WITH_CODE, code);
        }
        return geographicalGranularity;
    }

    @Override
    public List<GeographicalGranularity> retrieveGeographicalGranularities(ServiceContext ctx) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkRetrieveGeographicalGranularities(null);

        // Find
        return getGeographicalGranularityRepository().findAll();
    }

    @Override
    public GeographicalGranularity createGeographicalGranularity(ServiceContext ctx, GeographicalGranularity geographicalGranularity) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkCreateGeographicalGranularity(null, geographicalGranularity);
        validateGeographicalGranularityCodeUnique(geographicalGranularity);

        // Repository operation
        return getGeographicalGranularityRepository().save(geographicalGranularity);
    }

    @Override
    public GeographicalGranularity updateGeographicalGranularity(ServiceContext ctx, GeographicalGranularity geographicalGranularity) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkUpdateGeographicalGranularity(null, geographicalGranularity);
        validateGeographicalGranularityCodeUnique(geographicalGranularity);

        // Repository operation
        return getGeographicalGranularityRepository().save(geographicalGranularity);
    }

    @Override
    public void deleteGeographicalGranularity(ServiceContext ctx, String geographicalGranularityUuid) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkDeleteGeographicalGranularity(null, geographicalGranularityUuid);

        // Repository operation
        GeographicalGranularity geographicalGranularity = retrieveGeographicalGranularity(ctx, geographicalGranularityUuid);
        getGeographicalGranularityRepository().delete(geographicalGranularity);

    }

    private void validateGeographicalGranularityCodeUnique(GeographicalGranularity geographicalGranularity) throws MetamacException {
        // Prepare criteria
        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);

        List<ConditionalCriteria> conditions = criteriaFor(GeographicalGranularity.class).withProperty(GeographicalGranularityProperties.code()).ignoreCaseEq(geographicalGranularity.getCode())
                .build();

        if (geographicalGranularity.getId() != null) {
            conditions.add(ConditionalCriteria.not(ConditionalCriteria.equal(GeographicalGranularityProperties.id(), geographicalGranularity.getId())));
        }

        // Find
        try {
            PagedResult<GeographicalGranularity> result = getGeographicalGranularityRepository().findByCondition(conditions, pagingParameter);
            if (result.getValues().size() != 0) {
                throw new MetamacException(ServiceExceptionType.GEOGRAPHICAL_GRANULARITY_ALREADY_EXISTS_CODE_DUPLICATED, geographicalGranularity.getCode());
            }
        } catch (DataIntegrityViolationException e) {
            throw new MetamacException(e, ServiceExceptionType.GEOGRAPHICAL_GRANULARITY_ALREADY_EXISTS_CODE_DUPLICATED, geographicalGranularity.getCode());
        }
    }

    // --------------------------------------------------------------------------------------------
    // TIME VALUE
    // --------------------------------------------------------------------------------------------

    @Override
    public TimeValue retrieveTimeValue(ServiceContext ctx, String timeValue) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveTimeValue(timeValue, null);

        // Translate
        String translationCode = TimeVariableUtils.getTimeValueTranslationCode(timeValue);

        Translation translation = getTranslationRepository().findTranslationByCode(translationCode);

        return TimeVariableUtils.buildTimeValue(timeValue, translation);
    }

    // --------------------------------------------------------------------------------------------
    // TIME GRANULARITY
    // --------------------------------------------------------------------------------------------

    @Override
    public List<TimeGranularity> retrieveTimeGranularities(ServiceContext ctx) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkRetrieveTimeGranularities(null);

        List<TimeGranularity> timeGranularities = new ArrayList<TimeGranularity>();

        for (TimeGranularityEnum timeGranularityEnum : TimeGranularityEnum.values()) {
            String translationCode = new StringBuilder().append(IndicatorsConstants.TRANSLATION_TIME_GRANULARITY).append(".").append(timeGranularityEnum.name()).toString();
            Translation translation = getTranslationRepository().findTranslationByCode(translationCode);

            timeGranularities.add(TimeVariableUtils.buildTimeGranularity(timeGranularityEnum, translation));
        }

        return timeGranularities;
    }

    @Override
    public TimeGranularity retrieveTimeGranularity(ServiceContext ctx, TimeGranularityEnum timeGranularity) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveTimeGranularity(timeGranularity, null);

        // Translate
        String translationCode = new StringBuilder().append(IndicatorsConstants.TRANSLATION_TIME_GRANULARITY).append(".").append(timeGranularity.name()).toString();
        Translation translation = getTranslationRepository().findTranslationByCode(translationCode);

        return TimeVariableUtils.buildTimeGranularity(timeGranularity, translation);
    }

    // --------------------------------------------------------------------------------------------
    // MEASURE VALUE
    // --------------------------------------------------------------------------------------------

    @Override
    public MeasureValue retrieveMeasureValue(ServiceContext ctx, MeasureDimensionTypeEnum measureValue) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveMeasureValue(measureValue, null);

        MeasureValue measureValueDo = new MeasureValue();
        measureValueDo.setMeasureValue(measureValue);

        // Translate
        String translationCode = new StringBuilder().append(IndicatorsConstants.TRANSLATION_MEASURE_DIMENSION).append(".").append(measureValue.name()).toString();
        Translation translation = getTranslationRepository().findTranslationByCode(translationCode);
        fillMeasureValueTitlesWithTranslation(measureValueDo, translation);
        return measureValueDo;
    }

    private void fillMeasureValueTitlesWithTranslation(MeasureValue measureValueDo, Translation translation) {
        if (translation == null || translation.getTitle() == null) {
            InternationalString title = new InternationalString();
            String measureValueCode = measureValueDo.getMeasureValue().getName();
            LocalisedString localisedStringEs = new LocalisedString();
            localisedStringEs.setLabel(measureValueCode);
            localisedStringEs.setLocale(IndicatorsConstants.LOCALE_SPANISH);
            title.addText(localisedStringEs);
            LocalisedString localisedStringEn = new LocalisedString();
            localisedStringEn.setLabel(measureValueCode);
            localisedStringEn.setLocale(IndicatorsConstants.LOCALE_ENGLISH);
            title.addText(localisedStringEn);
            measureValueDo.setTitle(title);
            measureValueDo.setTitleSummary(measureValueDo.getTitle());
        } else {
            measureValueDo.setTitle(translateMeasureValue(translation.getTitle()));
            if (translation.getTitleSummary() != null && translation.getTitleSummary().getTexts() != null && translation.getTitleSummary().getTexts().size() != 0) {
                measureValueDo.setTitleSummary(translateMeasureValue(translation.getTitleSummary()));
            } else {
                measureValueDo.setTitleSummary(measureValueDo.getTitle());
            }
        }
    }

    @Override
    public List<MeasureValue> retrieveMeasuresValues(ServiceContext ctx, List<String> measureCodes) {
        List<String> translationCodes = new ArrayList<String>();
        for (String measureCode : measureCodes) {
            String translationCode = new StringBuilder().append(IndicatorsConstants.TRANSLATION_MEASURE_DIMENSION).append(".").append(measureCode).toString();
            translationCodes.add(translationCode);
        }

        Map<String, Translation> translationMap = getTranslationRepository().findTranslationsByCodes(translationCodes);

        List<MeasureValue> measureValues = new ArrayList<MeasureValue>();
        for (String measureCode : measureCodes) {
            String translationCode = new StringBuilder().append(IndicatorsConstants.TRANSLATION_MEASURE_DIMENSION).append(".").append(measureCode).toString();

            MeasureValue measureValueDo = new MeasureValue();
            measureValueDo.setMeasureValue(MeasureDimensionTypeEnum.valueOf(measureCode));

            Translation translation = translationMap.get(translationCode);

            fillMeasureValueTitlesWithTranslation(measureValueDo, translation);
            measureValues.add(measureValueDo);
        }
        return measureValues;
    }

    // --------------------------------------------------------------------------------------------
    // OTHER PRIVATE METHODS
    // --------------------------------------------------------------------------------------------

    /**
     * Checks not exists another indicators system with same code
     */
    private void checkIndicatorsSystemCodeUnique(String code) throws MetamacException {

        // Prepare criteria
        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
        ConditionRoot<IndicatorsSystem> conditionRoot = ConditionalCriteriaBuilder.criteriaFor(IndicatorsSystem.class);
        conditionRoot.withProperty(IndicatorsSystemProperties.code()).ignoreCaseEq(code);
        List<ConditionalCriteria> conditions = conditionRoot.distinctRoot().build();

        // Find
        PagedResult<IndicatorsSystem> result = getIndicatorsSystemRepository().findByCondition(conditions, pagingParameter);
        if (result.getValues().size() != 0) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED, code);
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
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, indicatorsSystemVersion.getIndicatorsSystem().getUuid(), new String[]{
                    ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_DRAFT, ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_VALIDATION_REJECTED,
                    ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_PRODUCTION_VALIDATION, ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_DIFFUSION_VALIDATION});
        }
    }

    /**
     * Retrieves version of an indicators system in production
     */
    private IndicatorsSystemVersion retrieveIndicatorsSystemProcStatusInProduction(ServiceContext ctx, String uuid, boolean throwsExceptionIfNotExistsInProduction) throws MetamacException {
        IndicatorsSystem indicatorsSystem = retrieveIndicatorsSystem(uuid);
        if (indicatorsSystem.getProductionVersion() == null && !throwsExceptionIfNotExistsInProduction) {
            // to throws an specific exception
            return null;
        }
        if (indicatorsSystem.getProductionVersion() == null) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_IN_PRODUCTION_NOT_FOUND, uuid);
        }
        return retrieveIndicatorsSystem(ctx, uuid, indicatorsSystem.getProductionVersion().getVersionNumber());
    }

    /**
     * Retrieves version of an indicators system in diffusion
     */
    private IndicatorsSystemVersion retrieveIndicatorsSystemProcStatusInDiffusion(ServiceContext ctx, String uuid, boolean throwsExceptionIfNotExistsInDiffusion) throws MetamacException {
        IndicatorsSystem indicatorsSystem = retrieveIndicatorsSystem(uuid);
        if (indicatorsSystem.getDiffusionVersion() == null && !throwsExceptionIfNotExistsInDiffusion) {
            // to throws an specific exception
            return null;
        }
        if (indicatorsSystem.getDiffusionVersion() == null) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_IN_DIFFUSION_NOT_FOUND, uuid);
        }
        return retrieveIndicatorsSystem(ctx, uuid, indicatorsSystem.getDiffusionVersion().getVersionNumber());
    }

    /**
     * Makes validations to sent to production validation
     * Checks actual processing status and if it is correct checks conditions to send to production validation
     */
    private void checkIndicatorsSystemToSendToProductionValidation(String uuid, IndicatorsSystemVersion indicatorsSystemVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        if (indicatorsSystemVersion == null
                || (!IndicatorsSystemProcStatusEnum.DRAFT.equals(indicatorsSystemVersion.getProcStatus()) && !IndicatorsSystemProcStatusEnum.VALIDATION_REJECTED.equals(indicatorsSystemVersion
                        .getProcStatus()))) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, uuid, new String[]{ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_DRAFT,
                    ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_VALIDATION_REJECTED}));
        } else {
            // Check other conditions
            checkConditionsToSendToProductionValidation(indicatorsSystemVersion, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to reject production validation
     * Checks actual processing status
     */
    private void checkIndicatorsSystemToRejectProductionValidation(String uuid, IndicatorsSystemVersion indicatorsSystemVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        if (indicatorsSystemVersion == null || !IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION.equals(indicatorsSystemVersion.getProcStatus())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, uuid,
                    new String[]{ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_PRODUCTION_VALIDATION}));
        } else {
            // Nothing more to check
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to sent to diffusion validation
     * Checks actual processing status and if it is correct checks same conditions to send to production validation
     */
    private void checkIndicatorsSystemToSendToDiffusionValidation(String uuid, IndicatorsSystemVersion indicatorsSystemVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        if (indicatorsSystemVersion == null || !IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION.equals(indicatorsSystemVersion.getProcStatus())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, uuid,
                    new String[]{ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_PRODUCTION_VALIDATION}));
        } else {
            // Check other conditions
            checkConditionsToSendToProductionValidation(indicatorsSystemVersion, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to reject diffusion validation
     * Checks actual processing status
     */
    private void checkIndicatorsSystemToRejectDiffusionValidation(String uuid, IndicatorsSystemVersion indicatorsSystemVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        if (indicatorsSystemVersion == null || !IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION.equals(indicatorsSystemVersion.getProcStatus())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, uuid,
                    new String[]{ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_DIFFUSION_VALIDATION}));
        } else {
            // Nothing more to check
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to publish
     * Checks actual processing status and if it is correct checks same conditions to send to production validation and additional conditions to publish
     */
    private void checkIndicatorsSystemToPublish(String uuid, IndicatorsSystemVersion indicatorsSystemVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        if (indicatorsSystemVersion == null || !IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION.equals(indicatorsSystemVersion.getProcStatus())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, uuid,
                    new String[]{ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_DIFFUSION_VALIDATION}));
        } else {
            // Check other conditions
            checkConditionsToPublish(indicatorsSystemVersion, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to archive
     * Checks actual processing status
     */
    private void checkIndicatorsSystemToArchive(String uuid, IndicatorsSystemVersion indicatorsSystemVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        if (indicatorsSystemVersion == null || !IndicatorsSystemProcStatusEnum.PUBLISHED.equals(indicatorsSystemVersion.getProcStatus())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, uuid, new String[]{ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_PUBLISHED}));
        } else {
            // nothing more to check
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * - Check exists at least one indicator instance
     */
    private void checkConditionsToSendToProductionValidation(IndicatorsSystemVersion indicatorsSystemVersion, List<MetamacExceptionItem> exceptions) {

        // Check exists at least one indicator instance
        String uuid = indicatorsSystemVersion.getIndicatorsSystem().getUuid();
        String versionNumber = indicatorsSystemVersion.getVersionNumber();
        if (!getIndicatorInstanceRepository().existAnyIndicatorInstance(uuid, versionNumber)) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.INDICATORS_SYSTEM_MUST_HAVE_INDICATOR_INSTANCE, uuid));
        }
    }

    /**
     * - Validations when send to production validation
     * - Checks linked indicators are published
     */
    private void checkConditionsToPublish(IndicatorsSystemVersion indicatorsSystemVersion, List<MetamacExceptionItem> exceptions) {

        // Conditions to send to production validation
        checkConditionsToSendToProductionValidation(indicatorsSystemVersion, exceptions);

        // All linked indicators have one version published
        List<String> indicatorsUuid = getIndicatorInstanceRepository().findIndicatorsLinkedWithIndicatorsSystemVersion(indicatorsSystemVersion.getId());
        List<String> indicatorsNotPublishedUuid = getIndicatorRepository().filterIndicatorsNotPublished(indicatorsUuid);
        if (indicatorsNotPublishedUuid.size() != 0) {
            String[] indicatorsNotPublishedUuidArray = indicatorsNotPublishedUuid.toArray(new String[indicatorsNotPublishedUuid.size()]);
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.INDICATORS_SYSTEM_MUST_HAVE_ALL_INDICATORS_PUBLISHED, indicatorsSystemVersion.getIndicatorsSystem().getUuid(),
                    indicatorsNotPublishedUuidArray));
        }
    }

    /**
     * Create element level: dimension or indicator instance
     */
    private ElementLevel createElementLevel(ServiceContext ctx, String indicatorsSystemUuid, ElementLevel elementLevel) throws MetamacException {

        // Retrieve indicators system version and check it is in production
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorsSystemProcStatusInProduction(ctx, indicatorsSystemUuid, true);

        if (elementLevel.getParent() == null) {
            // Add to first level in indicators system

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
            updateIndicatorsSystemElementsOrdersInLevelAddingElement(indicatorsSystemVersion.getChildrenFirstLevel(), elementLevel);
        } else {
            // Add to parent element

            ElementLevel elementLevelParent = elementLevel.getParent();

            // Check dimension parent belogs to indicators system provided
            IndicatorsSystemVersion indicatorsSystemVersionOfDimensionParent = elementLevelParent.getIndicatorsSystemVersion();
            if (!indicatorsSystemVersionOfDimensionParent.getIndicatorsSystem().getUuid().equals(indicatorsSystemUuid)) {
                throw new MetamacException(ServiceExceptionType.DIMENSION_NOT_FOUND_IN_INDICATORS_SYSTEM, elementLevel.getParentUuid(), indicatorsSystemUuid);
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
            updateIndicatorsSystemElementsOrdersInLevelAddingElement(elementLevelParent.getChildren(), elementLevel);

            // Update indicators system adding element to all children
            indicatorsSystemVersion.addChildrenAllLevel(elementLevel);
            indicatorsSystemVersion = getIndicatorsSystemVersionRepository().save(indicatorsSystemVersion);
        }
        return elementLevel;
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
                updateIndicatorsSystemElementsOrdersInLevelRemovingElement(parentActual.getChildren(), elementLevel, orderInLevelBefore);
            } else {
                // Update order of other dimensions
                elementLevel.setIndicatorsSystemVersionFirstLevel(null);
                updateIndicatorsSystemElementsOrdersInLevelRemovingElement(indicatorsSystemVersion.getChildrenFirstLevel(), elementLevel, orderInLevelBefore);
            }

            // Update target parent, adding dimension
            List<ElementLevel> elementsInLevel = null;
            if (parentTarget == null) {
                indicatorsSystemVersion.addChildrenFirstLevel(elementLevel);
                indicatorsSystemVersion = getIndicatorsSystemVersionRepository().save(indicatorsSystemVersion);
                elementsInLevel = indicatorsSystemVersion.getChildrenFirstLevel();
            } else {
                parentTarget.addChildren(elementLevel);
                updateElementLevel(parentTarget);
                elementsInLevel = parentTarget.getChildren();
            }
            // Check order is correct and update orders
            updateIndicatorsSystemElementsOrdersInLevelAddingElement(elementsInLevel, elementLevel);

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
            updateIndicatorsSystemElementsOrdersInLevelChangingOrder(elementsInLevel, elementLevel, orderInLevelBefore, elementLevel.getOrderInLevel());
            elementLevel = updateElementLevel(elementLevel);
        }

        return elementLevel;
    }

    private void deleteElementLevel(ElementLevel elementLevel) throws MetamacException {

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
        updateIndicatorsSystemElementsOrdersInLevelRemovingElement(elementsAtLevel, elementLevel, elementLevel.getOrderInLevel());

        // Delete
        getElementLevelRepository().delete(elementLevel);
    }

    private void updateIndicatorsSystemElementsOrdersInLevelAddingElement(List<ElementLevel> elementsAtLevel, ElementLevel elementToAdd) throws MetamacException {

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
                // order incorrect
                break;
            }
        }

        // Checks orders
        if (!orders.isEmpty()) {
            if (elementToAdd.isDimension()) {
                throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.DIMENSION_ORDER_IN_LEVEL);
            } else {
                throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.INDICATOR_INSTANCE_ORDER_IN_LEVEL);
            }
        }
    }

    private void updateIndicatorsSystemElementsOrdersInLevelRemovingElement(List<ElementLevel> elementsAtLevel, ElementLevel elementToRemove, Long orderBeforeUpdate) throws MetamacException {
        for (ElementLevel elementInLevel : elementsAtLevel) {
            if (elementInLevel.getElementUuid().equals(elementToRemove.getElementUuid())) {
                // nothing
            } else if (elementInLevel.getOrderInLevel() > orderBeforeUpdate) {
                elementInLevel.setOrderInLevel(elementInLevel.getOrderInLevel() - 1);
                updateElementLevel(elementInLevel);
            }
        }
    }

    private void updateIndicatorsSystemElementsOrdersInLevelChangingOrder(List<ElementLevel> elementsAtLevel, ElementLevel elementToChangeOrder, Long orderBeforeUpdate, Long orderAfterUpdate)
            throws MetamacException {

        // Checks orders
        if (orderAfterUpdate > elementsAtLevel.size()) {
            if (elementToChangeOrder.isDimension()) {
                throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.DIMENSION_ORDER_IN_LEVEL);
            } else {
                throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.INDICATOR_INSTANCE_ORDER_IN_LEVEL);
            }
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

        // Check dimension parent belongs to indicators system provided
        if (!dimension.getElementLevel().getIndicatorsSystemVersion().getUuid().equals(dimensionTarget.getElementLevel().getIndicatorsSystemVersion().getUuid())) {
            throw new MetamacException(ServiceExceptionType.DIMENSION_NOT_FOUND_IN_INDICATORS_SYSTEM, parentTargetUuid, dimension.getElementLevel().getIndicatorsSystemVersion().getUuid());
        }

        ElementLevel dimensionParent = dimensionTarget.getElementLevel().getParent();
        while (dimensionParent != null) {
            if (dimensionParent.isDimension() && dimensionParent.getElementUuid().equals(dimension.getUuid())) {
                throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.DIMENSION_PARENT_UUID);
            }
            dimensionParent = dimensionParent.getParent();
        }
    }

    private IndicatorsSystem retrieveIndicatorsSystem(String uuid) throws MetamacException {

        IndicatorsSystem indicatorsSystem = getIndicatorsSystemRepository().retrieveIndicatorsSystem(uuid);
        if (indicatorsSystem == null) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND, uuid);
        }
        return indicatorsSystem;
    }

    private ElementLevel updateElementLevel(ElementLevel elementLevel) throws MetamacException {
        return getElementLevelRepository().save(elementLevel);
    }

    private InternationalString translateMeasureValue(InternationalString sourceTranslation) {
        InternationalString target = new InternationalString();
        for (LocalisedString localisedStringTranslation : sourceTranslation.getTexts()) {
            LocalisedString localisedString = new LocalisedString();
            localisedString.setLabel(localisedStringTranslation.getLabel());
            localisedString.setLocale(localisedStringTranslation.getLocale());
            target.addText(localisedString);
        }
        return target;
    }
}
