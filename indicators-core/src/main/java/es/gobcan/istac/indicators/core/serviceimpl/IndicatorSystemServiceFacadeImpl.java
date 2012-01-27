package es.gobcan.istac.indicators.core.serviceimpl;

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

    public IndicatorSystemDto retrieveIndicatorSystem(ServiceContext ctx, String uri) throws MetamacException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("retrieveIndicatorSystem not implemented");

    }

    public void deleteIndicatorSystem(ServiceContext ctx, String uri) throws MetamacException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("deleteIndicatorSystem not implemented");

    }
    
    private IndicatorSystemVersion retrieveIndicatorSystemDraft(ServiceContext ctx, String uuid) throws MetamacException {
        IndicatorSystem indicatorSystem = retrieveIndicatorSystemByUuid(ctx, uuid);
        if (indicatorSystem.getDraftVersion() == null) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_NOT_FOUND.getErrorCode(), ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_NOT_FOUND.getMessageForReasonType(), uuid, null);
        }
        IndicatorSystemVersion indicatorSystemVersionDraft = getIndicatorSystemService().retrieveIndicatorSystemVersion(ctx, uuid, indicatorSystem.getDraftVersion().getVersionNumber());
        return indicatorSystemVersionDraft;
    }
    
    private IndicatorSystem retrieveIndicatorSystemByUuid(ServiceContext ctx, String uuid) throws MetamacException {
        return getIndicatorSystemService().retrieveIndicatorSystem(ctx, uuid);
    }
}
