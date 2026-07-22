package es.gobcan.istac.indicators.core.mapper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;

public class VariableElementAvro2DoMapperImplTest {

    @Test
    public void testBuildGlobalOrder_nullGranularity_returnsCode() {
        String result = VariableElementAvro2DoMapperImpl.buildGlobalOrder(null, "CANARIAS");
        assertEquals("CANARIAS", result);
    }

    @Test
    public void testBuildGlobalOrder_nullGranularityOrder_returnsCode() {
        GeographicalGranularity granularity = new GeographicalGranularity();
        granularity.setGranularityOrder(null);

        String result = VariableElementAvro2DoMapperImpl.buildGlobalOrder(granularity, "CANARIAS");
        assertEquals("CANARIAS", result);
    }

    @Test
    public void testBuildGlobalOrder_withOrder_returnsPaddedPrefix() {
        GeographicalGranularity granularity = new GeographicalGranularity();
        granularity.setGranularityOrder(3);

        String result = VariableElementAvro2DoMapperImpl.buildGlobalOrder(granularity, "CANARIAS");
        assertEquals("00003_CANARIAS", result);
    }

    @Test
    public void testBuildGlobalOrder_orderOne_returnsPaddedPrefix() {
        GeographicalGranularity granularity = new GeographicalGranularity();
        granularity.setGranularityOrder(1);

        String result = VariableElementAvro2DoMapperImpl.buildGlobalOrder(granularity, "TENERIFE");
        assertEquals("00001_TENERIFE", result);
    }

    @Test
    public void testBuildGlobalOrder_orderTen_returnsPaddedPrefix() {
        GeographicalGranularity granularity = new GeographicalGranularity();
        granularity.setGranularityOrder(10);

        String result = VariableElementAvro2DoMapperImpl.buildGlobalOrder(granularity, "LAS_PALMAS");
        assertEquals("00010_LAS_PALMAS", result);
    }

    @Test
    public void testBuildGlobalOrder_orderMaxFiveDigits_returnsPaddedPrefix() {
        GeographicalGranularity granularity = new GeographicalGranularity();
        granularity.setGranularityOrder(99999);

        String result = VariableElementAvro2DoMapperImpl.buildGlobalOrder(granularity, "MUNICIPIO");
        assertEquals("99999_MUNICIPIO", result);
    }
}
