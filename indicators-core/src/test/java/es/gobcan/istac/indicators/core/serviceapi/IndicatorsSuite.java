package es.gobcan.istac.indicators.core.serviceapi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
    IndicatorsDataServiceBatchUpdateTest.class,
    IndicatorsDataServiceDataGpeTest.class,
    IndicatorsDataServicePopulateTest.class,
    IndicatorsDataServiceRetrieveGeoTimeTest.class,
    IndicatorsServiceFacadeDataTest.class,
    IndicatorsServiceFacadeIndicatorsSystemsTest.class,
    IndicatorsServiceFacadeIndicatorsTest.class,
    IndicatorsServiceFacadeTest.class,
    IndicatorsServiceTest.class,
    IndicatorsSystemsServiceTest.class,
    SecurityIndicatorsServiceFacadeIndicatorsSystemsTest.class,
    SecurityIndicatorsServiceFacadeIndicatorsTest.class
                })
public class IndicatorsSuite {
    
}
