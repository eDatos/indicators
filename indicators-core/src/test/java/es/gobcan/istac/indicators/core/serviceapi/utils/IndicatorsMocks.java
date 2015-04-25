package es.gobcan.istac.indicators.core.serviceapi.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;

import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;
import es.gobcan.istac.indicators.core.enume.domain.QuantityUnitSymbolPositionEnum;

/**
 * Mocks
 */
public class IndicatorsMocks extends MetamacMocks {

    // -----------------------------------------------------------------
    // LISTS
    // -----------------------------------------------------------------
    public static List<String> mockUuidsList(Integer listSize) {
        List<String> randomUuidsList = new ArrayList<String>();

        for (int i = 0; i < listSize; i++) {
            randomUuidsList.add(UUID.randomUUID().toString());
        }
        return randomUuidsList;
    }

    // -----------------------------------------------------------------
    // INTERNATIONAL STRING
    // -----------------------------------------------------------------

    public static InternationalString mockInternationalString() {
        InternationalString internationalString = new InternationalString();
        LocalisedString es = new LocalisedString();
        es.setLabel(mockString(10) + " en Espanol");
        es.setLocale("es");
        es.setVersion(Long.valueOf(0));
        LocalisedString en = new LocalisedString();
        en.setLabel(mockString(10) + " in English");
        en.setLocale("en");
        en.setVersion(Long.valueOf(0));
        internationalString.addText(es);
        internationalString.addText(en);
        internationalString.setVersion(Long.valueOf(0));
        return internationalString;
    }

    /**
     * Mock an InternationalString with one locale
     */
    public static InternationalString mockInternationalString(String locale, String label) {
        InternationalString target = new InternationalString();
        LocalisedString localisedString = new LocalisedString();
        localisedString.setLocale(locale);
        localisedString.setLabel(label);
        target.addText(localisedString);
        return target;
    }

    /**
     * Mock an InternationalString with two locales
     */
    public static InternationalString mockInternationalString(String locale01, String label01, String locale02, String label02) {
        InternationalString target = new InternationalString();
        LocalisedString localisedString01 = new LocalisedString();
        localisedString01.setLocale(locale01);
        localisedString01.setLabel(label01);
        target.addText(localisedString01);

        LocalisedString localisedString02 = new LocalisedString();
        localisedString02.setLocale(locale02);
        localisedString02.setLabel(label02);
        target.addText(localisedString02);
        return target;
    }

    /**
     * Mock a InternationalString with one locale
     */
    public static InternationalStringDto mockInternationalStringDto(String locale, String label) {
        InternationalStringDto target = new InternationalStringDto();
        LocalisedStringDto localisedStringDto = new LocalisedStringDto();
        localisedStringDto.setLocale(locale);
        localisedStringDto.setLabel(label);
        target.addText(localisedStringDto);
        return target;
    }

    // -----------------------------------------------------------------
    // GEOGRAPHIC GRANULARITY AND VALUE
    // -----------------------------------------------------------------

    /**
     * Mock a GeographicalGranularity
     */
    public static GeographicalGranularityDto mockGeographicalGranularity(String code) {
        GeographicalGranularityDto geographicalGranularityDto = new GeographicalGranularityDto();
        geographicalGranularityDto.setCode(code);
        geographicalGranularityDto.setTitle(mockInternationalStringDto());
        return geographicalGranularityDto;
    }

    /**
     * Mock a GeographicalValue
     */
    public static GeographicalValueDto mockGeographicalValue(String code, String order, String granularityUuid) {
        GeographicalValueDto geographicalValueDto = new GeographicalValueDto();
        geographicalValueDto.setCode(code);
        geographicalValueDto.setOrder(order);
        geographicalValueDto.setTitle(mockInternationalStringDto());
        geographicalValueDto.setLatitude(20.0656233);
        geographicalValueDto.setLongitude(-25.454564645);

        GeographicalGranularityDto granularity = new GeographicalGranularityDto();
        granularity.setUuid(granularityUuid);
        geographicalValueDto.setGranularity(granularity);

        return geographicalValueDto;
    }

    // -----------------------------------------------------------------
    // QUANTITY UNIT
    // -----------------------------------------------------------------

    /**
     * Mock a QuantityUnit
     */
    public static QuantityUnitDto mockQuantityUnit(String locale, String label) {
        QuantityUnitDto quantityUnitDto = new QuantityUnitDto();
        quantityUnitDto.setSymbol(mockString(2));
        quantityUnitDto.setSymbolPosition(QuantityUnitSymbolPositionEnum.START);
        quantityUnitDto.setTitle(mockInternationalStringDto(locale, label));
        return quantityUnitDto;
    }

    // -----------------------------------------------------------------
    // UNIT MULTIPLIER
    // -----------------------------------------------------------------

    /**
     * Mock a UnitMultiplier
     */
    public static UnitMultiplierDto mockUnitMultiplier(Integer unitMultiplierValue) {
        UnitMultiplierDto unitMultiplierDto = new UnitMultiplierDto();
        unitMultiplierDto.setUnitMultiplier(unitMultiplierValue);
        unitMultiplierDto.setTitle(mockInternationalStringDto());
        return unitMultiplierDto;
    }
}