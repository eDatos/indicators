package es.gobcan.istac.indicators.rest.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.arte.statistic.dataset.repository.dto.ConditionDimensionDto;

import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.MeasureValue;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum;

public class ConditionUtil {

    private ConditionUtil() {
    }

    public static List<TimeValue> filterTimeValues(Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities, List<TimeValue> timeValues,
            List<ConditionDimensionDto> conditionDimensionDtos) {
        List<String> timeSelectedValues = selectedRepresentations.get(IndicatorDataDimensionTypeEnum.TIME.name());
        List<String> timeSelectedGranularities = selectedGranularities.get(IndicatorDataDimensionTypeEnum.TIME.name());

        if ((timeSelectedValues != null && timeSelectedValues.size() != 0) || (timeSelectedGranularities != null && timeSelectedGranularities.size() != 0)) {
            List<TimeValue> timeValuesNew = new ArrayList<TimeValue>();
            ConditionDimensionDto timeConditionDimensionDto = new ConditionDimensionDto();
            timeConditionDimensionDto.setDimensionId(IndicatorDataDimensionTypeEnum.TIME.name());

            for (TimeValue timeValue : timeValues) {
                // Granularity
                if (timeSelectedGranularities != null && timeSelectedGranularities.size() != 0) {
                    if (!timeSelectedGranularities.contains(timeValue.getGranularity().getName())) {
                        continue;
                    }
                }

                // Value
                if (timeSelectedValues != null && timeSelectedValues.size() != 0) {
                    if (!timeSelectedValues.contains(timeValue.getTimeValue())) {
                        continue;
                    }
                }

                timeConditionDimensionDto.getCodesDimension().add(timeValue.getTimeValue());
                timeValuesNew.add(timeValue);
            }

            conditionDimensionDtos.add(timeConditionDimensionDto);
            return timeValuesNew;
        }
        return timeValues;
    }

    public static List<GeographicalValue> filterGeographicalValues(Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities,
            List<GeographicalValue> geographicalValues, List<ConditionDimensionDto> conditionDimensionDtos) {
        // GEOGRAPHICAL
        List<String> geographicalSelectedValues = selectedRepresentations.get(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name());
        List<String> geographicalSelectedGranularities = selectedGranularities.get(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name());

        if ((geographicalSelectedValues != null && geographicalSelectedValues.size() != 0) || (geographicalSelectedGranularities != null && geographicalSelectedGranularities.size() != 0)) {

            List<GeographicalValue> geographicalValuesNew = new ArrayList<GeographicalValue>();
            ConditionDimensionDto geographicalConditionDimensionDto = new ConditionDimensionDto();
            geographicalConditionDimensionDto.setDimensionId(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name());

            for (GeographicalValue geographicalValue : geographicalValues) {
                // Granularity
                if (geographicalSelectedGranularities != null && geographicalSelectedGranularities.size() != 0) {
                    if (!geographicalSelectedGranularities.contains(geographicalValue.getGranularity().getCode())) {
                        continue;
                    }
                }

                // Value
                if (geographicalSelectedValues != null && geographicalSelectedValues.size() != 0) {
                    if (!geographicalSelectedValues.contains(geographicalValue.getCode())) {
                        continue;
                    }
                }

                geographicalConditionDimensionDto.getCodesDimension().add(geographicalValue.getCode());
                geographicalValuesNew.add(geographicalValue);
            }

            conditionDimensionDtos.add(geographicalConditionDimensionDto);
            return geographicalValuesNew;
        }
        return geographicalValues;
    }
    public static List<MeasureValue> filterMeasureValues(Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities, List<MeasureValue> measureValues,
            List<ConditionDimensionDto> conditionDimensionDtos) {
        // GEOGRAPHICAL
        List<String> measureSelectedValues = selectedRepresentations.get(IndicatorDataDimensionTypeEnum.MEASURE.name());

        if (measureSelectedValues != null && measureSelectedValues.size() != 0) {
            List<MeasureValue> measureValuesNew = new ArrayList<MeasureValue>();
            ConditionDimensionDto measureConditionDimensionDto = new ConditionDimensionDto();
            measureConditionDimensionDto.setDimensionId(IndicatorDataDimensionTypeEnum.MEASURE.name());

            for (MeasureValue measureValue : measureValues) {
                // Value
                if (measureSelectedValues != null && measureSelectedValues.size() != 0) {
                    if (!measureSelectedValues.contains(measureValue.getMeasureValue().getName())) {
                        continue;
                    }
                }

                measureConditionDimensionDto.getCodesDimension().add(measureValue.getMeasureValue().getName());
                measureValuesNew.add(measureValue);
            }

            conditionDimensionDtos.add(measureConditionDimensionDto);
            return measureValuesNew;
        }
        return measureValues;
    }

}
