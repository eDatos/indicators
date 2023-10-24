package es.gobcan.istac.indicators.web.client;

import java.util.ArrayList;
import java.util.List;

import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;

public class IndicatorsValues {

    private static List<UnitMultiplierDto>          unitMultipliers;
    private static List<GeographicalGranularityDto> geoGranularities;

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
}
