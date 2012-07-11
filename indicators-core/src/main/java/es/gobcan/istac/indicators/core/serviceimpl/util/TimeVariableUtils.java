package es.gobcan.istac.indicators.core.serviceimpl.util;

import static org.siemac.metamac.core.common.constants.shared.TimeConstants.BIYEARLY_CHARACTER;
import static org.siemac.metamac.core.common.constants.shared.TimeConstants.MONTHLY_CHARACTER;
import static org.siemac.metamac.core.common.constants.shared.TimeConstants.QUARTERLY_CHARACTER;
import static org.siemac.metamac.core.common.constants.shared.TimeConstants.WEEKLY_CHARACTER;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.TimeUtils;

import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.enume.domain.TimeGranularityEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

/**
 * Utilities to time variables
 */
public class TimeVariableUtils extends TimeUtils {

    /**
     * Compare two time values. These values must be refered to same granularity
     * 
     * @return 0 if are equals; a value less than 0 if this value1 is less than value2; a value greater than 0 if this value1 is less than value2
     */
    public static int compareTo(String value1, String value2) throws MetamacException {
        TimeGranularityEnum timeGranularityEnum1 = guessTimeGranularity(value1);
        TimeGranularityEnum timeGranularityEnum2 = guessTimeGranularity(value2);
        if (!timeGranularityEnum1.equals(timeGranularityEnum2)) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, value2);
        }
        int compareResult = value1.compareTo(value2);
        // Inverse order
        return compareResult * -1;
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

    public static TimeValue parseTimeValue(String timeValue) throws MetamacException {

        TimeValue timeValueFields = new TimeValue();
        timeValueFields.setTimeValue(timeValue);

        TimeGranularityEnum timeGranularity = guessTimeGranularity(timeValue);
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
    public static String calculatePreviousTimeValue(String value) throws MetamacException {
        if (value == null) {
            return null;
        }
        TimeGranularityEnum timeGranularity = guessTimeGranularity(value);
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
        }
        throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, value);
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

    /**
     * Retrieves previous time value in yearly granularity
     */
    public static String calculatePreviousYearTimeValue(String value) throws MetamacException {
        if (value == null || !isTimeValue(value)) {
            return null;
        }

        // Year is in first 4 letters
        String yearStr = value.substring(0, 4);
        Integer year = Integer.parseInt(yearStr);

        String feb29 = "0229";
        if (feb29.equals(value.substring(4))) {
            return null;
        }
        year = year - 1;
        return value.replaceFirst(yearStr, year.toString());
    }
}
