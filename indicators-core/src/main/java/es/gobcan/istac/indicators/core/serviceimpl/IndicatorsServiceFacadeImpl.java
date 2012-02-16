package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.IndicatorsConstants;
import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionInformation;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorStateEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersiontTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.mapper.Do2DtoMapper;
import es.gobcan.istac.indicators.core.mapper.Dto2DoMapper;
import es.gobcan.istac.indicators.core.serviceimpl.util.InvocationValidator;
import es.gobcan.istac.indicators.core.serviceimpl.util.ServiceUtils;

/**
 * Implementation of IndicatorServiceFacade.
 * TODO no extender los DTO de auditableDto, porque tienen el Id
 */
@Service("indicatorsServiceFacade")
public class IndicatorsServiceFacadeImpl extends IndicatorsServiceFacadeImplBase {

    @Autowired
    private Do2DtoMapper do2DtoMapper;

    @Autowired
    private Dto2DoMapper dto2DoMapper;

    public IndicatorsServiceFacadeImpl() {
    }

    public IndicatorDto createIndicator(ServiceContext ctx, IndicatorDto indicatorDto) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkCreateIndicator(indicatorDto, null);
        validateCodeUnique(ctx, indicatorDto.getCode(), null);

        // Transform
        Indicator indicator = new Indicator();
        indicator.setDiffusionVersion(null);
        dto2DoMapper.indicatorDtoToDo(indicatorDto, indicator);
        // Version in draft
        IndicatorVersion draftVersion = dto2DoMapper.indicatorDtoToDo(indicatorDto);
        draftVersion.setState(IndicatorStateEnum.DRAFT);
        draftVersion.setVersionNumber(ServiceUtils.generateVersionNumber(null, VersiontTypeEnum.MAJOR));

        // Create
        IndicatorVersion indicatorVersionCreated = getIndicatorsService().createIndicatorVersion(ctx, indicator, draftVersion);

        // Transform to Dto
        indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersionCreated);
        return indicatorDto;
    }

    public IndicatorDto retrieveIndicator(ServiceContext ctx, String uuid, String version) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicator(uuid, version, null);

        // Retrieve version requested or last version
        IndicatorVersion indicatorVersion = null;
        if (version == null) {
            // Retrieve last version
            Indicator indicator = getIndicatorsService().retrieveIndicator(ctx, uuid);
            version = indicator.getProductionVersion() != null ? indicator.getProductionVersion().getVersionNumber() : indicator.getDiffusionVersion().getVersionNumber();
        }
        indicatorVersion = getIndicatorsService().retrieveIndicatorVersion(ctx, uuid, version);

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersion);
        return indicatorDto;
    }

    @Override
    public IndicatorDto retrieveIndicatorPublished(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveIndicatorPublished(uuid, null);

        // Retrieve published version
        IndicatorVersion publishedIndicatorVersion = retrieveIndicatorStateInDiffusion(ctx, uuid, false);
        if (publishedIndicatorVersion == null || !IndicatorStateEnum.PUBLISHED.equals(publishedIndicatorVersion.getState())) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_STATE, uuid, new IndicatorStateEnum[]{IndicatorStateEnum.PUBLISHED});
        }

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(publishedIndicatorVersion);
        return indicatorDto;
    }

    public void deleteIndicator(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkDeleteIndicator(uuid, null);

        // Retrieve version in production
        IndicatorVersion indicatorVersion = retrieveIndicatorStateInProduction(ctx, uuid, true);

        // Delete whole indicator or only last version
        if (IndicatorsConstants.VERSION_NUMBER_INITIAL.equals(indicatorVersion.getVersionNumber())) {
            // If indicator is not published or archived, delete whole indicator
            getIndicatorsService().deleteIndicator(ctx, uuid);
        } else {
            getIndicatorsService().deleteIndicatorVersion(ctx, uuid, indicatorVersion.getVersionNumber());
            indicatorVersion.getIndicator().setProductionVersion(null);
            getIndicatorsService().updateIndicator(ctx, indicatorVersion.getIndicator());
        }
    }

    public void updateIndicator(ServiceContext ctx, IndicatorDto indicatorDto) throws MetamacException {

        // Retrieve version in production
        IndicatorVersion indicatorInProduction = retrieveIndicatorStateInProduction(ctx, indicatorDto.getUuid(), true);
        if (!indicatorInProduction.getVersionNumber().equals(indicatorDto.getVersionNumber())) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_VERSION_WRONG_STATE, indicatorDto.getUuid(), indicatorDto.getVersionNumber());
        }

        // Validation
        InvocationValidator.checkUpdateIndicator(indicatorDto, indicatorInProduction, null);

        // Transform
        // Note: attributes in indicatorSystem entity are non modifiables
        dto2DoMapper.indicatorDtoToDo(indicatorDto, indicatorInProduction);

        // Update
        getIndicatorsService().updateIndicatorVersion(ctx, indicatorInProduction);
    }

    // // TODO validación: El indicador debe tener al menos un origen de datos asociado.
    // @Override
    // public void sendIndicatorToProductionValidation(ServiceContext ctx, String uuid) throws MetamacException {
    //
    // // Validation of parameters
    // InvocationValidator.checkSendIndicatorToProductionValidation(uuid, null);
    //
    // // Retrieve version in draft
    // IndicatorVersion indicatorInProduction = retrieveIndicatorStateInProduction(ctx, uuid, false);
    // if (indicatorInProduction == null
    // || (!IndicatorStateEnum.DRAFT.equals(indicatorInProduction.getState()) && !IndicatorStateEnum.VALIDATION_REJECTED.equals(indicatorInProduction.getState()))) {
    // throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_STATE.getCode(), ServiceExceptionType.INDICATOR_WRONG_STATE.getMessageForReasonType(),
    // uuid, new IndicatorStateEnum[]{IndicatorStateEnum.DRAFT, IndicatorStateEnum.VALIDATION_REJECTED});
    // }
    //
    // // Update state
    // indicatorInProduction.setState(IndicatorStateEnum.PRODUCTION_VALIDATION);
    // indicatorInProduction.setProductionValidationDate(new DateTime());
    // indicatorInProduction.setProductionValidationUser(ctx.getUserId());
    // getIndicatorsService().updateIndicatorVersion(ctx, indicatorInProduction);
    // }
    //
    // @Override
    // public void sendIndicatorToDiffusionValidation(ServiceContext ctx, String uuid) throws MetamacException {
    //
    // // Validation of parameters
    // InvocationValidator.checkSendIndicatorToDiffusionValidation(uuid, null);
    //
    // // Retrieve version in production validation
    // IndicatorVersion indicatorInProduction = retrieveIndicatorStateInProduction(ctx, uuid, false);
    // if (indicatorInProduction == null || !IndicatorStateEnum.PRODUCTION_VALIDATION.equals(indicatorInProduction.getState())) {
    // throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_STATE.getCode(), ServiceExceptionType.INDICATOR_WRONG_STATE.getMessageForReasonType(),
    // uuid, IndicatorStateEnum.PRODUCTION_VALIDATION);
    // }
    //
    // // Update state
    // indicatorInProduction.setState(IndicatorStateEnum.DIFFUSION_VALIDATION);
    // indicatorInProduction.setDiffusionValidationDate(new DateTime());
    // indicatorInProduction.setDiffusionValidationUser(ctx.getUserId());
    // getIndicatorsService().updateIndicatorVersion(ctx, indicatorInProduction);
    // }
    //
    // @Override
    // public void rejectIndicatorValidation(ServiceContext ctx, String uuid) throws MetamacException {
    //
    // // Validation of parameters
    // InvocationValidator.checkRejectIndicatorValidation(uuid, null);
    //
    // // Retrieve version in production (state can be production or diffusion validation)
    // IndicatorVersion indicatorInProduction = retrieveIndicatorStateInProduction(ctx, uuid, false);
    // if (indicatorInProduction == null
    // || (!IndicatorStateEnum.PRODUCTION_VALIDATION.equals(indicatorInProduction.getState()) && !IndicatorStateEnum.DIFFUSION_VALIDATION
    // .equals(indicatorInProduction.getState()))) {
    // throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_STATE.getCode(), ServiceExceptionType.INDICATOR_WRONG_STATE.getMessageForReasonType(),
    // uuid, new IndicatorStateEnum[]{IndicatorStateEnum.PRODUCTION_VALIDATION, IndicatorStateEnum.DIFFUSION_VALIDATION});
    // }
    //
    // // Update state
    // indicatorInProduction.setState(IndicatorStateEnum.VALIDATION_REJECTED);
    // indicatorInProduction.setProductionValidationDate(null);
    // indicatorInProduction.setProductionValidationUser(null);
    // indicatorInProduction.setDiffusionValidationDate(null);
    // indicatorInProduction.setDiffusionValidationUser(null);
    // getIndicatorsService().updateIndicatorVersion(ctx, indicatorInProduction);
    // }
    //
    // // TODO comprobar que todos los indicadores tienen alguna versión en difusión
    // @Override
    // public void publishIndicator(ServiceContext ctx, String uuid) throws MetamacException {
    //
    // // Validation of parameters
    // InvocationValidator.checkPublishIndicator(uuid, null);
    //
    // // Retrieve version in diffusion validation
    // IndicatorVersion indicatorInProduction = retrieveIndicatorStateInProduction(ctx, uuid, false);
    // if (indicatorInProduction == null || !IndicatorStateEnum.DIFFUSION_VALIDATION.equals(indicatorInProduction.getState())) {
    // throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_STATE.getCode(), ServiceExceptionType.INDICATOR_WRONG_STATE.getMessageForReasonType(),
    // uuid, IndicatorStateEnum.DIFFUSION_VALIDATION);
    // }
    //
    // // Update state
    // indicatorInProduction.setState(IndicatorStateEnum.PUBLISHED);
    // indicatorInProduction.setPublicationDate(new DateTime());
    // indicatorInProduction.setPublicationUser(ctx.getUserId());
    // getIndicatorsService().updateIndicatorVersion(ctx, indicatorInProduction);
    //
    // Indicator indicator = indicatorInProduction.getIndicator();
    // // Remove possible last version in diffusion
    // if (indicator.getDiffusionVersion() != null) {
    // getIndicatorsService().deleteIndicatorVersion(ctx, uuid, indicator.getDiffusionVersion().getVersionNumber());
    // }
    // indicator.setDiffusionVersion(new IndicatorVersionInformation(indicatorInProduction.getId(), indicatorInProduction.getVersionNumber()));
    // indicator.setProductionVersion(null);
    //
    // getIndicatorsService().updateIndicator(ctx, indicator);
    // }
    //
    // @Override
    // public void archiveIndicator(ServiceContext ctx, String uuid) throws MetamacException {
    //
    // // Validation of parameters
    // InvocationValidator.checkArchiveIndicator(uuid, null);
    //
    // // Retrieve version published
    // IndicatorVersion indicatorInDiffusion = retrieveIndicatorStateInDiffusion(ctx, uuid, false);
    // if (indicatorInDiffusion == null || !IndicatorStateEnum.PUBLISHED.equals(indicatorInDiffusion.getState())) {
    // throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_STATE.getCode(), ServiceExceptionType.INDICATOR_WRONG_STATE.getMessageForReasonType(),
    // uuid, IndicatorStateEnum.PUBLISHED);
    // }
    //
    // // Update state
    // indicatorInDiffusion.setState(IndicatorStateEnum.ARCHIVED);
    // indicatorInDiffusion.setArchiveDate(new DateTime());
    // indicatorInDiffusion.setArchiveUser(ctx.getUserId());
    // getIndicatorsService().updateIndicatorVersion(ctx, indicatorInDiffusion);
    // }
    //
    // // TODO copiar orígenes de datos
    // @Override
    // public IndicatorDto versioningIndicator(ServiceContext ctx, String uuid, VersiontTypeEnum versionType) throws MetamacException {
    //
    // // Validation of parameters
    // InvocationValidator.checkVersioningIndicator(uuid, versionType, null);
    //
    // Indicator indicator = getIndicatorsService().retrieveIndicator(ctx, uuid);
    // if (indicator.getProductionVersion() != null) {
    // throw new MetamacException(ServiceExceptionType.INDICATOR_WRONG_STATE.getCode(), ServiceExceptionType.INDICATOR_WRONG_STATE.getMessageForReasonType(),
    // uuid, new IndicatorStateEnum[]{IndicatorStateEnum.PUBLISHED, IndicatorStateEnum.ARCHIVED});
    // }
    //
    // // Initialize new version, copying values of version in diffusion
    // IndicatorVersion indicatorVersionDiffusion = retrieveIndicatorStateInDiffusion(ctx, uuid, true);
    // IndicatorVersion indicatorNewVersion = DoCopyUtils.copy(indicatorVersionDiffusion);
    // indicatorNewVersion.setState(IndicatorStateEnum.DRAFT);
    // indicatorNewVersion.setVersionNumber(ServiceUtils.generateVersionNumber(indicatorVersionDiffusion.getVersionNumber(), versionType));
    //
    // // Create
    // IndicatorVersion indicatorVersionCreated = getIndicatorsService().createIndicatorVersion(ctx, indicator, indicatorNewVersion);
    //
    // // Transform to Dto
    // IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(indicatorVersionCreated);
    // return indicatorDto;
    // }
    //
    // TODO paginación
    // TODO criteria
    // TODO obtener directamente las últimas versiones con consulta? añadir columna lastVersion?
    @Override
    public List<IndicatorDto> findIndicators(ServiceContext ctx) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindIndicators(null);

        // Find
        List<Indicator> indicators = getIndicatorsService().findIndicators(ctx, null);

        // Transform
        List<IndicatorDto> indicatorDto = new ArrayList<IndicatorDto>();
        for (Indicator indicator : indicators) {
            // Last version
            IndicatorVersionInformation lastVersion = indicator.getProductionVersion() != null ? indicator.getProductionVersion() : indicator.getDiffusionVersion();
            IndicatorVersion indicatorLastVersion = getIndicatorsService().retrieveIndicatorVersion(ctx, indicator.getUuid(), lastVersion.getVersionNumber());
            indicatorDto.add(do2DtoMapper.indicatorDoToDto(indicatorLastVersion));
        }

        return indicatorDto;
    }

    // TODO paginación
    // TODO criteria
    @Override
    public List<IndicatorDto> findIndicatorsPublished(ServiceContext ctx) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindIndicatorsPublished(null);

        // Retrieve published
        List<IndicatorVersion> indicatorsVersions = getIndicatorsService().findIndicatorsVersions(ctx, null, IndicatorStateEnum.PUBLISHED);

        // Transform
        List<IndicatorDto> indicatorDto = new ArrayList<IndicatorDto>();
        for (IndicatorVersion indicatorVersion : indicatorsVersions) {
            indicatorDto.add(do2DtoMapper.indicatorDoToDto(indicatorVersion));
        }

        return indicatorDto;
    }

    @Override
    public DataSourceDto createDataSource(ServiceContext ctx, String indicatorUuid, DataSourceDto dataSourceDto) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkCreateDataSource(indicatorUuid, dataSourceDto, null);

        // Retrieve indicator version and check it is in production
        IndicatorVersion indicatorVersion = retrieveIndicatorStateInProduction(ctx, indicatorUuid, true);

        // Transform
        DataSource dataSource = dto2DoMapper.dataSourceDtoToDo(dataSourceDto);
        dataSource = getIndicatorsService().createDataSource(ctx, dataSource);

        // Create dataSource
        dataSource.setIndicatorVersion(indicatorVersion);
        dataSource = getIndicatorsService().createDataSource(ctx, dataSource);

        // Update indicator adding dataSource
        indicatorVersion.addDataSource(dataSource);
        getIndicatorsService().updateIndicatorVersion(ctx, indicatorVersion);

        // Transform to Dto to return
        dataSourceDto = do2DtoMapper.dataSourceDoToDto(dataSource);
        return dataSourceDto;
    }

    @Override
    public DataSourceDto retrieveDataSource(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkRetrieveDataSource(uuid, null);

        // Retrieve
        DataSource dataSource = getIndicatorsService().retrieveDataSource(ctx, uuid);
        DataSourceDto dataSourceDto = do2DtoMapper.dataSourceDoToDto(dataSource);
        return dataSourceDto;
    }

    @Override
    public void deleteDataSource(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkDeleteDataSource(uuid, null);

        // Check indicator state
        DataSource dataSource = getIndicatorsService().retrieveDataSource(ctx, uuid);
        checkIndicatorVersionInProduction(dataSource.getIndicatorVersion());

        // Delete
        getIndicatorsService().deleteDataSource(ctx, dataSource);
    }

    @Override
    public List<DataSourceDto> findDataSources(ServiceContext ctx, String indicatorUuid, String indicatorVersion) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkFindDataSources(indicatorUuid, indicatorVersion, null);

        // Retrieve dataSources and transform
        List<DataSource> dataSources = getIndicatorsService().findDataSources(ctx, indicatorUuid, indicatorVersion);
        List<DataSourceDto> dataSourcesDto = new ArrayList<DataSourceDto>();
        for (DataSource dataSource : dataSources) {
            dataSourcesDto.add(do2DtoMapper.dataSourceDoToDto(dataSource));
        }

        return dataSourcesDto;
    }

    // TODO guardar other variables
    @Override
    public void updateDataSource(ServiceContext ctx, DataSourceDto dataSourceDto) throws MetamacException {

        // Retrieve
        // TODO comprobar parámetros antes?
        DataSource dataSource = getIndicatorsService().retrieveDataSource(ctx, dataSourceDto.getUuid());

        // Validation of parameters
        InvocationValidator.checkUpdateDataSource(dataSourceDto, dataSource, null);

        // Check indicator state
        checkIndicatorVersionInProduction(dataSource.getIndicatorVersion());

        // Transform and update
        dto2DoMapper.dataSourceDtoToDo(dataSourceDto, dataSource);
        getIndicatorsService().updateDataSource(ctx, dataSource);
    }

    /**
     * Retrieves version of an indicator in production
     */
    private IndicatorVersion retrieveIndicatorStateInProduction(ServiceContext ctx, String uuid, boolean throwsExceptionIfNotExistsInProduction) throws MetamacException {
        Indicator indicator = getIndicatorsService().retrieveIndicator(ctx, uuid);
        if (indicator == null) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_FOUND, uuid);
        }
        if (indicator.getProductionVersion() == null && !throwsExceptionIfNotExistsInProduction) {
            return null; // to throws an specific exception
        }
        if (indicator.getProductionVersion() == null) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_IN_PRODUCTION_NOT_FOUND, uuid);
        }
        IndicatorVersion indicatorVersionProduction = getIndicatorsService().retrieveIndicatorVersion(ctx, uuid, indicator.getProductionVersion().getVersionNumber());
        return indicatorVersionProduction;
    }

    /**
     * Retrieves version of an indicator in diffusion
     */
    private IndicatorVersion retrieveIndicatorStateInDiffusion(ServiceContext ctx, String uuid, boolean throwsExceptionIfNotExistsInDiffusion) throws MetamacException {
        Indicator indicator = getIndicatorsService().retrieveIndicator(ctx, uuid);
        if (indicator.getDiffusionVersion() == null && !throwsExceptionIfNotExistsInDiffusion) {
            return null; // to throws an specific exception
        }
        if (indicator.getDiffusionVersion() == null) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_IN_DIFFUSION_NOT_FOUND, uuid);
        }
        IndicatorVersion indicatorVersionDiffusion = getIndicatorsService().retrieveIndicatorVersion(ctx, uuid, indicator.getDiffusionVersion().getVersionNumber());
        return indicatorVersionDiffusion;
    }

    /**
     * Checks not exists another indicator with same code. Checks indicator retrieved not is actual indicator.
     */
    private void validateCodeUnique(ServiceContext ctx, String code, String actualUuid) throws MetamacException {
        List<Indicator> indicator = getIndicatorsService().findIndicators(ctx, code);
        if (indicator != null && indicator.size() != 0 && !indicator.get(0).getUuid().equals(actualUuid)) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_ALREADY_EXIST_CODE_DUPLICATED, code);
        }
    }

    private void checkIndicatorVersionInProduction(IndicatorVersion indicatorVersion) throws MetamacException {
        IndicatorStateEnum state = indicatorVersion.getState();
        boolean inProduction = IndicatorStateEnum.DRAFT.equals(state) || IndicatorStateEnum.VALIDATION_REJECTED.equals(state) || IndicatorStateEnum.PRODUCTION_VALIDATION.equals(state)
                || IndicatorStateEnum.DIFFUSION_VALIDATION.equals(state);
        if (!inProduction) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_VERSION_WRONG_STATE, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());

        }
    }
}
