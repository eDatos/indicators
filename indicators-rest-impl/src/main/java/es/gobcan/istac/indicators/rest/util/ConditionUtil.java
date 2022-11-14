package es.gobcan.istac.indicators.rest.util;

import static es.gobcan.istac.indicators.core.serviceimpl.util.TimeVariableUtils.parseTimeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.rest.RequestUtil;

import es.gobcan.istac.edatos.dataset.repository.repository.impl.util.Pair;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum;
import es.gobcan.istac.indicators.core.serviceimpl.util.MetamacTimeUtils;
import es.gobcan.istac.indicators.core.serviceimpl.util.TimeVariableUtils;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataGeoDimensionFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataMeasureDimensionFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataTimeDimensionFilterVO;

public final class ConditionUtil {

    private ConditionUtil() {
    }

    public static IndicatorsDataTimeDimensionFilterVO normalizeAndFilterTimeDimension(Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities)
            throws MetamacException {
        List<String> selectedValues = new ArrayList<String>();
        IndicatorsDataTimeDimensionFilterVO filter = new IndicatorsDataTimeDimensionFilterVO();
        final List<String> temporalRepresentations = selectedRepresentations.get(IndicatorDataDimensionTypeEnum.TIME.name());
        if (temporalRepresentations != null) {
            for (String temporalRepresentation : temporalRepresentations) {
                // es.gobcan.istac.indicators.core.serviceimpl.util.TimeVariableUtils.parseTimeValue(String)
                Matcher matcherAfter = RequestUtil.PATTERN_AFTER.matcher(temporalRepresentation);
                if (matcherAfter.matches()) {
                    String startRange = matcherAfter.group(1);
                    TimeValue oldConditionAfter = filter.getConditionAfter();
                    TimeValue newConditionAfter = parseTimeValue(startRange);
                    // If we receive several ~after, we only keep the older or lowest
                    if (oldConditionAfter == null || TimeVariableUtils.firstValueEqualOrLowerThanSecondValueByHighestGranularityFirst(newConditionAfter, oldConditionAfter)) {
                        filter.setConditionAfter(newConditionAfter);
                    }
                    continue;
                }

                Matcher matcherLast = RequestUtil.PATTERN_LAST.matcher(temporalRepresentation);
                if (matcherLast.matches()) {
                    // If we receive several ~last, we only keep the largest
                    Integer lastN = Integer.valueOf(matcherLast.group(1));
                    Integer oldLastN = filter.getConditionLast();
                    if (oldLastN == null || oldLastN < lastN) {
                        filter.setConditionLast(lastN);
                    }
                    continue;
                }

                Matcher matcherRange = RequestUtil.PATTERN_RANGE.matcher(temporalRepresentation);
                if (matcherRange.matches()) {
                    // We only keep the last ~range
                    String startRange = matcherRange.group(1);
                    String endRange = matcherRange.group(2);
                    filter.setConditionRange(new Pair<TimeValue, TimeValue>(parseTimeValue(startRange), parseTimeValue(endRange)));
                    continue;
                }

                selectedValues.add(MetamacTimeUtils.normalizeToMetamacTimeValue(temporalRepresentation));
            }
        }
        List<String> selectedGranularityCodes = selectedGranularities.get(IndicatorDataDimensionTypeEnum.TIME.name());

        filter.setCodes(selectedValues);
        filter.setGranularityCodes(selectedGranularityCodes);

        return filter;
    }

    public static IndicatorsDataGeoDimensionFilterVO filterGeographicalDimension(Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities) {
        // GEOGRAPHICAL
        List<String> geographicalSelectedValues = selectedRepresentations.get(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name());
        List<String> geographicalSelectedGranularities = selectedGranularities.get(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name());

        IndicatorsDataGeoDimensionFilterVO filter = new IndicatorsDataGeoDimensionFilterVO();
        filter.setCodes(geographicalSelectedValues);
        filter.setGranularityCodes(geographicalSelectedGranularities);

        return filter;
    }

    public static IndicatorsDataMeasureDimensionFilterVO filterMeasureDimension(Map<String, List<String>> selectedRepresentations) {
        List<String> selectedValues = selectedRepresentations.get(IndicatorDataDimensionTypeEnum.MEASURE.name());

        IndicatorsDataMeasureDimensionFilterVO filter = new IndicatorsDataMeasureDimensionFilterVO();
        filter.setCodes(selectedValues);

        return filter;
    }

}
