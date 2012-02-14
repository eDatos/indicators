package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionInformation;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

/**
 * Implementation of IndicatorsService.
 */
@Service("indicatorsService")
public class IndicatorsServiceImpl extends IndicatorsServiceImplBase {

    public IndicatorsServiceImpl() {
    }

    @Override
    public IndicatorVersion createIndicatorVersion(ServiceContext ctx, Indicator indicator, IndicatorVersion indicatorDraft) throws MetamacException {

        // Save indicator
        indicator = getIndicatorRepository().save(indicator);

        // Save draft version
        indicatorDraft.setIndicator(indicator);
        indicatorDraft = getIndicatorVersionRepository().save(indicatorDraft);

        // Update indicator with draft version
        indicator.setProductionVersion(new IndicatorVersionInformation(indicatorDraft.getId(), indicatorDraft.getVersionNumber()));
        indicator.getVersions().add(indicatorDraft);
        getIndicatorRepository().save(indicatorDraft.getIndicator());

        return indicatorDraft;
    }
    
    @Override
    public Indicator retrieveIndicator(ServiceContext ctx, String uuid) throws MetamacException {
        Indicator indicator = getIndicatorRepository().retrieveIndicator(uuid);
        if (indicator == null) {
            throw new MetamacException(ServiceExceptionType.SERVICE_INDICATOR_NOT_FOUND.getErrorCode(), ServiceExceptionType.SERVICE_INDICATOR_NOT_FOUND.getMessageForReasonType(), uuid);
        }
        return indicator;
    }
    
    @Override
    public IndicatorVersion retrieveIndicatorVersion(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {
        IndicatorVersion indicatorVersion = getIndicatorVersionRepository().retrieveIndicatorVersion(uuid, versionNumber);
        if (indicatorVersion == null) {
            if (versionNumber == null) {
                throw new MetamacException(ServiceExceptionType.SERVICE_INDICATOR_NOT_FOUND.getErrorCode(), ServiceExceptionType.SERVICE_INDICATOR_NOT_FOUND.getMessageForReasonType(), uuid);
            } else {
                throw new MetamacException(ServiceExceptionType.SERVICE_INDICATOR_VERSION_NOT_FOUND.getErrorCode(), ServiceExceptionType.SERVICE_INDICATOR_VERSION_NOT_FOUND.getMessageForReasonType(), uuid, versionNumber);
            }
        }
        return indicatorVersion;
    }    
    
    @Override
    public List<Indicator> findIndicators(ServiceContext ctx, String code) throws MetamacException {
        return getIndicatorRepository().findIndicators(code);
    }
}
