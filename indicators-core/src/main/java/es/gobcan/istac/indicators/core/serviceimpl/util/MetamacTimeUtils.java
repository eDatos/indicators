package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.regex.Matcher;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.constants.shared.SDMXCommonRegExpV2_1;
import org.siemac.metamac.core.common.enume.domain.IstacTimeGranularityEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.time.TimeSdmx;
import org.siemac.metamac.core.common.util.SdmxTimeUtils;

import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

public class MetamacTimeUtils {

    private static final String HYPHEN = "-";

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
                timeValueFields.setSubperiod(SDMXCommonRegExpV2_1.REPORTING_MONTH_PERIOD_INDICATOR, calculateMonthlyFromMonth(parseTime.getStartDateTime().getMonthOfYear()));
                break;
            }
            case WEEKLY: {
                timeValueFields.setYear(String.valueOf(parseTime.getStartDateTime().getYear()));
                String week = formatToTwoDigits(parseTime.getStartDateTime().getWeekOfWeekyear());
                timeValueFields.setWeek(week);
                timeValueFields.setSubperiod(SDMXCommonRegExpV2_1.REPORTING_WEEK_PERIOD_INDICATOR, week);
                break;
            }
            case DAILY: {
                timeValueFields.setYear(String.valueOf(parseTime.getStartDateTime().getYear()));
                timeValueFields.setMonth(formatToTwoDigits(parseTime.getStartDateTime().getMonthOfYear()));
                timeValueFields.setDay(formatToTwoDigits(parseTime.getStartDateTime().getDayOfMonth()));
                break;
            }
            case HOURLY: {
                timeValueFields.setYear(String.valueOf(parseTime.getStartDateTime().getYear()));
                timeValueFields.setMonth(formatToTwoDigits(parseTime.getStartDateTime().getMonthOfYear()));
                timeValueFields.setDay(formatToTwoDigits(parseTime.getStartDateTime().getDayOfMonth()));
                timeValueFields.setHour(formatToTwoDigits(parseTime.getStartDateTime().getHourOfDay()));
                timeValueFields.setMinutes(formatToTwoDigits(parseTime.getStartDateTime().getMinuteOfHour()));
                timeValueFields.setSeconds(formatToTwoDigits(parseTime.getStartDateTime().getSecondOfMinute()));
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

    private static String calculateMonthlyFromMonth(int value) {
        return formatToTwoDigits(value);
    }

    private static String formatToTwoDigits(int value) {
        return String.format("%02d", value);
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
                return (new StringBuilder()).append(String.valueOf(previous.getYear())).append(HYPHEN).append(SDMXCommonRegExpV2_1.REPORTING_SEMESTER_PERIOD_INDICATOR)
                        .append(calculateBiyearlyFromMonth(previous.getMonthOfYear())).toString();
            }
            case FOUR_MONTHLY: {
                DateTime previous = parseTime.getStartDateTime().minusMonths(4);
                return (new StringBuilder()).append(String.valueOf(previous.getYear())).append(HYPHEN).append(SDMXCommonRegExpV2_1.REPORTING_TRIMESTER_PERIOD_INDICATOR)
                        .append(calculateQuarterlyFromMonth(previous.getMonthOfYear())).toString();
            }
            case QUARTERLY: {
                DateTime previous = parseTime.getStartDateTime().minusMonths(3);
                return (new StringBuilder()).append(String.valueOf(previous.getYear())).append(HYPHEN).append(SDMXCommonRegExpV2_1.REPORTING_QUARTER_PERIOD_INDICATOR)
                        .append(calculateQuarterlyFromMonth(previous.getMonthOfYear())).toString();
            }
            case MONTHLY: {
                DateTime previous = parseTime.getStartDateTime().minusMonths(1);
                return buildGregorianYearMonth(previous);
            }
            case WEEKLY: {
                DateTime previous = parseTime.getStartDateTime().minusWeeks(1);
                return (new StringBuilder()).append(String.valueOf(previous.getYear())).append(HYPHEN).append(SDMXCommonRegExpV2_1.REPORTING_WEEK_PERIOD_INDICATOR).append(previous.getWeekOfWeekyear())
                        .toString();
            }
            case DAILY: {
                DateTime previous = parseTime.getStartDateTime().minusDays(1);
                return previous.toString("yyyy-MM-dd");
            }
            case HOURLY: {
                DateTime previous = parseTime.getStartDateTime().minusHours(1);
                return previous.toString("yyyy'-'MM'-'dd'T'HH':'mm':'ss");
            }
        }
        throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, value);
    }

    public static String normalizeToMetamacTimeValue(String timeValue) throws MetamacException {
        if (timeValue == null) {
            return null;
        }
        String normalizedTimeValue = convertGPETimeValueToMetamacTimeValue(timeValue);
        normalizedTimeValue = convertAmbiguousTimeValueToMetamacTimeValue(normalizedTimeValue);
        return normalizedTimeValue;
    }

    /*
     * Normalize those metamac time values that can be written on on different ways, like 2000-A1 and 2000-M12
     */
    public static String convertAmbiguousTimeValueToMetamacTimeValue(String value) throws MetamacException {
        IstacTimeGranularityEnum timeGranularity = org.siemac.metamac.core.common.time.IstacTimeUtils.guessTimeGranularity(value);
        DateTime parseDateTime = SdmxTimeUtils.parseTime(value).getStartDateTime();
        switch (timeGranularity) {
            case YEARLY: {
                return String.valueOf(parseDateTime.getYear());
            }

            case MONTHLY: {
                return buildGregorianYearMonth(parseDateTime);
            }
            default:
                return value;
        }
    }

    public static String convertGPETimeValueToMetamacTimeValue(String timeValue) throws MetamacException {

        if (!GpeTimeUtils.isTimeValue(timeValue)) {
            return timeValue;
        }

        IstacTimeGranularityEnum timeGranularity = GpeTimeUtils.guessTimeGranularity(timeValue);

        switch (timeGranularity) {
            case YEARLY: {
                Matcher matcher = GpeTimeUtils.patternYearlyValue.matcher(timeValue);
                if (matcher.matches()) {
                    return String.valueOf(matcher.group(1));
                }
                break;
            }
            case BIYEARLY: {
                Matcher matcher = GpeTimeUtils.patternBiyearlyValue.matcher(timeValue);
                if (matcher.matches()) {
                    return (new StringBuilder()).append(matcher.group(1)).append(HYPHEN).append(SDMXCommonRegExpV2_1.REPORTING_SEMESTER_PERIOD_INDICATOR).append(matcher.group(2)).toString();
                }
                break;
            }
            case QUARTERLY: {
                Matcher matcher = GpeTimeUtils.patternQuaterlyValue.matcher(timeValue);
                if (matcher.matches()) {
                    return (new StringBuilder()).append(matcher.group(1)).append(HYPHEN).append(SDMXCommonRegExpV2_1.REPORTING_QUARTER_PERIOD_INDICATOR).append(matcher.group(2)).toString();
                }
                break;
            }
            case MONTHLY: {
                Matcher matcher = GpeTimeUtils.patternMonthlyValue.matcher(timeValue);
                if (matcher.matches()) {
                    return buildReportingMonth(matcher.group(1), matcher.group(2));
                }
                break;
            }
            case WEEKLY: {
                Matcher matcher = GpeTimeUtils.patternWeeklyValue.matcher(timeValue);
                if (matcher.matches()) {
                    return (new StringBuilder()).append(matcher.group(1)).append(HYPHEN).append(SDMXCommonRegExpV2_1.REPORTING_WEEK_PERIOD_INDICATOR).append(matcher.group(2)).toString();
                }
                break;
            }
            case DAILY: {
                Matcher matcher = GpeTimeUtils.patternDailyValue.matcher(timeValue);
                if (matcher.matches()) {
                    return (new StringBuilder()).append(matcher.group(1)).append(HYPHEN).append(matcher.group(2)).append(HYPHEN).append(matcher.group(3)).toString();
                }
                break;
            }
            default:
                throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, timeValue);
        }

        throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, timeValue);
    }

    /*
     * @see org.siemac.edatos.core.common.constants.shared.SDMXCommonRegExpV2_1.REPORTING_MONTH_TYPE
     */
    private static String buildReportingMonth(String year, String month) {
        return (new StringBuilder()).append(year).append(HYPHEN).append(SDMXCommonRegExpV2_1.REPORTING_MONTH_PERIOD_INDICATOR).append(month).toString();
    }

    private static String buildReportingMonth(int year, int month) {
        return buildReportingMonth(String.valueOf(year), calculateMonthlyFromMonth(month));
    }

    /*
     * @see org.siemac.edatos.core.common.constants.shared.SDMXCommonRegExpV2_1.GREGORIAN_TIME_PERIOD_TYPE
     */
    private static String buildGregorianYearMonth(DateTime time) {
        return time.toString("YYYY-MM");
    }

}
