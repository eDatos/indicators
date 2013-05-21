package es.gobcan.istac.indicators.core.serviceimpl.util;

import static org.siemac.metamac.core.common.constants.shared.TimeConstants.BIYEARLY_CHARACTER;
import static org.siemac.metamac.core.common.constants.shared.TimeConstants.MONTHLY_CHARACTER;
import static org.siemac.metamac.core.common.constants.shared.TimeConstants.QUARTERLY_CHARACTER;
import static org.siemac.metamac.core.common.constants.shared.TimeConstants.WEEKLY_CHARACTER;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.TimeUtils;
import org.springframework.util.Assert;

import com.ibm.icu.util.Calendar;

import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.enume.domain.TimeGranularityEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

/**
 * Utilities to time variables
 */
public class TimeVariableUtils extends TimeUtils {

    public static TimeValue convertToLastMonth(TimeValue timeValue) throws MetamacException {
        Date date = timeValueToLastPossibleDate(timeValue);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;

        String yearStr = String.valueOf(year);
        String monthStr = month < 10 ? "0" + String.valueOf(month) : String.valueOf(month);

        return parseTimeValue(yearStr + "M" + monthStr);
    }

    public static TimeValue convertToLastDay(TimeValue timeValue) throws MetamacException {
        Date date = timeValueToLastPossibleDate(timeValue);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        String yearStr = String.valueOf(year);
        String monthStr = month < 10 ? "0" + String.valueOf(month) : String.valueOf(month);
        String dayStr = day < 10 ? "0" + String.valueOf(day) : String.valueOf(day);

        return parseTimeValue(yearStr + monthStr + dayStr);
    }

    private static int getTimeGranularityOrder(TimeValue timeValue) {
        List<TimeGranularityEnum> granularityOrder = Arrays.asList(TimeGranularityEnum.DAILY, TimeGranularityEnum.WEEKLY, TimeGranularityEnum.MONTHLY, TimeGranularityEnum.QUARTERLY,
                TimeGranularityEnum.BIYEARLY, TimeGranularityEnum.YEARLY);
        Assert.isTrue(granularityOrder.size() == TimeGranularityEnum.values().length, "Se debe especificar un orden para cada valor de granularidad");
        return granularityOrder.indexOf(timeValue.getGranularity()) + 1;
    }

    /* Returns a Date representation for time value, it chooses the last time instant represented by the TimeValue */
    private static Date timeValueToLastPossibleDate(TimeValue timeValue) {
        MutableDateTime dt = new MutableDateTime();
        switch (timeValue.getGranularity()) {
            case BIYEARLY: {
                dt.setYear(Integer.parseInt(timeValue.getYear()));
                int subPeriod = Integer.parseInt(timeValue.getSubperiod().substring(1)); // Ignore granularity character
                dt.setMonthOfYear(subPeriod * 6);
                dt.setDayOfMonth(dt.dayOfMonth().getMaximumValue());
                break;
            }
            case YEARLY: {
                dt.setYear(Integer.parseInt(timeValue.getYear()));
                dt.setDayOfYear(dt.dayOfYear().getMaximumValue());
                break;
            }
            case QUARTERLY: {
                dt.setYear(Integer.parseInt(timeValue.getYear()));
                int subPeriod = Integer.parseInt(timeValue.getSubperiod().substring(1)); // Ignore granularity character
                dt.setMonthOfYear(subPeriod * 3);
                dt.setDayOfMonth(dt.dayOfMonth().getMaximumValue());
                break;
            }
            case MONTHLY: {
                dt.setYear(Integer.parseInt(timeValue.getYear()));
                int subPeriod = Integer.parseInt(timeValue.getSubperiod().substring(1)); // Ignore granularity character
                dt.setMonthOfYear(subPeriod);
                dt.setDayOfMonth(dt.dayOfMonth().getMaximumValue());
                break;
            }
            case WEEKLY: {
                dt.setWeekyear(Integer.parseInt(timeValue.getYear()));
                int subPeriod = Integer.parseInt(timeValue.getWeek());
                dt.setWeekOfWeekyear(subPeriod);
                dt.setDayOfWeek(dt.dayOfWeek().getMaximumValue());
                break;
            }
            case DAILY: {
                dt.setYear(Integer.parseInt(timeValue.getYear()));
                int month = Integer.parseInt(timeValue.getMonth());
                int day = Integer.parseInt(timeValue.getDay());
                dt.setMonthOfYear(month);
                dt.setDayOfMonth(day);
                break;
            }
        }
        dt.setMillisOfDay(dt.millisOfDay().getMaximumValue());
        return dt.toDate();
    }

    public static TimeValue findLatestTimeValue(List<TimeValue> timeValues) {
        if (timeValues == null || timeValues.size() == 0) {
            return null;
        }
        sortTimeValuesMostRecentFirstLowestGranularityMostRecent(timeValues);

        return timeValues.get(0);
    }

    public static void sortTimeValuesMostRecentFirst(List<TimeValue> values) {
        Collections.sort(values, new Comparator<TimeValue>() {

            @Override
            public int compare(TimeValue o1, TimeValue o2) {
                return compareToHighestGranularityFirst(o1, o2);
            }
        });
    }
    
    public static void sortTimeValuesMostRecentFirstLastValue(List<TimeValue> values) {
        Collections.sort(values, new Comparator<TimeValue>() {
            
            @Override
            public int compare(TimeValue o1, TimeValue o2) {
                return compareToLowestGranularityFirst(o1, o2);
            }
        });
    }

    private static void sortTimeValuesMostRecentFirstLowestGranularityMostRecent(List<TimeValue> values) {
        Collections.sort(values, new Comparator<TimeValue>() {

            @Override
            public int compare(TimeValue o1, TimeValue o2) {
                return compareToLowestGranularityFirst(o1, o2);
            }
        });
    }

    /**
     * Compare two time values.
     * 
     * @return 0 if are equals; a value less than 0 if this timeValue1 is less than timeValue2; a value greater than 0 if this timeValue1 is less than timeValue2
     */
    private static int compareToLowestGranularityFirst(TimeValue timeValue1, TimeValue timeValue2) {
        Date date1 = timeValueToLastPossibleDate(timeValue1);
        Date date2 = timeValueToLastPossibleDate(timeValue2);
        if (date1.after(date2)) {
            return -1;
        } else if (date1.before(date2)) {
            return 1;
        } else {
            if (getTimeGranularityOrder(timeValue1) < getTimeGranularityOrder(timeValue2)) { // least the granularity is latest the value will be
                return -1;
            } else if (getTimeGranularityOrder(timeValue1) > getTimeGranularityOrder(timeValue2)) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    /**
     * Compare two time values. if date values are the same
     * 
     * @return 0 if are equals; a value less than 0 if this timeValue1 is less than timeValue2; a value greater than 0 if this timeValue1 is less than timeValue2
     */
    private static int compareToHighestGranularityFirst(TimeValue timeValue1, TimeValue timeValue2) {
        Date date1 = timeValueToLastPossibleDate(timeValue1);
        Date date2 = timeValueToLastPossibleDate(timeValue2);
        if (date1.after(date2)) {
            return -1;
        } else if (date1.before(date2)) {
            return 1;
        } else {
            if (getTimeGranularityOrder(timeValue1) > getTimeGranularityOrder(timeValue2)) { // highest the granularity is latest the value will be
                return -1;
            } else if (getTimeGranularityOrder(timeValue1) < getTimeGranularityOrder(timeValue2)) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    /**
     * Compare two time values in String representation
     * 
     * @return 0 if are equals; a value less than 0 if this value1 is less than value2; a value greater than 0 if this value1 is less than value2
     */
    public static int compareToMostRecentFirstLowestGranularityMostRecent(String value1, String value2) throws MetamacException {
        TimeValue timeValue1 = parseTimeValue(value1);
        TimeValue timeValue2 = parseTimeValue(value2);
        return compareToLowestGranularityFirst(timeValue1, timeValue2);
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
