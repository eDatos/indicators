package es.gobcan.istac.indicators.core.serviceapi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import es.gobcan.istac.indicators.core.repositoryimpl.IndicatorRepositoryTest;
import es.gobcan.istac.indicators.core.repositoryimpl.IndicatorVersionTimeCoverageRepositoryTest;

@RunWith(Suite.class)
// @formatter:off
@SuiteClasses({
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
    TimeVariableUtilsTest.class,
    IndicatorsServiceVersioningTest.class,
    IndicatorRepositoryTest.class,
    IndicatorVersionTimeCoverageRepositoryTest.class
                })
//@formatter:on
public class IndicatorsSuite {

}
