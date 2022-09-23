package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.ArrayList;
import java.util.List;

import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataTimeDimensionFilterVO;

public class DimensionFilterUtils {

    public static List<String> filterTimeCodes(IndicatorsDataTimeDimensionFilterVO filter, List<TimeValue> coverage) {
        if (filter != null) {
            List<String> filteredCodes = new ArrayList<String>();
            boolean codeFilterEnabled = !filter.getCodes().isEmpty();
            boolean granularityFilterEnabled = !filter.getGranularityCodes().isEmpty();

            for (int i = coverage.size() - 1; i >= 0; i--) {
                TimeValue timeValue = coverage.get(i);

                if (granularityFilterEnabled && !filter.getGranularityCodes().contains(timeValue.getGranularity().name())) {
                    continue;
                }

                if (codeFilterEnabled && !filter.getCodes().contains(timeValue.getTimeValue())) {
                    continue;
                }

                filteredCodes.add(0, timeValue.getTimeValue());
            }
            return filteredCodes;
        }
        return DimensionFilterUtils.getCodesInTimeValues(coverage);
    }

    public static List<String> getCodesInTimeValues(List<TimeValue> timeValues) {
        List<String> codes = new ArrayList<String>();
        for (TimeValue timeValue : timeValues) {
            codes.add(timeValue.getTimeValue());
        }
        return codes;
    }

}
