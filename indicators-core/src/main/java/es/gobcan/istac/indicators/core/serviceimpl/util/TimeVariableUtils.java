package es.gobcan.istac.indicators.core.serviceimpl.util;

import static es.gobcan.istac.indicators.core.constants.TimeVariableConstants.BIYEARLY_CHARACTER;
import static es.gobcan.istac.indicators.core.constants.TimeVariableConstants.BIYEARLY_PATTERN;
import static es.gobcan.istac.indicators.core.constants.TimeVariableConstants.DAILY_PATTERN;
import static es.gobcan.istac.indicators.core.constants.TimeVariableConstants.END;
import static es.gobcan.istac.indicators.core.constants.TimeVariableConstants.MONTHLY_CHARACTER;
import static es.gobcan.istac.indicators.core.constants.TimeVariableConstants.MONTHLY_PATTERN;
import static es.gobcan.istac.indicators.core.constants.TimeVariableConstants.QUARTERLY_CHARACTER;
import static es.gobcan.istac.indicators.core.constants.TimeVariableConstants.QUARTERLY_PATTERN;
import static es.gobcan.istac.indicators.core.constants.TimeVariableConstants.START;
import static es.gobcan.istac.indicators.core.constants.TimeVariableConstants.TIME_VALUE_PATTERN;
import static es.gobcan.istac.indicators.core.constants.TimeVariableConstants.WEEKLY_CHARACTER;
import static es.gobcan.istac.indicators.core.constants.TimeVariableConstants.WEEKLY_PATTERN;
import static es.gobcan.istac.indicators.core.constants.TimeVariableConstants.YEARLY_PATTERN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.enume.domain.TimeGranularityEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

/**
 * Utilities to time variables
 */
public class TimeVariableUtils {

    private static Pattern patternTimeValue     = Pattern.compile(START + TIME_VALUE_PATTERN + END);

    private static Pattern patternYearlyValue   = Pattern.compile(START + YEARLY_PATTERN + END);
    private static Pattern patternBiyearlyValue = Pattern.compile(START + BIYEARLY_PATTERN + END);
    private static Pattern patternQuaterlyValue = Pattern.compile(START + QUARTERLY_PATTERN + END);
    private static Pattern patternMonthlyValue  = Pattern.compile(START + MONTHLY_PATTERN + END);
    private static Pattern patternWeeklyValue   = Pattern.compile(START + WEEKLY_PATTERN + END);
    private static Pattern patternDailyValue    = Pattern.compile(START + DAILY_PATTERN + END);

    /**
     * Checks if a time value is valid
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
        TimeGranularityEnum timeGranularityEnum1 = guessTimeGranularity(value1);
        TimeGranularityEnum timeGranularityEnum2 = guessTimeGranularity(value2);
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
    private static String calculatePreviousTimeValueWithSubperiod(Pattern pattern, String timeValue, String subperiodCharacter, String maximumSubperiodAtYear, int subperiodStringSize) throws MetamacException {
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
        
        //Year is in first 4 letters
        String yearStr = value.substring(0, 4);
        Integer year = Integer.parseInt(yearStr); 
        
        String feb29 = "0229";
        if (feb29.equals(value.substring(4))) {
            return null;
        }
        year = year-1;
        return value.replaceFirst(yearStr, year.toString());
    }
}
