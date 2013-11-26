package es.gobcan.istac.indicators.core.serviceapi.utils;

import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;

import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;
import es.gobcan.istac.indicators.core.enume.domain.QuantityUnitSymbolPositionEnum;

/**
 * Mocks
 */
public class IndicatorsMocks extends MetamacMocks {

    /**
     * Mock a InternationalString with locales "en" and "es"
     */
    public static InternationalStringDto mockInternationalString() {
        return mockInternationalStringDto();
    }

    /**
     * Mock a InternationalString with one locale
     */
    public static InternationalStringDto mockInternationalString(String locale, String label) {
        InternationalStringDto target = new InternationalStringDto();
        LocalisedStringDto localisedStringDto = new LocalisedStringDto();
        localisedStringDto.setLocale(locale);
        localisedStringDto.setLabel(label);
        target.addText(localisedStringDto);
        return target;
    }

    /**
     * Mock a GeographicalGranularity
     */
    public static GeographicalGranularityDto mockGeographicalGranularity(String code) {
        GeographicalGranularityDto geographicalGranularityDto = new GeographicalGranularityDto();
        geographicalGranularityDto.setCode(code);
        geographicalGranularityDto.setTitle(mockInternationalString());
        return geographicalGranularityDto;
    }

    /**
     * Mock a GeographicalValue
     */
    public static GeographicalValueDto mockGeographicalValue(String code, String order, String granularityUuid) {
        GeographicalValueDto geographicalValueDto = new GeographicalValueDto();
        geographicalValueDto.setCode(code);
        geographicalValueDto.setOrder(order);
        geographicalValueDto.setTitle(mockInternationalString());
        geographicalValueDto.setGranularityUuid(granularityUuid);
        geographicalValueDto.setLatitude(20.0656233);
        geographicalValueDto.setLongitude(-25.454564645);
        return geographicalValueDto;
    }

    /**
     * Mock a QuantityUnit
     */
    public static QuantityUnitDto mockQuantityUnit(String locale, String label) {
        QuantityUnitDto quantityUnitDto = new QuantityUnitDto();
        quantityUnitDto.setSymbol(mockString(2));
        quantityUnitDto.setSymbolPosition(QuantityUnitSymbolPositionEnum.START);
        quantityUnitDto.setTitle(mockInternationalString(locale, label));
        return quantityUnitDto;
    }

    /**
     * Mock a UnitMultiplier
     */
    public static UnitMultiplierDto mockUnitMultiplier(Integer unitMultiplierValue) {
        UnitMultiplierDto unitMultiplierDto = new UnitMultiplierDto();
        unitMultiplierDto.setUnitMultiplier(unitMultiplierValue);
        unitMultiplierDto.setTitle(mockInternationalString());
        return unitMultiplierDto;
    }
}