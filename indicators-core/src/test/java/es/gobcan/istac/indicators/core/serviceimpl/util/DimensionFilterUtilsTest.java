package es.gobcan.istac.indicators.core.serviceimpl.util;

import static es.gobcan.istac.indicators.core.serviceimpl.util.TimeVariableUtils.parseTimeValue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.edatos.dataset.repository.repository.impl.util.Pair;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataTimeDimensionFilterVO;

public class DimensionFilterUtilsTest {

    @Test
    public void testFilterTimeCodes() throws MetamacException {

        List<String> allTimeCodesFromCoverage = Arrays.asList("2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008");
        List<TimeValue> coverage = new ArrayList<TimeValue>();
        for (String timeCode : allTimeCodesFromCoverage) {
            coverage.add(parseTimeValue(timeCode));
        }

        {
            IndicatorsDataTimeDimensionFilterVO filter = new IndicatorsDataTimeDimensionFilterVO();

            filter.setCodes(Arrays.asList("2000"));
            filter.setConditionLast(2);
            filter.setConditionRange(new Pair<TimeValue, TimeValue>(parseTimeValue("2002"), parseTimeValue("2004")));
            filter.setConditionAfter(parseTimeValue("2007"));
            List<String> filteredTimeCodes = DimensionFilterUtils.filterTimeCodes(filter, coverage);
            assertEquals(Arrays.asList("2000", "2002", "2003", "2004", "2007", "2008"), filteredTimeCodes);
        }

        {
            IndicatorsDataTimeDimensionFilterVO filter = new IndicatorsDataTimeDimensionFilterVO();
            List<String> filteredTimeCodes = DimensionFilterUtils.filterTimeCodes(filter, coverage);
            assertEquals(allTimeCodesFromCoverage, filteredTimeCodes);
        }

        // conditionRange
        {
            IndicatorsDataTimeDimensionFilterVO filter = new IndicatorsDataTimeDimensionFilterVO();
            filter.setConditionRange(new Pair<TimeValue, TimeValue>(parseTimeValue("2003"), null));
            List<String> filteredTimeCodes = DimensionFilterUtils.filterTimeCodes(filter, coverage);
            assertEquals(Arrays.asList("2003", "2004", "2005", "2006", "2007", "2008"), filteredTimeCodes);
        }
        {
            IndicatorsDataTimeDimensionFilterVO filter = new IndicatorsDataTimeDimensionFilterVO();
            filter.setConditionRange(new Pair<TimeValue, TimeValue>(null, parseTimeValue("2005")));
            List<String> filteredTimeCodes = DimensionFilterUtils.filterTimeCodes(filter, coverage);
            assertEquals(Arrays.asList("2000", "2001", "2002", "2003", "2004", "2005"), filteredTimeCodes);
        }
        {
            IndicatorsDataTimeDimensionFilterVO filter = new IndicatorsDataTimeDimensionFilterVO();
            filter.setConditionRange(new Pair<TimeValue, TimeValue>(parseTimeValue("2003"), parseTimeValue("2005")));
            List<String> filteredTimeCodes = DimensionFilterUtils.filterTimeCodes(filter, coverage);
            assertEquals(Arrays.asList("2003", "2004", "2005"), filteredTimeCodes);
        }
        {
            IndicatorsDataTimeDimensionFilterVO filter = new IndicatorsDataTimeDimensionFilterVO();
            filter.setConditionRange(new Pair<TimeValue, TimeValue>(null, null));
            List<String> filteredTimeCodes = DimensionFilterUtils.filterTimeCodes(filter, coverage);
            assertEquals(allTimeCodesFromCoverage, filteredTimeCodes);
        }

        // after and last
        {
            IndicatorsDataTimeDimensionFilterVO filter = new IndicatorsDataTimeDimensionFilterVO();
            filter.setConditionAfter(parseTimeValue("2006"));
            filter.setConditionLast(2);
            List<String> filteredTimeCodes = DimensionFilterUtils.filterTimeCodes(filter, coverage);
            assertEquals(Arrays.asList("2006", "2007", "2008"), filteredTimeCodes);
        }
        {
            IndicatorsDataTimeDimensionFilterVO filter = new IndicatorsDataTimeDimensionFilterVO();
            filter.setConditionAfter(parseTimeValue("2007"));
            filter.setConditionLast(3);
            List<String> filteredTimeCodes = DimensionFilterUtils.filterTimeCodes(filter, coverage);
            assertEquals(Arrays.asList("2006", "2007", "2008"), filteredTimeCodes);
        }
    }

    @Test
    public void testFilterTimeCodesWithMixedGranularities() throws MetamacException {

        List<String> allTimeCodesFromCoverage = Arrays.asList("1999", "2000M01", "2000M02", "2000M03", "2000M04", "2000M05", "2000M06", "2000M07", "2000", "2001");
        List<TimeValue> coverage = new ArrayList<TimeValue>();
        for (String timeCode : allTimeCodesFromCoverage) {
            coverage.add(parseTimeValue(timeCode));
        }

        {
            IndicatorsDataTimeDimensionFilterVO filter = new IndicatorsDataTimeDimensionFilterVO();
            filter.setGranularityCodes(Arrays.asList("YEARLY"));
            List<String> filteredTimeCodes = DimensionFilterUtils.filterTimeCodes(filter, coverage);
            assertEquals(Arrays.asList("1999", "2000", "2001"), filteredTimeCodes);
        }

        {
            IndicatorsDataTimeDimensionFilterVO filter = new IndicatorsDataTimeDimensionFilterVO();
            filter.setGranularityCodes(Arrays.asList("MONTHLY"));
            List<String> filteredTimeCodes = DimensionFilterUtils.filterTimeCodes(filter, coverage);
            assertEquals(Arrays.asList("2000M01", "2000M02", "2000M03", "2000M04", "2000M05", "2000M06", "2000M07"), filteredTimeCodes);
        }

        {
            IndicatorsDataTimeDimensionFilterVO filter = new IndicatorsDataTimeDimensionFilterVO();
            filter.setGranularityCodes(Arrays.asList("MONTHLY"));
            filter.setCodes(Arrays.asList("2000")); // Won´t appear, we work monthly
            filter.setConditionLast(2); // "2000M06", "2000M07"
            filter.setConditionRange(new Pair<TimeValue, TimeValue>(parseTimeValue("1999"), parseTimeValue("2000M02"))); // "2000M01", "2000M02"
            filter.setConditionAfter(parseTimeValue("2000M07")); // "2000M07"
            List<String> filteredTimeCodes = DimensionFilterUtils.filterTimeCodes(filter, coverage);
            assertEquals(Arrays.asList("2000M01", "2000M02", "2000M06", "2000M07"), filteredTimeCodes);
        }

        {
            IndicatorsDataTimeDimensionFilterVO filter = new IndicatorsDataTimeDimensionFilterVO();
            List<String> filteredTimeCodes = DimensionFilterUtils.filterTimeCodes(filter, coverage);
            assertEquals(allTimeCodesFromCoverage, filteredTimeCodes);
        }

        // conditionRange
        {
            IndicatorsDataTimeDimensionFilterVO filter = new IndicatorsDataTimeDimensionFilterVO();
            filter.setGranularityCodes(Arrays.asList("MONTHLY"));
            filter.setConditionRange(new Pair<TimeValue, TimeValue>(parseTimeValue("2000M03"), null));
            List<String> filteredTimeCodes = DimensionFilterUtils.filterTimeCodes(filter, coverage);
            assertEquals(Arrays.asList("2000M03", "2000M04", "2000M05", "2000M06", "2000M07"), filteredTimeCodes);
        }
        {
            IndicatorsDataTimeDimensionFilterVO filter = new IndicatorsDataTimeDimensionFilterVO();
            filter.setGranularityCodes(Arrays.asList("MONTHLY"));
            filter.setConditionRange(new Pair<TimeValue, TimeValue>(null, parseTimeValue("2000M05")));
            List<String> filteredTimeCodes = DimensionFilterUtils.filterTimeCodes(filter, coverage);
            assertEquals(Arrays.asList("2000M01", "2000M02", "2000M03", "2000M04", "2000M05"), filteredTimeCodes);
        }
        {
            IndicatorsDataTimeDimensionFilterVO filter = new IndicatorsDataTimeDimensionFilterVO();
            filter.setGranularityCodes(Arrays.asList("MONTHLY"));
            filter.setConditionRange(new Pair<TimeValue, TimeValue>(parseTimeValue("2000M03"), parseTimeValue("2000M05")));
            List<String> filteredTimeCodes = DimensionFilterUtils.filterTimeCodes(filter, coverage);
            assertEquals(Arrays.asList("2000M03", "2000M04", "2000M05"), filteredTimeCodes);
        }
        {
            IndicatorsDataTimeDimensionFilterVO filter = new IndicatorsDataTimeDimensionFilterVO();
            filter.setGranularityCodes(Arrays.asList("MONTHLY", "YEARLY"));
            filter.setConditionRange(new Pair<TimeValue, TimeValue>(null, null));
            List<String> filteredTimeCodes = DimensionFilterUtils.filterTimeCodes(filter, coverage);
            assertEquals(allTimeCodesFromCoverage, filteredTimeCodes);
        }
        {
            IndicatorsDataTimeDimensionFilterVO filter = new IndicatorsDataTimeDimensionFilterVO();
            filter.setGranularityCodes(Arrays.asList("MONTHLY"));
            filter.setConditionRange(new Pair<TimeValue, TimeValue>(null, null));
            List<String> filteredTimeCodes = DimensionFilterUtils.filterTimeCodes(filter, coverage);
            assertEquals(Arrays.asList("2000M01", "2000M02", "2000M03", "2000M04", "2000M05", "2000M06", "2000M07"), filteredTimeCodes);
        }

        // after and last
        {
            IndicatorsDataTimeDimensionFilterVO filter = new IndicatorsDataTimeDimensionFilterVO();
            filter.setGranularityCodes(Arrays.asList("MONTHLY"));
            filter.setConditionAfter(parseTimeValue("2000M05"));
            filter.setConditionLast(2);
            List<String> filteredTimeCodes = DimensionFilterUtils.filterTimeCodes(filter, coverage);
            assertEquals(Arrays.asList("2000M05", "2000M06", "2000M07"), filteredTimeCodes);
        }
        {
            IndicatorsDataTimeDimensionFilterVO filter = new IndicatorsDataTimeDimensionFilterVO();
            filter.setGranularityCodes(Arrays.asList("MONTHLY"));
            filter.setConditionAfter(parseTimeValue("2000M07"));
            filter.setConditionLast(3);
            List<String> filteredTimeCodes = DimensionFilterUtils.filterTimeCodes(filter, coverage);
            assertEquals(Arrays.asList("2000M05", "2000M06", "2000M07"), filteredTimeCodes);
        }
        {
            IndicatorsDataTimeDimensionFilterVO filter = new IndicatorsDataTimeDimensionFilterVO();
            filter.setConditionAfter(parseTimeValue("2000M07"));
            filter.setConditionLast(2);
            List<String> filteredTimeCodes = DimensionFilterUtils.filterTimeCodes(filter, coverage);
            assertEquals(Arrays.asList("2000M07", "2000", "2001"), filteredTimeCodes);
        }
        {
            IndicatorsDataTimeDimensionFilterVO filter = new IndicatorsDataTimeDimensionFilterVO();
            filter.setConditionAfter(parseTimeValue("2000M07"));
            filter.setConditionLast(3);
            List<String> filteredTimeCodes = DimensionFilterUtils.filterTimeCodes(filter, coverage);
            assertEquals(Arrays.asList("2000M07", "2000", "2001"), filteredTimeCodes);
        }
    }
}
