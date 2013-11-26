package es.gobcan.istac.indicators.core.serviceapi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
    IndicatorsDataServiceBatchUpdateTest.class,
    IndicatorsDataServiceDataGpeTest.class,
    IndicatorsDataServicePopulateTest.class,
    IndicatorsDataServiceLastValueTest.class,
    IndicatorsDataServiceRetrieveGeoTimeTest.class,
    IndicatorsServiceFacadeDataTest.class,
    IndicatorsServiceFacadeIndicatorsSystemsTest.class,
    IndicatorsServiceFacadeIndicatorsTest.class,
    IndicatorsServiceFacadeTest.class,
    IndicatorsServiceTest.class,
    IndicatorsSystemsServiceTest.class,
    DsplExporterServiceTest.class,
    DsplTransformerTest.class,
    SecurityIndicatorsServiceFacadeIndicatorsSystemsTest.class,
    SecurityIndicatorsServiceFacadeIndicatorsTest.class,
    FillData.class,
    IndicatorsConfigurationServiceTest.class,
    TimeVariableUtilsTest.class
                })
public class IndicatorsSuite {
    
}
