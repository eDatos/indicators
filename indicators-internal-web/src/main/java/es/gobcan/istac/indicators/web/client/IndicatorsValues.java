package es.gobcan.istac.indicators.web.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;

import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;

public class IndicatorsValues {

    private static List<QuantityUnitDto>            quantityUnits;
    private static List<UnitMultiplierDto>          unitMultipliers;
    private static List<GeographicalGranularityDto> geoGranularities;

    public static List<QuantityUnitDto> getQuantityUnits() {
        if (quantityUnits != null) {
            return quantityUnits;
        }
        return new ArrayList<QuantityUnitDto>();
    }

    public static void setQuantityUnits(List<QuantityUnitDto> quantityUnits) {
        sortQuantityUnits(quantityUnits);
        IndicatorsValues.quantityUnits = quantityUnits;
    }

    public static List<UnitMultiplierDto> getUnitMultipliers() {
        if (unitMultipliers != null) {
            return unitMultipliers;
        }
        return new ArrayList<UnitMultiplierDto>();
    }

    public static void setUnitMultipliers(List<UnitMultiplierDto> unitMultipliers) {
        IndicatorsValues.unitMultipliers = unitMultipliers;
    }

    public static List<GeographicalGranularityDto> getGeographicalGranularities() {
        if (geoGranularities != null) {
            return geoGranularities;
        }
        return new ArrayList<GeographicalGranularityDto>();
    }

    public static void setGeographicalGranularities(List<GeographicalGranularityDto> geoGranularities) {
        IndicatorsValues.geoGranularities = geoGranularities;
    }

    private static void sortQuantityUnits(List<QuantityUnitDto> units) {
        Collections.sort(units, new Comparator<QuantityUnitDto>() {

            @Override
            public int compare(QuantityUnitDto first, QuantityUnitDto second) {
                String firstText = InternationalStringUtils.getLocalisedString(first.getTitle());
                String secondText = InternationalStringUtils.getLocalisedString(second.getTitle());
                return firstText.compareTo(secondText);
            }
        });
    }
}
