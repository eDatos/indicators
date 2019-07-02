package es.gobcan.istac.indicators.core.serviceimpl;

import static es.gobcan.istac.indicators.core.util.IndicatorsVersionUtils.setVersionNumber;
import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.ConditionRoot;
import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.dataset.repository.dto.DatasetRepositoryDto;
import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorProperties;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionProperties;
import es.gobcan.istac.indicators.core.domain.Quantity;
import es.gobcan.istac.indicators.core.domain.QuantityUnit;
import es.gobcan.istac.indicators.core.domain.Subject;
import es.gobcan.istac.indicators.core.domain.SubjectRepository;
import es.gobcan.istac.indicators.core.domain.UnitMultiplier;
import es.gobcan.istac.indicators.core.domain.UnitMultiplierProperties;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorProcStatusEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionParameters;
import es.gobcan.istac.indicators.core.error.ServiceExceptionParametersInternal;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.repositoryimpl.finders.SubjectIndicatorResult;
import es.gobcan.istac.indicators.core.service.NoticesRestInternalService;
import es.gobcan.istac.indicators.core.serviceimpl.util.DoCopyUtils;
import es.gobcan.istac.indicators.core.serviceimpl.util.InvocationValidator;
import es.gobcan.istac.indicators.core.serviceimpl.util.PublishIndicatorResult;
import es.gobcan.istac.indicators.core.util.IndicatorsVersionUtils;

/**
 * Implementation of IndicatorsService
 */
@Service("indicatorsService")
public class IndicatorsServiceImpl extends IndicatorsServiceImplBase {

    @Autowired(required = false)
    private SubjectRepository              subjectRepository;

    @Autowired
    private IndicatorsConfigurationService indicatorsConfigurationService;

    private static final Logger            LOG = LoggerFactory.getLogger(IndicatorsServiceImpl.class);

    @Override
    public IndicatorVersion createIndicator(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {

        Indicator indicator = indicatorVersion.getIndicator();

        // Validation of parameters
        InvocationValidator.checkCreateIndicator(indicatorVersion, null);
        checkIndicatorCodeUnique(ctx, indicator.getCode());
        checkIndicatorViewCodeUnique(ctx, indicator.getViewCode());

        // Save indicator
        indicator.setDiffusionIdIndicatorVersion(null);
        indicator.setDiffusionVersionNumber(null);
        indicator.setDiffusionProcStatus(null);
        indicator.setIsPublished(Boolean.FALSE);
        indicator.setNotifyPopulationErrors(Boolean.TRUE);
        indicator = getIndicatorRepository().save(indicator);

        // Save draft version
        indicatorVersion.setProcStatus(IndicatorProcStatusEnum.DRAFT);
        indicatorVersion.setIsLastVersion(Boolean.TRUE);
        indicatorVersion.setNeedsUpdate(Boolean.FALSE);
        indicatorVersion.setVersionNumber(IndicatorsVersionUtils.INITIAL_VERSION);
        indicatorVersion.setIndicator(indicator);
        indicatorVersion = getIndicatorVersionRepository().save(indicatorVersion);

        // Update indicator with draft version
        indicator.setProductionIdIndicatorVersion(indicatorVersion.getId());
        indicator.setProductionVersionNumber(indicatorVersion.getVersionNumber());
        indicator.setProductionProcStatus(indicatorVersion.getProcStatus());

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
            versionNumber = indicator.getProductionVersionNumber() != null ? indicator.getProductionVersionNumber() : indicator.getDiffusionVersionNumber();
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
            throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, uuid, new String[]{ServiceExceptionParameters.INDICATOR_PROC_STATUS_PUBLISHED});
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
                        new String[]{ServiceExceptionParameters.INDICATOR_PROC_STATUS_PUBLISHED});
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
        Indicator indicator = indicatorVersion.getIndicator();

        // Delete whole indicator or only last version
        if (IndicatorsVersionUtils.isInitialVersion(indicatorVersion.getVersionNumber())) {
            // If indicator is not published or archived, delete whole indicator

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
            if (StringUtils.isNotBlank(indicator.getViewCode())) {
                getIndicatorsDataService().deleteDatabaseView(ctx, indicator.getViewCode());
            }

        } else {
            indicator.getVersions().remove(indicatorVersion);
            indicator.setProductionIdIndicatorVersion(null);
            indicator.setProductionVersionNumber(null);
            indicator.setProductionProcStatus(null);
            // another version is now last version
            indicator.getVersions().get(0).setIsLastVersion(Boolean.TRUE);

            // Update
            indicator = getIndicatorRepository().save(indicator);
            getIndicatorVersionRepository().delete(indicatorVersion);

            // Update view for the last version of the indicator
            getIndicatorsDataService().manageDatabaseViewForLastVersion(ctx, indicator.getVersions().get(0));
        }

        if (indicatorVersion.getDataRepositoryId() != null) {
            getIndicatorsDataService().deleteDatasetRepository(ctx, indicatorVersion.getDataRepositoryId());
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
    public String exportIndicatorsTsv(ServiceContext ctx, List<ConditionalCriteria> conditions) throws MetamacException {
        LOG.info("Exporting indicators");

        // Validation of parameters
        InvocationValidator.checkExportIndicators(conditions, null);

        List<IndicatorVersion> indicatorsVersions = findIndicators(ctx, conditions, PagingParameter.noLimits()).getValues();

        List<String> languages = indicatorsConfigurationService.retrieveLanguages();

        // Export
        OutputStream outputStream = null;
        OutputStreamWriter writer = null;

        try {
            File file = File.createTempFile("indicators", ".tsv");
            outputStream = new FileOutputStream(file);
            writer = new OutputStreamWriter(outputStream, IndicatorsConstants.TSV_EXPORTATION_ENCODING);

            writeHeader(writer, languages);

            for (IndicatorVersion indicatorVersion : indicatorsVersions) {
                Indicator indicator = indicatorVersion.getIndicator();

                if (indicator != null) {
                    writer.write(IndicatorsConstants.TSV_LINE_SEPARATOR);

                    writer.write(indicator.getCode());
                    writeCell(writer, indicator.getNotifyPopulationErrors());

                    if (indicator.getProductionIndicatorVersion() == null) {
                        writeIndicatorVersion(writer, indicator.getDiffusionIndicatorVersion(), languages);
                    } else {
                        writeIndicatorVersion(writer, indicator.getProductionIndicatorVersion(), languages);
                    }

                    writeIndicatorVersion(writer, indicator.getDiffusionIndicatorVersion(), languages);
                } else {
                    LOG.warn("Indicator is null for indicatorVersion ", indicatorVersion);
                }
            }

            writer.flush();
            return file.getName();
        } catch (Exception e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.EXPORTATION_TSV_ERROR).withMessageParameters(e).build();
        } finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(writer);
        }
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
        indicatorInProduction.getIndicator().setProductionProcStatus(indicatorInProduction.getProcStatus()); // Para permitir busquedas y ordenamientos
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
        indicatorInProduction.getIndicator().setProductionProcStatus(indicatorInProduction.getProcStatus()); // Para permitir busquedas y ordenamientos
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
        indicatorInProduction.getIndicator().setProductionProcStatus(indicatorInProduction.getProcStatus()); // Para permitir busquedas y ordenamientos
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
        indicatorInProduction.getIndicator().setProductionProcStatus(indicatorInProduction.getProcStatus()); // Para permitir busquedas y ordenamientos
        indicatorInProduction = getIndicatorVersionRepository().save(indicatorInProduction);

        return indicatorInProduction;
    }

    @Override
    public PublishIndicatorResult publishIndicator(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkPublishIndicator(uuid, null);

        // Retrieve version in diffusion validation
        IndicatorVersion indicatorInProduction = retrieveIndicatorProcStatusInProduction(ctx, uuid, false);

        // Retrieve version published that have to ve removed after published the new versión
        IndicatorVersion indicatorPreviouslyPublished = retrieveIndicatorProcStatusInDiffusion(ctx, uuid, false);

        // Validate to publish
        checkIndicatorToPublish(ctx, uuid, indicatorInProduction);

        // Populate data
        try {
            getIndicatorsDataService().populateIndicatorVersionData(ctx, uuid, indicatorInProduction.getVersionNumber());
        } catch (MetamacException e) {
            LOG.warn("Error populating indicator with UUID: " + uuid, e);
            indicatorInProduction.setProcStatus(IndicatorProcStatusEnum.PUBLICATION_FAILED);
            indicatorInProduction.setPublicationFailedDate(new DateTime());
            indicatorInProduction.setPublicationUser(ctx.getUserId());

            indicatorInProduction.getIndicator().setProductionProcStatus(indicatorInProduction.getProcStatus()); // Para permitir ordenamientos y busquedas

            indicatorInProduction = getIndicatorVersionRepository().save(indicatorInProduction);
            return new PublishIndicatorResult(indicatorInProduction, e);
        }

        tryRefreshSubjectTitle(indicatorInProduction);

        // Update indicator version metadata
        indicatorInProduction.setProcStatus(IndicatorProcStatusEnum.PUBLISHED);
        indicatorInProduction.setPublicationDate(new DateTime());
        indicatorInProduction.setPublicationUser(ctx.getUserId());
        indicatorInProduction.setPublicationFailedDate(null);
        indicatorInProduction.setPublicationFailedUser(null);
        indicatorInProduction = getIndicatorVersionRepository().save(indicatorInProduction);
        Indicator indicator = indicatorInProduction.getIndicator();

        // Remove possible last version in diffusion
        if (indicatorPreviouslyPublished != null) {
            try {
                getIndicatorsDataService().deleteIndicatorVersionData(ctx, uuid, indicatorPreviouslyPublished.getVersionNumber());
            } catch (MetamacException e) {
                LOG.warn("Dataset datasetId:" + indicatorPreviouslyPublished.getDataRepositoryId() + " for indicator version with uuid:" + uuid + " and version:"
                        + indicator.getDiffusionVersionNumber() + " could not be deleted", e);
            }
            indicator.getVersions().remove(indicatorPreviouslyPublished);
            getIndicatorRepository().save(indicator);
            getIndicatorVersionRepository().delete(indicatorPreviouslyPublished);

        }

        // Update indicator metadata
        indicator.setIsPublished(Boolean.TRUE);
        indicator.setDiffusionIdIndicatorVersion(indicatorInProduction.getId());
        indicator.setDiffusionVersionNumber(indicatorInProduction.getVersionNumber());
        indicator.setDiffusionProcStatus(indicatorInProduction.getProcStatus());
        indicator.setProductionIdIndicatorVersion(null);
        indicator.setProductionVersionNumber(null);
        indicator.setProductionProcStatus(null);
        getIndicatorRepository().save(indicator);

        return new PublishIndicatorResult(indicatorInProduction);
    }

    private void tryRefreshSubjectTitle(IndicatorVersion indicatorVersion) {
        try {
            Subject subject = subjectRepository.retrieveSubject(indicatorVersion.getSubjectCode());
            InternationalString title = new InternationalString();
            LocalisedString localised = new LocalisedString();
            localised.setLabel(subject.getTitle());
            localised.setLocale(IndicatorsConstants.LOCALE_SPANISH);
            title.addText(localised);
            indicatorVersion.setSubjectTitle(title);
            LOG.info("Subject title successfully refreshed for indicator: " + indicatorVersion.getUuid() + " version: " + indicatorVersion.getVersionNumber());
        } catch (Exception e) {
            LOG.warn("Can not update the subject title for subject code: " + indicatorVersion.getSubjectCode() + " for indicator: " + indicatorVersion.getUuid() + " version "
                    + indicatorVersion.getVersionNumber(), e);
        }
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
        indicator.setDiffusionProcStatus(IndicatorProcStatusEnum.ARCHIVED);
        getIndicatorRepository().save(indicator);

        indicatorInDiffusion.setProcStatus(IndicatorProcStatusEnum.ARCHIVED);
        indicatorInDiffusion.setArchiveDate(new DateTime());
        indicatorInDiffusion.setArchiveUser(ctx.getUserId());
        indicatorInDiffusion = getIndicatorVersionRepository().save(indicatorInDiffusion);

        try {
            getIndicatorsDataService().deleteIndicatorVersionData(ctx, indicator.getUuid(), indicatorInDiffusion.getVersionNumber());
        } catch (MetamacException e) {
            LOG.warn("Data could not be deleted for indicator version just archived uuid:" + indicator.getUuid() + " version:" + indicatorInDiffusion.getVersionNumber(), e);
        }

        return indicatorInDiffusion;
    }

    @Override
    public IndicatorVersion versioningIndicator(ServiceContext ctx, String uuid, VersionTypeEnum versionType) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkVersioningIndicator(uuid, versionType, null);

        // Retrieve
        Indicator indicator = retrieveIndicator(ctx, uuid);
        if (indicator.getProductionVersionNumber() != null) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, uuid,
                    new String[]{ServiceExceptionParameters.INDICATOR_PROC_STATUS_PUBLISHED, ServiceExceptionParameters.INDICATOR_PROC_STATUS_ARCHIVED});
        }

        // Initialize new version, copying values of version in diffusion
        IndicatorVersion indicatorVersionDiffusion = retrieveIndicatorProcStatusInDiffusion(ctx, uuid, true);
        IndicatorVersion indicatorNewVersion = DoCopyUtils.copy(indicatorVersionDiffusion);
        indicatorNewVersion.setProcStatus(IndicatorProcStatusEnum.DRAFT);

        setVersionNumber(indicatorNewVersion, indicatorVersionDiffusion.getVersionNumber(), versionType, getNoticesRestInternalService(), indicatorVersionDiffusion.getIndicator().getCode());

        indicatorNewVersion.setIsLastVersion(Boolean.TRUE);
        indicatorNewVersion.setNeedsUpdate(Boolean.TRUE);

        // Update diffusion version
        indicatorVersionDiffusion.setIsLastVersion(Boolean.FALSE);
        getIndicatorVersionRepository().save(indicatorVersionDiffusion);

        // Create draft version
        indicatorNewVersion.setIndicator(indicator);
        indicatorNewVersion = getIndicatorVersionRepository().save(indicatorNewVersion);

        // Update indicator with draft version
        indicator.setProductionIdIndicatorVersion(indicatorNewVersion.getId());
        indicator.setProductionVersionNumber(indicatorNewVersion.getVersionNumber());
        indicator.setProductionProcStatus(indicatorNewVersion.getProcStatus());

        indicator.getVersions().add(indicatorNewVersion);
        getIndicatorRepository().save(indicator);

        // Populate indicator data
        String indicatorsUuid = indicatorNewVersion.getIndicator().getUuid();
        String indicatorVersionNumber = indicatorNewVersion.getVersionNumber();

        try {
            getIndicatorsDataService().populateIndicatorVersionData(ctx, indicatorsUuid, indicatorVersionNumber);
        } catch (MetamacException e) {
            createDatasetRepositoryAndUpdateViewReference(ctx, indicatorNewVersion, indicatorsUuid, indicatorVersionNumber);
        }
        indicatorNewVersion = retrieveIndicator(ctx, indicatorsUuid, indicatorVersionNumber);

        return indicatorNewVersion;
    }

    @Override
    public void disableNotifyPopulationErrors(ServiceContext ctx, String indicatorUuid) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkDisableNotifyPopulationErrors(indicatorUuid, null);

        // Retrieve
        Indicator indicator = retrieveIndicator(ctx, indicatorUuid);

        // Set notifyPopulationErros
        indicator.setNotifyPopulationErrors(Boolean.FALSE);

        // Update
        updateIndicator(ctx, indicator);
    }

    @Override
    public void enableNotifyPopulationErrors(ServiceContext ctx, String indicatorUuid) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkEnableNotifyPopulationErrors(indicatorUuid, null);

        // Retrieve
        Indicator indicator = retrieveIndicator(ctx, indicatorUuid);

        // Set notifyPopulationErros
        indicator.setNotifyPopulationErrors(Boolean.TRUE);

        // Update
        updateIndicator(ctx, indicator);
    }

    private void createDatasetRepositoryAndUpdateViewReference(ServiceContext ctx, IndicatorVersion indicatorNewVersion, String indicatorsUuid, String indicatorVersionNumber) throws MetamacException {
        // If we can not populate data because the datasource has been deleted we have to create the table and change the view relation
        DatasetRepositoryDto datasetRepositoryDto = getIndicatorsDataService().createDatasetRepositoryDefinition(ctx, indicatorsUuid, indicatorVersionNumber);
        getIndicatorsDataService().setDatasetRepositoryDeleteOldOne(ctx, indicatorNewVersion, datasetRepositoryDto);
        getIndicatorsDataService().manageDatabaseViewForLastVersion(ctx, indicatorNewVersion);

        // We set that data needs update
        indicatorNewVersion.setNeedsUpdate(Boolean.TRUE);
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
        indicatorVersion.setNeedsUpdate(Boolean.TRUE);
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
        indicatorVersion.setNeedsUpdate(Boolean.TRUE);
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

        indicatorVersion.setNeedsUpdate(Boolean.TRUE);
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

    /**
     * This operation retrieve subject from table view. Won't be accesible in public web application.
     */
    @Override
    public Subject retrieveSubject(ServiceContext ctx, String code) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveSubject(code, null);

        // Retrieve
        Subject subject = subjectRepository.retrieveSubject(code);
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
        List<Subject> subjects = subjectRepository.findSubjects();
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
     * This operation retrieves subjects from indicators table
     */
    @Override
    public List<SubjectIndicatorResult> retrieveSubjectsInLastVersionIndicators(ServiceContext ctx) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveSubjectsInLastVersionIndicators(null);

        // Find
        List<SubjectIndicatorResult> subjects = getIndicatorVersionRepository().findSubjectsInLastVersionIndicators();
        return subjects;
    }

    // --------------------------------------------------------------------------------------------
    // QUANTITY UNITS
    // --------------------------------------------------------------------------------------------

    @Override
    public PagedResult<QuantityUnit> findQuantityUnits(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkFindQuantityUnits(null, conditions, pagingParameter);

        // Find
        PagedResult<QuantityUnit> result = getQuantityUnitRepository().findByCondition(conditions, pagingParameter);
        return result;
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

    @Override
    public QuantityUnit createQuantityUnit(ServiceContext ctx, QuantityUnit quantityUnit) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkCreateQuantityUnit(null, quantityUnit);

        // Repository operation
        return getQuantityUnitRepository().save(quantityUnit);
    }

    @Override
    public QuantityUnit updateQuantityUnit(ServiceContext ctx, QuantityUnit quantityUnit) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkUpdateQuantityUnit(null, quantityUnit);

        // Repository operation
        return getQuantityUnitRepository().save(quantityUnit);
    }

    @Override
    public void deleteQuantityUnit(ServiceContext ctx, String quantityUnitUuid) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkDeleteQuantityUnit(null, quantityUnitUuid);

        // Repository operation
        QuantityUnit quantityUnit = retrieveQuantityUnit(ctx, quantityUnitUuid);
        getQuantityUnitRepository().delete(quantityUnit);
    }

    // --------------------------------------------------------------------------------------------
    // UNIT MULTIPLIER
    // --------------------------------------------------------------------------------------------

    @Override
    public PagedResult<UnitMultiplier> findUnitMultipliers(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkFindUnitMultipliers(null, conditions, pagingParameter);

        // Find
        PagedResult<UnitMultiplier> result = getUnitMultiplierRepository().findByCondition(conditions, pagingParameter);
        return result;
    }

    @Override
    public UnitMultiplier retrieveUnitMultiplier(ServiceContext ctx, Integer unitMultiplierValue) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkRetrieveUnitMultiplier(unitMultiplierValue, null);

        // Retrieve
        UnitMultiplier unitMultiplier = getUnitMultiplierRepository().retrieveUnitMultiplier(unitMultiplierValue);
        if (unitMultiplier == null) {
            throw new MetamacException(ServiceExceptionType.UNIT_MULTIPLIER_NOT_FOUND, unitMultiplierValue);
        }
        return unitMultiplier;
    }

    @Override
    public UnitMultiplier retrieveUnitMultiplier(ServiceContext ctx, String unitMultiplierUuid) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkRetrieveUnitMultiplier(unitMultiplierUuid, null);

        // Retrieve
        UnitMultiplier unitMultiplier = getUnitMultiplierRepository().retrieveUnitMultiplier(unitMultiplierUuid);
        if (unitMultiplier == null) {
            throw new MetamacException(ServiceExceptionType.UNIT_MULTIPLIER_NOT_FOUND_UUID, unitMultiplierUuid);
        }
        return unitMultiplier;

    }

    @Override
    public List<UnitMultiplier> retrieveUnitsMultipliers(ServiceContext ctx) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkRetrieveUnitsMultipliers(null);

        // Find
        List<UnitMultiplier> unitsMultipliers = getUnitMultiplierRepository().findAllOrdered();
        return unitsMultipliers;
    }

    @Override
    public UnitMultiplier createUnitMultiplier(ServiceContext ctx, UnitMultiplier unitMultiplier) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkCreateUnitMultiplier(null, unitMultiplier);
        validateUnitMultiplierValueUnique(ctx, unitMultiplier);

        // Repository operation
        return getUnitMultiplierRepository().save(unitMultiplier);
    }

    @Override
    public UnitMultiplier updateUnitMultiplier(ServiceContext ctx, UnitMultiplier unitMultiplier) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkUpdateUnitMultiplier(null, unitMultiplier);
        validateUnitMultiplierValueUnique(ctx, unitMultiplier);

        // Repository operation
        return getUnitMultiplierRepository().save(unitMultiplier);
    }

    @Override
    public void deleteUnitMultiplier(ServiceContext ctx, String unitMultiplierUuid) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkDeleteUnitMultiplier(null, unitMultiplierUuid);

        // Repository operation
        UnitMultiplier unitMultiplier = retrieveUnitMultiplier(ctx, unitMultiplierUuid);
        getUnitMultiplierRepository().delete(unitMultiplier);

    }

    private void validateUnitMultiplierValueUnique(ServiceContext ctx, UnitMultiplier unitMultiplier) throws MetamacException {
        // Prepare criteria
        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);

        List<ConditionalCriteria> conditions = criteriaFor(UnitMultiplier.class).withProperty(UnitMultiplierProperties.unitMultiplier()).eq(unitMultiplier.getUnitMultiplier()).build();

        if (unitMultiplier.getId() != null) {
            conditions.add(ConditionalCriteria.not(ConditionalCriteria.equal(UnitMultiplierProperties.id(), unitMultiplier.getId())));
        }

        // Find
        try {
            PagedResult<UnitMultiplier> result = getUnitMultiplierRepository().findByCondition(conditions, pagingParameter);
            if (result.getValues().size() != 0) {
                throw new MetamacException(ServiceExceptionType.UNIT_MULTIPLIER_ALREADY_EXISTS_VALUE_DUPLICATED, unitMultiplier.getUnitMultiplier());
            }
        } catch (DataIntegrityViolationException e) {
            throw new MetamacException(e, ServiceExceptionType.UNIT_MULTIPLIER_ALREADY_EXISTS_VALUE_DUPLICATED, unitMultiplier.getUnitMultiplier());
        }
    }

    // --------------------------------------------------------------------------------------------
    // OTHER PRIVATE METHODS
    // --------------------------------------------------------------------------------------------

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
     * Checks not exists another indicator with same view code
     */
    private void checkIndicatorViewCodeUnique(ServiceContext ctx, String viewCode) throws MetamacException {

        // Prepare criteria
        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
        ConditionRoot<Indicator> conditionRoot = ConditionalCriteriaBuilder.criteriaFor(Indicator.class);
        conditionRoot.withProperty(IndicatorProperties.viewCode()).ignoreCaseEq(viewCode);
        List<ConditionalCriteria> conditions = conditionRoot.distinctRoot().build();

        // Find
        PagedResult<Indicator> result = getIndicatorRepository().findByCondition(conditions, pagingParameter);
        if (result.getValues().size() != 0) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_ALREADY_EXIST_VIEW_CODE_DUPLICATED, viewCode);
        }
    }

    /**
     * Retrieves version of an indicator in production
     */
    private IndicatorVersion retrieveIndicatorProcStatusInProduction(ServiceContext ctx, String uuid, boolean throwsExceptionIfNotExistsInProduction) throws MetamacException {
        Indicator indicator = retrieveIndicator(ctx, uuid);
        if (indicator.getProductionVersionNumber() == null && !throwsExceptionIfNotExistsInProduction) {
            // to throws an specific exception
            return null;
        }
        if (indicator.getProductionVersionNumber() == null) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_IN_PRODUCTION_NOT_FOUND, uuid);
        }
        return retrieveIndicator(ctx, uuid, indicator.getProductionVersionNumber());
    }

    /**
     * Retrieves version of an indicator in diffusion
     */
    private IndicatorVersion retrieveIndicatorProcStatusInDiffusion(ServiceContext ctx, String uuid, boolean throwsExceptionIfNotExistsInDiffusion) throws MetamacException {
        Indicator indicator = retrieveIndicator(ctx, uuid);
        if (indicator.getDiffusionVersionNumber() == null && !throwsExceptionIfNotExistsInDiffusion) {
            return null; // to throws an specific exception
        }
        if (indicator.getDiffusionVersionNumber() == null) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_IN_DIFFUSION_NOT_FOUND, uuid);
        }
        return retrieveIndicator(ctx, uuid, indicator.getDiffusionVersionNumber());
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
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, uuid,
                    new String[]{ServiceExceptionParameters.INDICATOR_PROC_STATUS_DRAFT, ServiceExceptionParameters.INDICATOR_PROC_STATUS_VALIDATION_REJECTED}));
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
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, uuid, new String[]{ServiceExceptionParameters.INDICATOR_PROC_STATUS_PRODUCTION_VALIDATION}));
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
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, uuid, new String[]{ServiceExceptionParameters.INDICATOR_PROC_STATUS_PRODUCTION_VALIDATION}));
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
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, uuid, new String[]{ServiceExceptionParameters.INDICATOR_PROC_STATUS_DIFFUSION_VALIDATION}));
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
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, uuid,
                    new String[]{ServiceExceptionParameters.INDICATOR_PROC_STATUS_DIFFUSION_VALIDATION, ServiceExceptionParameters.INDICATOR_PROC_STATUS_PUBLICATION_FAILED}));
        } else {
            // Check other conditions
            checkConditionsToPublish(ctx, indicatorVersion, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to archive
     * Check actual processing status
     * If state is correct checks same conditions to archive
     */
    private void checkIndicatorToArchive(ServiceContext ctx, String uuid, IndicatorVersion indicatorVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        if (indicatorVersion == null || !IndicatorProcStatusEnum.PUBLISHED.equals(indicatorVersion.getProcStatus())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, uuid, new String[]{ServiceExceptionParameters.INDICATOR_PROC_STATUS_PUBLISHED}));
        } else {
            // Check other conditions
            checkConditionsToArchive(ctx, uuid, exceptions);
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
     * - Validations when archive
     * - Check indicator is not in any published indicators system
     */
    private void checkConditionsToArchive(ServiceContext ctx, String indicatorUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        List<String> indicatorsSystemsDiffusion = getIndicatorInstanceRepository().findIndicatorsSystemsPublishedWithIndicator(indicatorUuid);
        for (String indicatorsSystemUuid : indicatorsSystemsDiffusion) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, indicatorsSystemUuid,
                    new String[]{ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_DRAFT, ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_VALIDATION_REJECTED,
                            ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_PRODUCTION_VALIDATION, ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_DIFFUSION_VALIDATION}));
        }
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
            String indicatorUuidLinked = iterator.next();
            if (indicatorUuidLinked.equals(indicatorVersion.getIndicator().getUuid())) {
                iterator.remove();
            }
        }

        // Checks published
        if (indicatorsUuidLinked.size() != 0) {
            List<String> indicatorsNotPublishedUuid = getIndicatorRepository().filterIndicatorsNotPublished(indicatorsUuidLinked);
            if (indicatorsNotPublishedUuid.size() != 0) {
                String[] indicatorsNotPublishedUuidArray = indicatorsNotPublishedUuid.toArray(new String[indicatorsNotPublishedUuid.size()]);
                exceptions.add(
                        new MetamacExceptionItem(ServiceExceptionType.INDICATOR_MUST_HAVE_ALL_LINKED_INDICATORS_PUBLISHED, indicatorVersion.getIndicator().getUuid(), indicatorsNotPublishedUuidArray));
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
            throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, indicatorVersion.getIndicator().getUuid(),
                    new String[]{ServiceExceptionParameters.INDICATOR_PROC_STATUS_DRAFT, ServiceExceptionParameters.INDICATOR_PROC_STATUS_VALIDATION_REJECTED,
                            ServiceExceptionParameters.INDICATOR_PROC_STATUS_PRODUCTION_VALIDATION, ServiceExceptionParameters.INDICATOR_PROC_STATUS_DIFFUSION_VALIDATION,
                            ServiceExceptionParameters.INDICATOR_PROC_STATUS_PUBLICATION_FAILED});
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

    // Export helper methods
    private void writeHeader(OutputStreamWriter writer, List<String> languages) throws IOException {
        writer.write(IndicatorsConstants.TSV_HEADER_CODE);
        writeCell(writer, IndicatorsConstants.TSV_HEADER_NOTIFY_POPULATION_ERRORS);
        writeHeaderSummaryColumns(writer, IndicatorsConstants.TSV_HEADER_PRODUCTION, languages);
        writeHeaderSummaryColumns(writer, IndicatorsConstants.TSV_HEADER_DIFFUSION, languages);
    }

    private void writeHeaderSummaryColumns(OutputStreamWriter writer, String environment, List<String> languages) throws IOException {
        for (String language : languages) {
            writeEnvironmentHeader(writer, environment, IndicatorsConstants.TSV_HEADER_TITLE + IndicatorsConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }

        for (String language : languages) {
            writeEnvironmentHeader(writer, environment, IndicatorsConstants.TSV_HEADER_SUBJECT_TITLE + IndicatorsConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        writeEnvironmentHeader(writer, environment, IndicatorsConstants.TSV_HEADER_VERSION_NUMBER);
        writeEnvironmentHeader(writer, environment, IndicatorsConstants.TSV_HEADER_PROC_STATUS);
        writeEnvironmentHeader(writer, environment, IndicatorsConstants.TSV_HEADER_NEEDS_UPDATE);
        writeEnvironmentHeader(writer, environment, IndicatorsConstants.TSV_HEADER_PRODUCTION_VALIDATION_DATE);
        writeEnvironmentHeader(writer, environment, IndicatorsConstants.TSV_HEADER_PRODUCTION_VALIDATION_USER);
        writeEnvironmentHeader(writer, environment, IndicatorsConstants.TSV_HEADER_DIFFUSION_VALIDATION_DATE);
        writeEnvironmentHeader(writer, environment, IndicatorsConstants.TSV_HEADER_DIFFUSION_VALIDATION_USER);
        writeEnvironmentHeader(writer, environment, IndicatorsConstants.TSV_HEADER_PUBLICATION_DATE);
        writeEnvironmentHeader(writer, environment, IndicatorsConstants.TSV_HEADER_PUBLICATION_USER);
        writeEnvironmentHeader(writer, environment, IndicatorsConstants.TSV_HEADER_PUBLICATION_FAILED_DATE);
        writeEnvironmentHeader(writer, environment, IndicatorsConstants.TSV_HEADER_PUBLICATION_FAILED_USER);
        writeEnvironmentHeader(writer, environment, IndicatorsConstants.TSV_HEADER_ARCHIVE_DATE);
        writeEnvironmentHeader(writer, environment, IndicatorsConstants.TSV_HEADER_ARCHIVE_USER);
        writeEnvironmentHeader(writer, environment, IndicatorsConstants.TSV_HEADER_CREATED_DATE);
        writeEnvironmentHeader(writer, environment, IndicatorsConstants.TSV_HEADER_CREATED_BY);
    }

    private void writeEnvironmentHeader(OutputStreamWriter writer, String environment, String header) throws IOException {
        writeCell(writer, environment + IndicatorsConstants.TSV_HEADER_ENVIRONMENT_SEPARATOR + header);
    }

    private void writeIndicatorVersion(OutputStreamWriter writer, IndicatorVersion indicatorVersion, List<String> languages) throws IOException {
        if (indicatorVersion != null) {
            writeInternationalString(writer, indicatorVersion.getTitle(), languages);
            writeInternationalString(writer, indicatorVersion.getSubjectTitle(), languages);
            writeCell(writer, indicatorVersion.getVersionNumber());
            writeCell(writer, indicatorVersion.getProcStatus());
            writeCell(writer, indicatorVersion.getNeedsUpdate());
            writeCell(writer, indicatorVersion.getProductionValidationDate());
            writeCell(writer, indicatorVersion.getProductionValidationUser());
            writeCell(writer, indicatorVersion.getDiffusionValidationDate());
            writeCell(writer, indicatorVersion.getDiffusionValidationUser());
            writeCell(writer, indicatorVersion.getPublicationDate());
            writeCell(writer, indicatorVersion.getPublicationUser());
            writeCell(writer, indicatorVersion.getPublicationFailedDate());
            writeCell(writer, indicatorVersion.getPublicationFailedUser());
            writeCell(writer, indicatorVersion.getArchiveDate());
            writeCell(writer, indicatorVersion.getArchiveUser());
            writeCell(writer, indicatorVersion.getCreatedDate());
            writeCell(writer, indicatorVersion.getCreatedBy());
        } else {
            writeEmptyIndicatorVersion(writer, languages);
        }
    }

    private void writeEmptyIndicatorVersion(OutputStreamWriter writer, List<String> languages) throws IOException {
        int INDICATOR_VERSION_INTERNATIONALIZED_FIELDS = 2;
        int INDICATOR_VERSION_NON_INTERNATIONALIZED_FIELDS = 15;
        writer.write(StringUtils.repeat(IndicatorsConstants.TSV_SEPARATOR, INDICATOR_VERSION_INTERNATIONALIZED_FIELDS * languages.size() + INDICATOR_VERSION_NON_INTERNATIONALIZED_FIELDS));
    }

    private void writeInternationalString(OutputStreamWriter writer, InternationalString internationalString, List<String> languages) throws IOException {
        for (String language : languages) {
            if (internationalString != null) {
                writeCell(writer, internationalString.getLocalisedLabel(language));
            } else {
                writeCell(writer, null);
            }
        }
    }

    private void writeCell(OutputStreamWriter writer, Object cell) throws IOException {
        writer.write(IndicatorsConstants.TSV_SEPARATOR);
        if (cell != null) {
            writer.write(cell.toString());
        }
    }

    private NoticesRestInternalService getNoticesRestInternalService() {
        return (NoticesRestInternalService) ApplicationContextProvider.getApplicationContext().getBean(NoticesRestInternalService.BEAN_ID);
    }

}
