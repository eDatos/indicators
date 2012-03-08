package es.gobcan.istac.indicators.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities to temporary granularities
 */
public class TemporaryGranularityUtils {

    private static String  YEAR_PATTERN                 = "[1-2]\\d{3}";
    private static String  YEARLY_PATTERN               = YEAR_PATTERN;
    private static String  BIYEARLY_PATTERN             = YEAR_PATTERN + "H[1-2]";
    private static String  QUATERLY_PATTERN             = YEAR_PATTERN + "Q[1-4]";
    private static String  MONTH_PATTERN                = "(0[1-9]|1[0-2])";
    private static String  MONTHLY_PATTERN              = YEAR_PATTERN + "M" + MONTH_PATTERN;
    private static String  WEEKLY_PATTERN               = YEAR_PATTERN + "W(0[1-9]|[1-4][0-9]|5[0-2])";
    private static String  DAILY_PATTERN                = YEAR_PATTERN + MONTH_PATTERN + "(0[1-9]|[1-2][0-9]|3[0-1])";
    private static String  TEMPORAL_GRANULARITY_PATTERN = YEARLY_PATTERN + "|" + BIYEARLY_PATTERN + "|" + QUATERLY_PATTERN + "|" + MONTHLY_PATTERN + "|" + WEEKLY_PATTERN + "|" + DAILY_PATTERN;

    private static Pattern pattern                      = Pattern.compile("^" + TEMPORAL_GRANULARITY_PATTERN + "$");

    /**
     * Validate temporary granularity is valid.
     * Posibilities:
     * - Yearly: yyyy (Example: 1999)
     * - Biyearly: yyyyHs (Example: 1999H1)
     * - Quaterly: yyyyQt (Example: 1999Q1)
     * - Monthly: yyyyMmm (Example: 1999M01)
     * - Weekly: yyyyWss (Example: 1999W51).
     * - Daily: yyyymmdd (Example: 19990101)
     */
    public static Boolean isTemporaryGranularity(String value) {
        Matcher matching = pattern.matcher(value);
        Boolean isValid = matching.matches();
        return isValid;
    }
}
