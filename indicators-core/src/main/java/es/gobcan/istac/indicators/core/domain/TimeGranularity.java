package es.gobcan.istac.indicators.core.domain;

import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.enume.domain.IstacTimeGranularityEnum;

/**
 * It is not an entity. Only to returns in Service
 */
public class TimeGranularity {

    private IstacTimeGranularityEnum granularity;
    private InternationalString      title;
    private InternationalString      titleSummary;

    public IstacTimeGranularityEnum getGranularity() {
        return granularity;
    }

    public void setGranularity(IstacTimeGranularityEnum granularity) {
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
