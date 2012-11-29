package es.gobcan.istac.indicators.rest.types;

import java.util.List;

public class SubjectType extends SubjectBaseType {

    private List<IndicatorBaseType> elements;

    public List<IndicatorBaseType> getElements() {
        return elements;
    }

    public void setElements(List<IndicatorBaseType> elements) {
        this.elements = elements;
    }
}
