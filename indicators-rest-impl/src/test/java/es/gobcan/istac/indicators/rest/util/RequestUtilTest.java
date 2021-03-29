package es.gobcan.istac.indicators.rest.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Test;

public class RequestUtilTest {

    @Test
    public void testParseCodes() {
        Pattern patternCode = RequestUtil.getPatternCode();

        assertCodes(RequestUtil.parseCodes(patternCode, null));

        assertCodes(RequestUtil.parseCodes(patternCode, ""));

        assertCodes(RequestUtil.parseCodes(patternCode, "            "));

        assertCodes(RequestUtil.parseCodes(patternCode, "000|001|002"), "000", "001", "002");

        assertCodes(RequestUtil.parseCodes(patternCode, "005|006"), "005", "006");

        assertCodes(RequestUtil.parseCodes(patternCode, "ABSOLUTE"), "ABSOLUTE");

        assertCodes(RequestUtil.parseCodes(patternCode, "2012"), "2012");

        // REPORTING_YEAR_TYPE
        assertCodes(RequestUtil.parseCodes(patternCode, "2020-A1"), "2020-A1");
        assertCodes(RequestUtil.parseCodes(patternCode, "2020-A1|2021-A1|2022-A1"), "2020-A1", "2021-A1", "2022-A1");

        // REPORTING_SEMESTER_TYPE
        assertCodes(RequestUtil.parseCodes(patternCode, "2001-S1"), "2001-S1");
        assertCodes(RequestUtil.parseCodes(patternCode, "2001-S1|2001-S2|2002-S1|2002-S2"), "2001-S1", "2001-S2", "2002-S1", "2002-S2");

        // REPORTING_TRIMESTER_TYPE
        assertCodes(RequestUtil.parseCodes(patternCode, "2002-T2"), "2002-T2");
        assertCodes(RequestUtil.parseCodes(patternCode, "2002-T1|2002-T2|2002-T3"), "2002-T1", "2002-T2", "2002-T3");

        // REPORTING_QUARTER_TYPE
        assertCodes(RequestUtil.parseCodes(patternCode, "2003-Q3"), "2003-Q3");
        assertCodes(RequestUtil.parseCodes(patternCode, "2003-Q1|2003-Q2|2003-Q3|2003-Q4"), "2003-Q1", "2003-Q2", "2003-Q3", "2003-Q4");

        // REPORTING_MONTH_TYPE
        assertCodes(RequestUtil.parseCodes(patternCode, "2020-M06"), "2020-M06");
        assertCodes(RequestUtil.parseCodes(patternCode, "2020-M06|2020-M07|2020-M08"), "2020-M06", "2020-M07", "2020-M08");
        assertCodes(RequestUtil.parseCodes(patternCode, "2004-M12|2005-M01|2005-M02|2005-M03|2005-M04|2005-M05"), "2004-M12", "2005-M01", "2005-M02", "2005-M03", "2005-M04", "2005-M05");

        // REPORTING_WEEK_TYPE
        assertCodes(RequestUtil.parseCodes(patternCode, "2005-W52"), "2005-W52");
        assertCodes(RequestUtil.parseCodes(patternCode, "2005-W52|2006-W01|2006-W02|2006-W03|2006-W04"), "2005-W52", "2006-W01", "2006-W02", "2006-W03", "2006-W04");

        // REPORTING_DAY_TYPE
        assertCodes(RequestUtil.parseCodes(patternCode, "2006-D010"), "2006-D010");
        assertCodes(RequestUtil.parseCodes(patternCode, "2006-D010|2006-D011|2006-D012|2006-D365"), "2006-D010", "2006-D011", "2006-D012", "2006-D365");

        // DATE_TIME
        assertCodes(RequestUtil.parseCodes(patternCode, "2013-07-24T08:21:52.519+01:00"), "2013-07-24T08:21:52.519+01:00");
        assertCodes(RequestUtil.parseCodes(patternCode, "2013-07-24T08:22:52.519+01:00|2013-07-24T08:22:52.519+01:00|2013-08-24T08:21:52.519+01:00|2014-07-24T08:21:52.519+01:00"),
                "2013-07-24T08:22:52.519+01:00", "2013-07-24T08:22:52.519+01:00", "2013-08-24T08:21:52.519+01:00", "2014-07-24T08:21:52.519+01:00");
        assertCodes(RequestUtil.parseCodes(patternCode, "2008-08-30T01:45:36.123Z"), "2008-08-30T01:45:36.123Z");
        assertCodes(RequestUtil.parseCodes(patternCode, "2008-08-30T01:45:36.123Z|2008-08-30T02:45:36.123Z|2008-09-30T03:45:36.123Z|2009-01-30T03:30:36.123Z"), "2008-08-30T01:45:36.123Z",
                "2008-08-30T02:45:36.123Z", "2008-09-30T03:45:36.123Z", "2009-01-30T03:30:36.123Z");
        assertCodes(RequestUtil.parseCodes(patternCode, "2008-08-30T01:45:36"), "2008-08-30T01:45:36");
        assertCodes(RequestUtil.parseCodes(patternCode, "2008-08-30T01:45:36|2009-08-30T01:45:36|2010-08-30T01:45:36"), "2008-08-30T01:45:36", "2009-08-30T01:45:36", "2010-08-30T01:45:36");

        // G_YEAR
        assertCodes(RequestUtil.parseCodes(patternCode, "2012"), "2012");
        assertCodes(RequestUtil.parseCodes(patternCode, "2012|2013|2014|2015"), "2012", "2013", "2014", "2015");
        assertCodes(RequestUtil.parseCodes(patternCode, "2004Z"), "2004Z");
        assertCodes(RequestUtil.parseCodes(patternCode, "2004Z|2005Z|2006Z|2007Z"), "2004Z", "2005Z", "2006Z", "2007Z");
        assertCodes(RequestUtil.parseCodes(patternCode, "-0045"), "-0045");
        assertCodes(RequestUtil.parseCodes(patternCode, "-0045|-0046|-0047"), "-0045", "-0046", "-0047");
        assertCodes(RequestUtil.parseCodes(patternCode, "2004-05:00"), "2004-05:00");
        assertCodes(RequestUtil.parseCodes(patternCode, "2004-05:00|2004-06:00|2004-07:00"), "2004-05:00", "2004-06:00", "2004-07:00");

        // G_YEAR_MONTH
        assertCodes(RequestUtil.parseCodes(patternCode, "2013-10"), "2013-10");
        assertCodes(RequestUtil.parseCodes(patternCode, "2013-10|2014-10|2015-11|2018-01"), "2013-10", "2014-10", "2015-11", "2018-01");
        assertCodes(RequestUtil.parseCodes(patternCode, "2001-10+02:00"), "2001-10+02:00");
        assertCodes(RequestUtil.parseCodes(patternCode, "2001-10+02:00|2002-10+02:00|2003-10+02:00"), "2001-10+02:00", "2002-10+02:00", "2003-10+02:00");
        assertCodes(RequestUtil.parseCodes(patternCode, "2001-10Z"), "2001-10Z");
        assertCodes(RequestUtil.parseCodes(patternCode, "2001-09Z|2002-10Z|2003-11Z|2004-12Z"), "2001-09Z", "2002-10Z", "2003-11Z", "2004-12Z");
        assertCodes(RequestUtil.parseCodes(patternCode, "-2001-10"), "-2001-10");
        assertCodes(RequestUtil.parseCodes(patternCode, "-2001-10|-2002-10|-2003-10"), "-2001-10", "-2002-10", "-2003-10");

        // G_DATE
        assertCodes(RequestUtil.parseCodes(patternCode, "2013-08-30"), "2013-08-30");
        assertCodes(RequestUtil.parseCodes(patternCode, "2013-08-30|2013-09-01"), "2013-08-30", "2013-09-01");
        assertCodes(RequestUtil.parseCodes(patternCode, "2013-08-30+02:00"), "2013-08-30+02:00");
        assertCodes(RequestUtil.parseCodes(patternCode, "2013-08-30+02:00|2012-08-30+02:00|2011-08-30+02:00"), "2013-08-30+02:00", "2012-08-30+02:00", "2011-08-30+02:00");

        // TIME_RANGE_DATETIME
        assertCodes(RequestUtil.parseCodes(patternCode, "2013-07-24T13:21:52.519+01:00/P1M"), "2013-07-24T13:21:52.519+01:00/P1M");
        assertCodes(RequestUtil.parseCodes(patternCode, "2013-07-24T13:21:52.519+01:00/P1M|2014-07-24T13:21:52.519+01:00/P1M|2015-08-24T13:21:52.519+01:00/P1M"), "2013-07-24T13:21:52.519+01:00/P1M",
                "2014-07-24T13:21:52.519+01:00/P1M", "2015-08-24T13:21:52.519+01:00/P1M");

        assertCodes(RequestUtil.parseCodes(patternCode, "1999-07-01T03:04:05.519+01:00/P1Y1M2D"), "1999-07-01T03:04:05.519+01:00/P1Y1M2D");
        assertCodes(RequestUtil.parseCodes(patternCode, "1999-07-01T03:04:05.519+01:00/P1Y1M2D|2000-07-01T03:04:05.519+01:00/P1Y1M2D"), "1999-07-01T03:04:05.519+01:00/P1Y1M2D",
                "2000-07-01T03:04:05.519+01:00/P1Y1M2D");

        // TIME_RANGE_DATE
        assertCodes(RequestUtil.parseCodes(patternCode, "2013-07-24/P1M"), "2013-07-24/P1M");
        assertCodes(RequestUtil.parseCodes(patternCode, "2013-07-24/P1M|2013-07-25/P1M|2013-07-26/P1M|2013-07-27/P1M"), "2013-07-24/P1M", "2013-07-25/P1M", "2013-07-26/P1M", "2013-07-27/P1M");
    }

    @Test
    public void testParseParamExpression() {
        {
            Map<String, List<String>> parseParamExpression = RequestUtil.parseParamExpression("");

            assertParamExpression(parseParamExpression);
        }

        {
            Map<String, List<String>> parseParamExpression = RequestUtil.parseParamExpression(null);

            assertParamExpression(parseParamExpression);
        }

        {
            Map<String, List<String>> parseParamExpression = RequestUtil.parseParamExpression("MOTIVOS_ESTANCIA[000|001|002]:ISLAS_DESTINO_PRINCIPAL[005|006]");

            assertParamExpression(parseParamExpression, "MOTIVOS_ESTANCIA", "ISLAS_DESTINO_PRINCIPAL");
            assertCodes(parseParamExpression.get("MOTIVOS_ESTANCIA"), "000", "001", "002");
            assertCodes(parseParamExpression.get("ISLAS_DESTINO_PRINCIPAL"), "005", "006");
        }

        {
            Map<String, List<String>> parseParamExpression = RequestUtil.parseParamExpression("MEASURE[ABSOLUTE],TIME[2012]");

            assertParamExpression(parseParamExpression, "MEASURE", "TIME");
            assertCodes(parseParamExpression.get("MEASURE"), "ABSOLUTE");
            assertCodes(parseParamExpression.get("TIME"), "2012");
        }

        {
            Map<String, List<String>> parsedParamExpression = RequestUtil.parseParamExpression("MEASURE[ABSOLUTE],TIME[2020-M06]");

            assertParamExpression(parsedParamExpression, "MEASURE", "TIME");
            assertCodes(parsedParamExpression.get("MEASURE"), "ABSOLUTE");
            assertCodes(parsedParamExpression.get("TIME"), "2020-M06");
        }

        {
            // REPORTING_YEAR_TYPE - REPORTING_SEMESTER_TYPE - REPORTING_TRIMESTER_TYPE - REPORTING_QUARTER_TYPE
            // @formatter:off
            Map<String, List<String>> parsedParamExpression = RequestUtil.parseParamExpression(
                    "TIME1[2020-A1|2021-A1|2022-A1],"
                    + "TIME2[2001-S1|2001-S2|2002-S1|2002-S2],"
                    + "TIME3[2002-T1|2002-T2|2002-T3],"
                    + "TIME4[2003-Q1|2003-Q2|2003-Q3|2003-Q4]");
            // @formatter:on            

            assertParamExpression(parsedParamExpression, "TIME1", "TIME2", "TIME3", "TIME4");
            assertCodes(parsedParamExpression.get("TIME1"), "2020-A1", "2021-A1", "2022-A1");
            assertCodes(parsedParamExpression.get("TIME2"), "2001-S1", "2001-S2", "2002-S1", "2002-S2");
            assertCodes(parsedParamExpression.get("TIME3"), "2002-T1", "2002-T2", "2002-T3");
            assertCodes(parsedParamExpression.get("TIME4"), "2003-Q1", "2003-Q2", "2003-Q3", "2003-Q4");
        }

        {
            // REPORTING_MONTH_TYPE - REPORTING_WEEK_TYPE - REPORTING_DAY_TYPE
            // @formatter:off
            Map<String, List<String>> parsedParamExpression = RequestUtil.parseParamExpression(
                    "TYPE1[2004-M12|2005-M01|2005-M02|2005-M03|2005-M04|2005-M05],"
                    + "TYPE2[2005-W52|2006-W01|2006-W02|2006-W03|2006-W04],"
                    + "TYPE3[2006-D010|2006-D011|2006-D012|2006-D365]");
            // @formatter:on           

            assertParamExpression(parsedParamExpression, "TYPE1", "TYPE2", "TYPE3");
            assertCodes(parsedParamExpression.get("TYPE1"), "2004-M12", "2005-M01", "2005-M02", "2005-M03", "2005-M04", "2005-M05");
            assertCodes(parsedParamExpression.get("TYPE2"), "2005-W52", "2006-W01", "2006-W02", "2006-W03", "2006-W04");
            assertCodes(parsedParamExpression.get("TYPE3"), "2006-D010", "2006-D011", "2006-D012", "2006-D365");
        }

        {
            // DATE_TIME
            // @formatter:off
            Map<String, List<String>> parsedParamExpression = RequestUtil.parseParamExpression(
                    "DATE_TIME_A[2013-07-24T08:22:52.519+01:00|2013-07-24T08:22:52.519+01:00|2013-08-24T08:21:52.519+01:00|2014-07-24T08:21:52.519+01:00],"
                    + "DATE_TIME_B[2008-08-30T01:45:36.123Z|2008-08-30T02:45:36.123Z|2008-09-30T03:45:36.123Z|2009-01-30T03:30:36.123Z],"
                    + "DATE_TIME_C[2008-08-30T01:45:36|2009-08-30T01:45:36|2010-08-30T01:45:36]");
            // @formatter:on

            assertParamExpression(parsedParamExpression, "DATE_TIME_A", "DATE_TIME_B", "DATE_TIME_C");
            assertCodes(parsedParamExpression.get("DATE_TIME_A"), "2013-07-24T08:22:52.519+01:00", "2013-07-24T08:22:52.519+01:00", "2013-08-24T08:21:52.519+01:00", "2014-07-24T08:21:52.519+01:00");
            assertCodes(parsedParamExpression.get("DATE_TIME_B"), "2008-08-30T01:45:36.123Z", "2008-08-30T02:45:36.123Z", "2008-09-30T03:45:36.123Z", "2009-01-30T03:30:36.123Z");
            assertCodes(parsedParamExpression.get("DATE_TIME_C"), "2008-08-30T01:45:36", "2009-08-30T01:45:36", "2010-08-30T01:45:36");
        }

        {
            // G_YEAR
            // @formatter:off
            Map<String, List<String>> parsedParamExpression = RequestUtil.parseParamExpression(
                    "G_YEAR_A[2012|2013|2014|2015],"
                    + "G_YEAR_B[2004Z|2005Z|2006Z|2007Z],"
                    + "G_YEAR_C[-0045|-0046|-0047]"
                    + "G_YEAR_D[2004-05:00|2004-06:00|2004-07:00]");
            // @formatter:on

            assertParamExpression(parsedParamExpression, "G_YEAR_A", "G_YEAR_B", "G_YEAR_C", "G_YEAR_D");
            assertCodes(parsedParamExpression.get("G_YEAR_A"), "2012", "2013", "2014", "2015");
            assertCodes(parsedParamExpression.get("G_YEAR_B"), "2004Z", "2005Z", "2006Z", "2007Z");
            assertCodes(parsedParamExpression.get("G_YEAR_C"), "-0045", "-0046", "-0047");
            assertCodes(parsedParamExpression.get("G_YEAR_D"), "2004-05:00", "2004-06:00", "2004-07:00");
        }

        {
            // G_YEAR_MONTH
            // @formatter:off
            Map<String, List<String>> parsedParamExpression = RequestUtil.parseParamExpression(
                    "G_YEAR_MONTH_A[2013-10|2014-10|2015-11|2018-01],"
                    + "G_YEAR_MONTH_B[2001-10+02:00|2002-10+02:00|2003-10+02:00],"
                    + "G_YEAR_MONTH_C[2001-09Z|2002-10Z|2003-11Z|2004-12Z],"
                    + "G_YEAR_MONTH_D[-2001-10|-2002-10|-2003-10]");
            // @formatter:on

            assertParamExpression(parsedParamExpression, "G_YEAR_MONTH_A", "G_YEAR_MONTH_B", "G_YEAR_MONTH_C", "G_YEAR_MONTH_D");
            assertCodes(parsedParamExpression.get("G_YEAR_MONTH_A"), "2013-10", "2014-10", "2015-11", "2018-01");
            assertCodes(parsedParamExpression.get("G_YEAR_MONTH_B"), "2001-10+02:00", "2002-10+02:00", "2003-10+02:00");
            assertCodes(parsedParamExpression.get("G_YEAR_MONTH_C"), "2001-09Z", "2002-10Z", "2003-11Z", "2004-12Z");
            assertCodes(parsedParamExpression.get("G_YEAR_MONTH_D"), "-2001-10", "-2002-10", "-2003-10");
        }

        {
            // G_DATE
            // @formatter:off
            Map<String, List<String>> parsedParamExpression = RequestUtil.parseParamExpression(
                    "G_DATE_A[2013-08-30|2013-09-01],"
                    + "G_DATE_B[2013-08-30+02:00|2012-08-30+02:00|2011-08-30+02:00]");
            // @formatter:on

            assertParamExpression(parsedParamExpression, "G_DATE_A", "G_DATE_B");
            assertCodes(parsedParamExpression.get("G_DATE_A"), "2013-08-30", "2013-09-01");
            assertCodes(parsedParamExpression.get("G_DATE_B"), "2013-08-30+02:00", "2012-08-30+02:00", "2011-08-30+02:00");
        }

        {
            // TIME_RANGE_DATETIME
            // @formatter:off
            Map<String, List<String>> parsedParamExpression = RequestUtil.parseParamExpression(
                    "TIME_RANGE_DATETIME_A[2013-07-24T13:21:52.519+01:00/P1M|2014-07-24T13:21:52.519+01:00/P1M|2015-08-24T13:21:52.519+01:00/P1M],"
                    + "TIME_RANGE_DATETIME_B[1999-07-01T03:04:05.519+01:00/P1Y1M2D|2000-07-01T03:04:05.519+01:00/P1Y1M2D]");
            // @formatter:on

            assertParamExpression(parsedParamExpression, "TIME_RANGE_DATETIME_A", "TIME_RANGE_DATETIME_B");
            assertCodes(parsedParamExpression.get("TIME_RANGE_DATETIME_A"), "2013-07-24T13:21:52.519+01:00/P1M", "2014-07-24T13:21:52.519+01:00/P1M", "2015-08-24T13:21:52.519+01:00/P1M");
            assertCodes(parsedParamExpression.get("TIME_RANGE_DATETIME_B"), "1999-07-01T03:04:05.519+01:00/P1Y1M2D", "2000-07-01T03:04:05.519+01:00/P1Y1M2D");
        }

        {
            // TIME_RANGE_DATE
            Map<String, List<String>> parsedParamExpression = RequestUtil.parseParamExpression("TIME_RANGE_DATE[2013-07-24/P1M|2013-07-25/P1M|2013-07-26/P1M|2013-07-27/P1M]");

            assertParamExpression(parsedParamExpression, "TIME_RANGE_DATE");
            assertCodes(parsedParamExpression.get("TIME_RANGE_DATE"), "2013-07-24/P1M", "2013-07-25/P1M", "2013-07-26/P1M", "2013-07-27/P1M");
        }

    }

    private void assertParamExpression(Map<String, List<String>> parsedParamExpression, String... expectedParamExpressions) {
        assertEquals(expectedParamExpressions.length, parsedParamExpression.size());
        for (String expectedParamExpression : expectedParamExpressions) {
            assertTrue(parsedParamExpression.containsKey(expectedParamExpression));
        }
    }

    private void assertCodes(List<String> parsedCodes, String... expectedParsedCodes) {
        assertEquals(expectedParsedCodes.length, parsedCodes.size());
        for (String expectedParsedCode : expectedParsedCodes) {
            assertTrue(parsedCodes.contains(expectedParsedCode));
        }
    }
}
