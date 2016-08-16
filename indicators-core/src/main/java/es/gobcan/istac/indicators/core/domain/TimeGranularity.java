package es.gobcan.istac.indicators.core.domain;

import org.siemac.metamac.core.common.ent.domain.InternationalString;

import es.gobcan.istac.indicators.core.enume.domain.TimeGranularityEnum;

/**
 * It is not an entity. Only to returns in Service
 */
public class TimeGranularity {

    private TimeGranularityEnum granularity;
    private InternationalString title;
    private InternationalString titleSummary;

   public TimeGranularityEnum getGranularity() {
        return granularity;
    }

    public void setGranularity(TimeGranularityEnum granularity) {
        this.granularity = granularity;
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
