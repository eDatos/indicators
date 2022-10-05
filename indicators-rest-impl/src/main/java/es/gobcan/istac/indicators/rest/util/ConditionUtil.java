package es.gobcan.istac.indicators.rest.util;

import static es.gobcan.istac.indicators.core.serviceimpl.util.TimeVariableUtils.parseTimeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.siemac.edatos.core.common.constants.shared.SDMXCommonRegExpV2_1;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.edatos.dataset.repository.repository.impl.util.Pair;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum;
import es.gobcan.istac.indicators.core.serviceimpl.util.MetamacTimeUtils;
import es.gobcan.istac.indicators.core.serviceimpl.util.TimeVariableUtils;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataGeoDimensionFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataMeasureDimensionFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataTimeDimensionFilterVO;

public final class ConditionUtil {

    private static final String  RANGE_PATTERN_REGEX = "~range=(" + removeCapturing(SDMXCommonRegExpV2_1.OBSERVATIONAL_TIME_PERIOD) + ");("
            + removeCapturing(SDMXCommonRegExpV2_1.OBSERVATIONAL_TIME_PERIOD) + ")";

    private static final String  AFTER_PATTERN_REGEX = "~after=(" + removeCapturing(SDMXCommonRegExpV2_1.OBSERVATIONAL_TIME_PERIOD) + ")";

    private static final String  LAST_PATTERN_REGEX  = "~last=(\\d+)";

    private static final String  CODE                = removeCapturing(RANGE_PATTERN_REGEX) + "|" + removeCapturing(AFTER_PATTERN_REGEX) + "|" + removeCapturing(LAST_PATTERN_REGEX) + "|"
            + removeCapturing(SDMXCommonRegExpV2_1.OBSERVATIONAL_TIME_PERIOD) + "|" + removeCapturing(SDMXCommonRegExpV2_1.IDTYPE);

    // Here we would have something like:
    // Pattern.compile("(\\w+)\\[((" + CODE + "|" + "\\|" + ")+)\\]")
    // but CODE includes TIME_RANGE_TYPE_1 and others that include ., so it matchs ] and breaks. That´s why it doesn´t work,
    // and that´s why we need to use [^\\]] instead. Because we later use patternCodes again, we are safe, but it would probably
    // be better to go to SDMXCommonRegExpV2_1.OBSERVATIONAL_TIME_PERIOD and modify it.
    private static final Pattern patternDimension    = Pattern.compile("(\\w+)\\[((" + "[^\\]]" + ")+)\\]");

    // return Pattern.compile("^(" + SDMXCommonRegExpV2_1.OBSERVATIONAL_TIME_PERIOD + "|" + SDMXCommonRegExpV2_1.IDTYPE + ")" + "$");
    private static final Pattern patternCodes        = Pattern.compile("^(" + CODE + ")$");

    // Dates after or equals to a date: dim=TIME_PERIOD:~after=1999
    public static final Pattern  patternAfter        = Pattern.compile(AFTER_PATTERN_REGEX);

    // Date range, inclusive: dim=TIME_PERIOD:~range=2009;2010
    public static final Pattern  patternRange        = Pattern.compile(RANGE_PATTERN_REGEX);

    // Last n elements: dim=TIME_PERIOD:~last=2
    public static final Pattern  patternLast         = Pattern.compile(LAST_PATTERN_REGEX);
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
                Matcher matcherAfter = patternAfter.matcher(temporalRepresentation);
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

                Matcher matcherLast = patternLast.matcher(temporalRepresentation);
                if (matcherLast.matches()) {
                    // If we receive several ~last, we only keep the largest
                    Integer lastN = Integer.valueOf(matcherLast.group(1));
                    Integer oldLastN = filter.getConditionLast();
                    if (oldLastN == null || oldLastN < lastN) {
                        filter.setConditionLast(lastN);
                    }
                    continue;
                }

                Matcher matcherRange = patternRange.matcher(temporalRepresentation);
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

    protected static Pattern getPatternCode() {
        return patternCodes;
    }

    protected static Pattern getPatternDimension() {
        return patternDimension;
    }

    private static String removeCapturing(String regex) {
        return regex.replace("(?:", "(").replace("(", "(?:");
    }
}
