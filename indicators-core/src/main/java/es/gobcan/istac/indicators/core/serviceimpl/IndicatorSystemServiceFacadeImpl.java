package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.domain.IndicatorSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorSystemStateEnum;
import es.gobcan.istac.indicators.core.domain.IndicatorSystemVersion;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.mapper.Do2DtoMapper;
import es.gobcan.istac.indicators.core.mapper.Dto2DoMapper;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorSystemDto;
import es.gobcan.istac.indicators.core.serviceimpl.util.InvocationValidator;

/**
 * Implementation of IndicatorSystemServiceFacade.
 */
@Service("indicatorSystemServiceFacade")
public class IndicatorSystemServiceFacadeImpl extends IndicatorSystemServiceFacadeImplBase {

    @Autowired
    private Do2DtoMapper do2DtoMapper;
    
    @Autowired
    private Dto2DoMapper dto2DoMapper;

    
    public IndicatorSystemServiceFacadeImpl() {
    }

    /**
     * TODO Devolver una uri, en lugar del uuid (ojo! uris rests?)
     * TODO validación de code, title, acronym, uri (únicos)
     */
    public IndicatorSystemDto createIndicatorSystem(ServiceContext ctx, IndicatorSystemDto indicatorSystemDto) throws MetamacException {

        // Validation
        InvocationValidator.checkCreateIndicatorSystem(indicatorSystemDto);
        validateCodeUnique(ctx, indicatorSystemDto.getCode(), null);
        
        // Transform
        IndicatorSystem indicatorSystem = new IndicatorSystem();
        indicatorSystem.setPublishedVersion(null);
        dto2DoMapper.indicatorSystemDtoToDo(indicatorSystemDto, indicatorSystem, ctx);
        // Draft version
        IndicatorSystemVersion draftVersion = dto2DoMapper.indicatorSystemDtoToDo(indicatorSystemDto, ctx);
        draftVersion.setState(IndicatorSystemStateEnum.DRAFT);
        draftVersion.setVersionNumber(Long.valueOf(1));
        draftVersion.setPublishingDate(null);
        
        // Create
        IndicatorSystemVersion indicatorSystemVersionCreated = getIndicatorSystemService().createIndicatorSystem(ctx, indicatorSystem, draftVersion);
        
        // Transform to Dto
        indicatorSystemDto = do2DtoMapper.indicatorSystemDoToDto(indicatorSystemVersionCreated); 
        
        return indicatorSystemDto;
    }
    
    public IndicatorSystemDto retrieveIndicatorSystem(ServiceContext ctx, String uuid, Long version) throws MetamacException {
        
        // Retrieve version requested or last version
        IndicatorSystemVersion indicatorSystemVersion = null;
        if (version == null) {
            // Retrieve last version TODO retrieve published?
            IndicatorSystem indicatorSystem = retrieveIndicatorSystemByUuid(ctx, uuid);
            version = indicatorSystem.getDraftVersion() != null ? indicatorSystem.getDraftVersion().getVersionNumber() : indicatorSystem.getPublishedVersion().getVersionNumber();
        }
        indicatorSystemVersion = getIndicatorSystemService().retrieveIndicatorSystemVersion(ctx, uuid, version);

        // Transform to Dto
        IndicatorSystemDto indicatorSystemDto = do2DtoMapper.indicatorSystemDoToDto(indicatorSystemVersion); 
        return indicatorSystemDto;
    }

    public String makeDraftIndicatorSystem(ServiceContext ctx, IndicatorSystemDto indicatorSystemDto) throws MetamacException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("makeDraftIndicatorSystem not implemented");

    }

    public String updateIndicatorSystem(ServiceContext ctx, IndicatorSystemDto indicatorSystemDto) throws MetamacException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("updateIndicatorSystem not implemented");

    }

    public String publishIndicatorSystem(ServiceContext ctx, IndicatorSystemDto indicatorSystemDto) throws MetamacException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("publishIndicatorSystem not implemented");

    }

    public void deleteIndicatorSystem(ServiceContext ctx, String uri) throws MetamacException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("deleteIndicatorSystem not implemented");
    }
    
    private IndicatorSystem retrieveIndicatorSystemByUuid(ServiceContext ctx, String uuid) throws MetamacException {
        return getIndicatorSystemService().retrieveIndicatorSystem(ctx, uuid);
    }
    
    private IndicatorSystemVersion retrieveIndicatorSystemDraft(ServiceContext ctx, String uuid) throws MetamacException {
        IndicatorSystem indicatorSystem = retrieveIndicatorSystemByUuid(ctx, uuid);
        if (indicatorSystem.getDraftVersion() == null) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_NOT_FOUND.getErrorCode(), ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_NOT_FOUND.getMessageForReasonType(), uuid);
        }
        IndicatorSystemVersion indicatorSystemVersionDraft = getIndicatorSystemService().retrieveIndicatorSystemVersion(ctx, uuid, indicatorSystem.getDraftVersion().getVersionNumber());
        return indicatorSystemVersionDraft;
    }
    
    /**
     * Check not exists another indicator system with same code. Checks system retrieved not is actual system.
     */
    private void validateCodeUnique(ServiceContext ctx, String code, String actualUuid) throws MetamacException {
        List<IndicatorSystem> indicatorsSystems = getIndicatorSystemService().findIndicatorsSystems(ctx, code);
        if (indicatorsSystems != null && indicatorsSystems.size() != 0 && !indicatorsSystems.get(0).getUuid().equals(actualUuid)) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED.getErrorCode(), ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED.getMessageForReasonType(), code);
        }
    }
}
