package es.gobcan.istac.indicators.core.serviceimpl.util;

import static org.siemac.metamac.core.common.constants.shared.RegularExpressionConstants.END;
import static org.siemac.metamac.core.common.constants.shared.RegularExpressionConstants.GROUP_LEFT;
import static org.siemac.metamac.core.common.constants.shared.RegularExpressionConstants.GROUP_RIGHT;
import static org.siemac.metamac.core.common.constants.shared.RegularExpressionConstants.START;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.enume.domain.IstacTimeGranularityEnum;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

/**
 * Utilities to time variables
 */
public class GpeTimeUtils {

    // Constants
    /**
     * Regular expressions to time variables
     * Possibilities:
     * - Yearly: yyyy (Example: 1999)
     * - Biyearly: yyyyHs (Example: 1999H1)
     * - Quarterly: yyyyQt (Example: 1999Q1)
     * - FourMonth: yyyyTt (Example: 1999T1)
     * - Monthly: yyyyMmm (Example: 1999M01)
     * - Weekly: yyyyWss (Example: 1999W51)
     * - Daily: yyyymmdd (Example: 19990101)
     */
    public static final String BIYEARLY_CHARACTER    = "H";
    public static final String QUARTERLY_CHARACTER   = "Q";
    public static final String MONTHLY_CHARACTER     = "M";
    public static final String WEEKLY_CHARACTER      = "W";

    public static final String YEAR_PATTERN          = "[1-2]\\d{3}";
    public static final String YEARLY_PATTERN        = GROUP_LEFT + YEAR_PATTERN + GROUP_RIGHT;
    public static final String BIYEARLY_PATTERN      = GROUP_LEFT + YEAR_PATTERN + GROUP_RIGHT + BIYEARLY_CHARACTER + GROUP_LEFT + "[1-2]" + GROUP_RIGHT;
    public static final String QUARTERLY_PATTERN     = GROUP_LEFT + YEAR_PATTERN + GROUP_RIGHT + QUARTERLY_CHARACTER + GROUP_LEFT + "[1-4]" + GROUP_RIGHT;
    public static final String MONTH_PATTERN         = "0[1-9]|1[0-2]";
    public static final String MONTHLY_PATTERN       = GROUP_LEFT + YEAR_PATTERN + GROUP_RIGHT + MONTHLY_CHARACTER + GROUP_LEFT + MONTH_PATTERN + GROUP_RIGHT;
    public static final String WEEKLY_PATTERN        = GROUP_LEFT + YEAR_PATTERN + GROUP_RIGHT + WEEKLY_CHARACTER + GROUP_LEFT + "0[1-9]|[1-4][0-9]|5[0-3]" + GROUP_RIGHT;
    public static final String DAILY_PATTERN         = GROUP_LEFT + YEAR_PATTERN + GROUP_RIGHT + GROUP_LEFT + MONTH_PATTERN + GROUP_RIGHT + GROUP_LEFT + "0[1-9]|[1-2][0-9]|3[0-1]" + GROUP_RIGHT;
    public static final String TIME_VALUE_PATTERN    = YEARLY_PATTERN + "|" + BIYEARLY_PATTERN + "|" + QUARTERLY_PATTERN + "|" + MONTHLY_PATTERN + "|" + WEEKLY_PATTERN + "|" + DAILY_PATTERN;

    // Patterns
    protected static Pattern patternTimeValue     = Pattern.compile(START + TIME_VALUE_PATTERN + END);

    protected static Pattern patternYearlyValue   = Pattern.compile(START + YEARLY_PATTERN + END);
    protected static Pattern patternBiyearlyValue = Pattern.compile(START + BIYEARLY_PATTERN + END);
    protected static Pattern patternQuaterlyValue = Pattern.compile(START + QUARTERLY_PATTERN + END);
    protected static Pattern patternMonthlyValue  = Pattern.compile(START + MONTHLY_PATTERN + END);
    protected static Pattern patternWeeklyValue   = Pattern.compile(START + WEEKLY_PATTERN + END);
    protected static Pattern patternDailyValue    = Pattern.compile(START + DAILY_PATTERN + END);

    public static Boolean isTimeValue(String value) {
        Matcher matching = patternTimeValue.matcher(value);
        Boolean isValid = matching.matches();
        return isValid;
    }

    /**
     * Guess time granularity of a time value
     */
    public static IstacTimeGranularityEnum guessTimeGranularity(String value) throws MetamacException {
        if (patternYearlyValue.matcher(value).matches()) {
            return IstacTimeGranularityEnum.YEARLY;
        } else if (patternBiyearlyValue.matcher(value).matches()) {
            return IstacTimeGranularityEnum.BIYEARLY;
        } else if (patternQuaterlyValue.matcher(value).matches()) {
            return IstacTimeGranularityEnum.QUARTERLY;
        } else if (patternMonthlyValue.matcher(value).matches()) {
            return IstacTimeGranularityEnum.MONTHLY;
        } else if (patternWeeklyValue.matcher(value).matches()) {
            return IstacTimeGranularityEnum.WEEKLY;
        } else if (patternDailyValue.matcher(value).matches()) {
            return IstacTimeGranularityEnum.DAILY;
        }
        throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, value);
    }

    public static TimeValue parseTimeValue(String timeValue) throws MetamacException {

        TimeValue timeValueFields = new TimeValue();
        timeValueFields.setTimeValue(timeValue);

        IstacTimeGranularityEnum timeGranularity = guessTimeGranularity(timeValue);
        timeValueFields.setGranularity(timeGranularity);

        switch (timeGranularity) {
            case YEARLY: {
                Matcher matcher = patternYearlyValue.matcher(timeValue);
                if (matcher.matches()) {
                    timeValueFields.setYear(matcher.group(1));
                }
                break;
            }
            case BIYEARLY: {
                Matcher matcher = patternBiyearlyValue.matcher(timeValue);
                if (matcher.matches()) {
                    timeValueFields.setYear(matcher.group(1));
                    timeValueFields.setSubperiod(BIYEARLY_CHARACTER, matcher.group(2));
                }
                break;
            }
            case QUARTERLY: {
                Matcher matcher = patternQuaterlyValue.matcher(timeValue);
                if (matcher.matches()) {
                    timeValueFields.setYear(matcher.group(1));
                    timeValueFields.setSubperiod(QUARTERLY_CHARACTER, matcher.group(2));
                }
                break;
            }
            case MONTHLY: {
                Matcher matcher = patternMonthlyValue.matcher(timeValue);
                if (matcher.matches()) {
                    timeValueFields.setYear(matcher.group(1));
                    timeValueFields.setSubperiod(MONTHLY_CHARACTER, matcher.group(2));
                }
                break;
            }
            case WEEKLY: {
                Matcher matcher = patternWeeklyValue.matcher(timeValue);
                if (matcher.matches()) {
                    timeValueFields.setYear(matcher.group(1));
                    timeValueFields.setWeek(matcher.group(2));
                    timeValueFields.setSubperiod(WEEKLY_CHARACTER, matcher.group(2));
                }
                break;
            }
            case DAILY: {
                Matcher matcher = patternDailyValue.matcher(timeValue);
                if (matcher.matches()) {
                    timeValueFields.setYear(matcher.group(1));
                    timeValueFields.setMonth(matcher.group(2));
                    timeValueFields.setDay(matcher.group(3));
                }
                break;
            }
            default:
                throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, timeValue);
        }

        return timeValueFields;
    }

    /**
     * Retrieves previous time value in same granularity
     */
    @Deprecated
    public static String calculatePreviousTimeValue(String value) throws MetamacException {
        if (value == null) {
            return null;
        }
        IstacTimeGranularityEnum timeGranularity = guessTimeGranularity(value);
        switch (timeGranularity) {
            case YEARLY: {
                Matcher matcher = patternYearlyValue.matcher(value);
                if (matcher.matches()) {
                    int year = Integer.valueOf(matcher.group(1));
                    return String.valueOf(year - 1);
                }
            }
            case BIYEARLY:
                return calculatePreviousTimeValueWithSubperiod(patternBiyearlyValue, value, BIYEARLY_CHARACTER, "2", 1);
            case QUARTERLY:
                return calculatePreviousTimeValueWithSubperiod(patternQuaterlyValue, value, QUARTERLY_CHARACTER, "4", 1);
            case MONTHLY:
                return calculatePreviousTimeValueWithSubperiod(patternMonthlyValue, value, MONTHLY_CHARACTER, "12", 2);
            case WEEKLY:
                return calculatePreviousTimeValueWithSubperiod(patternWeeklyValue, value, WEEKLY_CHARACTER, "52", 2);
            case DAILY: {
                Matcher matcher = patternDailyValue.matcher(value);
                if (matcher.matches()) {
                    int year = Integer.valueOf(matcher.group(1));
                    int month = Integer.valueOf(matcher.group(2));
                    int day = Integer.valueOf(matcher.group(3));
                    return new DateTime(year, month, day, 0, 0, 0, 0).minusDays(1).toString("yyyyMMdd");
                }
            }
            default:
                throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, value);
        }
    }

    private static String calculatePreviousTimeValueWithSubperiod(Pattern pattern, String timeValue, String subperiodCharacter, String maximumSubperiodAtYear, int subperiodStringSize)
            throws MetamacException {
        Matcher matcher = pattern.matcher(timeValue);
        if (matcher.matches()) {
            int year = Integer.valueOf(matcher.group(1));
            int subperiod = Integer.valueOf(matcher.group(2));
            if (subperiod == 1) {
                return (new StringBuilder()).append(String.valueOf(year - 1)).append(subperiodCharacter).append(maximumSubperiodAtYear).toString();
            } else {
                return (new StringBuilder()).append(String.valueOf(year)).append(subperiodCharacter).append(StringUtils.leftPad(String.valueOf(subperiod - 1), subperiodStringSize, "0")).toString();
            }
        }
        throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, timeValue);
    }

}
