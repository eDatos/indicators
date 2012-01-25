package es.gobcan.istac.indicators.core.service.impl;

import org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;

import org.springframework.stereotype.Service;

/**
 * Implementation of IndicatorsSystemServiceFacade.
 */
@Service("indicatorsSystemServiceFacade")
public class IndicatorsSystemServiceFacadeImpl
    extends IndicatorsSystemServiceFacadeImplBase {
    public IndicatorsSystemServiceFacadeImpl() {
    }

    public String createIndicatorSystem(ServiceContext ctx)
        throws ApplicationException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "createIndicatorSystem not implemented");

    }
}
