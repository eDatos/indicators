package es.gobcan.istac.indicators.rest.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class RequestUtilTest {

    private void assertCodes(List<String> parsedCodes, String... expectedParsedCodes) {
        assertEquals(expectedParsedCodes.length, parsedCodes.size());
        for (String expectedParsedCode : expectedParsedCodes) {
            assertTrue(parsedCodes.contains(expectedParsedCode));
        }
    }

    private List<String> parseCodes(String codes) {
        return RequestUtil.parseCodes(RequestUtil.getPatternCode(), codes);
    }

    @Test
    public void testParseCodes() {
        assertCodes(parseCodes("000|001|002"), "000", "001", "002");

        assertCodes(parseCodes("005|006"), "005", "006");

        assertCodes(parseCodes("ABSOLUTE"), "ABSOLUTE");

        assertCodes(parseCodes("2012"), "2012");

        // REPORTING_YEAR_TYPE
        assertCodes(parseCodes("2020-A1"), "2020-A1");
        assertCodes(parseCodes("2020-A1|2021-A1|2022-A1"), "2020-A1", "2021-A1", "2022-A1");

        // REPORTING_SEMESTER_TYPE
        assertCodes(parseCodes("2001-S1"), "2001-S1");
        assertCodes(parseCodes("2001-S1|2001-S2|2002-S1|2002-S2"), "2001-S1", "2001-S2", "2002-S1", "2002-S2");

        // REPORTING_TRIMESTER_TYPE
        assertCodes(parseCodes("2002-T2"), "2002-T2");
        assertCodes(parseCodes("2002-T1|2002-T2|2002-T3"), "2002-T1", "2002-T2", "2002-T3");

        // REPORTING_QUARTER_TYPE
        assertCodes(parseCodes("2003-Q3"), "2003-Q3");
        assertCodes(parseCodes("2003-Q1|2003-Q2|2003-Q3|2003-Q4"), "2003-Q1", "2003-Q2", "2003-Q3", "2003-Q4");

        // REPORTING_MONTH_TYPE
        assertCodes(parseCodes("2020-M06"), "2020-M06");
        assertCodes(parseCodes("2020-M06|2020-M07|2020-M08"), "2020-M06", "2020-M07", "2020-M08");
        assertCodes(parseCodes("2004-M12|2005-M01|2005-M02|2005-M03|2005-M04|2005-M05"), "2004-M12", "2005-M01", "2005-M02", "2005-M03", "2005-M04", "2005-M05");

        // REPORTING_WEEK_TYPE
        assertCodes(parseCodes("2005-W52"), "2005-W52");
        assertCodes(parseCodes("2005-W52|2006-W01|2006-W02|2006-W03|2006-W04"), "2005-W52", "2006-W01", "2006-W02", "2006-W03", "2006-W04");

        // REPORTING_DAY_TYPE
        assertCodes(parseCodes("2006-D010"), "2006-D010");
        assertCodes(parseCodes("2006-D010|2006-D011|2006-D012|2006-D365"), "2006-D010", "2006-D011", "2006-D012", "2006-D365");

        // DATE_TIME
        assertCodes(parseCodes("2013-07-24T08:21:52.519+01:00"), "2013-07-24T08:21:52.519+01:00");
        assertCodes(parseCodes("2013-07-24T08:22:52.519+01:00|2013-07-24T08:22:52.519+01:00|2013-08-24T08:21:52.519+01:00|2014-07-24T08:21:52.519+01:00"), "2013-07-24T08:22:52.519+01:00",
                "2013-07-24T08:22:52.519+01:00", "2013-08-24T08:21:52.519+01:00", "2014-07-24T08:21:52.519+01:00");

        // G_YEAR
        assertCodes(parseCodes("2012"), "2012");
        assertCodes(parseCodes("2012|2013|2014|2015"), "2012", "2013", "2014", "2015");
        assertCodes(parseCodes("2004Z"), "2004Z");
        assertCodes(parseCodes("2004Z|2005Z|2006Z|2007Z"), "2004Z", "2005Z", "2006Z", "2007Z");
        assertCodes(parseCodes("-0045"), "-0045");
        assertCodes(parseCodes("-0045|-0046|-0047"), "-0045", "-0046", "-0047");
        // assertCodes(parseCodes("2004-05:00"), "2004-05:00");
        // assertCodes(parseCodes("2004-05:00|2004-06:00|2004-07:00"), "2004-05:00", "2004-06:00", "2004-07:00");

        // G_YEAR_MONTH
        assertCodes(parseCodes("2013-10"), "2013-10");
        assertCodes(parseCodes("2013-10|2014-10|2015-11|2018-01"), "2013-10", "2014-10", "2015-11", "2018-01");
        assertCodes(parseCodes("2001-10+02:00"), "2001-10+02:00");
        assertCodes(parseCodes("2001-10+02:00|2002-10+02:00|2003-10+02:00"), "2001-10+02:00", "2002-10+02:00", "2003-10+02:00");
        assertCodes(parseCodes("2001-10Z"), "2001-10Z");
        assertCodes(parseCodes("2001-09Z|2002-10Z|2003-11Z|2004-12Z"), "2001-09Z", "2002-10Z", "2003-11Z", "2004-12Z");
        assertCodes(parseCodes("-2001-10"), "-2001-10");
        assertCodes(parseCodes("-2001-10|-2002-10|-2003-10"), "-2001-10", "-2002-10", "-2003-10");

        // G_DATE
        assertCodes(parseCodes("2013-08-30"), "2013-08-30");
        assertCodes(parseCodes("2013-08-30|2013-09-01"), "2013-08-30", "2013-09-01");
        assertCodes(parseCodes("2013-08-30+02:00"), "2013-08-30+02:00");
        assertCodes(parseCodes("2013-08-30+02:00|2012-08-30+02:00|2011-08-30+02:00"), "2013-08-30+02:00", "2012-08-30+02:00", "2011-08-30+02:00");

        // TIME_RANGE_DATETIME
        assertCodes(parseCodes("2013-07-24T13:21:52.519+01:00/P1M"), "2013-07-24T13:21:52.519+01:00/P1M");
        // assertCodes(parseCodes("2013-07-24T13:21:52.519+01:00/P1M|2014-07-24T13:21:52.519+01:00/P1M|2015-08-24T13:21:52.519+01:00/P1M"), "2013-07-24T13:21:52.519+01:00/P1M",
        // "2014-07-24T13:21:52.519+01:00/P1M", "2015-08-24T13:21:52.519+01:00/P1M");

        assertCodes(parseCodes("1999-07-01T03:04:05.519+01:00/P1Y1M2D"), "1999-07-01T03:04:05.519+01:00/P1Y1M2D");
        // assertCodes(parseCodes("1999-07-01T03:04:05.519+01:00/P1Y1M2D|2000-07-01T03:04:05.519+01:00/P1Y1M2D"), "1999-07-01T03:04:05.519+01:00/P1Y1M2D",
        // "2000-07-01T03:04:05.519+01:00/P1Y1M2D");

        // TIME_RANGE_DATE
        assertCodes(parseCodes("2013-07-24/P1M"), "2013-07-24/P1M");
        // assertCodes(parseCodes("2013-07-24/P1M|2013-07-25/P1M|2013-07-26/P1M|2013-07-27/P1M"), "2013-07-24/P1M", "2013-07-25/P1M", "2013-07-26/P1M", "2013-07-27/P1M");
    }

    @Test
    public void testParseParamExpression() {
        {
            Map<String, List<String>> parseParamExpression = RequestUtil.parseParamExpression("");
            assertTrue(parseParamExpression.isEmpty());
        }

        {
            Map<String, List<String>> parseParamExpression = RequestUtil.parseParamExpression(null);
            assertTrue(parseParamExpression.isEmpty());
        }

        {
            Map<String, List<String>> parseParamExpression = RequestUtil.parseParamExpression("MOTIVOS_ESTANCIA[000|001|002]:ISLAS_DESTINO_PRINCIPAL[005|006]");
            assertFalse(parseParamExpression.isEmpty());
            assertEquals(2, parseParamExpression.size());
            assertEquals(3, parseParamExpression.get("MOTIVOS_ESTANCIA").size());
            assertTrue(parseParamExpression.get("MOTIVOS_ESTANCIA").contains("000"));
            assertTrue(parseParamExpression.get("MOTIVOS_ESTANCIA").contains("001"));
            assertTrue(parseParamExpression.get("MOTIVOS_ESTANCIA").contains("002"));
            assertEquals(2, parseParamExpression.get("ISLAS_DESTINO_PRINCIPAL").size());
            assertTrue(parseParamExpression.get("ISLAS_DESTINO_PRINCIPAL").contains("005"));
            assertTrue(parseParamExpression.get("ISLAS_DESTINO_PRINCIPAL").contains("006"));
        }

        {
            Map<String, List<String>> parseParamExpression = RequestUtil.parseParamExpression("MEASURE[ABSOLUTE],TIME[2012]");
            assertFalse(parseParamExpression.isEmpty());
            assertEquals(2, parseParamExpression.size());
            assertEquals(1, parseParamExpression.get("MEASURE").size());
            assertTrue(parseParamExpression.get("MEASURE").contains("ABSOLUTE"));
            assertEquals(1, parseParamExpression.get("TIME").size());
            assertTrue(parseParamExpression.get("TIME").contains("2012"));
        }

        {
            Map<String, List<String>> parseParamExpression = RequestUtil.parseParamExpression("MEASURE[ABSOLUTE],TIME[2020-M06]");
            assertFalse(parseParamExpression.isEmpty());
            assertEquals(2, parseParamExpression.size());
            assertEquals(1, parseParamExpression.get("MEASURE").size());
            assertTrue(parseParamExpression.get("MEASURE").contains("ABSOLUTE"));
            assertEquals(1, parseParamExpression.get("TIME").size());
            assertTrue(parseParamExpression.get("TIME").contains("2020-M06"));
        }
    }
}
