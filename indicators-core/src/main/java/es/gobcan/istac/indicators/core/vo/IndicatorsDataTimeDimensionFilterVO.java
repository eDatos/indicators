package es.gobcan.istac.indicators.core.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.gobcan.istac.edatos.dataset.repository.repository.impl.util.Pair;
import es.gobcan.istac.indicators.core.domain.TimeValue;

public class IndicatorsDataTimeDimensionFilterVO {

    private List<String>               granularityCodes = new ArrayList<String>();
    private List<String>               codes            = new ArrayList<String>();
    private Integer                    conditionLast    = null;
    private TimeValue                  conditionAfter   = null;
    private Pair<TimeValue, TimeValue> conditionRange   = null;

    public List<String> getGranularityCodes() {
        if (granularityCodes == null) {
            return new ArrayList<String>();
        }
        return granularityCodes;
    }

    public void setGranularityCodes(List<String> granularityCodes) {
        this.granularityCodes = granularityCodes;
    }

    public List<String> getCodes() {
        if (codes == null) {
            return new ArrayList<String>();
        }
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    public Integer getConditionLast() {
        return conditionLast;
    }

    public void setConditionLast(Integer conditionLast) {
        this.conditionLast = conditionLast;
    }

    public TimeValue getConditionAfter() {
        return conditionAfter;
    }

    public void setConditionAfter(TimeValue conditionAfter) {
        this.conditionAfter = conditionAfter;
    }

    public Pair<TimeValue, TimeValue> getConditionRange() {
        return conditionRange;
    }

    public void setConditionRange(Pair<TimeValue, TimeValue> conditionRange) {
        this.conditionRange = conditionRange;
    }

    // Builders
    public static IndicatorsDataTimeDimensionFilterVO buildTimeGranularityFilter(String... granularityCodes) {
        IndicatorsDataTimeDimensionFilterVO filter = new IndicatorsDataTimeDimensionFilterVO();
        filter.setGranularityCodes(new ArrayList<String>(Arrays.asList(granularityCodes)));
        return filter;
    }
}
