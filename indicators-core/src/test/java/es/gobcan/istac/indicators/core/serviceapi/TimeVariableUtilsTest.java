package es.gobcan.istac.indicators.core.serviceapi;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.serviceimpl.util.TimeVariableUtils;

public class TimeVariableUtilsTest {

    @Test
    public void testCompareTimeValuesLastValue() throws MetamacException {
        List<TimeValue> timeValues = new ArrayList<TimeValue>();

        timeValues.add(TimeVariableUtils.parseTimeValue("2010"));
        timeValues.add(TimeVariableUtils.parseTimeValue("2010Q4"));
        TimeVariableUtils.sortTimeValuesMostRecentFirstLastValue(timeValues);

        assertEquals("2010Q4", timeValues.get(0).getTimeValue());
        assertEquals("2010", timeValues.get(1).getTimeValue());
    }

    @Test
    public void testCompareTimeValuesLastValueWeekMonth() throws MetamacException {
        List<TimeValue> timeValues = buildTimeValues("2006", "2007", "2011M01", "2011W04");

        TimeVariableUtils.sortTimeValuesMostRecentFirstLastValue(timeValues);

        compareTimeValues(timeValues, "2011M01", "2011W04", "2007", "2006");
    }

    @Test
    public void testCompareTimeValuesRegular() throws MetamacException {
        List<TimeValue> timeValues = new ArrayList<TimeValue>();

        timeValues.add(TimeVariableUtils.parseTimeValue("2010"));
        timeValues.add(TimeVariableUtils.parseTimeValue("2010Q4"));
        TimeVariableUtils.sortTimeValuesMostRecentFirst(timeValues);

        assertEquals("2010", timeValues.get(0).getTimeValue());
        assertEquals("2010Q4", timeValues.get(1).getTimeValue());
    }

    private List<TimeValue> buildTimeValues(String... values) throws MetamacException {
        List<TimeValue> timeValues = new ArrayList<TimeValue>();
        for (String value : values) {
            timeValues.add(TimeVariableUtils.parseTimeValue(value));
        }
        return timeValues;
    }

    private List<TimeValue> compareTimeValues(List<TimeValue> timeValues, String... expectedStrs) {
        assertEquals(expectedStrs.length, timeValues.size());
        for (int i = 0; i < timeValues.size(); i++) {
            assertEquals(expectedStrs[i], timeValues.get(i).getTimeValue());
        }
        return timeValues;
    }
}
