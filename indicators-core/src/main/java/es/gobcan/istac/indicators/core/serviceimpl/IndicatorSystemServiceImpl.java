package es.gobcan.istac.indicators.core.serviceimpl;

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
    public IndicatorSystemVersion createIndicatorSystem(ServiceContext ctx, IndicatorSystemVersion indicatorSystemDraft) throws MetamacException {
        
        // Save draft version
        IndicatorSystemVersion indicatorSystemVersion = getIndicatorSystemVersionRepository().save(indicatorSystemDraft);
        
        // Save indicator
        indicatorSystemDraft.getIndicatorSystem().setDraftVersion(new IndicatorSystemVersionInformation(indicatorSystemVersion.getId(), indicatorSystemVersion.getVersionNumber()));
        getIndicatorSystemRepository().save(indicatorSystemDraft.getIndicatorSystem());
        
        return indicatorSystemVersion;
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
}
