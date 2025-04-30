package es.gobcan.istac.indicators.rest.domain;

import java.util.List;

import es.gobcan.istac.indicators.rest.enume.LabelVisualisationModeEnum;

public class IndicatorSelectionDimension {

    private final String               id;
    private List<String>               selectedDimensionValues;

    private LabelVisualisationModeEnum labelVisualisationMode;
    private Integer                    position;

    public IndicatorSelectionDimension(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public LabelVisualisationModeEnum getLabelVisualisationMode() {
        return labelVisualisationMode;
    }

    public void setLabelVisualisationMode(LabelVisualisationModeEnum labelVisualisationMode) {
        this.labelVisualisationMode = labelVisualisationMode;
    }

    public List<String> getSelectedDimensionValues() {
        return selectedDimensionValues;
    }

    public void setSelectedDimensionValues(List<String> selectedDimensionValues) {
        this.selectedDimensionValues = selectedDimensionValues;
    }

}
