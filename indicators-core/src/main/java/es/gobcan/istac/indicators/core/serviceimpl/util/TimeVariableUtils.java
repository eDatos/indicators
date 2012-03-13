package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static es.gobcan.istac.indicators.core.constants.TimeVariableConstants.*;
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
}
