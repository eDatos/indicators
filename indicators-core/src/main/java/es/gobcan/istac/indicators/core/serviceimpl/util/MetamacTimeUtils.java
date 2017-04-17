package es.gobcan.istac.indicators.core.serviceimpl.util;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.constants.shared.SDMXCommonRegExpV2_1;
import org.siemac.metamac.core.common.enume.domain.IstacTimeGranularityEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.time.TimeSdmx;
import org.siemac.metamac.core.common.util.SdmxTimeUtils;

import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

public class MetamacTimeUtils {

    public static Boolean isTimeValue(String value) {
        return SdmxTimeUtils.isObservationalTimePeriod(value);
    }

    public static TimeValue parseTimeValue(String timeValue) throws MetamacException {

        TimeValue timeValueFields = new TimeValue();
        timeValueFields.setTimeValue(timeValue);

        IstacTimeGranularityEnum timeGranularity = org.siemac.metamac.core.common.time.IstacTimeUtils.guessTimeGranularity(timeValue);
        timeValueFields.setGranularity(timeGranularity);

        TimeSdmx parseTime = SdmxTimeUtils.parseTime(timeValue);

        switch (timeGranularity) {
            case YEARLY: {
                timeValueFields.setYear(String.valueOf(parseTime.getStartDateTime().getYear()));
                break;
            }
            case BIYEARLY: {
                timeValueFields.setYear(String.valueOf(parseTime.getStartDateTime().getYear()));
                timeValueFields.setSubperiod(SDMXCommonRegExpV2_1.REPORTING_SEMESTER_PERIOD_INDICATOR, calculateBiyearlyFromMonth(parseTime.getStartDateTime().getMonthOfYear()));
                break;
            }
            case FOUR_MONTHLY: {
                timeValueFields.setYear(String.valueOf(parseTime.getStartDateTime().getYear()));
                timeValueFields.setSubperiod(SDMXCommonRegExpV2_1.REPORTING_TRIMESTER_PERIOD_INDICATOR, calculateQuarterlyFromMonth(parseTime.getStartDateTime().getMonthOfYear()));
                break;
            }
            case QUARTERLY: {
                timeValueFields.setYear(String.valueOf(parseTime.getStartDateTime().getYear()));
                timeValueFields.setSubperiod(SDMXCommonRegExpV2_1.REPORTING_QUARTER_PERIOD_INDICATOR, calculateQuarterlyFromMonth(parseTime.getStartDateTime().getMonthOfYear()));
                break;
            }
            case MONTHLY: {
                timeValueFields.setYear(String.valueOf(parseTime.getStartDateTime().getYear()));
                timeValueFields.setSubperiod(SDMXCommonRegExpV2_1.REPORTING_MONTH_PERIOD_INDICATOR, String.valueOf(parseTime.getStartDateTime().getMonthOfYear()));
                break;
            }
            case WEEKLY: {
                timeValueFields.setYear(String.valueOf(parseTime.getStartDateTime().getYear()));
                String week = String.valueOf(parseTime.getStartDateTime().getWeekOfWeekyear());
                timeValueFields.setWeek(week);
                timeValueFields.setSubperiod(SDMXCommonRegExpV2_1.REPORTING_WEEK_PERIOD_INDICATOR, week);
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
        return (monthOfYear < 7) ? "1" : "2";
    }

    private static String calculateQuarterlyFromMonth(int monthOfYear) {
        if (monthOfYear < 4) {
            return "1";
        } else if (monthOfYear < 7) {
            return "2";
        } else if (monthOfYear < 10) {
            return "3";
        } else {
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
        IstacTimeGranularityEnum timeGranularity = org.siemac.metamac.core.common.time.IstacTimeUtils.guessTimeGranularity(value);
        TimeSdmx parseTime = SdmxTimeUtils.parseTime(value);
        switch (timeGranularity) {
            case YEARLY: {
                return String.valueOf(parseTime.getStartDateTime().minusYears(1).getYear());
            }
            case BIYEARLY: {
                DateTime previous = parseTime.getStartDateTime().minusMonths(6);
                return (new StringBuilder()).append(String.valueOf(previous.getYear())).append(SDMXCommonRegExpV2_1.REPORTING_SEMESTER_PERIOD_INDICATOR)
                        .append(calculateBiyearlyFromMonth(previous.getMonthOfYear())).toString();
            }
            case FOUR_MONTHLY: {
                DateTime previous = parseTime.getStartDateTime().minusMonths(4);
                return (new StringBuilder()).append(String.valueOf(previous.getYear())).append(SDMXCommonRegExpV2_1.REPORTING_TRIMESTER_PERIOD_INDICATOR)
                        .append(calculateQuarterlyFromMonth(previous.getMonthOfYear())).toString();
            }
            case QUARTERLY: {
                DateTime previous = parseTime.getStartDateTime().minusMonths(3);
                return (new StringBuilder()).append(String.valueOf(previous.getYear())).append(SDMXCommonRegExpV2_1.REPORTING_QUARTER_PERIOD_INDICATOR)
                        .append(calculateQuarterlyFromMonth(previous.getMonthOfYear())).toString();
            }
            case MONTHLY: {
                DateTime previous = parseTime.getStartDateTime().minusMonths(1);
                return (new StringBuilder()).append(String.valueOf(previous.getYear())).append(SDMXCommonRegExpV2_1.REPORTING_MONTH_PERIOD_INDICATOR).append(previous.getMonthOfYear()).toString();
            }
            case WEEKLY: {
                DateTime previous = parseTime.getStartDateTime().minusWeeks(1);
                return (new StringBuilder()).append(String.valueOf(previous.getYear())).append(SDMXCommonRegExpV2_1.REPORTING_WEEK_PERIOD_INDICATOR).append(previous.getWeekOfWeekyear()).toString();
            }
            case DAILY: {
                DateTime previous = parseTime.getStartDateTime().minusDays(1);
                return previous.toString("yyyyMMdd");
            }
        }
        throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, value);
    }

}
