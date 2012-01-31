package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.domain.IndicatorSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorSystemVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorSystemVersionInformation;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

/**
 * Implementation of IndicatorSystemService.
 */
@Service("indicatorSystemService")
public class IndicatorSystemServiceImpl extends IndicatorSystemServiceImplBase {

    public IndicatorSystemServiceImpl() {
    }

    @Override
    public IndicatorSystemVersion createIndicatorSystem(ServiceContext ctx, IndicatorSystem indicatorSystem, IndicatorSystemVersion indicatorSystemDraft) throws MetamacException {
        
        // Save indicator
        indicatorSystem = getIndicatorSystemRepository().save(indicatorSystem);
        
        // Save draft version
        indicatorSystemDraft.setIndicatorSystem(indicatorSystem);
        indicatorSystemDraft = getIndicatorSystemVersionRepository().save(indicatorSystemDraft);
        
        // Update indicator with draft version
        indicatorSystem.setDraftVersion(new IndicatorSystemVersionInformation(indicatorSystemDraft.getId(), indicatorSystemDraft.getVersionNumber()));
        indicatorSystem.getVersions().add(indicatorSystemDraft);
        getIndicatorSystemRepository().save(indicatorSystemDraft.getIndicatorSystem());
        
        return indicatorSystemDraft;
    }

    @Override
    public IndicatorSystem retrieveIndicatorSystem(ServiceContext ctx, String uuid) throws MetamacException {
        IndicatorSystem indicatorSystem = getIndicatorSystemRepository().retrieveIndicatorSystem(uuid);
        if (indicatorSystem == null) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_NOT_FOUND.getErrorCode(), ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_NOT_FOUND.getMessageForReasonType(), uuid);
        }
        return indicatorSystem;
    }
    
    @Override
    public IndicatorSystemVersion retrieveIndicatorSystemVersion(ServiceContext ctx, String uuid, Long versionNumber) throws MetamacException {
        IndicatorSystemVersion indicatorSystemVersion = getIndicatorSystemVersionRepository().retrieveIndicatorSystemVersion(uuid, versionNumber);
        if (indicatorSystemVersion == null) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_NOT_FOUND.getErrorCode(), ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_NOT_FOUND.getMessageForReasonType(), uuid, versionNumber);
        }
        return indicatorSystemVersion;
    }

    @Override
    public List<IndicatorSystem> findIndicatorsSystems(ServiceContext ctx, String code) throws MetamacException {
        return getIndicatorSystemRepository().findIndicatorsSystems(code);
    }

    @Override
    public List<IndicatorSystemVersion> findIndicatorSystemVersions(ServiceContext ctx, String uri) throws MetamacException {
        return getIndicatorSystemVersionRepository().findIndicatorSystemVersions(uri);
    }
}
