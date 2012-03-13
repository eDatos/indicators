package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.enume.domain.TimeGranularityEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

/**
 * Utilities to time variables
 */
public class TimeVariableUtils {

    private static String  START                = "^";
    private static String  END                  = "$";
    private static String  YEAR_PATTERN         = "[1-2]\\d{3}";
    private static String  YEARLY_PATTERN       = YEAR_PATTERN;
    private static String  BIYEARLY_PATTERN     = YEAR_PATTERN + "H[1-2]";
    private static String  QUARTERLY_PATTERN    = YEAR_PATTERN + "Q[1-4]";
    private static String  MONTH_PATTERN        = "(0[1-9]|1[0-2])";
    private static String  MONTHLY_PATTERN      = YEAR_PATTERN + "M" + MONTH_PATTERN;
    private static String  WEEKLY_PATTERN       = YEAR_PATTERN + "W(0[1-9]|[1-4][0-9]|5[0-2])";
    private static String  DAILY_PATTERN        = YEAR_PATTERN + MONTH_PATTERN + "(0[1-9]|[1-2][0-9]|3[0-1])";
    private static String  TIME_VALUE_PATTERN   = YEARLY_PATTERN + "|" + BIYEARLY_PATTERN + "|" + QUARTERLY_PATTERN + "|" + MONTHLY_PATTERN + "|" + WEEKLY_PATTERN + "|" + DAILY_PATTERN;

    private static Pattern patternTimeValue     = Pattern.compile(START + TIME_VALUE_PATTERN + END);

    private static Pattern patternYearlyValue   = Pattern.compile(START + YEARLY_PATTERN + END);
    private static Pattern patternBiyearlyValue = Pattern.compile(START + BIYEARLY_PATTERN + END);
    private static Pattern patternQuaterlyValue = Pattern.compile(START + QUARTERLY_PATTERN + END);
    private static Pattern patternMonthlyValue  = Pattern.compile(START + MONTHLY_PATTERN + END);
    private static Pattern patternWeeklyValue   = Pattern.compile(START + WEEKLY_PATTERN + END);
    private static Pattern patternDailyValue    = Pattern.compile(START + DAILY_PATTERN + END);

    /**
     * Checks if a time value is valid
     * Posibilities:
     * - Yearly: yyyy (Example: 1999)
     * - Biyearly: yyyyHs (Example: 1999H1)
     * - Quarterly: yyyyQt (Example: 1999Q1)
     * - Monthly: yyyyMmm (Example: 1999M01)
     * - Weekly: yyyyWss (Example: 1999W51).
     * - Daily: yyyymmdd (Example: 19990101)
     */
    public static Boolean isTimeValue(String value) {
        Matcher matching = patternTimeValue.matcher(value);
        Boolean isValid = matching.matches();
        return isValid;
    }

    /**
     * Compare two time values. These values must be refered to same granularity
     * 
     * @return 0 if are equals; a value less than 0 if this value1 is less than value2; a value greater than 0 if this value1 is less than value2
     */
    public static int compareTo(String value1, String value2) throws MetamacException {
        TimeGranularityEnum timeGranularityEnum1 = guessTimeGranularity(value1) ;
        TimeGranularityEnum timeGranularityEnum2 = guessTimeGranularity(value2) ;
        if (!timeGranularityEnum1.equals(timeGranularityEnum2)) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, value2);
        }
        return value1.compareTo(value2);
    }
    
    /**
     * Guess time granularity of a time value
     */
    public static TimeGranularityEnum guessTimeGranularity(String value) throws MetamacException {
        if (patternYearlyValue.matcher(value).matches()) {
            return TimeGranularityEnum.YEARLY;
        } else if (patternBiyearlyValue.matcher(value).matches()) {
            return TimeGranularityEnum.BIYEARLY;
        } else if (patternQuaterlyValue.matcher(value).matches()) {
            return TimeGranularityEnum.QUARTERLY;
        } else if (patternMonthlyValue.matcher(value).matches()) {
            return TimeGranularityEnum.MONTHLY;
        } else if (patternWeeklyValue.matcher(value).matches()) {
            return TimeGranularityEnum.WEEKLY;
        } else if (patternDailyValue.matcher(value).matches()) {
            return TimeGranularityEnum.DAILY;
        }
        throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, value);
    }
}
