package es.gobcan.istac.indicators.core.domain;

import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.enume.domain.IstacTimeGranularityEnum;

/**
 * It is not an entity. Only to returns in Service
 */

public class TimeValue {

    private String                   timeValue;
    private IstacTimeGranularityEnum granularity;
    private String                   year;
    private String                   subperiod;
    private String                   month;
    private String                   week;
    private String                   day;
    private String                   hour;
    private String                   minutes;
    private String                   seconds;
    private InternationalString      title;
    private InternationalString      titleSummary;

    public String getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(String timeValue) {
        this.timeValue = timeValue;
    }

    public IstacTimeGranularityEnum getGranularity() {
        return granularity;
    }

    public void setGranularity(IstacTimeGranularityEnum granularity) {
        this.granularity = granularity;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSubperiod() {
        return subperiod;
    }

    public void setSubperiod(String subperiodCharacter, String subperiodValue) {
        this.subperiod = new StringBuilder().append(subperiodCharacter).append(subperiodValue).toString();
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }

    public InternationalString getTitle() {
        return title;
    }

    public void setTitle(InternationalString title) {
        this.title = title;
    }

    public InternationalString getTitleSummary() {
        return titleSummary;
    }

    public void setTitleSummary(InternationalString titleSummary) {
        this.titleSummary = titleSummary;
    }
}
