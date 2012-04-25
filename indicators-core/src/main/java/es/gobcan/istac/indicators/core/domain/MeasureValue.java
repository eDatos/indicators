package es.gobcan.istac.indicators.core.domain;

import org.siemac.metamac.core.common.ent.domain.InternationalString;

import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;

/**
 * It is not an entity. Only to returns in Service
 */
public class MeasureValue {

    private MeasureDimensionTypeEnum measureValue;
    private InternationalString title;
    private InternationalString titleSummary;

    public MeasureDimensionTypeEnum getMeasureValue() {
        return measureValue;
    }
    
    public void setMeasureValue(MeasureDimensionTypeEnum measureValue) {
        this.measureValue = measureValue;
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
