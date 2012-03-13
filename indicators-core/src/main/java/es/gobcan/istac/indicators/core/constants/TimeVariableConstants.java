package es.gobcan.istac.indicators.core.constants;

/**
 * Regular expressions to time variables
 * Posibilities:
 * - Yearly: yyyy (Example: 1999)
 * - Biyearly: yyyyHs (Example: 1999H1)
 * - Quarterly: yyyyQt (Example: 1999Q1)
 * - Monthly: yyyyMmm (Example: 1999M01)
 * - Weekly: yyyyWss (Example: 1999W51).
 * - Daily: yyyymmdd (Example: 19990101)
 */
public class TimeVariableConstants {

    public static final String START              = "^";
    public static final String END                = "$";
 
    public static final String YEAR_PATTERN       = "[1-2]\\d{3}";
    public static final String YEARLY_PATTERN     = YEAR_PATTERN;
    public static final String BIYEARLY_PATTERN   = YEAR_PATTERN + "H[1-2]";
    public static final String QUARTERLY_PATTERN  = YEAR_PATTERN + "Q[1-4]";
    public static final String MONTH_PATTERN      = "(0[1-9]|1[0-2])";
    public static final String MONTHLY_PATTERN    = YEAR_PATTERN + "M" + MONTH_PATTERN;
    public static final String WEEKLY_PATTERN     = YEAR_PATTERN + "W(0[1-9]|[1-4][0-9]|5[0-2])";
    public static final String DAILY_PATTERN      = YEAR_PATTERN + MONTH_PATTERN + "(0[1-9]|[1-2][0-9]|3[0-1])";
    public static final String TIME_VALUE_PATTERN = YEARLY_PATTERN + "|" + BIYEARLY_PATTERN + "|" + QUARTERLY_PATTERN + "|" + MONTHLY_PATTERN + "|" + WEEKLY_PATTERN + "|" + DAILY_PATTERN;
}
