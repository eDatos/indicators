package es.gobcan.istac.indicators.rest.domain;

import es.gobcan.istac.indicators.rest.enume.LabelVisualisationModeEnum;

public class IndicatorSelectionAttribute {

    private final String               id;
    private LabelVisualisationModeEnum labelVisualisationMode;

    public IndicatorSelectionAttribute(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public LabelVisualisationModeEnum getLabelVisualisationMode() {
        return labelVisualisationMode;
    }

    public void setLabelVisualisationMode(LabelVisualisationModeEnum labelVisualisationMode) {
        this.labelVisualisationMode = labelVisualisationMode;
    }
}
