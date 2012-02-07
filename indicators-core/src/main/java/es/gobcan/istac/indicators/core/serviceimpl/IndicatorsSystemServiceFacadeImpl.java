package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemStateEnum;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.mapper.Do2DtoMapper;
import es.gobcan.istac.indicators.core.mapper.Dto2DoMapper;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.serviceimpl.util.InvocationValidator;

/**
 * Implementation of IndicatorsSystemServiceFacade.
 */
@Service("indicatorsSystemServiceFacade")
public class IndicatorsSystemServiceFacadeImpl extends IndicatorsSystemServiceFacadeImplBase {

    @Autowired
    private Do2DtoMapper do2DtoMapper;
    
    @Autowired
    private Dto2DoMapper dto2DoMapper;
    
    private static final Long VERSION_INITIAL = Long.valueOf(1);
    
    public IndicatorsSystemServiceFacadeImpl() {
    }

    /**
     * TODO Devolver una uri, en lugar del uuid (ojo! uris rests?)
     * 
     */
    public IndicatorsSystemDto createIndicatorsSystem(ServiceContext ctx, IndicatorsSystemDto indicatorsSystemDto) throws MetamacException {

        // Validation
        InvocationValidator.checkCreateIndicatorsSystem(indicatorsSystemDto, null);
        validateCodeUnique(ctx, indicatorsSystemDto.getCode(), null);
        validateUriUnique(ctx, indicatorsSystemDto.getUri(), null);
        
        // Transform
        IndicatorsSystem indicatorsSystem = new IndicatorsSystem();
        indicatorsSystem.setPublishedVersion(null);
        dto2DoMapper.indicatorsSystemDtoToDo(indicatorsSystemDto, indicatorsSystem, ctx);
        // Draft version
        IndicatorsSystemVersion draftVersion = dto2DoMapper.indicatorsSystemDtoToDo(indicatorsSystemDto, ctx);
        draftVersion.setState(IndicatorsSystemStateEnum.DRAFT);
        draftVersion.setVersionNumber(VERSION_INITIAL);
        draftVersion.setPublishingDate(null);
        
        // Create
        IndicatorsSystemVersion indicatorsSystemVersionCreated = getIndicatorsSystemService().createIndicatorsSystem(ctx, indicatorsSystem, draftVersion);
        
        // Transform to Dto
        indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersionCreated); 
        
        return indicatorsSystemDto;
    }
    
    public IndicatorsSystemDto retrieveIndicatorsSystem(ServiceContext ctx, String uuid, Long version) throws MetamacException {
        
        // Validation
        InvocationValidator.checkRetrieveIndicatorsSystem(uuid, version, null);
        
        // Retrieve version requested or last version
        IndicatorsSystemVersion indicatorsSystemVersion = null;
        if (version == null) {
            // Retrieve last version
            IndicatorsSystem indicatorsSystem = retrieveIndicatorsSystemByUuid(ctx, uuid);
            version = indicatorsSystem.getDraftVersion() != null ? indicatorsSystem.getDraftVersion().getVersionNumber() : indicatorsSystem.getPublishedVersion().getVersionNumber();
        }
        indicatorsSystemVersion = getIndicatorsSystemService().retrieveIndicatorsSystemVersion(ctx, uuid, version);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(indicatorsSystemVersion); 
        return indicatorsSystemDto;
    }
    
    @Override
    public IndicatorsSystemDto retrieveIndicatorsSystemPublished(ServiceContext ctx, String uuid) throws MetamacException {
        
        // Validation
        InvocationValidator.checkRetrieveIndicatorsSystemPublished(uuid, null);

        // Retrieve published version
        IndicatorsSystemVersion publishedIndicatorsSystemVersion = retrieveIndicatorsSystemStatePublished(ctx, uuid);

        // Transform to Dto
        IndicatorsSystemDto indicatorsSystemDto = do2DtoMapper.indicatorsSystemDoToDto(publishedIndicatorsSystemVersion); 
        return indicatorsSystemDto;
    }
    
    public void deleteIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException {

        // Validation
        InvocationValidator.checkDeleteIndicatorsSystem(uuid, null);
        
        // Retrieve
        IndicatorsSystem indicatorsSystem = retrieveIndicatorsSystemByUuid(ctx, uuid);
        if (indicatorsSystem.getDraftVersion() == null) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND_IN_STATE.getErrorCode(), ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND_IN_STATE.getMessageForReasonType(), uuid, IndicatorsSystemStateEnum.DRAFT);       
        }
        
        // Delete whole indicators system or only last version
        if (VERSION_INITIAL.equals(indicatorsSystem.getDraftVersion().getVersionNumber())) {
            // If indicator system is not published or archived, delete whole indicators system
            getIndicatorsSystemService().deleteIndicatorsSystem(ctx, uuid);
        } else {
            getIndicatorsSystemService().deleteIndicatorsSystemVersion(ctx, uuid, indicatorsSystem.getDraftVersion().getVersionNumber());
            indicatorsSystem.setDraftVersion(null);
            getIndicatorsSystemService().updateIndicatorsSystem(ctx, indicatorsSystem);
        }
    }

    public String makeDraftIndicatorsSystem(ServiceContext ctx, IndicatorsSystemDto indicatorsSystemDto) throws MetamacException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("makeDraftIndicatorsSystem not implemented");

    }

    public String updateIndicatorsSystem(ServiceContext ctx, IndicatorsSystemDto indicatorsSystemDto) throws MetamacException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("updateIndicatorsSystem not implemented");

    }

    public String publishIndicatorsSystem(ServiceContext ctx, IndicatorsSystemDto indicatorsSystemDto) throws MetamacException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("publishIndicatorsSystem not implemented");

    }
    
    private IndicatorsSystem retrieveIndicatorsSystemByUuid(ServiceContext ctx, String uuid) throws MetamacException {
        return getIndicatorsSystemService().retrieveIndicatorsSystem(ctx, uuid);
    }
    
    /**
     * Retrieves version published of an indicators system
     */
    private IndicatorsSystemVersion retrieveIndicatorsSystemStatePublished(ServiceContext ctx, String uuid) throws MetamacException {
        IndicatorsSystem indicatorsSystem = retrieveIndicatorsSystemByUuid(ctx, uuid);
        if (indicatorsSystem.getPublishedVersion() == null) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND_IN_STATE.getErrorCode(), ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND_IN_STATE.getMessageForReasonType(), uuid, IndicatorsSystemStateEnum.PUBLISHED);
        }
        IndicatorsSystemVersion indicatorsSystemVersionPublished = getIndicatorsSystemService().retrieveIndicatorsSystemVersion(ctx, uuid, indicatorsSystem.getPublishedVersion().getVersionNumber());
        return indicatorsSystemVersionPublished;
    }
    
    /**
     * Checks not exists another indicator system with same code. Checks system retrieved not is actual system.
     */
    private void validateCodeUnique(ServiceContext ctx, String code, String actualUuid) throws MetamacException {
        List<IndicatorsSystem> indicatorsSystems = getIndicatorsSystemService().findIndicatorsSystems(ctx, code);
        if (indicatorsSystems != null && indicatorsSystems.size() != 0 && !indicatorsSystems.get(0).getUuid().equals(actualUuid)) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED.getErrorCode(), ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED.getMessageForReasonType(), code);
        }
    }
    
    /**
     * Checks not exists another indicator system with same uri. Checks system retrieved not is actual system.
     */
    private void validateUriUnique(ServiceContext ctx, String uri, String actualUuid) throws MetamacException {
        List<IndicatorsSystemVersion> indicatorsSystemVersions = getIndicatorsSystemService().findIndicatorsSystemVersions(ctx, uri);
        if (indicatorsSystemVersions != null && indicatorsSystemVersions.size() != 0) {
            for (IndicatorsSystemVersion indicatorsSystemVersion : indicatorsSystemVersions) {
                if (!indicatorsSystemVersion.getIndicatorsSystem().getUuid().equals(actualUuid)) {
                    throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_ALREADY_EXIST_URI_DUPLICATED.getErrorCode(), ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_ALREADY_EXIST_URI_DUPLICATED.getMessageForReasonType(), uri);    
                }
            }
        }
    }
}
