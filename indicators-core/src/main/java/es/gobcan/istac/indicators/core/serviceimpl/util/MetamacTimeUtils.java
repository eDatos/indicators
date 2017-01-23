package es.gobcan.istac.indicators.core.serviceimpl.util;

import static org.siemac.metamac.core.common.constants.shared.TimeConstants.BIYEARLY_CHARACTER;
import static org.siemac.metamac.core.common.constants.shared.TimeConstants.MONTHLY_CHARACTER;
import static org.siemac.metamac.core.common.constants.shared.TimeConstants.QUARTERLY_CHARACTER;
import static org.siemac.metamac.core.common.constants.shared.TimeConstants.WEEKLY_CHARACTER;

import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.time.TimeSdmx;
import org.siemac.metamac.core.common.util.SdmxTimeUtils;

import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.enume.domain.TimeGranularityEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

public class MetamacTimeUtils  {

    private static final List<TimeGranularityEnum> GRANULARITY_ORDER = Arrays.asList(TimeGranularityEnum.DAILY,
                                                                                        TimeGranularityEnum.WEEKLY,
                                                                                        TimeGranularityEnum.MONTHLY,
                                                                                        TimeGranularityEnum.QUARTERLY,
                                                                                        TimeGranularityEnum.BIYEARLY,
                                                                                        TimeGranularityEnum.YEARLY);
    // @formatter:on
    
    
    public static Boolean isTimeValue(String value) {
        return SdmxTimeUtils.isObservationalTimePeriod(value);
    }
    
    /**
     * Guess time granularity of a time value
     */
    public static TimeGranularityEnum guessTimeGranularity(String value) throws MetamacException {
        TimeSdmx parseTime = SdmxTimeUtils.parseTime(value);
        if (parseTime == null) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, value);
        }
        
        switch (parseTime.getType()) {
            case REPORTING_TIME_PERIOD_YEAR:
                return TimeGranularityEnum.YEARLY;
            case REPORTING_TIME_PERIOD_SEMESTER:
                return TimeGranularityEnum.BIYEARLY;
            case REPORTING_TIME_PERIOD_TRIMESTER:
                // 3 groups of 4 months per year.
                // NOT SUPPORTED IN INDICATORS: Possible WorkAround: It could be included in the upper slot (SEMESTER)
                break;
            case REPORTING_TIME_PERIOD_QUARTER:
                // 4 groups of 3 months per year.
                return TimeGranularityEnum.QUARTERLY;
            case REPORTING_TIME_PERIOD_MONTH:
                return TimeGranularityEnum.MONTHLY;
            case REPORTING_TIME_PERIOD_WEEK:
                return TimeGranularityEnum.WEEKLY;
            case REPORTING_TIME_PERIOD_DAY:
                return TimeGranularityEnum.DAILY;
            case DATETIME:
                return TimeGranularityEnum.DAILY;
            case GREGORIAN_TIME_YEAR:
                return TimeGranularityEnum.YEARLY;
            case GREGORIAN_TIME_MONTH:
                return TimeGranularityEnum.MONTHLY;
            case GREGORIAN_TIME_DATE:
                return TimeGranularityEnum.DAILY;
            case TIME_RANGE_DATE:
                // NOT SUPPORTED IN INDICATORS: Possible WorkAround: Using the calculated duration, enclose the time in the upper slot
                break;
            case TIME_RANGE_DATETIME:
                // NOT SUPPORTED IN INDICATORS: Possible WorkAround: Using the calculated duration, enclose the time in the upper slot
                break;
            default:
                break;
        }
        throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, value);
    }

    public static TimeValue parseTimeValue(String timeValue) throws MetamacException {

        TimeValue timeValueFields = new TimeValue();
        timeValueFields.setTimeValue(timeValue);

        TimeGranularityEnum timeGranularity = guessTimeGranularity(timeValue);
        timeValueFields.setGranularity(timeGranularity);
        
        TimeSdmx parseTime = SdmxTimeUtils.parseTime(timeValue);

        switch (timeGranularity) {
            case YEARLY: {
                timeValueFields.setYear(String.valueOf(parseTime.getStartDateTime().getYear()));
                break;
            }
            case BIYEARLY: {
                timeValueFields.setYear(String.valueOf(parseTime.getStartDateTime().getYear()));
                timeValueFields.setSubperiod(BIYEARLY_CHARACTER, calculateBiyearlyFromMonth(parseTime.getStartDateTime().getMonthOfYear()));
                break;
            }
            case QUARTERLY: {
                timeValueFields.setYear(String.valueOf(parseTime.getStartDateTime().getYear()));
                timeValueFields.setSubperiod(QUARTERLY_CHARACTER, calculateQuarterlyFromMonth(parseTime.getStartDateTime().getMonthOfYear()));
                break;
            }
            case MONTHLY: {
                timeValueFields.setYear(String.valueOf(parseTime.getStartDateTime().getYear()));
                timeValueFields.setSubperiod(MONTHLY_CHARACTER, String.valueOf(parseTime.getStartDateTime().getMonthOfYear()));
                break;
            }
            case WEEKLY: {
                timeValueFields.setYear(String.valueOf(parseTime.getStartDateTime().getYear()));
                String week = String.valueOf(parseTime.getStartDateTime().getWeekOfWeekyear());
                timeValueFields.setWeek(week);
                timeValueFields.setSubperiod(WEEKLY_CHARACTER, week);
                break;
            }
            case DAILY: {
                timeValueFields.setYear(String.valueOf(parseTime.getStartDateTime().getYear()));
                timeValueFields.setMonth(String.valueOf(parseTime.getStartDateTime().getMonthOfYear()));
                timeValueFields.setDay(String.valueOf(parseTime.getStartDateTime().getDayOfYear()));
                break;
            }
            default:
                throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, timeValue);
        }

        return timeValueFields;
    }
    
    private static String calculateBiyearlyFromMonth(int monthOfYear) {
        return (monthOfYear < 7) ? "1": "2"; 
    }
    
    private static String calculateQuarterlyFromMonth(int monthOfYear) {
        if (monthOfYear < 4) {
            return "1";
        }
        else if (monthOfYear < 7) {
            return "2";
        }
        else if (monthOfYear < 10) {
            return "3"; 
        }
        else {
            return "4"; 
        }
    }

    /**
     * Retrieves previous time value in same granularity
     */
    public static String calculatePreviousTimeValue(String value) throws MetamacException {
        if (value == null) {
            return null;
        }
        TimeGranularityEnum timeGranularity = guessTimeGranularity(value);
        TimeSdmx parseTime = SdmxTimeUtils.parseTime(value);
        switch (timeGranularity) {
            case YEARLY: {
                return String.valueOf(parseTime.getStartDateTime().minusYears(1).getYear());
            }
            case BIYEARLY: {
                DateTime previous = parseTime.getStartDateTime().minusMonths(6);
                return (new StringBuilder()).append(String.valueOf(previous.getYear())).append(BIYEARLY_CHARACTER).append(calculateBiyearlyFromMonth(previous.getMonthOfYear())).toString();
            }
            case QUARTERLY: {
                DateTime previous = parseTime.getStartDateTime().minusMonths(3);
                return (new StringBuilder()).append(String.valueOf(previous.getYear())).append(QUARTERLY_CHARACTER).append(calculateQuarterlyFromMonth(previous.getMonthOfYear())).toString();
            }
            case MONTHLY: {
                DateTime previous = parseTime.getStartDateTime().minusMonths(1);
                return (new StringBuilder()).append(String.valueOf(previous.getYear())).append(MONTHLY_CHARACTER).append(previous.getMonthOfYear()).toString();
            }
            case WEEKLY: {
                DateTime previous = parseTime.getStartDateTime().minusWeeks(1);
                return (new StringBuilder()).append(String.valueOf(previous.getYear())).append(WEEKLY_CHARACTER).append(previous.getWeekOfWeekyear()).toString();
            }
            case DAILY:  {
                DateTime previous = parseTime.getStartDateTime().minusDays(1);
                return previous.toString("yyyyMMdd");
            }
        }
        throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, value);
    }
 
}
