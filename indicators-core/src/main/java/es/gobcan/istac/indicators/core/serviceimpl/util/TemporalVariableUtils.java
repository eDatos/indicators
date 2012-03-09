package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities to temporal variables
 */
public class TemporalVariableUtils {

    private static String  YEAR_PATTERN                 = "[1-2]\\d{3}";
    private static String  YEARLY_PATTERN               = YEAR_PATTERN;
    private static String  BIYEARLY_PATTERN             = YEAR_PATTERN + "H[1-2]";
    private static String  QUARTERLY_PATTERN            = YEAR_PATTERN + "Q[1-4]";
    private static String  MONTH_PATTERN                = "(0[1-9]|1[0-2])";
    private static String  MONTHLY_PATTERN              = YEAR_PATTERN + "M" + MONTH_PATTERN;
    private static String  WEEKLY_PATTERN               = YEAR_PATTERN + "W(0[1-9]|[1-4][0-9]|5[0-2])";
    private static String  DAILY_PATTERN                = YEAR_PATTERN + MONTH_PATTERN + "(0[1-9]|[1-2][0-9]|3[0-1])";
    private static String  TEMPORAL_GRANULARITY_PATTERN = YEARLY_PATTERN + "|" + BIYEARLY_PATTERN + "|" + QUARTERLY_PATTERN + "|" + MONTHLY_PATTERN + "|" + WEEKLY_PATTERN + "|" + DAILY_PATTERN;

    private static Pattern pattern                      = Pattern.compile("^" + TEMPORAL_GRANULARITY_PATTERN + "$");

    /**
     * Checks if a temporal value is valid
     * Posibilities:
     * - Yearly: yyyy (Example: 1999)
     * - Biyearly: yyyyHs (Example: 1999H1)
     * - Quarterly: yyyyQt (Example: 1999Q1)
     * - Monthly: yyyyMmm (Example: 1999M01)
     * - Weekly: yyyyWss (Example: 1999W51).
     * - Daily: yyyymmdd (Example: 19990101)
     */
    public static Boolean isTemporalValue(String value) {
        Matcher matching = pattern.matcher(value);
        Boolean isValid = matching.matches();
        return isValid;
    }

    /**
     * Compare two temporal values. These values must be refered to same granularity
     * TODO comprobar que tienen la misma granularidad?
     * 
     * @return 0 if are equals; a value less than 0 if this value1 is less than value2; a value greater than 0 if this value1 is less than value2
     */
    public static int compareTo(String value1, String value2) {
        return value1.compareTo(value2);
    }
}
