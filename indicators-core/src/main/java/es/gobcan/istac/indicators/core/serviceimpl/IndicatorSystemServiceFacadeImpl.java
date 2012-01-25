package es.gobcan.istac.indicators.core.serviceimpl;

import org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;

import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorSystemDto;

/**
 * Implementation of IndicatorSystemServiceFacade.
 */
@Service("indicatorSystemServiceFacade")
public class IndicatorSystemServiceFacadeImpl
    extends IndicatorSystemServiceFacadeImplBase {
    public IndicatorSystemServiceFacadeImpl() {
    }

    public String createIndicatorSystem(ServiceContext ctx,
        IndicatorSystemDto indicatorSystemDto) throws ApplicationException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "createIndicatorSystem not implemented");

    }

    public String makeDraftIndicatorSystem(ServiceContext ctx,
        IndicatorSystemDto indicatorSystemDto) throws ApplicationException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "makeDraftIndicatorSystem not implemented");

    }

    public String updateIndicatorSystem(ServiceContext ctx,
        IndicatorSystemDto indicatorSystemDto) throws ApplicationException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "updateIndicatorSystem not implemented");

    }

    public String publishIndicatorSystem(ServiceContext ctx,
        IndicatorSystemDto indicatorSystemDto) throws ApplicationException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "publishIndicatorSystem not implemented");

    }

    public IndicatorSystemDto retrieveIndicatorSystem(ServiceContext ctx,
        String uri) throws ApplicationException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "retrieveIndicatorSystem not implemented");

    }

    public void deleteIndicatorSystem(ServiceContext ctx, String uri)
        throws ApplicationException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "deleteIndicatorSystem not implemented");

    }
}
