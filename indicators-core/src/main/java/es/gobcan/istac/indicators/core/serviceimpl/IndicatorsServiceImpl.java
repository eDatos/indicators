package es.gobcan.istac.indicators.core.serviceimpl;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionInformation;

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
}
