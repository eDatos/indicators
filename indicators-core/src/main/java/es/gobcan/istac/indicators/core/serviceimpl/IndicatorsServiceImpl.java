package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.ArrayList;
import java.util.Iterator;
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
import es.gobcan.istac.indicators.core.domain.Quantity;
import es.gobcan.istac.indicators.core.domain.QuantityUnit;
import es.gobcan.istac.indicators.core.domain.Subject;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorProcStatusEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersiontTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceimpl.util.DoCopyUtils;
import es.gobcan.istac.indicators.core.serviceimpl.util.InvocationValidator;
import es.gobcan.istac.indicators.core.serviceimpl.util.ServiceUtils;

/**
 * Implementation of IndicatorsService.
 */
@Service("indicatorsService")
public class IndicatorsServiceImpl extends IndicatorsServiceImplBase {

    public IndicatorsServiceImpl() {
    }

    // TODO Búsquedas por subjectCode
    // TODO Metadato 'base_time': corresponde a un valor time siguiendo el formato para valores temporales.
    // TODO Metadato 'baseLocation': corresponde a un valor de la tabla de valores geográficos (foreign key)
    @Override
    public IndicatorVersion createIndicator(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {

        Indicator indicator = indicatorVersion.getIndicator();

        // Validation of parameters
        InvocationValidator.checkCreateIndicator(indicatorVersion, null);
        checkIndicatorCodeUnique(ctx, indicator.getCode(), null);

        // Save indicator
        indicator.setDiffusionVersion(null);
        indicator.setIsPublished(Boolean.FALSE);
        indicator = getIndicatorRepository().save(indicator);

        // Save draft version
        indicatorVersion.setProcStatus(IndicatorProcStatusEnum.DRAFT);
        indicatorVersion.setVersionNumber(ServiceUtils.generateVersionNumber(null, VersiontTypeEnum.MAJOR));
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

        // Retrieve indicator by code
        List<Indicator> indicators = findIndicators(ctx, code);
        if (indicators.size() == 0) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_FOUND_WITH_CODE, code);
        } else if (indicators.size() > 1) {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "Found more than one indicator with code " + code);
        }

        // Retrieve version requested or last version
        Indicator indicator = indicators.get(0);
        IndicatorVersion indicatorVersion = retrieveIndicator(ctx, indicator.getUuid(), versionNumber);

        return indicatorVersion;
    }

    @Override
    public IndicatorVersion retrieveIndicatorPublishedByCode(ServiceContext ctx, String code) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorPublishedByCode(code, null);

        // Retrieve indicator by code
        List<Indicator> indicators = findIndicators(ctx, code);
        if (indicators.size() == 0) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_FOUND_WITH_CODE, code);
        } else if (indicators.size() > 1) {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "Found more than one indicator with code " + code);
        }

        // Retrieve only published
        Indicator indicator = indicators.get(0);
        if (indicator.getDiffusionVersion() == null) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, indicator.getUuid(), new IndicatorProcStatusEnum[]{IndicatorProcStatusEnum.PUBLISHED});
        }
        IndicatorVersion indicatorVersion = retrieveIndicator(ctx, indicator.getUuid(), indicator.getDiffusionVersion().getVersionNumber());
        if (!IndicatorProcStatusEnum.PUBLISHED.equals(indicatorVersion.getProcStatus())) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, indicator.getUuid(), new IndicatorProcStatusEnum[]{IndicatorProcStatusEnum.PUBLISHED});
        }

        return indicatorVersion;
    }

    @Override
    public IndicatorVersion updateIndicatorVersion(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {

        // Validation
        InvocationValidator.checkUpdateIndicator(indicatorVersion, null);

        // Validate indicators system proc status and linked indicators
        checkIndicatorVersionInProduction(indicatorVersion);
        checkIndicatorsLinked(indicatorVersion.getQuantity(), indicatorVersion.getIndicator().getUuid(), Boolean.FALSE, "INDICATOR.QUANTITY");

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

            // Update
            getIndicatorRepository().save(indicator);
            getIndicatorVersionRepository().delete(indicatorVersion);
        }
    }

    // TODO obtener directamente las últimas versiones con consulta? añadir columna lastVersion?
    @Override
    public List<IndicatorVersion> findIndicators(ServiceContext ctx) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindIndicators(null);

        // Find
        List<Indicator> indicators = findIndicators(ctx, null);

        // Transform
        List<IndicatorVersion> indicatorsVersions = new ArrayList<IndicatorVersion>();
        for (Indicator indicator : indicators) {
            // Last version
            IndicatorVersionInformation lastVersion = indicator.getProductionVersion() != null ? indicator.getProductionVersion() : indicator.getDiffusionVersion();
            IndicatorVersion indicatorLastVersion = retrieveIndicator(ctx, indicator.getUuid(), lastVersion.getVersionNumber());
            indicatorsVersions.add(indicatorLastVersion);
        }

        return indicatorsVersions;
    }

    @Override
    public List<IndicatorVersion> findIndicatorsPublished(ServiceContext ctx) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindIndicatorsPublished(null);

        // Retrieve published
        List<IndicatorVersion> indicatorsVersion = getIndicatorVersionRepository().findIndicatorsVersions(IndicatorProcStatusEnum.PUBLISHED);

        // Transform
        List<IndicatorVersion> indicatorsVersions = new ArrayList<IndicatorVersion>();
        for (IndicatorVersion indicatorVersion : indicatorsVersion) {
            indicatorsVersions.add(indicatorVersion);
        }

        return indicatorsVersions;
    }

    @Override
    public IndicatorVersion sendIndicatorToProductionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkSendIndicatorToProductionValidation(uuid, null);

        // Retrieve version in draft
        IndicatorVersion indicatorInProduction = retrieveIndicatorProcStatusInProduction(ctx, uuid, false);
        if (indicatorInProduction == null || (!IndicatorProcStatusEnum.DRAFT.equals(indicatorInProduction.getProcStatus()) && !IndicatorProcStatusEnum.VALIDATION_REJECTED.equals(indicatorInProduction.getProcStatus()))) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, uuid, new IndicatorProcStatusEnum[]{IndicatorProcStatusEnum.DRAFT, IndicatorProcStatusEnum.VALIDATION_REJECTED});
        }

        // Validate to send to production
        checkIndicatorToSendToProductionValidation(ctx, indicatorInProduction);

        // Update proc status
        indicatorInProduction.setProcStatus(IndicatorProcStatusEnum.PRODUCTION_VALIDATION);
        indicatorInProduction.setProductionValidationDate(new DateTime());
        indicatorInProduction.setProductionValidationUser(ctx.getUserId());
        indicatorInProduction = getIndicatorVersionRepository().save(indicatorInProduction);
        
        return indicatorInProduction;
    }

    @Override
    public IndicatorVersion sendIndicatorToDiffusionValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkSendIndicatorToDiffusionValidation(uuid, null);

        // Retrieve version in production validation
        IndicatorVersion indicatorInProduction = retrieveIndicatorProcStatusInProduction(ctx, uuid, false);
        if (indicatorInProduction == null || !IndicatorProcStatusEnum.PRODUCTION_VALIDATION.equals(indicatorInProduction.getProcStatus())) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, uuid, new IndicatorProcStatusEnum[]{IndicatorProcStatusEnum.PRODUCTION_VALIDATION});
        }

        // Validate to send to diffusion
        checkIndicatorToSendToDiffusionValidation(ctx, indicatorInProduction);

        // Update proc status
        indicatorInProduction.setProcStatus(IndicatorProcStatusEnum.DIFFUSION_VALIDATION);
        indicatorInProduction.setDiffusionValidationDate(new DateTime());
        indicatorInProduction.setDiffusionValidationUser(ctx.getUserId());
        indicatorInProduction = getIndicatorVersionRepository().save(indicatorInProduction);
        
        return indicatorInProduction;
    }

    @Override
    public IndicatorVersion rejectIndicatorValidation(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRejectIndicatorValidation(uuid, null);

        // Retrieve version in production (proc status can be production or diffusion validation)
        IndicatorVersion indicatorInProduction = retrieveIndicatorProcStatusInProduction(ctx, uuid, false);
        if (indicatorInProduction == null
                || (!IndicatorProcStatusEnum.PRODUCTION_VALIDATION.equals(indicatorInProduction.getProcStatus()) && !IndicatorProcStatusEnum.DIFFUSION_VALIDATION.equals(indicatorInProduction.getProcStatus()))) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, uuid, new IndicatorProcStatusEnum[]{IndicatorProcStatusEnum.PRODUCTION_VALIDATION, IndicatorProcStatusEnum.DIFFUSION_VALIDATION});
        }

        // Validate to reject
        checkIndicatorToReject(ctx, indicatorInProduction);

        // Update proc status
        indicatorInProduction.setProcStatus(IndicatorProcStatusEnum.VALIDATION_REJECTED);
        indicatorInProduction.setProductionValidationDate(null);
        indicatorInProduction.setProductionValidationUser(null);
        indicatorInProduction.setDiffusionValidationDate(null);
        indicatorInProduction.setDiffusionValidationUser(null);
        indicatorInProduction = getIndicatorVersionRepository().save(indicatorInProduction);
        
        return indicatorInProduction;
    }

    // TODO Implica realizar también INDICADORES-CU-6
    @Override
    public IndicatorVersion publishIndicator(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkPublishIndicator(uuid, null);

        // Retrieve version in diffusion validation
        IndicatorVersion indicatorInProduction = retrieveIndicatorProcStatusInProduction(ctx, uuid, false);
        if (indicatorInProduction == null
                || (!IndicatorProcStatusEnum.DIFFUSION_VALIDATION.equals(indicatorInProduction.getProcStatus()) && !IndicatorProcStatusEnum.PUBLICATION_FAILED.equals(indicatorInProduction.getProcStatus()))) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, uuid, new IndicatorProcStatusEnum[]{IndicatorProcStatusEnum.DIFFUSION_VALIDATION, IndicatorProcStatusEnum.PUBLICATION_FAILED});
        }

        // Validate to publish
        checkIndicatorToPublish(ctx, indicatorInProduction);

        // Update proc status
        indicatorInProduction.setProcStatus(IndicatorProcStatusEnum.PUBLISHED);
        indicatorInProduction.setPublicationDate(new DateTime());
        indicatorInProduction.setPublicationUser(ctx.getUserId());
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
        
        return indicatorInProduction;
    }

    @Override
    public IndicatorVersion archiveIndicator(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkArchiveIndicator(uuid, null);

        // Retrieve version published
        IndicatorVersion indicatorInDiffusion = retrieveIndicatorProcStatusInDiffusion(ctx, uuid, false);
        if (indicatorInDiffusion == null || !IndicatorProcStatusEnum.PUBLISHED.equals(indicatorInDiffusion.getProcStatus())) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, uuid, new IndicatorProcStatusEnum[]{IndicatorProcStatusEnum.PUBLISHED});
        }

        // Validate to archive
        checkIndicatorToArchive(ctx, indicatorInDiffusion);

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
    public IndicatorVersion versioningIndicator(ServiceContext ctx, String uuid, VersiontTypeEnum versionType) throws MetamacException {

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

        // Create
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
        checkIndicatorsLinked(dataSource.getAnnualRate().getQuantity(), indicatorVersion.getIndicator().getUuid(), Boolean.TRUE, "DATA_SOURCE.ANNUAL_RATE.QUANTITY");
        checkIndicatorsLinked(dataSource.getInterperiodRate().getQuantity(), indicatorVersion.getIndicator().getUuid(), Boolean.TRUE, "DATA_SOURCE.INTERPERIOD_RATE.QUANTITY");

        // Create dataSource
        dataSource.setIndicatorVersion(indicatorVersion);
        dataSource = getDataSourceRepository().save(dataSource);

        // Update indicator adding dataSource
        indicatorVersion.addDataSource(dataSource);
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
        checkIndicatorsLinked(dataSource.getAnnualRate().getQuantity(), dataSource.getIndicatorVersion().getIndicator().getUuid(), Boolean.TRUE, "DATA_SOURCE.ANNUAL_RATE.QUANTITY");
        checkIndicatorsLinked(dataSource.getInterperiodRate().getQuantity(), dataSource.getIndicatorVersion().getIndicator().getUuid(), Boolean.TRUE, "DATA_SOURCE.INTERPERIOD_RATE.QUANTITY");

        // Update
        return getDataSourceRepository().save(dataSource);
    }

    @Override
    public void deleteDataSource(ServiceContext ctx, String uuid) throws MetamacException {
        // Validation of parameters
        InvocationValidator.checkDeleteDataSource(uuid, null);

        // Check indicator proc status
        DataSource dataSource = retrieveDataSource(ctx, uuid);
        checkIndicatorVersionInProduction(dataSource.getIndicatorVersion());

        // Delete
        getDataSourceRepository().delete(dataSource);
    }

    @Override
    public List<DataSource> findDataSources(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindDataSources(indicatorUuid, indicatorVersionNumber, null);

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
    public List<QuantityUnit> findQuantityUnits(ServiceContext ctx) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindQuantityUnits(null);

        // Find
        List<QuantityUnit> quantityUnits = getQuantityUnitRepository().findAll();
        return quantityUnits;
    }
    
    @Override
    public Subject retrieveSubject(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveSubject(uuid, null);

        // Retrieve
        Subject subject = getSubjectRepository().retrieveSubject(uuid);
        if (subject == null) {
            throw new MetamacException(ServiceExceptionType.SUBJECT_NOT_FOUND, uuid);
        }
        return subject;
    }

    @Override
    public List<Subject> findSubjects(ServiceContext ctx) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindSubjects(null);

        // Find
        List<Subject> subjects = getSubjectRepository().findSubjects();
        return subjects;
    }

    /**
     * Checks not exists another indicator with same code. Checks indicator retrieved not is actual indicator.
     */
    private void checkIndicatorCodeUnique(ServiceContext ctx, String code, String actualUuid) throws MetamacException {
        List<Indicator> indicator = findIndicators(ctx, code);
        if (indicator != null && indicator.size() != 0 && !indicator.get(0).getUuid().equals(actualUuid)) {
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
     * Makes validations to sent to production validation
     * 1) Must exists at least one indicator instance
     */
    private void checkIndicatorToSendToProductionValidation(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {

        // Check indicator has at least one data source
        if (indicatorVersion.getDataSources().size() == 0) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_MUST_HAVE_DATA_SOURCES, indicatorVersion.getIndicator().getUuid());
        }
    }

    /**
     * Makes validations to sent to diffusion validation
     * 1) Validations when send to production validation
     */
    private void checkIndicatorToSendToDiffusionValidation(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {

        checkIndicatorToSendToProductionValidation(ctx, indicatorVersion);
    }

    /**
     * Makes validations to publish
     * 1) Validations when send to diffusion validation
     * 2) Checks numerator and denominator are published (for indicator and all datasources)
     * 3) Checks base quantity is published (if it is not own indicator) (for indicator and all datasources)
     */
    private void checkIndicatorToPublish(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {

        checkIndicatorToSendToDiffusionValidation(ctx, indicatorVersion);

        // Check linked indicators
        checkQuantityIndicatorsPublished(ctx, indicatorVersion);
    }

    private void checkQuantityIndicatorsPublished(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {

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
                throw new MetamacException(ServiceExceptionType.INDICATOR_MUST_HAVE_ALL_LINKED_INDICATORS_PUBLISHED, indicatorVersion.getIndicator().getUuid(), indicatorsNotPublishedUuidArray);
            }
        }
    }
    
    /**
     * Makes validations to archive
     * 1) Validations when publish
     */
    private void checkIndicatorToArchive(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {

        checkIndicatorToPublish(ctx, indicatorVersion);
    }

    /**
     * Makes validations to reject
     */
    private void checkIndicatorToReject(ServiceContext ctx, IndicatorVersion indicatorVersion) {

        // Nothing
    }

    /**
     * Checks that the indicator version is in any proc status in production
     */
    private void checkIndicatorVersionInProduction(IndicatorVersion indicatorVersion) throws MetamacException {
        IndicatorProcStatusEnum procStatus = indicatorVersion.getProcStatus();
        boolean inProduction = IndicatorProcStatusEnum.DRAFT.equals(procStatus) || IndicatorProcStatusEnum.VALIDATION_REJECTED.equals(procStatus) || IndicatorProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)
                || IndicatorProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus);
        if (!inProduction) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS, indicatorVersion.getIndicator().getUuid(), new IndicatorProcStatusEnum[]{IndicatorProcStatusEnum.DRAFT,
                    IndicatorProcStatusEnum.VALIDATION_REJECTED, IndicatorProcStatusEnum.PRODUCTION_VALIDATION, IndicatorProcStatusEnum.DIFFUSION_VALIDATION});
        }
    }

    private List<Indicator> findIndicators(ServiceContext ctx, String code) throws MetamacException {
        return getIndicatorRepository().findIndicators(code);
    }

    /**
     * Numerator and denominator never must be own indicator.
     * Base quantity never must be own indicator, except when it is a quantity of a datasource and it is change rate. In this case it always must be own indicator
     */
    private void checkIndicatorsLinked(Quantity quantity, String indicatorUuid, Boolean isDataSource, String parameterName) throws MetamacException {
        if (quantity.getNumerator() != null && quantity.getNumerator().getUuid().equals(indicatorUuid)) {
            throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, parameterName + ".NUMERATOR_INDICATOR_UUID");
        }
        if (quantity.getDenominator() != null && quantity.getDenominator().getUuid().equals(indicatorUuid)) {
            throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, parameterName + ".DENOMINATOR_INDICATOR_UUID");
        }
        if (quantity.getBaseQuantity() != null) {
            if (isDataSource) {
                if (!quantity.getBaseQuantity().getUuid().equals(indicatorUuid)) {
                    throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, parameterName + ".BASE_QUANTITY_INDICATOR_UUID");
                }
            } else {
                if (quantity.getBaseQuantity().getUuid().equals(indicatorUuid)) {
                    throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, parameterName + ".BASE_QUANTITY_INDICATOR_UUID");
                }
            }
        }
    }
}
