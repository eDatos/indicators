package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.ArrayList;
import java.util.List;

import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataTimeDimensionFilterVO;

public class DimensionFilterUtils {

    public static List<String> filterTimeCodes(IndicatorsDataTimeDimensionFilterVO filter, List<TimeValue> sortedCoverage) {
        // The coverage is already sorted before entering here on TimeVariableUtils.sortTimeValuesMostRecentFirst
        if (filter != null) {
            List<String> filteredCodes = new ArrayList<String>();
            boolean granularityFilterEnabled = !filter.getGranularityCodes().isEmpty();

            boolean codeFilterEnabled = !filter.getCodes().isEmpty();
            boolean rangeFilterEnabled = filter.getConditionRange() != null;
            boolean lastFilterEnabled = filter.getConditionLast() != null;
            boolean afterFilterEnabled = filter.getConditionAfter() != null;

            boolean noFilterEnabled = !(codeFilterEnabled || rangeFilterEnabled || lastFilterEnabled || afterFilterEnabled);

            int lastAdded = 0;
            for (TimeValue timeValue : sortedCoverage) {

                // Granularity has a greater "weight". We wont´t return anything outside the granularity
                if (granularityFilterEnabled && !filter.getGranularityCodes().contains(timeValue.getGranularity().name())) {
                    continue;
                }

                // If no other filters are enabled, we simply add the value
                if (noFilterEnabled) {
                    filteredCodes.add(timeValue.getTimeValue());
                    continue;
                }

                // But if we have any other kind of filter, we need to check "additively" for each filter.
                // The code for this approach would be simpler if we do 2 or 3 loops, but it would reduce the performance

                // We process "last" first, because it´s simpler to keep track on "the last n" elements
                // Remember that coverage is ordered from most recent (or greater) to olders (or lower) value, so this is the correct order and place to handle it
                if (lastFilterEnabled) {
                    if (lastAdded < filter.getConditionLast()) {
                        filteredCodes.add(timeValue.getTimeValue());
                        lastAdded++;
                        continue;
                    }
                }

                // We add everything else
                if (codeFilterEnabled) {
                    if (filter.getCodes().contains(timeValue.getTimeValue())) {
                        filteredCodes.add(timeValue.getTimeValue());
                        continue;
                    }
                }

                if (rangeFilterEnabled) {
                    TimeValue first = filter.getConditionRange().getFirst();
                    TimeValue last = filter.getConditionRange().getSecond();
                    boolean valueIsInsideLowerLimit = first == null || TimeVariableUtils.firstValueEqualOrLowerThanSecondValueByHighestGranularityFirst(first, timeValue);
                    boolean valueIsInsideUpperLimit = last == null || TimeVariableUtils.firstValueEqualOrGreaterThanSecondValueByHighestGranularityFirst(last, timeValue);
                    if (valueIsInsideLowerLimit && valueIsInsideUpperLimit) {
                        filteredCodes.add(timeValue.getTimeValue());
                        continue;
                    }
                }

                if (afterFilterEnabled) {
                    if (TimeVariableUtils.firstValueEqualOrLowerThanSecondValueByHighestGranularityFirst(filter.getConditionAfter(), timeValue)) {
                        filteredCodes.add(timeValue.getTimeValue());
                        continue;
                    }
                }
            }
            return filteredCodes;
        }
        return DimensionFilterUtils.getCodesInTimeValues(sortedCoverage);

    }

    public static List<String> getCodesInTimeValues(List<TimeValue> timeValues) {
        List<String> codes = new ArrayList<String>();
        for (TimeValue timeValue : timeValues) {
            codes.add(timeValue.getTimeValue());
        }
        return codes;
    }

}
