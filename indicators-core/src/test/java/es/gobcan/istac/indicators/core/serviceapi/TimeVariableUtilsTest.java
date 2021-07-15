package es.gobcan.istac.indicators.core.serviceapi;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
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
    public void testCompareTimeValuesWeek() throws MetamacException {
        List<TimeValue> timeValues = buildTimeValues("2011W01", "2011W15", "2011W05");

        TimeVariableUtils.sortTimeValuesMostRecentFirstLastValue(timeValues);

        compareTimeValues(timeValues, "2011W15", "2011W05", "2011W01");
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

    @Test
    public void testParseHourlyTimeValue() throws MetamacException {
        {
            String hourlyTimeValue = "2008-11-29T23:45:36";

            TimeValue timeValue = TimeVariableUtils.parseTimeValue(hourlyTimeValue);

            assertEquals("2008", timeValue.getYear());
            assertEquals("11", timeValue.getMonth());
            assertEquals("29", timeValue.getDay());
            assertEquals("23", timeValue.getHour());
            assertEquals("45", timeValue.getMinutes());
            assertEquals("36", timeValue.getSeconds());
        }

        {
            String hourlyTimeValue = "2008-01-01T01:01:01";

            TimeValue timeValue = TimeVariableUtils.parseTimeValue(hourlyTimeValue);

            assertEquals("2008", timeValue.getYear());
            assertEquals("01", timeValue.getMonth());
            assertEquals("01", timeValue.getDay());
            assertEquals("01", timeValue.getHour());
            assertEquals("01", timeValue.getMinutes());
            assertEquals("01", timeValue.getSeconds());
        }
    }

    @Test
    public void testHourlyTimeValueToLastPossibleDate() throws MetamacException, ParseException {
        String hourlyTimeValue = "2008-08-29T23:45:36";

        TimeValue timeValue = TimeVariableUtils.parseTimeValue(hourlyTimeValue);

        Date expectedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(hourlyTimeValue);
        Date actualDate = TimeVariableUtils.timeValueToLastPossibleDate(timeValue);

        assertTrue(DateUtils.truncatedEquals(expectedDate, actualDate, Calendar.SECOND));
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
