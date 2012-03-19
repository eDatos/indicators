package es.gobcan.istac.indicators.core.serviceapi;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;

public interface IndicatorsDataProviderService {

    /**
     * Retrieve the Data's structure info
     */
    public String retrieveDataStructureJson(ServiceContext ctx, String uuid) throws MetamacException;
    

    /**
     * Retrieve Data content
     */
    public String retrieveDataJson(ServiceContext ctx, String uuid) throws MetamacException;
}