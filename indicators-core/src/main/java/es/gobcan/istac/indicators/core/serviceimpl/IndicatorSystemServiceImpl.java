package es.gobcan.istac.indicators.core.serviceimpl;

import org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;

import org.springframework.stereotype.Service;

/**
 * Implementation of IndicatorSystemService.
 */
@Service("indicatorSystemService")
public class IndicatorSystemServiceImpl extends IndicatorSystemServiceImplBase {
    public IndicatorSystemServiceImpl() {
    }

    public void createIndicatorSystem(ServiceContext ctx)
        throws ApplicationException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "createIndicatorSystem not implemented");

    }
}
