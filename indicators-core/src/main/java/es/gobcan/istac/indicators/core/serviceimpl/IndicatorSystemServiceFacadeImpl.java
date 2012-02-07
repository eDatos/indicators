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
    
    private static final Long VERSION_INITIAL = Long.valueOf(1);
    
    public IndicatorSystemServiceFacadeImpl() {
    }

    /**
     * TODO Devolver una uri, en lugar del uuid (ojo! uris rests?)
     * 
     */
    public IndicatorSystemDto createIndicatorSystem(ServiceContext ctx, IndicatorSystemDto indicatorSystemDto) throws MetamacException {

        // Validation
        InvocationValidator.checkCreateIndicatorSystem(indicatorSystemDto, null);
        validateCodeUnique(ctx, indicatorSystemDto.getCode(), null);
        validateUriUnique(ctx, indicatorSystemDto.getUri(), null);
        
        // Transform
        IndicatorSystem indicatorSystem = new IndicatorSystem();
        indicatorSystem.setPublishedVersion(null);
        dto2DoMapper.indicatorSystemDtoToDo(indicatorSystemDto, indicatorSystem, ctx);
        // Draft version
        IndicatorSystemVersion draftVersion = dto2DoMapper.indicatorSystemDtoToDo(indicatorSystemDto, ctx);
        draftVersion.setState(IndicatorSystemStateEnum.DRAFT);
        draftVersion.setVersionNumber(VERSION_INITIAL);
        draftVersion.setPublishingDate(null);
        
        // Create
        IndicatorSystemVersion indicatorSystemVersionCreated = getIndicatorSystemService().createIndicatorSystem(ctx, indicatorSystem, draftVersion);
        
        // Transform to Dto
        indicatorSystemDto = do2DtoMapper.indicatorSystemDoToDto(indicatorSystemVersionCreated); 
        
        return indicatorSystemDto;
    }
    
    public IndicatorSystemDto retrieveIndicatorSystem(ServiceContext ctx, String uuid, Long version) throws MetamacException {
        
        // Validation
        InvocationValidator.checkRetrieveIndicatorSystem(uuid, version, null);
        
        // Retrieve version requested or last version
        IndicatorSystemVersion indicatorSystemVersion = null;
        if (version == null) {
            // Retrieve last version
            IndicatorSystem indicatorSystem = retrieveIndicatorSystemByUuid(ctx, uuid);
            version = indicatorSystem.getDraftVersion() != null ? indicatorSystem.getDraftVersion().getVersionNumber() : indicatorSystem.getPublishedVersion().getVersionNumber();
        }
        indicatorSystemVersion = getIndicatorSystemService().retrieveIndicatorSystemVersion(ctx, uuid, version);

        // Transform to Dto
        IndicatorSystemDto indicatorSystemDto = do2DtoMapper.indicatorSystemDoToDto(indicatorSystemVersion); 
        return indicatorSystemDto;
    }
    
    @Override
    public IndicatorSystemDto retrieveIndicatorSystemPublished(ServiceContext ctx, String uuid) throws MetamacException {
        
        // Validation
        InvocationValidator.checkRetrieveIndicatorSystemPublished(uuid, null);

        // Retrieve published version
        IndicatorSystemVersion publishedIndicatorSystemVersion = retrieveIndicatorSystemStatePublished(ctx, uuid);

        // Transform to Dto
        IndicatorSystemDto indicatorSystemDto = do2DtoMapper.indicatorSystemDoToDto(publishedIndicatorSystemVersion); 
        return indicatorSystemDto;
    }
    
    public void deleteIndicatorSystem(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation
        InvocationValidator.checkDeleteIndicatorSystem(uuid, null);
        
        // Retrieve
        IndicatorSystem indicatorSystem = retrieveIndicatorSystemByUuid(ctx, uuid);
        if (indicatorSystem.getDraftVersion() == null) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_NOT_FOUND_IN_STATE.getErrorCode(), ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_NOT_FOUND_IN_STATE.getMessageForReasonType(), uuid, IndicatorSystemStateEnum.DRAFT);       
        }
        
        // Delete whole indicators system or only last version
        if (VERSION_INITIAL.equals(indicatorSystem.getDraftVersion().getVersionNumber())) {
            // If indicator system is not published or archived, delete whole indicators system
            getIndicatorSystemService().deleteIndicatorSystem(ctx, uuid);
        } else {
            getIndicatorSystemService().deleteIndicatorSystemVersion(ctx, uuid, indicatorSystem.getDraftVersion().getVersionNumber());
            indicatorSystem.setDraftVersion(null);
            getIndicatorSystemService().updateIndicatorSystem(ctx, indicatorSystem);
        }
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
    
    private IndicatorSystem retrieveIndicatorSystemByUuid(ServiceContext ctx, String uuid) throws MetamacException {
        return getIndicatorSystemService().retrieveIndicatorSystem(ctx, uuid);
    }
    
    /**
     * Retrieves version published of an indicators system
     */
    private IndicatorSystemVersion retrieveIndicatorSystemStatePublished(ServiceContext ctx, String uuid) throws MetamacException {
        IndicatorSystem indicatorSystem = retrieveIndicatorSystemByUuid(ctx, uuid);
        if (indicatorSystem.getPublishedVersion() == null) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_NOT_FOUND_IN_STATE.getErrorCode(), ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_NOT_FOUND_IN_STATE.getMessageForReasonType(), uuid, IndicatorSystemStateEnum.PUBLISHED);
        }
        IndicatorSystemVersion indicatorSystemVersionPublished = getIndicatorSystemService().retrieveIndicatorSystemVersion(ctx, uuid, indicatorSystem.getPublishedVersion().getVersionNumber());
        return indicatorSystemVersionPublished;
    }
    
    /**
     * Checks not exists another indicator system with same code. Checks system retrieved not is actual system.
     */
    private void validateCodeUnique(ServiceContext ctx, String code, String actualUuid) throws MetamacException {
        List<IndicatorSystem> indicatorsSystems = getIndicatorSystemService().findIndicatorsSystems(ctx, code);
        if (indicatorsSystems != null && indicatorsSystems.size() != 0 && !indicatorsSystems.get(0).getUuid().equals(actualUuid)) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED.getErrorCode(), ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED.getMessageForReasonType(), code);
        }
    }
    
    /**
     * Checks not exists another indicator system with same uri. Checks system retrieved not is actual system.
     */
    private void validateUriUnique(ServiceContext ctx, String uri, String actualUuid) throws MetamacException {
        List<IndicatorSystemVersion> indicatorSystemVersions = getIndicatorSystemService().findIndicatorSystemVersions(ctx, uri);
        if (indicatorSystemVersions != null && indicatorSystemVersions.size() != 0) {
            for (IndicatorSystemVersion indicatorSystemVersion : indicatorSystemVersions) {
                if (!indicatorSystemVersion.getIndicatorSystem().getUuid().equals(actualUuid)) {
                    throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_ALREADY_EXIST_URI_DUPLICATED.getErrorCode(), ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_ALREADY_EXIST_URI_DUPLICATED.getMessageForReasonType(), uri);    
                }
            }
        }
    }
}
