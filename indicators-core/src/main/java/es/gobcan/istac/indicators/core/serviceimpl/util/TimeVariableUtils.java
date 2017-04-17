package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.joda.time.MutableDateTime;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.enume.domain.IstacTimeGranularityEnum;
import org.siemac.metamac.core.common.exception.MetamacException;

import com.ibm.icu.util.Calendar;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.domain.TimeGranularity;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.domain.Translation;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

/**
 * Utilities to time variables
 */
public class TimeVariableUtils {

    // @formatter:off
    private static final List<IstacTimeGranularityEnum> GRANULARITY_ORDER = Arrays.asList(IstacTimeGranularityEnum.DAILY,
                                                                                        IstacTimeGranularityEnum.WEEKLY,
                                                                                        IstacTimeGranularityEnum.MONTHLY,
                                                                                        IstacTimeGranularityEnum.QUARTERLY,
                                                                                        IstacTimeGranularityEnum.FOUR_MONTHLY,
                                                                                        IstacTimeGranularityEnum.BIYEARLY,
                                                                                        IstacTimeGranularityEnum.YEARLY);
    // @formatter:on

    public static Boolean isTimeValue(String value) {
        if (GpeTimeUtils.isTimeValue(value)) {
            return true;
        } else if (MetamacTimeUtils.isTimeValue(value)) {
            return true;
        }
        return false;
    }

    public static TimeValue parseTimeValue(String timeValue) throws MetamacException {
        if (GpeTimeUtils.isTimeValue(timeValue)) {
            return GpeTimeUtils.parseTimeValue(timeValue);
        } else if (MetamacTimeUtils.isTimeValue(timeValue)) {
            return MetamacTimeUtils.parseTimeValue(timeValue);
        }
        throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, timeValue);
    }

    public static IstacTimeGranularityEnum guessTimeGranularity(String value) throws MetamacException {
        if (GpeTimeUtils.isTimeValue(value)) {
            return GpeTimeUtils.guessTimeGranularity(value);
        } else if (MetamacTimeUtils.isTimeValue(value)) {
            return org.siemac.metamac.core.common.time.IstacTimeUtils.guessTimeGranularity(value);
        }
        throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, value);
    }

    public static String calculatePreviousTimeValue(String value) throws MetamacException {
        if (GpeTimeUtils.isTimeValue(value)) {
            return GpeTimeUtils.calculatePreviousTimeValue(value);
        } else if (MetamacTimeUtils.isTimeValue(value)) {
            return MetamacTimeUtils.calculatePreviousTimeValue(value);
        }
        throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, value);
    }

    public static TimeValue convertToLastMonth(TimeValue timeValue) throws MetamacException {
        Date date = timeValueToLastPossibleDate(timeValue);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;

        String yearStr = String.valueOf(year);
        String monthStr = month < 10 ? "0" + String.valueOf(month) : String.valueOf(month);

        return GpeTimeUtils.parseTimeValue(yearStr + "M" + monthStr);
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

        return GpeTimeUtils.parseTimeValue(yearStr + monthStr + dayStr);
    }

    public static int getTimeValueOrderByGranularity(TimeValue timeValue) {
        return GRANULARITY_ORDER.indexOf(timeValue.getGranularity()) + 1;
    }

    public static int getTimeGranularityOrder(IstacTimeGranularityEnum timeGranularity) {
        return GRANULARITY_ORDER.indexOf(timeGranularity) + 1;
    }

    /* Returns a Date representation for time value, it chooses the last time instant represented by the TimeValue */
    public static Date timeValueToLastPossibleDate(TimeValue timeValue) {
        MutableDateTime dt = new MutableDateTime();
        switch (timeValue.getGranularity()) {
            case YEARLY: {
                dt.setYear(Integer.parseInt(timeValue.getYear()));
                dt.setDayOfYear(dt.dayOfYear().getMaximumValue());
                break;
            }
            case BIYEARLY: {
                dt.setYear(Integer.parseInt(timeValue.getYear()));
                // Ignore granularity character
                int subPeriod = Integer.parseInt(timeValue.getSubperiod().substring(1));
                dt.setMonthOfYear(subPeriod * 6);
                dt.setDayOfMonth(dt.dayOfMonth().getMaximumValue());
                break;
            }
            case FOUR_MONTHLY: {
                dt.setYear(Integer.parseInt(timeValue.getYear()));
                // Ignore granularity character
                int subPeriod = Integer.parseInt(timeValue.getSubperiod().substring(1));
                dt.setMonthOfYear(subPeriod * 4);
                dt.setDayOfMonth(dt.dayOfMonth().getMaximumValue());
                break;
            }
            case QUARTERLY: {
                dt.setYear(Integer.parseInt(timeValue.getYear()));
                // Ignore granularity character
                int subPeriod = Integer.parseInt(timeValue.getSubperiod().substring(1));
                dt.setMonthOfYear(subPeriod * 3);
                dt.setDayOfMonth(dt.dayOfMonth().getMaximumValue());
                break;
            }
            case MONTHLY: {
                dt.setYear(Integer.parseInt(timeValue.getYear()));
                // Ignore granularity character
                int subPeriod = Integer.parseInt(timeValue.getSubperiod().substring(1));
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
            // least the granularity is latest the value will be
            if (getTimeValueOrderByGranularity(timeValue1) < getTimeValueOrderByGranularity(timeValue2)) {
                return -1;
            } else if (getTimeValueOrderByGranularity(timeValue1) > getTimeValueOrderByGranularity(timeValue2)) {
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
            // highest the granularity is latest the value will be
            if (getTimeValueOrderByGranularity(timeValue1) > getTimeValueOrderByGranularity(timeValue2)) {
                return -1;
            } else if (getTimeValueOrderByGranularity(timeValue1) < getTimeValueOrderByGranularity(timeValue2)) {
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

    public static TimeValue buildTimeValue(String timeCode, Translation translation) throws MetamacException {
        TimeValue timeValue = parseTimeValue(timeCode);
        if (translation != null) {
            timeValue.setTitle(replaceTranslationPlaceHolder(timeValue, translation.getTitle()));
            if (translation.getTitleSummary() != null) {
                timeValue.setTitleSummary(replaceTranslationPlaceHolder(timeValue, translation.getTitleSummary()));
            } else {
                timeValue.setTitleSummary(replaceTranslationPlaceHolder(timeValue, translation.getTitle()));
            }
        } else {
            timeValue = TimeVariableUtils.parseTimeValue(timeCode);
            timeValue.setTitle(ServiceUtils.generateInternationalStringInDefaultLocales(timeCode));
            timeValue.setTitleSummary(ServiceUtils.generateInternationalStringInDefaultLocales(timeCode));
        }
        return timeValue;
    }

    public static TimeGranularity buildTimeGranularity(IstacTimeGranularityEnum timeGranularityEnum, Translation translation) throws MetamacException {
        TimeGranularity timeGranularity = new TimeGranularity();
        timeGranularity.setGranularity(timeGranularityEnum);

        if (translation != null) {
            timeGranularity.setTitle(translateTimeGranularity(translation.getTitle()));
            if (translation.getTitleSummary() != null) {
                timeGranularity.setTitleSummary(translateTimeGranularity(translation.getTitleSummary()));
            } else {
                timeGranularity.setTitleSummary(translation.getTitle());
            }
        } else {
            timeGranularity.setTitle(ServiceUtils.generateInternationalStringInDefaultLocales(timeGranularityEnum.name()));
            timeGranularity.setTitleSummary(ServiceUtils.generateInternationalStringInDefaultLocales(timeGranularityEnum.name()));
        }
        return timeGranularity;
    }

    private static InternationalString translateTimeGranularity(InternationalString sourceTranslation) {
        InternationalString target = new InternationalString();
        for (LocalisedString localisedStringTranslation : sourceTranslation.getTexts()) {
            LocalisedString localisedString = new LocalisedString();
            localisedString.setLabel(localisedStringTranslation.getLabel());
            localisedString.setLocale(localisedStringTranslation.getLocale());
            target.addText(localisedString);
        }
        return target;
    }

    private static InternationalString replaceTranslationPlaceHolder(TimeValue timeValue, InternationalString sourceTranslation) {
        InternationalString target = new InternationalString();
        for (LocalisedString localisedStringTranslation : sourceTranslation.getTexts()) {
            String label = localisedStringTranslation.getLabel();
            if (timeValue.getYear() != null) {
                label = label.replace(IndicatorsConstants.TRANSLATION_YEAR_IN_LABEL, timeValue.getYear());
            }
            if (timeValue.getMonth() != null) {
                label = label.replace(IndicatorsConstants.TRANSLATION_MONTH_IN_LABEL, timeValue.getMonth());
            }
            if (timeValue.getWeek() != null) {
                label = label.replace(IndicatorsConstants.TRANSLATION_WEEK_IN_LABEL, timeValue.getWeek());
            }
            if (timeValue.getDay() != null) {
                label = label.replace(IndicatorsConstants.TRANSLATION_DAY_IN_LABEL, timeValue.getDay());
            }
            LocalisedString localisedString = new LocalisedString();
            localisedString.setLabel(label);
            localisedString.setLocale(localisedStringTranslation.getLocale());
            target.addText(localisedString);
        }
        return target;
    }

    public static String getTimeValueTranslationCode(String timeCode) throws MetamacException {
        TimeValue timeValueDo = TimeVariableUtils.parseTimeValue(timeCode);
        return getTimeValueTranslationCode(timeValueDo);
    }

    public static String getTimeGranularityTranslationCode(IstacTimeGranularityEnum timeGranularity) {
        return new StringBuilder().append(IndicatorsConstants.TRANSLATION_TIME_GRANULARITY).append(".").append(timeGranularity.name()).toString();
    }

    public static String getTimeValueTranslationCode(TimeValue timeValueDo) throws MetamacException {
        String translationCode = null;
        switch (timeValueDo.getGranularity()) {
            case YEARLY:
                translationCode = IndicatorsConstants.TRANSLATION_TIME_VALUE_YEARLY;
                break;
            case BIYEARLY:
                translationCode = new StringBuilder().append(IndicatorsConstants.TRANSLATION_TIME_VALUE_BIYEARLY).append(".").append(timeValueDo.getSubperiod()).toString();
                break;
            case FOUR_MONTHLY:
                translationCode = new StringBuilder().append(IndicatorsConstants.TRANSLATION_TIME_VALUE_FOUR_MONTHLY).append(".").append(timeValueDo.getSubperiod()).toString();
                break;
            case QUARTERLY:
                translationCode = new StringBuilder().append(IndicatorsConstants.TRANSLATION_TIME_VALUE_QUARTERLY).append(".").append(timeValueDo.getSubperiod()).toString();
                break;
            case MONTHLY:
                translationCode = new StringBuilder().append(IndicatorsConstants.TRANSLATION_TIME_VALUE_MONTHLY).append(".").append(timeValueDo.getSubperiod()).toString();
                break;
            case WEEKLY:
                translationCode = IndicatorsConstants.TRANSLATION_TIME_VALUE_WEEKLY;
                break;
            case DAILY:
                translationCode = IndicatorsConstants.TRANSLATION_TIME_VALUE_DAILY;
                break;
        }
        return translationCode;
    }

}
