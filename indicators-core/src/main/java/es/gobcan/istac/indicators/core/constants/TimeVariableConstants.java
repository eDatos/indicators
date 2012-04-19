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

    // TODO: Eliminar esta clase y usar la de la web que se llama TimeConstants
    
    public static final String START               = "^";
    public static final String END                 = "$";

    public static final String GROUP_LEFT          = "(";
    public static final String GROUP_RIGHT         = ")";

    public static final String BIYEARLY_CHARACTER  = "H";
    public static final String QUARTERLY_CHARACTER = "Q";
    public static final String MONTHLY_CHARACTER   = "M";
    public static final String WEEKLY_CHARACTER    = "W";

    public static final String YEAR_PATTERN        = "[1-2]\\d{3}";
    public static final String YEARLY_PATTERN      = GROUP_LEFT + YEAR_PATTERN + GROUP_RIGHT;
    public static final String BIYEARLY_PATTERN    = GROUP_LEFT + YEAR_PATTERN + GROUP_RIGHT + BIYEARLY_CHARACTER + GROUP_LEFT + "[1-2]" + GROUP_RIGHT;
    public static final String QUARTERLY_PATTERN   = GROUP_LEFT + YEAR_PATTERN + GROUP_RIGHT + QUARTERLY_CHARACTER + GROUP_LEFT + "[1-4]" + GROUP_RIGHT;
    public static final String MONTH_PATTERN       = "0[1-9]|1[0-2]";
    public static final String MONTHLY_PATTERN     = GROUP_LEFT + YEAR_PATTERN + GROUP_RIGHT + MONTHLY_CHARACTER + GROUP_LEFT + MONTH_PATTERN + GROUP_RIGHT;
    public static final String WEEKLY_PATTERN      = GROUP_LEFT + YEAR_PATTERN + GROUP_RIGHT + WEEKLY_CHARACTER + GROUP_LEFT + "0[1-9]|[1-4][0-9]|5[0-2]" + GROUP_RIGHT;
    public static final String DAILY_PATTERN       = GROUP_LEFT + YEAR_PATTERN + GROUP_RIGHT + GROUP_LEFT + MONTH_PATTERN + GROUP_RIGHT + GROUP_LEFT + "0[1-9]|[1-2][0-9]|3[0-1]" + GROUP_RIGHT;
    public static final String TIME_VALUE_PATTERN  = YEARLY_PATTERN + "|" + BIYEARLY_PATTERN + "|" + QUARTERLY_PATTERN + "|" + MONTHLY_PATTERN + "|" + WEEKLY_PATTERN + "|" + DAILY_PATTERN;
}
