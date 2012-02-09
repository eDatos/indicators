package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersionInformation;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemStateEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

/**
 * Implementation of IndicatorsSystemService.
 */
@Service("indicatorsSystemService")
public class IndicatorsSystemServiceImpl extends IndicatorsSystemServiceImplBase {

    public IndicatorsSystemServiceImpl() {
    }

    @Override
    public IndicatorsSystemVersion createIndicatorsSystemVersion(ServiceContext ctx, IndicatorsSystem indicatorsSystem, IndicatorsSystemVersion indicatorsSystemDraft) throws MetamacException {
       
        // Save indicator
        indicatorsSystem = getIndicatorsSystemRepository().save(indicatorsSystem);
        
        // Save draft version
        indicatorsSystemDraft.setIndicatorsSystem(indicatorsSystem);
        indicatorsSystemDraft = getIndicatorsSystemVersionRepository().save(indicatorsSystemDraft);
        
        // Update indicator with draft version
        indicatorsSystem.setProductionVersion(new IndicatorsSystemVersionInformation(indicatorsSystemDraft.getId(), indicatorsSystemDraft.getVersionNumber()));
        indicatorsSystem.getVersions().add(indicatorsSystemDraft);
        getIndicatorsSystemRepository().save(indicatorsSystemDraft.getIndicatorsSystem());
        
        return indicatorsSystemDraft;
    }

    @Override
    public IndicatorsSystem retrieveIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException {
        IndicatorsSystem indicatorsSystem = getIndicatorsSystemRepository().retrieveIndicatorsSystem(uuid);
        if (indicatorsSystem == null) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND.getErrorCode(), ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND.getMessageForReasonType(), uuid);
        }
        return indicatorsSystem;
    }
    
    @Override
    public IndicatorsSystemVersion retrieveIndicatorsSystemVersion(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {
        IndicatorsSystemVersion indicatorsSystemVersion = getIndicatorsSystemVersionRepository().retrieveIndicatorsSystemVersion(uuid, versionNumber);
        if (indicatorsSystemVersion == null) {
            if (versionNumber == null) {
                throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND.getErrorCode(), ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND.getMessageForReasonType(), uuid);
            } else {
                throw new MetamacException(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND_IN_VERSION.getErrorCode(), ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND_IN_VERSION.getMessageForReasonType(), uuid, versionNumber);
            }
        }
        return indicatorsSystemVersion;
    }
    
    @Override
    public void updateIndicatorsSystem(ServiceContext ctx, IndicatorsSystem indicatorsSystem) throws MetamacException {
        getIndicatorsSystemRepository().save(indicatorsSystem);
    }
    
    @Override
    public void updateIndicatorsSystemVersion(ServiceContext ctx, IndicatorsSystemVersion indicatorsSystemVersion) throws MetamacException {
        getIndicatorsSystemVersionRepository().save(indicatorsSystemVersion);
    }
    
    @Override
    public void deleteIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException {
        IndicatorsSystem indicatorsSystem = retrieveIndicatorsSystem(ctx, uuid);
        getIndicatorsSystemRepository().delete(indicatorsSystem);
    }

    @Override
    public void deleteIndicatorsSystemVersion(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorsSystemVersion(ctx, uuid, versionNumber);
        IndicatorsSystem indicatorsSystem = indicatorsSystemVersion.getIndicatorsSystem();
        indicatorsSystem.getVersions().remove(indicatorsSystemVersion);
        
        // Update
        getIndicatorsSystemRepository().save(indicatorsSystem);
        getIndicatorsSystemVersionRepository().delete(indicatorsSystemVersion);
    }
    
    @Override
    public List<IndicatorsSystem> findIndicatorsSystems(ServiceContext ctx, String code) throws MetamacException {
        return getIndicatorsSystemRepository().findIndicatorsSystems(code);
    }

    // TODO criteria
    @Override
    public List<IndicatorsSystemVersion> findIndicatorsSystemVersions(ServiceContext ctx, String uri, IndicatorsSystemStateEnum state) throws MetamacException {
        return getIndicatorsSystemVersionRepository().findIndicatorsSystemVersions(uri, state);
    }

    @Override
    public Dimension createDimension(ServiceContext ctx, Dimension dimension) throws MetamacException {
        return getDimensionRepository().save(dimension);
    }

    @Override
    public Dimension retrieveDimension(ServiceContext ctx, String uuid) throws MetamacException {
        Dimension dimension = getDimensionRepository().findDimension(uuid);
        if (dimension == null) {
            throw new MetamacException(ServiceExceptionType.SERVICE_DIMENSION_NOT_FOUND.getErrorCode(), ServiceExceptionType.SERVICE_DIMENSION_NOT_FOUND.getMessageForReasonType(), uuid);
        }
        return dimension;
    }
    
    @Override
    public Dimension updateDimension(ServiceContext ctx, Dimension dimension) throws MetamacException {
        return getDimensionRepository().save(dimension);
    }
    

}
