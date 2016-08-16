package es.gobcan.istac.indicators.core.vo;

public class IndicatorsDataFilterVO {

    private IndicatorsDataGeoDimensionFilterVO     geoFilter;
    private IndicatorsDataTimeDimensionFilterVO    timeFilter;
    private IndicatorsDataMeasureDimensionFilterVO measureFilter;

    public IndicatorsDataGeoDimensionFilterVO getGeoFilter() {
        return geoFilter;
    }

    public void setGeoFilter(IndicatorsDataGeoDimensionFilterVO geoFilter) {
        this.geoFilter = geoFilter;
    }

    public IndicatorsDataTimeDimensionFilterVO getTimeFilter() {
        return timeFilter;
    }

    public void setTimeFilter(IndicatorsDataTimeDimensionFilterVO timeFilter) {
        this.timeFilter = timeFilter;
    }

    public IndicatorsDataMeasureDimensionFilterVO getMeasureFilter() {
        return measureFilter;
    }

    public void setMeasureFilter(IndicatorsDataMeasureDimensionFilterVO measureFilter) {
        this.measureFilter = measureFilter;
    }

}
