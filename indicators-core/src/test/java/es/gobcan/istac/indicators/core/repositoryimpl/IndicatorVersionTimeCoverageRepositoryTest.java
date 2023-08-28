package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionTimeCoverageRepository;
import es.gobcan.istac.indicators.core.domain.Quantity;
import es.gobcan.istac.indicators.core.domain.UnitMultiplierRepository;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsBaseTest;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsService;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "txManager")
@Transactional
public class IndicatorVersionTimeCoverageRepositoryTest extends IndicatorsBaseTest {

    @Autowired
    IndicatorVersionTimeCoverageRepository indicatorVersionTimeCoverageRepository;

    @Autowired
    protected IndicatorsService            indicatorService;

    @Autowired
    protected UnitMultiplierRepository     unitMultiplierRepository;

    @Test
    public void testRetrieveGranularityCoverageFilteredByInstanceTimeValues() throws Exception {
        List<String> dataGpeUuids = IndicatorsMocks.mockUuidsList(4);
        IndicatorVersion indicatorVersionCreated = indicatorService.createIndicator(getServiceContextAdministrador(), mockIndicatorVersion());
        indicatorVersionTimeCoverageRepository.retrieveGranularityCoverageFilteredByInstanceTimeValues(indicatorVersionCreated, dataGpeUuids);
    }

    @Test
    public void testRetrieveGranularityCoverageFilteredByInstanceTimeValuesWith1000Elements() throws Exception {
        List<String> dataGpeUuids = IndicatorsMocks.mockUuidsList(1000);
        IndicatorVersion indicatorVersionCreated = indicatorService.createIndicator(getServiceContextAdministrador(), mockIndicatorVersion());
        indicatorVersionTimeCoverageRepository.retrieveGranularityCoverageFilteredByInstanceTimeValues(indicatorVersionCreated, dataGpeUuids);
    }

    @Test
    public void testRetrieveGranularityCoverageFilteredByInstanceTimeValuesWithMoreThan1000Elements() throws Exception {
        List<String> dataGpeUuids = IndicatorsMocks.mockUuidsList(1100);
        IndicatorVersion indicatorVersionCreated = indicatorService.createIndicator(getServiceContextAdministrador(), mockIndicatorVersion());
        indicatorVersionTimeCoverageRepository.retrieveGranularityCoverageFilteredByInstanceTimeValues(indicatorVersionCreated, dataGpeUuids);
    }

    @Test
    public void testRetrieveCoverageFilteredByInstanceTimeValues() throws Exception {
        List<String> dataGpeUuids = IndicatorsMocks.mockUuidsList(4);
        IndicatorVersion indicatorVersionCreated = indicatorService.createIndicator(getServiceContextAdministrador(), mockIndicatorVersion());
        indicatorVersionTimeCoverageRepository.retrieveCoverageFilteredByInstanceTimeValues(indicatorVersionCreated, dataGpeUuids);
    }

    @Test
    public void testRetrieveCoverageFilteredByInstanceTimeValuesWith1000Elements() throws Exception {
        List<String> dataGpeUuids = IndicatorsMocks.mockUuidsList(1000);
        IndicatorVersion indicatorVersionCreated = indicatorService.createIndicator(getServiceContextAdministrador(), mockIndicatorVersion());
        indicatorVersionTimeCoverageRepository.retrieveCoverageFilteredByInstanceTimeValues(indicatorVersionCreated, dataGpeUuids);
    }

    @Test
    public void testRetrieveCoverageFilteredByInstanceTimeValuesWithMoreThan1000Elements() throws Exception {
        List<String> dataGpeUuids = IndicatorsMocks.mockUuidsList(1100);
        IndicatorVersion indicatorVersionCreated = indicatorService.createIndicator(getServiceContextAdministrador(), mockIndicatorVersion());
        indicatorVersionTimeCoverageRepository.retrieveCoverageFilteredByInstanceTimeValues(indicatorVersionCreated, dataGpeUuids);
    }

    private IndicatorVersion mockIndicatorVersion() {
        IndicatorVersion indicatorVersion = new IndicatorVersion();
        indicatorVersion.setIndicator(new Indicator());
        indicatorVersion.getIndicator().setCode(("code" + (new Date()).getTime()));
        indicatorVersion.getIndicator().setViewCode(("viewCode" + (new Date()).getTime()));
        indicatorVersion.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorVersion.setCategoryElement(IndicatorsMocks.mockExternalItem("CATEGORY_ELEMENT_01", TypeExternalArtefactsEnum.CATEGORY_ELEMENT));
        indicatorVersion.setQuantity(new Quantity());
        indicatorVersion.getQuantity().setQuantityType(QuantityTypeEnum.AMOUNT);
        indicatorVersion.getQuantity().setUnit(IndicatorsMocks.mockExternalItem("QUANTITY_UNIT_01", TypeExternalArtefactsEnum.CODE));
        indicatorVersion.getQuantity().setUnitMultiplier(unitMultiplierRepository.retrieveUnitMultiplier(Integer.valueOf(1)));
        return indicatorVersion;
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsServiceFacadeIndicatorsTest.xml";
    }
}
