package es.gobcan.istac.indicators.core.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IndicatorsDataMeasureDimensionFilterVO {

    private List<String> codes = new ArrayList<String>();

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    public static IndicatorsDataMeasureDimensionFilterVO buildCodesFilter(String... codes) {
        IndicatorsDataMeasureDimensionFilterVO filter = new IndicatorsDataMeasureDimensionFilterVO();
        filter.setCodes(new ArrayList<String>(Arrays.asList(codes)));
        return filter;
    }
}
