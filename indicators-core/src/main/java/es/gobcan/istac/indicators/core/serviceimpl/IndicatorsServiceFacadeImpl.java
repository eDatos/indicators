package es.gobcan.istac.indicators.core.serviceimpl;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorStateEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersiontTypeEnum;
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

    // TODO atributo "code" único, no modificable?
    public IndicatorDto createIndicator(ServiceContext ctx, IndicatorDto indicatorDto) throws MetamacException {

        // Validation of parameters
        InvocationValidator.checkCreateIndicator(indicatorDto, null);

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
}
