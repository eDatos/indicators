package es.gobcan.istac.indicators.rest.test.mocks;

import es.gobcan.istac.indicators.core.domain.Dimension;


public class DimensionMock {
    
    public static Dimension mockDimension1() {
        Dimension dimension = new Dimension();
        dimension.setTitle(MockUtil.createInternationalStringDomain());
        return dimension;
    }

}
