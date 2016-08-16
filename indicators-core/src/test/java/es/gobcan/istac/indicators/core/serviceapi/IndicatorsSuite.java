package es.gobcan.istac.indicators.core.serviceapi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import es.gobcan.istac.indicators.core.repositoryimpl.IndicatorRepositoryTest;
import es.gobcan.istac.indicators.core.repositoryimpl.IndicatorVersionRepositoryTest;
import es.gobcan.istac.indicators.core.repositoryimpl.IndicatorVersionTimeCoverageRepositoryTest;

@RunWith(Suite.class)
// @formatter:off
@SuiteClasses({
    IndicatorsDataServiceBatchUpdateTest.class,
    IndicatorsDataServiceDataGpeTest.class,
    IndicatorsDataServicePopulateTest.class,
    IndicatorsDataServiceLastValueTest.class,
    IndicatorsCoverageServiceTest.class,
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
    TimeVariableUtilsTest.class,
    IndicatorsServiceVersioningTest.class,
    IndicatorRepositoryTest.class,
    IndicatorVersionRepositoryTest.class,
    IndicatorVersionTimeCoverageRepositoryTest.class
                })
//@formatter:on
public class IndicatorsSuite {

}
