package es.gobcan.istac.indicators.core.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IndicatorsDataTimeDimensionFilterVO {

    private List<String> granularityCodes = new ArrayList<String>();
    private List<String> codes            = new ArrayList<String>();

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

    // Builders
    public static IndicatorsDataTimeDimensionFilterVO buildTimeGranularityFilter(String... granularityCodes) {
        IndicatorsDataTimeDimensionFilterVO filter = new IndicatorsDataTimeDimensionFilterVO();
        filter.setGranularityCodes(new ArrayList<String>(Arrays.asList(granularityCodes)));
        return filter;
    }
}
