package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorStateEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersiontTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.mapper.Do2DtoMapper;
import es.gobcan.istac.indicators.core.mapper.Dto2DoMapper;
import es.gobcan.istac.indicators.core.serviceimpl.util.InvocationValidator;
import es.gobcan.istac.indicators.core.serviceimpl.util.ServiceUtils;

/**
 * Implementation of IndicatorsServiceFacade.
 */
@Service("indicatorsServiceFacade")
public class IndicatorsServiceFacadeImpl extends IndicatorsServiceFacadeImplBase {

    @Autowired
    private Do2DtoMapper        do2DtoMapper;

    @Autowired
    private Dto2DoMapper        dto2DoMapper;
    
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
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATOR_WRONG_STATE.getErrorCode(), ServiceExceptionType.SERVICE_INDICATOR_WRONG_STATE.getMessageForReasonType(),
                    uuid, IndicatorStateEnum.PUBLISHED);
        }

        // Transform to Dto
        IndicatorDto indicatorDto = do2DtoMapper.indicatorDoToDto(publishedIndicatorVersion);
        return indicatorDto;
    }
    
    /**
     * Retrieves version of an indicator in production
     */
    private IndicatorVersion retrieveIndicatorStateInProduction(ServiceContext ctx, String uuid, boolean throwsExceptionIfNotExistsInProduction) throws MetamacException {
        Indicator indicator = getIndicatorsService().retrieveIndicator(ctx, uuid);
        if (indicator == null) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATOR_NOT_FOUND.getErrorCode(), ServiceExceptionType.SERVICE_INDICATOR_NOT_FOUND.getMessageForReasonType(),
                    uuid);
        }
        if (indicator.getProductionVersion() == null && !throwsExceptionIfNotExistsInProduction) {
            return null; // to throws an specific exception
        }
        if (indicator.getProductionVersion() == null) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATOR_IN_PRODUCTION_NOT_FOUND.getErrorCode(),
                    ServiceExceptionType.SERVICE_INDICATOR_IN_PRODUCTION_NOT_FOUND.getMessageForReasonType(), uuid);
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
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATOR_IN_DIFFUSION_NOT_FOUND.getErrorCode(),
                    ServiceExceptionType.SERVICE_INDICATOR_IN_DIFFUSION_NOT_FOUND.getMessageForReasonType(), uuid);
        }
        IndicatorVersion indicatorVersionDiffusion = getIndicatorsService().retrieveIndicatorVersion(ctx, uuid, indicator.getDiffusionVersion().getVersionNumber());
        return indicatorVersionDiffusion;
    }
    
    /**
     * Checks not exists another indicator with same code. Checks indicator retrieved not is actual indicator.
     */
    private void validateCodeUnique(ServiceContext ctx, String code, String actualUuid) throws MetamacException {
        List<Indicator> indicators = getIndicatorsService().findIndicators(ctx, code);
        if (indicators != null && indicators.size() != 0 && !indicators.get(0).getUuid().equals(actualUuid)) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATOR_ALREADY_EXIST_CODE_DUPLICATED.getErrorCode(),
                    ServiceExceptionType.SERVICE_INDICATOR_ALREADY_EXIST_CODE_DUPLICATED.getMessageForReasonType(), code);
        }
    }
}
