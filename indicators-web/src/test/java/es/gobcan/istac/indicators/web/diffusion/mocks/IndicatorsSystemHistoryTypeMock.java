package es.gobcan.istac.indicators.web.diffusion.mocks;

import org.joda.time.DateTime;

import es.gobcan.istac.indicators.rest.types.IndicatorsSystemHistoryType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;


public class IndicatorsSystemHistoryTypeMock {

    public static IndicatorsSystemHistoryType mockIndicatorsSystemHistoryMonthsAgo(IndicatorsSystemType system, String version, int monthsAgo) {
        IndicatorsSystemHistoryType systemHist = new IndicatorsSystemHistoryType();
        
        systemHist.setIndicatorSystemId(system.getId());
        systemHist.setPublicationDate(new DateTime().minusMonths(monthsAgo).toDate());
        systemHist.setVersion(version);
        
        return systemHist;
    }
}
